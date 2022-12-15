package rub.inf.bi.extension.jena.sparql.bspTree3D.topologicalPredicates;
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
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;

import rub.inf.bi.extension.jena.utils.GeometryOperators3D;
import rub.inf.bi.extension.jena.vocabulary.ExtendedFunctionVocabolary;

public class TopologicalTouch extends FunctionBase2{

    @Override
    public NodeValue exec(NodeValue v1, NodeValue v2) {

        GeometryWrapper geometry1 = GeometryWrapper.extract(v1);
	    Geometry geom1 = geometry1.getParsingGeometry();
	    
		GeometryWrapper geometry2 = GeometryWrapper.extract(v2);
	    Geometry geom2 = geometry2.getParsingGeometry();

        Boolean isTouched = geom1.touches(geom2);
        return isTouched? NodeValue.TRUE:NodeValue.FALSE;
        // return GeometryOperators3D.intersection3D(geom1, geom2)? NodeValue.TRUE : NodeValue.FALSE;
        /* 
         * Ref.: Borrmann-"Topological analysis of 3D building models using a spatial query language" 
         * 
         * P-Point, L-Line, S-Surface, B-Body, 
         * "TOUCH":
         *  P  L  S  B 
         *  -1 x  x  x  P
         *  -  x  x  x  L
         *  -  -  x  x  S
         *  -  -  -  x  B
         */
    }
}
