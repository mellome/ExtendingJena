package rub.inf.bi.extension.jena.sparql.geometry3D;

import java.util.ArrayList;

import org.apache.commons.math3.geometry.euclidean.threed.Line;
import org.apache.commons.math3.geometry.euclidean.threed.Plane;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.jena.geosparql.implementation.GeometryWrapper;
import org.apache.jena.sparql.expr.NodeValue;
import org.apache.jena.sparql.function.FunctionBase2;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Polygon;

import rub.inf.bi.extension.jena.utils.GeometryOperators3D;

public class PLIntersection3D extends FunctionBase2 {

	@Override
	public NodeValue exec(NodeValue v1, NodeValue v2) {
		
		GeometryWrapper geometry1 = GeometryWrapper.extract(v1);
	    Geometry geom1 = geometry1.getParsingGeometry();
	    
		GeometryWrapper geometry2 = GeometryWrapper.extract(v2);
	    Geometry geom2 = geometry2.getParsingGeometry();
	    
	    if (geom1 instanceof Polygon && geom2 instanceof LineString) {
	    	
	    	ArrayList<Line> g1Lines = new ArrayList<Line>();
	    	for(int i = 1; i < geom2.getCoordinates().length; i++) {
	    		
	    		Vector3D p1 = new Vector3D(
	    				geom2.getCoordinates()[i-1].getX(), 
	    				geom2.getCoordinates()[i-1].getY(), 
	    				geom2.getCoordinates()[i-1].getZ()
	    		);
	    		
	    		Vector3D p2 = new Vector3D(
	    				geom2.getCoordinates()[i].getX(), 
	    				geom2.getCoordinates()[i].getY(), 
	    				geom2.getCoordinates()[i].getZ()
	    		);
	    		
	    		Line l = new Line(p1, p2, GeometryOperators3D.TOLERANCE);
	    		g1Lines.add(l);
	    	}
	    	
	    	
	    	if((((Polygon)geom1).getCoordinates().length-1) == 3) {
	    		
	    		Coordinate pA = ((Polygon)geom1).getCoordinates()[0];
	    		Coordinate pB = ((Polygon)geom1).getCoordinates()[1];
	    		Coordinate pC = ((Polygon)geom1).getCoordinates()[2];
	    		
	    		Plane plane = new Plane(
	    				new Vector3D(pA.getX(), pA.getY(), pA.getZ()), 
	    				new Vector3D(pB.getX(), pB.getY(), pB.getZ()),
	    				new Vector3D(pC.getX(), pC.getY(), pC.getZ()), 
	    				GeometryOperators3D.TOLERANCE
	    		);
	    		
	    		for(Line l1 : g1Lines) {
	    			Vector3D p = GeometryOperators3D.intersection3D(plane, l1);
	    			//System.out.println("1:" + p);

	    			if(p != null) {
	    		      	return NodeValue.TRUE;
		    			
		    		}
		    	}
	    		
	    	}
	    	
	    }
	    return NodeValue.FALSE;
		
	}


}
