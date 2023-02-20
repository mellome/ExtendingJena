package rub.inf.bi.extension.jena;

import org.apache.jena.geosparql.configuration.GeoSPARQLConfig;
import org.apache.jena.sparql.function.FunctionRegistry;

import rub.inf.bi.extension.jena.sparql.bspTree3D.PLIntersection3DBSP;
import rub.inf.bi.extension.jena.sparql.bspTree3D.PPIntersection3DBSP;
import rub.inf.bi.extension.jena.sparql.bspTree3D.PPIntersection3DBSPGeometry;
import rub.inf.bi.extension.jena.sparql.geometry3D.LLIntersection3D;
import rub.inf.bi.extension.jena.sparql.geometry3D.LLIntersection3DGeometry;
import rub.inf.bi.extension.jena.sparql.geometry3D.LPContains3D;
import rub.inf.bi.extension.jena.sparql.geometry3D.PLIntersection3D;
import rub.inf.bi.extension.jena.sparql.geometry3D.PLIntersection3DGeometry;
import rub.inf.bi.extension.jena.sparql.geometry3D.PPContains3D;
import rub.inf.bi.extension.jena.sparql.geometry3D.PPIntersection3D;
import rub.inf.bi.extension.jena.sparql.geometry3D.PPIntersection3DGeometry;
import rub.inf.bi.extension.jena.sparql.geometry3D.TLIntersection3D;
import rub.inf.bi.extension.jena.sparql.geometry3D.TLIntersection3DGeometry;
import rub.inf.bi.extension.jena.sparql.geometry3D.TTIntersection3D;
import rub.inf.bi.extension.jena.sparql.geometry3D.TTIntersection3DGeometry;
import rub.inf.bi.extension.jena.sparql.bspTree3D.topologicalPredicates.TopologicalDisjoint;
import rub.inf.bi.extension.jena.sparql.bspTree3D.topologicalPredicates.TopologicalOverlap;
import rub.inf.bi.extension.jena.sparql.bspTree3D.topologicalPredicates.TopologicalTouch;
import rub.inf.bi.extension.jena.sparql.bspTree3D.topologicalPredicates.TopologicalWithin;
import rub.inf.bi.extension.jena.sparql.bspTree3D.topologicalPredicates.TopologicalContain;
import rub.inf.bi.extension.jena.sparql.ifc.PerformStringDecode;
import rub.inf.bi.extension.jena.sparql.tunneling.AlignmentSegmentHasRadius;
import rub.inf.bi.extension.jena.sparql.tunneling.AlignmentCurveLength2D;
import rub.inf.bi.extension.jena.sparql.tunneling.AlignmentCurveMaxRadius;
import rub.inf.bi.extension.jena.sparql.tunneling.AlignmentCurveRadiusProfile;
import rub.inf.bi.extension.jena.vocabulary.ExtendedFunctionVocabolary;
import rub.inf.bi.extension.jena.vocabulary.ExtendedPropertyVocabolary;


public class ExtendedFunctionConfig {

    private static Boolean IS_FUNCTIONS_REGISTERED = false;

    public static final void setup() {

        //Only register functions once.
        if (!IS_FUNCTIONS_REGISTERED) {
        	//Register Functions to functionRegistry
            FunctionRegistry functionRegistry = FunctionRegistry.get();

            //Geometry 3D
            functionRegistry.put(ExtendedFunctionVocabolary.llIntersection3D.getURI(), LLIntersection3D.class);
            functionRegistry.put(ExtendedFunctionVocabolary.llIntersection3DGeometry.getURI(), LLIntersection3DGeometry.class);
            functionRegistry.put(ExtendedFunctionVocabolary.plIntersection3D.getURI(), PLIntersection3D.class);
            functionRegistry.put(ExtendedFunctionVocabolary.plIntersection3DGeometry.getURI(), PLIntersection3DGeometry.class);
            functionRegistry.put(ExtendedFunctionVocabolary.lpContains3D.getURI(), LPContains3D.class);
            functionRegistry.put(ExtendedFunctionVocabolary.ppContains3D.getURI(), PPContains3D.class);

            //!====================== yifeng's playground ===========================
            // functionRegistry.put(ExtendedFunctionVocabolary.topologicalWithin.getURI(), TopologicalWithin.class);
            // functionRegistry.put(ExtendedFunctionVocabolary.topologicalDisjoint.getURI(), TopologicalDisjoint.class);
            // functionRegistry.put(ExtendedFunctionVocabolary.topologicalTouch.getURI(), TopologicalTouch.class);
            functionRegistry.put(ExtendedFunctionVocabolary.ppIntersection3DBSP.getURI(), PPIntersection3DBSP.class);
            functionRegistry.put(ExtendedFunctionVocabolary.plIntersection3DBSP.getURI(), PLIntersection3DBSP.class);

            functionRegistry.put(ExtendedFunctionVocabolary.ppIntersection3D.getURI(), PPIntersection3D.class);
            functionRegistry.put(ExtendedFunctionVocabolary.ppIntersection3DGeometry.getURI(), PPIntersection3DGeometry.class);
            functionRegistry.put(ExtendedFunctionVocabolary.ttIntersection3D.getURI(), TTIntersection3D.class);
            functionRegistry.put(ExtendedFunctionVocabolary.ttIntersection3DGeometry.getURI(), TTIntersection3DGeometry.class);
            functionRegistry.put(ExtendedFunctionVocabolary.tlIntersection3D.getURI(), TLIntersection3D.class);
            functionRegistry.put(ExtendedFunctionVocabolary.tlIntersection3DGeometry.getURI(), TLIntersection3DGeometry.class);
            
            functionRegistry.put(ExtendedFunctionVocabolary.topologicalDISJOINT.getURI(), TopologicalDisjoint.class);
            functionRegistry.put(ExtendedFunctionVocabolary.topologicalTOUCH.getURI(), TopologicalTouch.class);
            functionRegistry.put(ExtendedFunctionVocabolary.topologicalCONTAIN.getURI(), TopologicalContain.class);
            functionRegistry.put(ExtendedFunctionVocabolary.topologicalWITHIN.getURI(), TopologicalWithin.class);
            functionRegistry.put(ExtendedFunctionVocabolary.topologicalOVERLAP.getURI(), TopologicalOverlap.class);

            functionRegistry.put(ExtendedFunctionVocabolary.ppIntersection3DBSPGeometry.getURI(), PPIntersection3DBSPGeometry.class);
            //!====================================================

            //Industrie Foundation Classes
            functionRegistry.put(ExtendedFunctionVocabolary.ifc_stringDecode.getURI(), PerformStringDecode.class);
            
            //Tunneling
            functionRegistry.put(ExtendedFunctionVocabolary.ifc_alignmentSegmentHasRadius.getURI(), AlignmentSegmentHasRadius.class);
            functionRegistry.put(ExtendedFunctionVocabolary.st_exampleFunctionB.getURI(), AlignmentSegmentHasRadius.class);
            functionRegistry.put(ExtendedFunctionVocabolary.alignmentCurveMaxRadius.getURI(), AlignmentCurveMaxRadius.class);
            functionRegistry.put(ExtendedFunctionVocabolary.alignmentCurveRadiusProfile.getURI(), AlignmentCurveRadiusProfile.class);
            functionRegistry.put(ExtendedFunctionVocabolary.alignmentCurveLength.getURI(), AlignmentCurveLength2D.class);
            
            GeoSPARQLConfig.setupMemoryIndex();
            IS_FUNCTIONS_REGISTERED = true;
        }
    }
    
}