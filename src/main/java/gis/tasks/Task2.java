package gis.tasks;

import gis.gui.MainFrame;
import gis.tools.GroupingBuilder;
import gis.tools.geoprocessing.Intersector;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.text.cql2.CQL;
import org.geotools.filter.text.cql2.CQLException;
import org.geotools.map.FeatureLayer;
import org.geotools.styling.SLD;
import org.geotools.swing.dialog.JExceptionReporter;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Function;

/**
 * @author Ignas Daukšas
 */
public class Task2 implements Runnable {
    private final FilterFactory ff = CommonFactoryFinder.getFilterFactory();
    private SimpleFeatureCollection muniesCol, watersCol, roadsCol,
            territoriesCol, buildingsCol;
    private JTable tableWaters, tableRoads, tableTerritories, tableBuildings;
    private MainFrame mainFrame;
    
    public void setCollections(SimpleFeatureCollection muniesCol,
            SimpleFeatureCollection watersCol, SimpleFeatureCollection roadsCol,
            SimpleFeatureCollection territoriesCol, SimpleFeatureCollection buildingsCol) {
        
        this.muniesCol = muniesCol;
        this.watersCol = watersCol;
        this.roadsCol = roadsCol;
        this.territoriesCol = territoriesCol;
        this.buildingsCol = buildingsCol;
    }
    
    public void setTables(JTable tableWaters, JTable tableRoads,
            JTable tableTerritories, JTable tableBuildings) {
        
        this.tableWaters = tableWaters;
        this.tableRoads = tableRoads;
        this.tableTerritories = tableTerritories;
        this.tableBuildings = tableBuildings;
    }
    
    public void setMainFrame(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
    }
    
    public void run() {
        System.out.println("INFO: Task2 has started");
        long millis = System.currentTimeMillis();
        
        findTotalLength(watersCol, tableWaters,
                new String[] {"Administracinis vientas", "Hidro. tinklo ilgis"});
        findTotalLength(roadsCol, tableRoads,
                new String[] {"Administracinis vientas", "Kelių ilgis"});
        part3And4();
        
        System.out.println("INFO: Task2 total: "+((System.currentTimeMillis()-millis)/1000d)+"s");
    }
    
    private void findTotalLength(SimpleFeatureCollection lines,
            JTable table, String[] tableTitleRow) {
        
        DefaultTableModel model = new DefaultTableModel(tableTitleRow, 0);
        
        String muniesPrefix = muniesCol.getSchema().getName().getLocalPart();
        String linesPrefix = lines.getSchema().getName().getLocalPart();
        
        Intersector intersector = new Intersector(lines, muniesCol);
        intersector.recalculateLength("SHAPE_len");
        intersector.setName(linesPrefix+"_intersected");
        intersector.intersect();
        SimpleFeatureCollection intersected = intersector.getIntersected();
        
        mainFrame.getMapContent().addLayer(new FeatureLayer(intersected,
                SLD.createSimpleStyle(intersected.getSchema())));
        
        Function function = ff.function("Collection_Sum", ff.property(linesPrefix+"_SHAPE_len"));
        
        SimpleFeatureIterator iter = muniesCol.features();
        try {
            while (iter.hasNext()) {
                SimpleFeature feature = iter.next();
                String title = (String) feature.getAttribute("SAV");
                Filter filter = CQL.toFilter(muniesPrefix+"_SAV = '"+title+"'");
                double result = ((Double) function.evaluate(intersected.subCollection(filter))).doubleValue();
                model.addRow(new String[] {title, String.valueOf(result)});
            }
        } catch (CQLException ex) {
            JExceptionReporter.showDialog(ex);
        } finally {
            iter.close();
        }
        
        table.setModel(model);
    }
    
