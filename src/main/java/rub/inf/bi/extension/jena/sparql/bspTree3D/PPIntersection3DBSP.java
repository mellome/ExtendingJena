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

public class PPIntersection3DBSP extends FunctionBase2{

    // NodeValue v1 and v2 are the same???
    @Override
    public NodeValue exec(NodeValue v1, NodeValue v2) {

        // TODO: test only one NodeValue
        List<Polygon> polygonsLst = node2Polygon(v1);
        BSPTree mpTree = new BSPTree();
        mpTree.buildBSPTree(polygonsLst);
        try {
            mpTree.traverse(mpTree, Vector3D.ZERO);
        } catch (IOException e) {
            System.out.println(e);
        }
        System.out.println("========================================================");
        return NodeValue.FALSE;
    }

    // @Override
    // public NodeValue exec(NodeValue v1, NodeValue v2) {

    //     // Parse the Node
    //     GeometryWrapper geometry1 = GeometryWrapper.extract(v1);
	// 	GeometryWrapper geometry2 = GeometryWrapper.extract(v2);
	//     Geometry geom1 = geometry1.getParsingGeometry();
	//     Geometry geom2 = geometry2.getParsingGeometry();	  

    //     // Extract the vertices
    //     Vector3D[] verticesGeom1 = new Vector3D[]{}; 
    //     Vector3D[] verticesGeom2 = new Vector3D[]{}; 
    //     Coordinate[] coordsGeom1 = geom1.getCoordinates();
    //     Coordinate[] coordsGeom2 = geom2.getCoordinates();

    //     for (int i=0; i<coordsGeom1.length; i++){
    //         verticesGeom1[i] = Vector3D.of( coordsGeom1[i].getX(), 
    //                                         coordsGeom1[i].getY(), 
    //                                         coordsGeom1[i].getZ() );
    //     }
    //     for (int i=0; i<coordsGeom2.length; i++){
    //         verticesGeom2[i] = Vector3D.of( coordsGeom2[i].getX(), 
    //                                         coordsGeom2[i].getY(), 
    //                                         coordsGeom2[i].getZ() );
    //     }

    //     // Extract the faces
    //     MultiPolygon mpG1 = (MultiPolygon) geom1;
    //     MultiPolygon mpG2 = (MultiPolygon) geom2;
    //     double[][] faceIndicesGeom1 = {};

    //     for (int i=0; i<mpG1.getNumGeometries(); i++){
    //         Polygon tempPo = (Polygon) mpG1.getGeometryN(i);
    //         Coordinate[] coordPo = tempPo.getCoordinates();
            
    //         double[] tempFace = new double[]{};

    //         // faceIndicesGeom1.addChild(tempFace);
    //     }

        
    //     // List<ConvexPolygon3D> faces = Planes.indexedConvexPolygons(vertices, faceIndices, precision);
    //     // // convert the vertices and faces to convex polygons and use to construct a BSP tree
    //     // RegionBSPTree3D tree = RegionBSPTree3D.from(faces);


    //     return NodeValue.FALSE;
    // }


    public static void main(String[] args) {
        // Precision.DoubleEquivalence precision = Precision.doubleEquivalenceOfEpsilon(1e-6);
        // // create a BSP tree representing the unit cube
        // RegionBSPTree3D tree = Parallelepiped.unitCube(precision).toTree();

        // // create a sphere centered on the origin
        // Sphere sphere = Sphere.from(Vector3D.ZERO, 0.65, precision);

        // // subtract a BSP tree approximation of the sphere containing 512 facets
        // // from the cube, modifying the cube tree in place
        // tree.difference(sphere.toTree(3));
        // RegionBSPTree3D tree1 = sphere.toTree(3);

        // // compute some properties of the resulting region
        // double size = tree.getSize(); // 0.11509505362599505
        // Vector3D centroid = tree.getCentroid(); // (0, 0, 0)

        // // convert to a triangle mesh
        // TriangleMesh mesh = tree.toTriangleMesh(precision);

        // Precision.DoubleEquivalence precision = Precision.doubleEquivalenceOfEpsilon(1e-6);

        // create the faces of a pyramid with a square base and its apex pointing along the
        // positive z axis
        // Vector3D[] vertices = {
        //     Vector3D.Unit.PLUS_Z,
        //     Vector3D.of(0.5, 0.5, 0.0),
        //     Vector3D.of(0.5, -0.5, 0.0),
        //     Vector3D.of(-0.5, -0.5, 0.0),
        //     Vector3D.of(-0.5, 0.5, 0.0)
        // };

        // int[][] faceIndices = {
        //     {1, 0, 2},
        //     {2, 0, 3},
        //     {3, 0, 4},
        //     {4, 0, 1},
        //     {1, 2, 3, 4}
        // };

        // Transform<Vector3D> trans = new Transform<Vector3D>() {

        //     @Override
        //     public Vector3D apply(Vector3D t) {
        //         return t;
        //     }

        //     @Override
        //     public Transform<Vector3D> inverse() {
        //         return null;
        //     }

        //     @Override
        //     public boolean preservesOrientation() {
        //         return false;
        //     }};

        // RegionBSPTree3D cubicTree = Parallelepiped.fromTransformedUnitCube(null, precision).toTree();
        // // convert the vertices and faces to convex polygons and use to construct a BSP tree
        // List<ConvexPolygon3D> faces = Planes.indexedConvexPolygons(vertices, faceIndices, precision);
        // RegionBSPTree3D tree = RegionBSPTree3D.from(faces);

        // // split the region through its centroid along a diagonal of the base
        // Plane cutter = Planes.fromPointAndNormal(tree.getCentroid(), Vector3D.Unit.from(1, 1, 0), precision);
        // Split<RegionBSPTree3D> split = tree.split(cutter);
        // // save as an OBJ file
        // IO3D.write(tree, Paths.get("src/main/resources/geoOBJ/cube-minus-sphere.obj"));
    }

