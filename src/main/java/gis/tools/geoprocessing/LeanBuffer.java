package gis.tools.geoprocessing;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.MultiPolygon;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.feature.AttributeTypeBuilder;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;

/**
 * @author Ignas Daukšas
 */
public class LeanBuffer {
    private static final String the_geom = "the_geom";
    
    private final String name;
    private final SimpleFeatureCollection col;
    private final double distance;
    
    private SimpleFeatureType sft;
    
    private final List<SimpleFeature> features = Collections.synchronizedList(new ArrayList<SimpleFeature>());
    private final BlockingQueue<SimpleFeature> bq = new LinkedBlockingQueue<SimpleFeature>(16);
    private final AtomicInteger fid = new AtomicInteger();
    private boolean producing = false;
    
    public LeanBuffer(String name, SimpleFeatureCollection col, double distance) {
        this.name = name;
        this.col = col;
        this.distance = distance;
    }
    
    public void buffer() {
        System.out.println("INFO: LeanBuffer has started");
        long millis = System.currentTimeMillis();
        
        makeSFT();
        
        doThreaded();
        
        System.out.println("INFO: LeanBuffer: "+((System.currentTimeMillis()-millis)/1000d)+"s");
    }
    
    private void makeSFT() {
        SimpleFeatureTypeBuilder sftb = new SimpleFeatureTypeBuilder();
        sftb.setName(name);
        sftb.setCRS(col.getSchema().getCoordinateReferenceSystem());
        
        AttributeTypeBuilder atb = new AttributeTypeBuilder();
        
        atb.setBinding(MultiPolygon.class);
        atb.setCRS(col.getSchema().getCoordinateReferenceSystem());
        AttributeDescriptor geomDescriptor = atb.buildDescriptor(the_geom);
        sftb.add(geomDescriptor);
        
        atb.binding(String.class);
        AttributeDescriptor pidDescriptor = atb.buildDescriptor("#PID#");
        sftb.add(pidDescriptor);
        
        sft = sftb.buildFeatureType();
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
    
    public SimpleFeatureCollection getBuffered() {
        return new ListFeatureCollection(sft, features);
    }
    
    private class Producer implements Runnable {

        public void run() {
            producing = true;
            SimpleFeatureIterator iter = col.features();
            try {
                while (iter.hasNext()) {
                    try {
                        bq.put(iter.next());
                    } catch (InterruptedException ex) {
                        System.out.println("Unexpected exception: "+ex);
                    }
                }
            } finally {
                iter.close();
            }
            producing = false;
            System.out.println("INFO: Producer has exited");
        }
        
    }
    
    private class Consumer implements Runnable {
        private final SimpleFeatureBuilder sfb = new SimpleFeatureBuilder(sft);

        public void run() {
            while (true) {
                SimpleFeature feature;
                try {
                    feature = bq.poll(1, TimeUnit.SECONDS);
                } catch (InterruptedException ex) {
                    System.out.println("Unexpected exception: "+ex);
                    break;
                }
                if (feature == null) {
                    if (producing) {
                        System.out.println("INFO: Consumer was idling for 1 sec.");
                        continue;
                    }
                    else {
                        System.out.println("INFO: Consumer has exited");
                        break;
                    }
                }
                
                Geometry geometry = (Geometry) feature.getDefaultGeometry();
                Geometry buffered = geometry.buffer(distance);
                
                sfb.add(buffered);
                sfb.add(feature.getID());

                features.add(sfb.buildFeature(String.valueOf(fid.getAndAdd(1))));
            }
        }
    }
    
}