    private void part3And4() {
        DefaultTableModel model = new DefaultTableModel(new String[] {"Administracinis vientas",
            "<- Plotas", "Teritorija", "<- Plotas", "Santykis (%)"}, 0);
        
        String muniesPrefix = muniesCol.getSchema().getName().getLocalPart();
        String territoriesPrefix = territoriesCol.getSchema().getName().getLocalPart();
        
        Intersector intersector = new Intersector(territoriesCol, muniesCol);
        intersector.recalculateArea("SHAPE_area");
        intersector.setName(territoriesPrefix+"_intersected");
        intersector.intersect();
        SimpleFeatureCollection intersected = intersector.getIntersected();
        
        mainFrame.getMapContent().addLayer(new FeatureLayer(intersected,
                SLD.createSimpleStyle(intersected.getSchema())));
        
        String[] grouping = GroupingBuilder.build(territoriesPrefix+"_GKODAS",
                new String[] {"LIKE 'hd%'", "= 'ms0'", "= 'pu0'", "= 'ms4'"});
        String[] titles = new String[] {"Hidrografijos teritorijos",
            "Medžiais ir krūmais apaugusios teritorijos",
            "Užstatytos teritorijos", "Pramoninių sodų masyvai"};
        
        Function function = ff.function("Collection_Sum", ff.property(territoriesPrefix+"_SHAPE_area"));
        
        SimpleFeatureIterator iter = muniesCol.features();
        try {
            while (iter.hasNext()) {
                SimpleFeature feature = iter.next();
                String title = (String) feature.getAttribute("SAV");
                for (int i=0 ; i<grouping.length ; i++) {
                    Filter filter = CQL.toFilter(muniesPrefix+"_SAV = '"+title+"' AND "+grouping[i]);
                    double result = ((Double) function.evaluate(intersected.subCollection(filter))).doubleValue();
                    double muniArea = ((Double) feature.getAttribute("PLOT")).doubleValue();
                    model.addRow(new String[] {title, String.valueOf(muniArea),
                        titles[i], String.valueOf(result),
                        String.valueOf(result*100/muniArea)});
                }
            }
        } catch (CQLException ex) {
            JExceptionReporter.showDialog(ex);
        } finally {
            iter.close();
        }
        
        tableTerritories.setModel(model);
        
        part4(intersected);
    }
    
    private void part4(SimpleFeatureCollection territoriesIntersected) {
        DefaultTableModel model = new DefaultTableModel(new String[] {
            "Administracinis vientas", "Teritorija", "<- Plotas",
            "Statinių plotas", "Santykis (%)"}, 0);
        
        String territoriesIntersectedPrefix = territoriesIntersected.getSchema().getName().getLocalPart();
        String muniesPrefix = muniesCol.getSchema().getName().getLocalPart();
        String territoriesPrefix = territoriesCol.getSchema().getName().getLocalPart();
        String buildingsPrefix = buildingsCol.getSchema().getName().getLocalPart();
        
        Intersector intersector = new Intersector(buildingsCol, territoriesIntersected);
        intersector.recalculateArea("SHAPE_area");
        intersector.setName(buildingsPrefix+"_intersected");
        intersector.intersect();
        SimpleFeatureCollection intersected = intersector.getIntersected();
        
        mainFrame.getMapContent().addLayer(new FeatureLayer(intersected,
                SLD.createSimpleStyle(intersected.getSchema())));
        
        String[] muniesGrouping = GroupingBuilder.build(muniesCol,
                "SAV",territoriesIntersectedPrefix+"_"+muniesPrefix+"_SAV");
        String[] territoriesGrouping = GroupingBuilder.build(territoriesIntersectedPrefix
                +"_"+territoriesPrefix+"_GKODAS",
                new String[] {"LIKE 'hd%'", "= 'ms0'", "= 'pu0'", "= 'ms4'"});
        String[] grouping = GroupingBuilder.multiply(muniesGrouping, territoriesGrouping);
        
        DefaultTableModel modelTerritories = (DefaultTableModel) tableTerritories.getModel();
        
        Function function = ff.function("Collection_Sum", ff.property(buildingsPrefix+"_SHAPE_area"));
        
        try {
            for (int i=0 ; i<grouping.length ; i++) {
                Filter filter = CQL.toFilter(grouping[i]);
                SimpleFeatureCollection filteredSubCol = intersected.subCollection(filter);
                double result = 0;
                if (!filteredSubCol.isEmpty()) {
                    result = ((Double) function.evaluate(filteredSubCol)).doubleValue();
                }
                double territoryArea = Double.parseDouble((String) modelTerritories.getValueAt(i, 3));
                model.addRow(new String[] {(String) modelTerritories.getValueAt(i, 0),
                    (String) modelTerritories.getValueAt(i, 2),
                    (String) modelTerritories.getValueAt(i, 3),
                    String.valueOf(result),
                    String.valueOf(result*100/territoryArea)});
            }
        } catch (CQLException ex) {
            JExceptionReporter.showDialog(ex);
        }
        
        tableBuildings.setModel(model);
    }
    
}
