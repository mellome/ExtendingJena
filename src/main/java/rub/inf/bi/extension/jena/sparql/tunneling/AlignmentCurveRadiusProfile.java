package rub.inf.bi.extension.jena.sparql.tunneling;

import java.util.ArrayList;

import org.apache.jena.geosparql.implementation.GeometryWrapper;
import org.apache.jena.geosparql.implementation.datatype.WKTDatatype;
import org.apache.jena.sparql.expr.NodeValue;
import org.apache.jena.sparql.function.FunctionBase1;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.LineString;

public class AlignmentCurveRadiusProfile extends FunctionBase1 {

	@Override
	public NodeValue exec(NodeValue v1) {
		
		if(v1.getNode().getLiteralDatatype() instanceof WKTDatatype) {
			GeometryWrapper geometry1 = GeometryWrapper.extract(v1);
		    Geometry geom1 = geometry1.getParsingGeometry();
			
		    ArrayList<Double> curvatures = new ArrayList<Double>();
		    if (geom1 instanceof LineString) {
		    	LineString alignmentGeom = (LineString)geom1;
		    	Coordinate[] coordinates = alignmentGeom.getCoordinates();

		    	for(int i = 2; i < coordinates.length; i++) {
		    		Coordinate p0 = coordinates[i-0];
		    		Coordinate p1 = coordinates[i-1];
		    		Coordinate p2 = coordinates[i-2];
		    		
		    		double curvature = computeCurvature(p0, p1, p2);
		    		if(curvature < 0.0) {
		    			curvature = -1 * curvature;
		    		}
		    		
		    		//System.out.println(curvature);
		    		curvatures.add(curvature);
		    		
		    	}
		    }

		    return NodeValue.makeNodeString(curvatures.toString());
		}
		
        return NodeValue.makeNodeString("[]");
	}
	
	private static double computeCurvature(Coordinate p0, Coordinate p1, Coordinate p2)
    {
        double dx1 = p1.getX() - p0.getX();
        double dy1 = p1.getY() - p0.getY();
        double dx2 = p2.getX() - p0.getX();
        double dy2 = p2.getY() - p0.getY();
        double area = dx1 * dy2 - dy1 * dx2;
        double len0 = p0.distance(p1);
        double len1 = p1.distance(p2);
        double len2 = p2.distance(p0);
        return 4 * area / (len0 * len1 * len2);
    }
}