    private static List<Polygon> polygonGen(){
        List<Polygon> polygons = new ArrayList<>();
        GeometryFactory gFactory = new GeometryFactory();
        Coordinate[] coordinates1 = new Coordinate[]{
            new Coordinate(4, 0, 0),
            new Coordinate(4, 4, 0),
            new Coordinate(0, 4, 0),
            new Coordinate(0, 0, 0),
            new Coordinate(4, 0, 0)
        };
        Coordinate[] coordinates2 = new Coordinate[]{
            new Coordinate(4, -2, 2),
            new Coordinate(4, 2, 2),
            new Coordinate(0, 2, 2),
            new Coordinate(0, -2, 2),
            new Coordinate(4, -2, 2)
        };

        Coordinate[] coordinates3 = new Coordinate[]{
            new Coordinate(5, -1, 2),
            new Coordinate(5, 3, 2),
            new Coordinate(1, 3, 2),
            new Coordinate(1, -1, 2),
            new Coordinate(5, -1, 2)
        };

        polygons.add(gFactory.createPolygon(coordinates1));
        polygons.add(gFactory.createPolygon(coordinates2));
        polygons.add(gFactory.createPolygon(coordinates3));

        return polygons;
    } 


    private Polygon genPolygon3D(Coordinates coords){
        // Coordinate pA1 = planeFaceArea.getCoordinates()[0];
        // Coordinate pB1 = planeFaceArea.getCoordinates()[1];
        // Coordinate pC1 = planeFaceArea.getCoordinates()[2];

        // Vector3D pt1V1 = new Vector3D(pA1.getX(), pA1.getY(), pA1.getZ());
        // Vector3D pt1V2 = new Vector3D(pB1.getX(), pB1.getY(), pB1.getZ());
        // Vector3D pt1V3 = new Vector3D(pC1.getX(), pC1.getY(), pC1.getZ());
        return null;
    }
    // public static void main(String[] args) throws IOException {
    //     BSPTree tree = new BSPTree();
    //     List<Polygon> polygons = polygonGen();
    //     tree.buildBSPTree(polygons);
    //     // tree.drawBSPTree(tree, new Vector3D(0, 0, 0));
    //     tree.drawBSPTree(tree, "root");
    // }

    private List<Polygon> node2Polygon(NodeValue... nValues){
        List<Polygon> poLst = new ArrayList<>();
        GeometryFactory fact = new GeometryFactory();

        for (NodeValue nValue : nValues){
            GeometryWrapper geoWrapper = GeometryWrapper.extract(nValue);
            MultiPolygon mpG = (MultiPolygon) geoWrapper.getParsingGeometry();

            // Extract Polygons out of MultiPolygon
            int numGeos = mpG.getNumGeometries();
            for (int i=0; i < numGeos; i++){

                try {
                    // Coordinates of each polygon
                    // Coordinate[] coordinates = mpG.getGeometryN(i).getCoordinates();
                    // LinearRing linear = new GeometryFactory().createLinearRing(coordinates);
                    // Polygon polygon = new Polygon(linear, null, fact);
                    Polygon polygon = (Polygon) mpG.getGeometryN(i);
                    poLst.add(polygon);
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        }
        return poLst;
    }
}