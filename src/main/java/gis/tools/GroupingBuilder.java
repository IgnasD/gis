package gis.tools;

import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.opengis.feature.simple.SimpleFeature;

/**
 * @author Ignas Daukšas
 */
public class GroupingBuilder {
    
    public static String[] build(SimpleFeatureCollection col,
            String attNameCol, String attNameQuery) {
        
        int size = col.size();
        String[] grouping = new String[size];
        
        SimpleFeatureIterator iter = col.features();
        int i=0;
        while (iter.hasNext()) {
            SimpleFeature feature = iter.next();
            String value = (String) feature.getAttribute(attNameCol);
            grouping[i] = attNameQuery+" = '"+value+"'";
            i++;
        }
        iter.close();
        
        return grouping;
    }
    
    public static String[] build(String attNameQuery, String[] queryEndings) {
        String[] grouping = new String[queryEndings.length];
        
        for (int i=0 ; i<queryEndings.length ; i++) {
            grouping[i] = attNameQuery+" "+queryEndings[i];
        }
        
        return grouping;
    }
    
    public static String[] multiply(String[] gr1, String[] gr2) {
        String[] grouping = new String[gr1.length*gr2.length];
        
        for (int i=0 ; i<gr1.length ; i++) {
            for (int j=0 ; j<gr2.length ; j++) {
                int n = j+(i*gr2.length);
                grouping[n] = gr1[i]+" AND "+gr2[j];
            }
        }
        
        return grouping;
    }
    
}
