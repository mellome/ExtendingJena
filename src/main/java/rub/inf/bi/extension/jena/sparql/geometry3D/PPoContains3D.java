package rub.inf.bi.extension.jena.sparql.geometry3D;

import org.apache.commons.math3.geometry.euclidean.threed.Plane;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.jena.geosparql.implementation.GeometryWrapper;
import org.apache.jena.sparql.expr.NodeValue;
import org.apache.jena.sparql.function.FunctionBase1;
import org.apache.jena.sparql.function.FunctionBase2;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Polygon;

import rub.inf.bi.extension.jena.utils.GeometryOperators3D;

import org.apache.jena.geosparql.implementation.GeometryWrapper;

public class PPoContains3D extends FunctionBase2{

    @Override
    public NodeValue exec(NodeValue v1, NodeValue v2) {
        
        GeometryWrapper geometry1 = GeometryWrapper.extract(v1);
	    Geometry geom1 = geometry1.getParsingGeometry();
	    
		GeometryWrapper geometry2 = GeometryWrapper.extract(v2);
	    Geometry geom2 = geometry2.getParsingGeometry();	
        
        System.out.println("Into the PPoContaines");
        if (geom1 instanceof Polygon && geom2 instanceof Polygon ){
            // TODO: reset the if 
            if(GeometryOperators3D.contains3D(geom1, geom2)) {
                return NodeValue.TRUE;
            }
        }

        return NodeValue.FALSE;
    }
    
}
