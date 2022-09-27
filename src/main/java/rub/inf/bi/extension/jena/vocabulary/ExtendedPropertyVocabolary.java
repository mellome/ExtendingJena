package rub.inf.bi.extension.jena.vocabulary;

import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.ResourceFactory;

import rub.inf.bi.extension.jena.NamespaceManager;

public class ExtendedPropertyVocabolary {

    protected static final Property property(String local) {
    	return ResourceFactory.createProperty(
    			NamespaceManager.getInstance().getNamespace("my"), 
    			local
    	);
    }

    // simple features topological relations
    public static final Property ifc_checkProperty = property("IFC_CheckProperty");
    public static final Property ifc_checkPropertyPropFunc = property("IFC_CheckPropertyPropFunc");
    public static final Property ifc_findPropertySet = property("IFC_FindPropertySet");
    public static final Property ifc_checkPropertySetName = property("IFC_CheckPropertySetName");
    public static final Property ifc_checkPropertyName = property("IFC_CheckPropertyName");
    public static final Property ifc_convertRepresentationToWKT = property("IFC_ConvertRepresentationToWKT");
    
    //Tunneling Property Functions
    public static final Property convertAlignmentToWKT = property("ConvertAlignmentToWKT");
    

}

