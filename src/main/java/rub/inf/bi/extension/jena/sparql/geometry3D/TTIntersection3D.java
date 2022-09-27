package rub.inf.bi.extension.jena.sparql.geometry3D;

import java.util.ArrayList;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.jena.geosparql.implementation.GeometryWrapper;
import org.apache.jena.sparql.expr.NodeValue;
import org.apache.jena.sparql.function.FunctionBase2;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.MultiPolygon;

import rub.inf.bi.extension.jena.utils.GeometryOperators3D;

public class TTIntersection3D extends FunctionBase2 {

	@Override
	public NodeValue exec(NodeValue v1, NodeValue v2) {
		
		GeometryWrapper geometry1 = GeometryWrapper.extract(v1);
	    Geometry geom1 = geometry1.getParsingGeometry();
	    
		GeometryWrapper geometry2 = GeometryWrapper.extract(v2);
	    Geometry geom2 = geometry2.getParsingGeometry();
    	
	    if (geom1 instanceof MultiPolygon && geom2 instanceof MultiPolygon) {
	    	
	    	MultiPolygon mpG1 = (MultiPolygon)geom1;
	    	MultiPolygon mpG2 = (MultiPolygon)geom2;

	    	ArrayList<Vector3D> result = GeometryOperators3D.intersection3D(mpG1, mpG2);
	    	
	    	if(result.size() > 0) {
	    		return NodeValue.TRUE;
	    	}
	    	
	    }
	    return NodeValue.FALSE;
		
	}
	

}
