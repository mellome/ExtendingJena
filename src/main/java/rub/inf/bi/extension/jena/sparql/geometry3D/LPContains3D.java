package rub.inf.bi.extension.jena.sparql.geometry3D;

import java.util.ArrayList;

import org.apache.commons.math3.geometry.euclidean.threed.Line;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.jena.geosparql.implementation.GeometryWrapper;
import org.apache.jena.sparql.expr.NodeValue;
import org.apache.jena.sparql.function.FunctionBase2;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;

import rub.inf.bi.extension.jena.utils.GeometryOperators3D;

public class LPContains3D extends FunctionBase2 {

	@Override
	public NodeValue exec(NodeValue v1, NodeValue v2) {
		
		
		GeometryWrapper geometry1 = GeometryWrapper.extract(v1);
	    Geometry geom1 = geometry1.getParsingGeometry();
	    
		GeometryWrapper geometry2 = GeometryWrapper.extract(v2);
	    Geometry geom2 = geometry2.getParsingGeometry();	    
	    
	    if (geom1 instanceof LineString && geom2 instanceof Point) {
	    	
	    	ArrayList<Line> g1Lines = new ArrayList<Line>();
	    	for(int i = 1; i < geom1.getCoordinates().length; i++) {
	    		
	    		Vector3D p1 = new Vector3D(
	    				geom1.getCoordinates()[i-1].getX(), 
	    				geom1.getCoordinates()[i-1].getY(), 
	    				geom1.getCoordinates()[i-1].getZ()
	    		);
	    		
	    		Vector3D p2 = new Vector3D(
	    				geom1.getCoordinates()[i].getX(), 
	    				geom1.getCoordinates()[i].getY(), 
	    				geom1.getCoordinates()[i].getZ()
	    		);
	    		
	    		Line l = new Line(p1, p2, GeometryOperators3D.TOLERANCE);
	    		g1Lines.add(l);
	    	}
	    	
	    	for(Line l1 : g1Lines) {
    			Vector3D vPoint = new Vector3D(
    					((Point)geom2).getCoordinate().getX(), 
    					((Point)geom2).getCoordinate().getY(), 
    					((Point)geom2).getCoordinate().getZ()
    			);
    			if(GeometryOperators3D.contains3D(l1, vPoint)) {
    		      	return NodeValue.TRUE;
	    			
	    		}
	    	}
	   
	    }
	    return NodeValue.FALSE;
		
	}


}
