package rub.inf.bi.extension.jena.vocabulary;

import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.ResourceFactory;

import rub.inf.bi.extension.jena.NamespaceManager;

public class ExtendedFunctionVocabolary {

    protected static final Property property(String local) {
    	return ResourceFactory.createProperty(
    			NamespaceManager.getInstance().getNamespace("my"), 
    			local
    	);
    }

    // simple features topological relations
    public static final Property ifc_checkProperty = property("IFC_CheckProperty");
    public static final Property ifc_stringDecode = property("IFC_StringDecode");
    
    public static final Property st_exampleFunctionB = property("ST_ExampleB");
    
    //Geometry 3D
    public static final Property llIntersection3D = property("LLIntersection3D"); //Line and Line
    public static final Property llIntersection3DGeometry = property("LLIntersection3DGeometry"); //Geometry of Line and Line
    public static final Property plIntersection3D = property("PLIntersection3D"); //Plane and Line
    public static final Property plIntersection3DGeometry = property("PLIntersection3DGeometry"); //Geometry of Plane and Line
    public static final Property lpContains3D = property("LPContains3D"); //Line contains Point
    public static final Property ppContains3D = property("LPContains3D"); //Plane contains Point
    
    // TODO: yifeng's playground
    public static final Property topologicalWithin = property("TopologicalWithin"); // All geometries in 3D
    public static final Property topologicalDisjoint = property("TopologicalDisjoint"); // All geometries in 3D
    public static final Property topologicalTouch = property("TopologicalTouch"); // All geometries in 3D
    public static final Property ppIntersection3DBSP = property("PPIntersection3DBSP"); // All geometries in 3D

    public static final Property ppIntersection3D = property("PPIntersection3D"); //Plane and plane
    public static final Property ppIntersection3DGeometry = property("PPIntersection3DGeometry"); //Geometry of Plane and plane
    public static final Property ttIntersection3D = property("TTIntersection3D"); //Triangle and Triangle (as MultiPolygon)
    public static final Property ttIntersection3DGeometry = property("TTIntersection3DGeometry"); //Geometry of Triangle and Triangle (as MultiPolygon)
    public static final Property tlIntersection3D = property("TLIntersection3D"); //Triangle and Line (as MultiPolygon and LineString)
    public static final Property tlIntersection3DGeometry = property("TLIntersection3DGeometry"); //Geometry of Triangle and Line (as MultiPolygon and LineString)
     
    //Tunneling
    public static final Property alignmentCurveMaxRadius = property("AlignmentCurveMaxRadius");
    public static final Property alignmentCurveRadiusProfile = property("AlignmentCurveRadiusProfile"); 
    public static final Property alignmentCurveLength = property("AlignmentCurveLength");
    public static final Property ifc_alignmentSegmentHasRadius = property("IFC_AlignmentSegmentHasRadius");
    
    
    // Namespaces
    public static final String GEO = "http://www.opengis.net/ont/geosparql#";

//   public static class Nodes {
//	   // simple features topological relations
//	   public static final Node st_hasRadius = ExtendedFunctionVocabolary.st_hasRadius.asNode();
//     public static final Node st_exampleFunctionB = ExtendedFunctionVocabolary.st_exampleFunctionB.asNode();
//   }
    
}

