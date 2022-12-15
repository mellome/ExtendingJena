package rub.inf.bi.extension.jena.sparql.bspTree3D.topologicalPredicates;

import org.apache.jena.geosparql.implementation.GeometryWrapper;
import org.apache.jena.sparql.expr.NodeValue;
import org.apache.jena.sparql.function.FunctionBase2;
import org.locationtech.jts.geom.Geometry;

import rub.inf.bi.extension.jena.utils.GeometryOperators3D;

public class TopologicalDisjoint extends FunctionBase2 {

    @Override
    public NodeValue exec(NodeValue v1, NodeValue v2) {
        GeometryWrapper geometry1 = GeometryWrapper.extract(v1);
        Geometry geom1 = geometry1.getParsingGeometry();

        GeometryWrapper geometry2 = GeometryWrapper.extract(v2);
        Geometry geom2 = geometry2.getParsingGeometry();

        // if (GeometryOperators3D.disjoint3D(geom1, geom2)) {
        //     return NodeValue.TRUE;
        // }
        // return NodeValue.FALSE;
        return geom1.disjoint(geom2)? NodeValue.TRUE:NodeValue.FALSE;
    }

}
