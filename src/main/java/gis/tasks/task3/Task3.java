package gis.tasks.task3;

import gis.Utils;
import gis.gui.MainFrame;
import gis.tools.Filters;
import gis.tools.IntersectingKeeper;
import gis.tools.geoprocessing.Intersector;
import gis.tools.geoprocessing.LeanBuffer;
import gis.tools.geoprocessing.Differencer;
import gis.tools.geoprocessing.InternalPolygonsRemover;
import gis.tools.geoprocessing.MultipolygonSeparator;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.text.cql2.CQL;
import org.geotools.filter.text.cql2.CQLException;
import org.geotools.swing.dialog.JExceptionReporter;
import org.opengis.filter.FilterFactory2;

/**
 * @author Ignas Daukšas
 */
public class Task3 implements Runnable {
    private final FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();
    
    private MainFrame mainFrame;
    private SimpleFeatureCollection territoriesCol, buildingsCol,
            slopesCol, anglesCol, roadsCol;
    private boolean lakes = false, ponds = false;
    private double minPoolArea, smallPoolBufferSize, largePoolBufferSize,
            buildingsBufferSize, slopeDegree, roadsBufferSize;
    
    private SimpleFeatureCollection workingCollection, localizedPools;
    
    public void setMainFrame(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
    }
    
    public void setCollections(SimpleFeatureCollection territoriesCol,
            SimpleFeatureCollection buildingsCol,
            SimpleFeatureCollection slopesCol, SimpleFeatureCollection anglesCol,
            SimpleFeatureCollection roadsCol) {
        
        this.territoriesCol = territoriesCol;
        this.buildingsCol = buildingsCol;
        this.slopesCol = slopesCol;
        this.anglesCol = anglesCol;
        this.roadsCol = roadsCol;
    }
    
    public void includeLakes(boolean lakes) {
        this.lakes = lakes;
    }
    
    public void includePonds(boolean ponds) {
        this.ponds = ponds;
    }
    
    public void setMinPoolArea(double minPoolArea) {
        this.minPoolArea = minPoolArea;
    }
    
    public void setSmallPoolBufferSize(double smallPoolBufferSize) {
        this.smallPoolBufferSize = smallPoolBufferSize;
    }
    
    public void setLargePoolBufferSize(double largePoolBufferSize) {
        this.largePoolBufferSize = largePoolBufferSize;
    }
    
    public void setBuildingsBufferSize(double buildingsBufferSize) {
        this.buildingsBufferSize = buildingsBufferSize;
    }
    
    public void setSlopeDegree(double slopeDegree) {
        this.slopeDegree = slopeDegree;
    }
    
    public void setRoadsBufferSize(double roadsBufferSize) {
        this.roadsBufferSize = roadsBufferSize;
    }

    public void run() {
        System.out.println("INFO: Task3 has started");
        long millis = System.currentTimeMillis();
        
        try {
            part1();
            part2();
            part3();
            part4();
            part5(); //sąlygos 5 pirma dalis
            part6(); //sąlyga 6
            part7(); //sąlygos 5 antra dalis
        } catch (CQLException ex) {
            JExceptionReporter.showDialog(ex);
        }
        
        System.out.println("INFO: Task3 total: "+((System.currentTimeMillis()-millis)/1000d)+"s");
    }
    
    private void part1() throws CQLException {
        String query = "";
        if (lakes) {
            query += "GKODAS = 'hd3'";
            if (ponds) {
                query += " OR ";
            }
        }
        if (ponds) {
            query += "GKODAS = 'hd9'";
        }
        
        workingCollection = territoriesCol.subCollection(CQL.toFilter(query));
    }
    
    private void part2() throws CQLException {
        workingCollection = workingCollection.subCollection(
                CQL.toFilter("SHAPE_area >= "+minPoolArea));
    }
    
    private void part3() {
        localizedPools = workingCollection.subCollection(ff.intersects(
                ff.property(workingCollection.getSchema().getGeometryDescriptor().getLocalName()),
                ff.literal(mainFrame.getSelectionPoly().buffer(largePoolBufferSize))));
        Utils.createSilentLayer(mainFrame, localizedPools, "Salyga3_Telkiniai");
        
        LeanBuffer leanBufferSmall = new LeanBuffer("Salyga3_MazBuf", localizedPools, smallPoolBufferSize);
        leanBufferSmall.buffer();
        SimpleFeatureCollection smallBuffered = leanBufferSmall.getBuffered();
        
        LeanBuffer leanBufferLarge = new LeanBuffer("Salyga3_DidBuf", localizedPools, largePoolBufferSize);
        leanBufferLarge.buffer();
        SimpleFeatureCollection largeBuffered = leanBufferLarge.getBuffered();
        
        Differencer differenceBuffers = new Differencer("Salyga3_Juosta", largeBuffered, smallBuffered);
        differenceBuffers.difference();
        SimpleFeatureCollection differenced = differenceBuffers.getDifferenced();
        
        InternalPolygonsRemover internalPolyRem = new InternalPolygonsRemover("Salyga3_BeSalu", differenced);
        internalPolyRem.remove();
        SimpleFeatureCollection noIslands = internalPolyRem.getRemoved();
        
        SimpleFeatureCollection selectionCollection = mainFrame.getSelectionCollection();
        Intersector intersector = new Intersector(noIslands, selectionCollection);
        intersector.setName("Salyga3_Baigta");
        intersector.setPrefixes("", "");
        intersector.intersect();
        workingCollection = intersector.getIntersected();
        Utils.createSilentLayer(mainFrame, workingCollection);
    }
    
