package gis.tools.geoprocessing;

import com.vividsolutions.jts.geom.Geometry;
import gis.tools.ExtendedSimpleFeatureTypeBuilder;
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
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.FilterFactory2;

/**
 * @author Ignas Daukšas
 */
public class Intersector {
    private final FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();
    
    private final SimpleFeatureCollection col1, col2;
    
    private String the_geom, prefix1, prefix2, name, recalculateLength, recalculateArea;
    
    private SimpleFeatureType sft;
    
    private final List<SimpleFeature> features = Collections.synchronizedList(new ArrayList<SimpleFeature>());
    private final BlockingQueue<BlockingQueueElement> bq = new LinkedBlockingQueue<BlockingQueueElement>(16);
    private final AtomicInteger fid = new AtomicInteger();
    private boolean producing = false;
    
    public Intersector(SimpleFeatureCollection col1, SimpleFeatureCollection col2) {
        this.col1 = col1;
        this.col2 = col2;
        prefix1 = col1.getSchema().getTypeName()+"_";
        prefix2 = col2.getSchema().getTypeName()+"_";
    }
    
    public void setPrefixes(String prefix1, String prefix2) {
        this.prefix1 = prefix1;
        this.prefix2 = prefix2;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public void recalculateLength(String recalculateLength) {
        this.recalculateLength = recalculateLength;
    }
    
    public void recalculateArea(String recalculateArea) {
        this.recalculateArea = recalculateArea;
    }
    
    public void intersect() {
        System.out.println("INFO: Intersector has started");
        long millis = System.currentTimeMillis();
        
        makeSFT();
        
        //doUnthreaded();
        doThreaded();
        
        System.out.println("INFO: Intersector: "+((System.currentTimeMillis()-millis)/1000d)+"s");
    }
    
    private void makeSFT() {
        SimpleFeatureType type1 = col1.getSchema();
        SimpleFeatureType type2 = col2.getSchema();
        
        ExtendedSimpleFeatureTypeBuilder esftb = new ExtendedSimpleFeatureTypeBuilder();
        esftb.init(type1, type2, prefix1, prefix2);
        if (name != null) {
            esftb.setName(name);
        }
        
        the_geom = type1.getGeometryDescriptor().getLocalName();
        
        sft = esftb.buildFeatureType();
    }
    
    private void doUnthreaded() {
        SimpleFeatureBuilder sfb = new SimpleFeatureBuilder(sft);
        int i=0;
        SimpleFeatureIterator iter2 = col2.features();
        try {
            while (iter2.hasNext()) {
                SimpleFeature feature2 = iter2.next();
                Geometry geometry2 = (Geometry) feature2.getDefaultGeometry();
                
                SimpleFeatureCollection partCol1 = col1.subCollection(
                        ff.intersects(ff.property(the_geom), ff.literal(geometry2)));
                
                SimpleFeatureIterator iter1 = partCol1.features();
                try {
                    while (iter1.hasNext()) {
                        SimpleFeature feature1 = iter1.next();
                        
                        Geometry intersected = null;
                        Object attRecalcLength = null;
                        Object attRecalcArea = null;
                        if (recalculateLength != null) {
                            attRecalcLength = feature1.getAttribute(recalculateLength);
                        }
                        if (recalculateArea != null) {
                            attRecalcArea = feature1.getAttribute(recalculateArea);
                        }
                        for (Object attribute : feature1.getAttributes()) {
                            if (attribute instanceof Geometry) {
                                Geometry geometry1 = (Geometry) attribute;
                                intersected = geometry1.intersection(geometry2);
                                sfb.add(intersected);
                            }
                            else {
                                if (attribute == attRecalcLength && intersected != null) {
                                    sfb.add(intersected.getLength());
                                }
                                else if (attribute == attRecalcArea && intersected != null) {
                                    sfb.add(intersected.getArea());
                                }
                                else {
                                    sfb.add(attribute);
                                }
                            }
                        }
                        
                        for (Object attribute : feature2.getAttributes()) {
                            if (!(attribute instanceof Geometry)) {
                                sfb.add(attribute);
                            }
                        }
                        
                        features.add(sfb.buildFeature(String.valueOf(i++)));
                    }
                } finally {
                    iter1.close();
                }
            }
        } finally {
            iter2.close();
        }
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
    
    public SimpleFeatureCollection getIntersected() {
        return new ListFeatureCollection(sft, features);
    }
    
    private class BlockingQueueElement {
        public SimpleFeature feature1;
        public SimpleFeature feature2;
        public Geometry geometry2;
    }
    
    private class Producer implements Runnable {

        public void run() {
            producing = true;
            SimpleFeatureIterator iter2 = col2.features();
            try {
                while (iter2.hasNext()) {
                    SimpleFeature feature2 = iter2.next();
                    Geometry geometry2 = (Geometry) feature2.getDefaultGeometry();

                    SimpleFeatureCollection partCol1 = col1.subCollection(
                            ff.intersects(ff.property(the_geom), ff.literal(geometry2)));

                    SimpleFeatureIterator iter1 = partCol1.features();
                    try {
                        while (iter1.hasNext()) {
                            BlockingQueueElement bqe = new BlockingQueueElement();
                            bqe.feature1 = iter1.next();
                            bqe.feature2 = feature2;
                            bqe.geometry2 = geometry2;
                            
                            try {
                                bq.put(bqe);
                            } catch (InterruptedException ex) {
                                System.out.println("Unexpected exception: "+ex);
                            }
                        }
                    } finally {
                        iter1.close();
                    }
                }
            } finally {
                iter2.close();
            }
            producing = false;
            System.out.println("INFO: Producer has exited");
        }
        
    }
    
    private class Consumer implements Runnable {
        private final SimpleFeatureBuilder sfb = new SimpleFeatureBuilder(sft);

        public void run() {
            while (true) {
                BlockingQueueElement bqe;
                try {
                    bqe = bq.poll(1, TimeUnit.SECONDS);
                } catch (InterruptedException ex) {
                    System.out.println("Unexpected exception: "+ex);
                    break;
                }
                if (bqe == null) {
                    if (producing) {
                        System.out.println("INFO: Consumer was idling for 1 sec.");
                        continue;
                    }
                    else {
                        System.out.println("INFO: Consumer has exited");
                        break;
                    }
                }
                
                Geometry intersected = null;
                Object attRecalcLength = null;
                Object attRecalcArea = null;
                if (recalculateLength != null) {
                    attRecalcLength = bqe.feature1.getAttribute(recalculateLength);
                }
                if (recalculateArea != null) {
                    attRecalcArea = bqe.feature1.getAttribute(recalculateArea);
                }
                for (Object attribute : bqe.feature1.getAttributes()) {
                    if (attribute instanceof Geometry) {
                        Geometry geometry1 = (Geometry) attribute;
                        intersected = geometry1.intersection(bqe.geometry2);
                        sfb.add(intersected);
                    }
                    else {
                        if (attribute == attRecalcLength && intersected != null) {
                            sfb.add(intersected.getLength());
                        }
                        else if (attribute == attRecalcArea && intersected != null) {
                            sfb.add(intersected.getArea());
                        }
                        else {
                            sfb.add(attribute);
                        }
                    }
                }

                for (Object attribute : bqe.feature2.getAttributes()) {
                    if (!(attribute instanceof Geometry)) {
                        sfb.add(attribute);
                    }
                }

                features.add(sfb.buildFeature(String.valueOf(fid.getAndAdd(1))));
            }
        }
    }
    
}
