package gis.tools;

import com.vividsolutions.jts.geom.Geometry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.FilterFactory2;

/**
 * @author Ignas Daukšas
 */
public class IntersectingKeeper {
    private final FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();
    
    private final String name;
    private final SimpleFeatureCollection col1, col2;
    
    private SimpleFeatureType sft;
    private String the_geom2;
    
    private final List<SimpleFeature> features = Collections.synchronizedList(new ArrayList<SimpleFeature>());
    private final BlockingQueue<SimpleFeature> bq = new LinkedBlockingQueue<SimpleFeature>(16);
    private boolean producing = false;
    
    public IntersectingKeeper(String name, SimpleFeatureCollection col1, SimpleFeatureCollection col2) {
        this.name = name;
        this.col1 = col1;
        this.col2 = col2;
    }
    
    public void check() {
        System.out.println("INFO: IntersectingKeeper has started");
        long millis = System.currentTimeMillis();
        
        makeSFT();
        
        doThreaded();
        
        System.out.println("INFO: IntersectingKeeper: "+((System.currentTimeMillis()-millis)/1000d)+"s");
    }
    
    private void makeSFT() {
        ExtendedSimpleFeatureTypeBuilder esftb = new ExtendedSimpleFeatureTypeBuilder();
        esftb.initCopy(col1.getSchema());
        esftb.setName(name);
        sft = esftb.buildFeatureType();
        the_geom2 = col2.getSchema().getGeometryDescriptor().getLocalName();
    }
    
    private void doThreaded() {
        Thread producer = new Thread(new Producer());
        producer.start();
        Thread consumer1 = new Thread(new Consumer());
        Thread consumer2 = new Thread(new Consumer());
        Thread consumer3 = new Thread(new Consumer());
        Thread consumer4 = new Thread(new Consumer());
        Thread consumer5 = new Thread(new Consumer());
        Thread consumer6 = new Thread(new Consumer());
        Thread consumer7 = new Thread(new Consumer());
        consumer1.start();
        consumer2.start();
        consumer3.start();
        consumer4.start();
        consumer5.start();
        consumer6.start();
        consumer7.start();
        
        try {
            producer.join();
            consumer1.join();
            consumer2.join();
            consumer3.join();
            consumer4.join();
            consumer5.join();
            consumer6.join();
            consumer7.join();
        } catch (InterruptedException ex) {
            System.out.println("Unexpected exception: "+ex);
        }
    }
    
    public SimpleFeatureCollection getKept() {
        return new ListFeatureCollection(sft, features);
    }
    
    private class Producer implements Runnable {

        public void run() {
            producing = true;
            SimpleFeatureIterator iter1 = col1.features();
            try {
                while (iter1.hasNext()) {
                    try {
                        bq.put(iter1.next());
                    } catch (InterruptedException ex) {
                        System.out.println("Unexpected exception: "+ex);
                    }
                }
            } finally {
                iter1.close();
            }
            producing = false;
            System.out.println("INFO: Producer has exited");
        }
        
    }
    
    private class Consumer implements Runnable {
        private final SimpleFeatureBuilder sfb = new SimpleFeatureBuilder(sft);

        public void run() {
            while (true) {
                SimpleFeature feature1;
                try {
                    feature1 = bq.poll(1, TimeUnit.SECONDS);
                } catch (InterruptedException ex) {
                    System.out.println("Unexpected exception: "+ex);
                    break;
                }
                if (feature1 == null) {
                    if (producing) {
                        System.out.println("INFO: Consumer was idling for 1 sec.");
                        continue;
                    }
                    else {
                        System.out.println("INFO: Consumer has exited");
                        break;
                    }
                }
                
                Geometry geometry1 = (Geometry) feature1.getDefaultGeometry();
                
                SimpleFeatureCollection partCol2 = col2.subCollection(
                        ff.intersects(ff.property(the_geom2), ff.literal(geometry1)));
                
                if (!partCol2.isEmpty()) {
                    sfb.addAll(feature1.getAttributes());
                    features.add(sfb.buildFeature(feature1.getID()));
                }
            }
        }
    }
    
}
