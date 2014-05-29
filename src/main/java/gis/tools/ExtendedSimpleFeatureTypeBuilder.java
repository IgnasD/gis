package gis.tools;

import org.geotools.feature.AttributeTypeBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;

/**
 * @author Ignas Daukšas
 */
public class ExtendedSimpleFeatureTypeBuilder extends SimpleFeatureTypeBuilder {
    
    public void init(SimpleFeatureType type1, SimpleFeatureType type2,
            String prefix1, String prefix2) {
        
        init(type1);
        attributes().clear();
        
        AttributeTypeBuilder atb = new AttributeTypeBuilder(factory);
        
        AttributeDescriptor geomDescriptor = type1.getGeometryDescriptor();
        for (AttributeDescriptor descriptor : type1.getAttributeDescriptors()) {
            atb.init(descriptor);
            String name;
            if (descriptor == geomDescriptor) {
                name = descriptor.getLocalName();
            }
            else {
                name = prefix1+descriptor.getLocalName();
                atb.setName(name);
            }
            add(atb.buildDescriptor(name));
        }
        
        geomDescriptor = type2.getGeometryDescriptor();
        for (AttributeDescriptor descriptor : type2.getAttributeDescriptors()) {
            if (descriptor != geomDescriptor) {
                atb.init(descriptor);
                String name = prefix2+descriptor.getLocalName();
                atb.setName(name);
                add(atb.buildDescriptor(name));
            }
        }
    }
    
    public void initCopy(SimpleFeatureType original) {
        init(original);
        attributes().clear();
        
        AttributeTypeBuilder atb = new AttributeTypeBuilder(factory);
        
        for (AttributeDescriptor descriptor : original.getAttributeDescriptors()) {
            atb.init(descriptor);
            add(atb.buildDescriptor(descriptor.getLocalName()));
        }
    }
    
}
