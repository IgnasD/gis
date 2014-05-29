package gis.tasks.task3;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.operation.distance.DistanceOp;
import gis.Utils;
import gis.gui.MainFrame;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.AttributeTypeBuilder;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.geotools.referencing.GeodeticCalculator;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.identity.FeatureId;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.TransformException;

/**
 * @author Ignas Daukšas
 */
public class Task3Part7 {
    private final FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();
    
    public MainFrame mainFrame;
    public SimpleFeatureCollection collection, poolCol;
    
    private CoordinateReferenceSystem crs;
    private SimpleFeatureType sft;
    
    private final List<SimpleFeature> slidesFeatures = Collections.synchronizedList(new ArrayList<SimpleFeature>());
    private final AtomicInteger sfid = new AtomicInteger();
    private final Set<FeatureId> sitesFids = Collections.synchronizedSet(new HashSet<FeatureId>());
    private final BlockingQueue<SimpleFeature> bq = new LinkedBlockingQueue<SimpleFeature>(16);
    private boolean producing = false;
    private final Map<String, Geometry> poolsCache = new ConcurrentHashMap<String, Geometry>(128);

    public void run() {
        System.out.println("INFO: Task3Part7 has started");
        long millis = System.currentTimeMillis();
        
        makeSFT();
        
        doThreaded();
        
        results();
        
        System.out.println("INFO: Task3Part7: "+((System.currentTimeMillis()-millis)/1000d)+"s");
    }
    
    private void makeSFT() {
        crs = collection.getSchema().getCoordinateReferenceSystem();
        
        SimpleFeatureTypeBuilder sftb = new SimpleFeatureTypeBuilder();
        sftb.setName("Ciuozyklos");
        sftb.setCRS(crs);
        
        AttributeTypeBuilder atb = new AttributeTypeBuilder();
        
        atb.setBinding(LineString.class);
        atb.setCRS(crs);
        sftb.add(atb.buildDescriptor("the_geom"));
        
        atb.binding(String.class);
        sftb.add(atb.buildDescriptor("Sklypo_ID"));
        
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
    
    private void results() {
        Utils.createSilentLayer(mainFrame, new ListFeatureCollection(sft, slidesFeatures));
        
        SimpleFeatureCollection resultCollection = collection.subCollection(ff.id(sitesFids));
        Utils.createSilentLayer(mainFrame, resultCollection, "Sklypai");
    }
    
    private class Producer implements Runnable {

        public void run() {
            producing = true;
            SimpleFeatureIterator iter = collection.features();
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
        private final GeometryFactory gf = JTSFactoryFinder.getGeometryFactory(null);
        private final SimpleFeatureBuilder sfb = new SimpleFeatureBuilder(sft);
        private final Pattern pattern = Pattern.compile("^\\w+\\s\\((\\d+\\.?\\d*)-(\\d+\\.?\\d*)(?:;\\s(\\d+\\.?\\d*)-(\\d+\\.?\\d*))?\\)$");
        
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
                
                if (!geometry.isValid()) {
                    continue;
                }
                
                Geometry centroid = geometry.getCentroid();
                Coordinate[] slideCoords = new Coordinate[2];
                slideCoords[0] = centroid.getCoordinate();
                
                String poolFid = (String) feature.getAttribute("#PID#");
                Geometry poolGeometry;
                
                if (poolsCache.containsKey(poolFid)) {
                    poolGeometry = poolsCache.get(poolFid);
                }
                else {
                    SimpleFeatureCollection singlePoolCol = poolCol.subCollection(
                            ff.id(ff.featureId(poolFid)));
                    
                    SimpleFeatureIterator poolIter = singlePoolCol.features();
                    SimpleFeature poolFeature = poolIter.next();
                    
                    poolGeometry = (Geometry) poolFeature.getDefaultGeometry();
                    poolsCache.put(poolFid, poolGeometry);
                    
                    poolIter.close();
                }
                
                slideCoords[1] = DistanceOp.nearestPoints(poolGeometry, centroid)[0];
                
                try {
                    GeodeticCalculator gc = new GeodeticCalculator(crs);
                    gc.setStartingPosition(JTS.toDirectPosition(slideCoords[0], crs));
                    gc.setDestinationPosition(JTS.toDirectPosition(slideCoords[1], crs));
                                
                    double angle = gc.getAzimuth();
                    if (angle < 0) {
                        angle += 360;
                    }
                    
                    String slopeAngle = (String) feature.getAttribute("kryp_Kryptis");
                    
                    Matcher matcher = pattern.matcher(slopeAngle);
                    if (!matcher.matches()) {
                        System.out.println("WARNING: Pattern does not match.");
                        continue;
                    }
                    
                    boolean correctAngle = false;
                    correctAngle = correctAngle ||
                            (Double.parseDouble(matcher.group(1)) <= angle &&
                            Double.parseDouble(matcher.group(2)) >= angle);
                    if (!correctAngle && matcher.group(3) != null) {
                        correctAngle = correctAngle ||
                                (Double.parseDouble(matcher.group(3)) <= angle &&
                                Double.parseDouble(matcher.group(4)) >= angle);
                    }
                    if (!correctAngle) {
                        continue;
                    }
                    
                    sitesFids.add(feature.getIdentifier());
                    
                    Geometry line = gf.createLineString(slideCoords);
                    line = line.difference(geometry);
                    
                    sfb.add(line);
                    sfb.add(feature.getID());
                    
                    slidesFeatures.add(sfb.buildFeature(String.valueOf(sfid.getAndAdd(1))));
                } catch (TransformException ex) {
                    System.out.println("Unexpected exception: "+ex);
                }
            }
        }
    }
    
}