    private void part4() {
        SimpleFeatureCollection localizedBuildings = buildingsCol.subCollection(ff.intersects(
                ff.property(buildingsCol.getSchema().getGeometryDescriptor().getLocalName()),
                ff.literal(mainFrame.getSelectionPoly())));
        
        LeanBuffer bufferBuildings = new LeanBuffer("Salyga4_BufPast", localizedBuildings, buildingsBufferSize);
        bufferBuildings.buffer();
        SimpleFeatureCollection bufferedBuildings = bufferBuildings.getBuffered();
        Utils.createSilentLayer(mainFrame, bufferedBuildings);
        
        Differencer differenceBuildings = new Differencer("Salyga4_Baigta", workingCollection, bufferedBuildings);
        differenceBuildings.difference();
        workingCollection = differenceBuildings.getDifferenced();
        Utils.createSilentLayer(mainFrame, workingCollection);
    }
    
    private void part5() throws CQLException {
        SimpleFeatureCollection slopesFiltered = slopesCol.subCollection(
                CQL.toFilter("Nuolydis >= "+slopeDegree));
        
        SimpleFeatureCollection localizedSlopes = slopesFiltered.subCollection(ff.intersects(
                ff.property(slopesFiltered.getSchema().getGeometryDescriptor().getLocalName()),
                ff.literal(mainFrame.getSelectionPoly())));
        
        Intersector slopesIntersector = new Intersector(workingCollection, localizedSlopes);
        slopesIntersector.setName("Salyga5_NuolInt");
        slopesIntersector.setPrefixes("", "nuol_");
        slopesIntersector.intersect();
        SimpleFeatureCollection withSlopes = slopesIntersector.getIntersected();
        
        SimpleFeatureCollection localizedAngles = anglesCol.subCollection(ff.intersects(
                ff.property(anglesCol.getSchema().getGeometryDescriptor().getLocalName()),
                ff.literal(mainFrame.getSelectionPoly())));
        
        Intersector anglesIntersector = new Intersector(withSlopes, localizedAngles);
        anglesIntersector.setName("Salyga5_KrypInt");
        anglesIntersector.setPrefixes("", "kryp_");
        anglesIntersector.intersect();
        SimpleFeatureCollection withSlopesAndAngles = anglesIntersector.getIntersected();
        
        MultipolygonSeparator separator = new MultipolygonSeparator("Salyga5_NetikrKryp", withSlopesAndAngles);
        separator.separate();
        workingCollection = separator.getSeparated();
        Utils.createSilentLayer(mainFrame, workingCollection);
    }
    
    private void part6() throws CQLException {
        LeanBuffer sitesBuffer = new LeanBuffer("Salyga6_BufSklyp", workingCollection, roadsBufferSize);
        sitesBuffer.buffer();
        SimpleFeatureCollection bufferedSites = sitesBuffer.getBuffered();
        Utils.createSilentLayer(mainFrame, bufferedSites);
        
        SimpleFeatureCollection filteredRoads = roadsCol.subCollection(CQL.toFilter("DANGA <> ''"));
        
        SimpleFeatureCollection localizedRoads = filteredRoads.subCollection(ff.intersects(
                ff.property(filteredRoads.getSchema().getGeometryDescriptor().getLocalName()),
                ff.literal(mainFrame.getSelectionPoly().buffer(roadsBufferSize))));
        Utils.createSilentLayer(mainFrame, localizedRoads, "Salyga6_Zvyrkeliai+");
        
        IntersectingKeeper nearRoadsKeeper = new IntersectingKeeper("Salyga6_BufPrieKel", bufferedSites, localizedRoads);
        nearRoadsKeeper.check();
        SimpleFeatureCollection nearRoads = nearRoadsKeeper.getKept();
        Utils.createSilentLayer(mainFrame, nearRoads);
        
        workingCollection = Filters.filterByPIDs(workingCollection, nearRoads);
        Utils.createSilentLayer(mainFrame, workingCollection, "Salyga6_Baigta");
    }
    
    private void part7() {
        Task3Part7 part7 = new Task3Part7();
        part7.mainFrame = mainFrame;
        part7.collection = workingCollection;
        part7.poolCol = localizedPools;
        part7.run();
    }
    
}
