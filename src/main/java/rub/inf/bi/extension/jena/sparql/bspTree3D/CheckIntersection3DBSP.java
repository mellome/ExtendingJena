package rub.inf.bi.extension.jena.sparql.bspTree3D;

// import org.apache.commons.math3.geometry.partitioning.BSPTree;
import org.apache.commons.math3.geometry.partitioning.BSPTreeVisitor;
import org.apache.commons.math3.geometry.euclidean.threed.Line;
import org.apache.commons.math3.geometry.euclidean.threed.PolyhedronsSet;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
// import org.apache.commons.geometry.euclidean.threed.Vector3D;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;


import org.apache.commons.geometry.core.Transform;
import org.apache.commons.geometry.core.partitioning.Split;
import org.apache.commons.geometry.euclidean.threed.ConvexPolygon3D;
import org.apache.commons.geometry.euclidean.threed.Plane;
import org.apache.commons.geometry.euclidean.threed.Planes;
import org.apache.commons.geometry.euclidean.threed.RegionBSPTree3D;
import org.apache.commons.geometry.euclidean.threed.shape.Parallelepiped;
import org.apache.commons.geometry.euclidean.threed.shape.Sphere;
import org.apache.commons.geometry.euclidean.threed.line.Line3D;
import org.apache.commons.geometry.euclidean.threed.mesh.TriangleMesh;
import org.apache.commons.geometry.io.euclidean.threed.IO3D;
import org.apache.jena.geosparql.implementation.GeometryWrapper;
import org.apache.commons.numbers.core.Precision;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Coordinates;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.geom.GeometryFactory;

import rub.inf.bi.extension.jena.utils.GeometryOperators3D;
import rub.inf.bi.extension.jena.utils.BSPTree;

import org.apache.jena.sparql.expr.NodeValue;
import org.apache.jena.sparql.function.FunctionBase2;

/*
 * MultiPolygon -- Surface
 */
public class CheckIntersection3DBSP extends FunctionBase2{

    @Override
    public NodeValue exec(NodeValue v1, NodeValue v2) {
        /**
         *  TODO: 
         *  1. check all types of intersection from the given 3D scene.
         *  2. return intersected geometry. (Point, Line, Surface)
         */

        return null;
    }
    
}
