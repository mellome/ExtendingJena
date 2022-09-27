package rub.inf.bi.extension.jena;

import org.apache.jena.sparql.pfunction.PropertyFunctionRegistry;

import rub.inf.bi.extension.jena.sparql.ifc.CheckPropertyName;
import rub.inf.bi.extension.jena.sparql.ifc.CheckPropertyPropFunc;
import rub.inf.bi.extension.jena.sparql.ifc.CheckPropertySetName;
import rub.inf.bi.extension.jena.sparql.ifc.ConvertRepresentationToWKT;
import rub.inf.bi.extension.jena.sparql.ifc.FindPropertySet;
import rub.inf.bi.extension.jena.sparql.tunneling.AlignmentCurveLength2D;
import rub.inf.bi.extension.jena.sparql.tunneling.ConvertAlignmentToWKT;
import rub.inf.bi.extension.jena.vocabulary.ExtendedPropertyVocabolary;


public class ExtendedPropertyConfig {

    private static Boolean IS_PROPFUNCTIONS_REGISTERED = false;

    public static final void setup() {

        //Only register functions once.
        if (!IS_PROPFUNCTIONS_REGISTERED) {
        	//Register Functions to functionRegistry
            PropertyFunctionRegistry propertyFunctionRegistry = PropertyFunctionRegistry.get();

            //Geometry 3D
            //TODO
            
            //Industrie Foundation Classes
            propertyFunctionRegistry.put(ExtendedPropertyVocabolary.ifc_checkPropertyPropFunc.getURI(), CheckPropertyPropFunc.class); //Test Only, can be removed
            propertyFunctionRegistry.put(ExtendedPropertyVocabolary.ifc_findPropertySet.getURI(), FindPropertySet.class);
            propertyFunctionRegistry.put(ExtendedPropertyVocabolary.ifc_checkPropertySetName.getURI(), CheckPropertySetName.class);
            propertyFunctionRegistry.put(ExtendedPropertyVocabolary.ifc_checkPropertyName.getURI(), CheckPropertyName.class);
            propertyFunctionRegistry.put(ExtendedPropertyVocabolary.ifc_convertRepresentationToWKT.getURI(), ConvertRepresentationToWKT.class);

            //Tunneling
            propertyFunctionRegistry.put(ExtendedPropertyVocabolary.convertAlignmentToWKT.getURI(), ConvertAlignmentToWKT.class);
            
            IS_PROPFUNCTIONS_REGISTERED = true;
        }
    }
    
}