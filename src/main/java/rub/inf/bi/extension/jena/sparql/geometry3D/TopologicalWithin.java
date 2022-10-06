package rub.inf.bi.extension.jena.sparql.geometry3D;

import org.apache.jena.geosparql.implementation.GeometryWrapper;
import org.apache.jena.sparql.expr.NodeValue;
import org.apache.jena.sparql.function.FunctionBase2;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.geom.util.PointExtracter;

import rub.inf.bi.extension.jena.utils.GeometryOperators3D;

import org.apache.commons.math3.geometry.euclidean.threed.Line;

public class TopologicalWithin extends FunctionBase2{
        
    // TODO: check the conditions of POINT, LINE, SURFACE, BODY ?
    @Override
    public NodeValue exec(NodeValue v1, NodeValue v2) {

        GeometryWrapper geometry1 = GeometryWrapper.extract(v1);
	    Geometry geom1 = geometry1.getParsingGeometry();
	    
		GeometryWrapper geometry2 = GeometryWrapper.extract(v2);
	    Geometry geom2 = geometry2.getParsingGeometry();	
        
        if(GeometryOperators3D.contains3D(geom1, geom2)) {
            return NodeValue.TRUE;
        }
        return NodeValue.FALSE;
    }
    
}
