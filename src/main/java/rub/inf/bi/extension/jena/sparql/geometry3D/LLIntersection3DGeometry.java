package rub.inf.bi.extension.jena.sparql.geometry3D;

import java.util.ArrayList;

import org.apache.commons.math3.geometry.euclidean.threed.Line;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.jena.geosparql.implementation.GeometryWrapper;
import org.apache.jena.geosparql.implementation.GeometryWrapperFactory;
import org.apache.jena.geosparql.implementation.datatype.WKTDatatype;
import org.apache.jena.sparql.expr.NodeValue;
import org.apache.jena.sparql.function.FunctionBase2;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.LineString;

import rub.inf.bi.extension.jena.utils.GeometryOperators3D;
import rub.inf.bi.extension.jena.vocabulary.ExtendedFunctionVocabolary;

public class LLIntersection3DGeometry extends FunctionBase2 {
	
	@Override
	public NodeValue exec(NodeValue v1, NodeValue v2) {
		
		GeometryWrapper geometry1 = GeometryWrapper.extract(v1);
	    Geometry geom1 = geometry1.getParsingGeometry();
	    
		GeometryWrapper geometry2 = GeometryWrapper.extract(v2);
	    Geometry geom2 = geometry2.getParsingGeometry();	    
	    
	    ArrayList<Coordinate> intersectionPoints = new ArrayList<Coordinate>();
	    if (geom1 instanceof LineString && geom2 instanceof LineString) {
	    	
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
	    	
	    	ArrayList<Line> g2Lines = new ArrayList<Line>();
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
	    		g2Lines.add(l);
	    	}
	    	
	    	for(Line l1 : g1Lines) {
	    		for(Line l2 : g2Lines) {
	    			Vector3D p = GeometryOperators3D.intersection3D(l1, l2);
	    			//System.out.println("1:" + p);

	    			if(p != null) {
	    				intersectionPoints.add(new Coordinate(p.getX(), p.getY(), p.getZ()));
		    		}
		    	}
	    	}
	   
	    }
	    
	    GeometryWrapper newGeo = GeometryWrapperFactory.createMultiPoint(intersectionPoints, ExtendedFunctionVocabolary.GEO + "wktLiteral");
	    return NodeValue.makeNode(newGeo.asLiteral().getLexicalForm(), WKTDatatype.INSTANCE);
	}

}
