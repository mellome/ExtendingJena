package rub.inf.bi.extension.jena.sparql.tunneling;

import org.apache.jena.geosparql.implementation.GeometryWrapper;
import org.apache.jena.geosparql.implementation.datatype.WKTDatatype;
import org.apache.jena.sparql.expr.NodeValue;
import org.apache.jena.sparql.function.FunctionBase1;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.LineString;

public class AlignmentCurveLength3D extends FunctionBase1 {

	@Override
	public NodeValue exec(NodeValue v1) {
		
		if(v1.getNode().getLiteralDatatype() instanceof WKTDatatype) {
			GeometryWrapper geometry1 = GeometryWrapper.extract(v1);
		    Geometry geom1 = geometry1.getParsingGeometry();
			
		    double distanceSum = 0.0;
		    if (geom1 instanceof LineString) {
		    	LineString alignmentGeom = (LineString)geom1;
		    	Coordinate[] coordinates = alignmentGeom.getCoordinates();

		    	for(int i = 1; i < coordinates.length; i++) {
		    		Coordinate p0 = coordinates[i-0];
		    		Coordinate p1 = coordinates[i-1];
		    		double distance = p0.distance3D(p1);
		    		distanceSum += distance;
		    		
		    	}
		    }
		    return NodeValue.makeDouble(distanceSum);
		}
		
        return NodeValue.makeDouble(Double.NaN);
	}
	
}

