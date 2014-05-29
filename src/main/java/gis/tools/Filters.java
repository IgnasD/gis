package gis.tools;

import java.util.HashSet;
import java.util.Set;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.factory.CommonFactoryFinder;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.identity.FeatureId;

/**
 * @author Ignas Daukšas
 */
public class Filters {
    private static final FilterFactory ff = CommonFactoryFinder.getFilterFactory();
    
    public static SimpleFeatureCollection filterByPIDs(SimpleFeatureCollection colID,
            SimpleFeatureCollection colPID) {
        
        Set<FeatureId> IDs = new HashSet<FeatureId>();
        
        SimpleFeatureIterator iterPID = colPID.features();
        try {
            while (iterPID.hasNext()) {
                SimpleFeature feature = iterPID.next();
                IDs.add(ff.featureId((String) feature.getAttribute("#PID#")));
            }
        } finally {
            iterPID.close();
        }
        
        return colID.subCollection(ff.id(IDs));
    }
    
}
