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

        // return GeometryOperators3D.touch3D(geom1, geom2)? NodeValue.TRUE : NodeValue.FALSE;
        if (geom1 instanceof LineString && geom2 instanceof Polygon) {
            Coordinate sp = geom1.getCoordinates()[0];
            Coordinate ep = geom1.getCoordinates()[1];
            Vector3D spv  =new Vector3D(sp.getX(), sp.getY(), sp.getZ());
            Vector3D epv  =new Vector3D(ep.getX(), ep.getY(), ep.getZ());

            Line line = new Line(spv, epv, GeometryOperators3D.TOLERANCE);
            ArrayList<Vector3D> iPoints = GeometryOperators3D.intersection3D(line, (Polygon)geom2);
            return iPoints.size() > 0? NodeValue.TRUE:NodeValue.FALSE;
        }
        return NodeValue.FALSE;
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
        // // LP
        // if (geom1 instanceof LineString && geom2 instanceof Point) {

        //     return GeometryOperators3D.contains3D(geom1, geom2)? NodeValue.TRUE : NodeValue.FALSE;
        // }
        // // SP
        // if (geom1 instanceof Polygon && geom2 instanceof Point) {
            
        //     Geometry boundryGeom1 = geom1.getBoundary();
        //     return GeometryOperators3D.contains3D(boundryGeom1, geom2)? NodeValue.TRUE : NodeValue.FALSE;
        // }
        // // BP
        // if (geom1 instanceof MultiPolygon && geom2 instanceof Point) {

        // }

        // // LL
        // if (geom1 instanceof LineString && geom2 instanceof LineString) {

        // }
        // // SL
        // if (geom1 instanceof Polygon && geom2 instanceof LineString) {

        // }
        // // BL
        // if (geom1 instanceof MultiPolygon && geom2 instanceof LineString) {

        // }

        // // SS
        // if (geom1 instanceof Polygon && geom2 instanceof Polygon) {

        // }
        // // BS
        // if (geom1 instanceof MultiPolygon && geom2 instanceof Polygon) {

        // }

        // // BB
        // if (geom1 instanceof MultiPolygon && geom2 instanceof MultiPolygon) {

        // }
    }
    
    
}
