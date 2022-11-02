package rub.inf.bi.extension.jena.sparql.bspTree3D;

// import org.apache.commons.math3.geometry.partitioning.BSPTree;
import org.apache.commons.math3.geometry.partitioning.BSPTreeVisitor;
import org.apache.commons.math3.geometry.euclidean.threed.Line;
// import org.apache.commons.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.geometry.euclidean.threed.RegionBSPTree3D;
import org.apache.commons.geometry.euclidean.threed.shape.Parallelepiped;
import org.apache.commons.geometry.euclidean.threed.shape.Sphere;
import org.apache.commons.geometry.euclidean.threed.line.Line3D;
import org.apache.commons.geometry.euclidean.threed.mesh.TriangleMesh;
import org.apache.commons.geometry.io.euclidean.threed.IO3D;
import org.apache.jena.geosparql.implementation.GeometryWrapper;
import org.apache.commons.numbers.core.Precision;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Polygon;

import rub.inf.bi.extension.jena.utils.GeometryOperators3D;
import rub.inf.bi.extension.jena.utils.BSPTree;

import org.apache.jena.sparql.expr.NodeValue;
import org.apache.jena.sparql.function.FunctionBase2;

public class BSPTree3DGeometry extends FunctionBase2{

    @Override
    public NodeValue exec(NodeValue v1, NodeValue v2) {
        GeometryWrapper geometry1 = GeometryWrapper.extract(v1);
	    Geometry geom1 = geometry1.getParsingGeometry();
		GeometryWrapper geometry2 = GeometryWrapper.extract(v2);
	    Geometry geom2 = geometry2.getParsingGeometry();	    
        RegionBSPTree3D tree1 = Parallelepiped.unitCube(Precision.doubleEquivalenceOfEpsilon(1e-6)).toTree();
        RegionBSPTree3D tree2 = Parallelepiped.unitCube(Precision.doubleEquivalenceOfEpsilon(1e-6)).toTree();

        Precision.DoubleEquivalence precision = Precision.doubleEquivalenceOfEpsilon(1e-6);

        if (geom1 instanceof LineString && geom2 instanceof LineString){
            ArrayList<Line> g1Lines = new ArrayList<Line>();
	    	for(int i = 1; i < geom1.getCoordinates().length; i++) {
	    		
	    		// Vector3D p1 = Vector3D.Unit.from(
	    		// 		geom1.getCoordinates()[i-1].getX(), 
	    		// 		geom1.getCoordinates()[i-1].getY(), 
	    		// 		geom1.getCoordinates()[i-1].getZ()
	    		// );
	    		// Vector3D p2 = Vector3D.Unit.from(
	    		// 		geom1.getCoordinates()[i].getX(), 
	    		// 		geom1.getCoordinates()[i].getY(),  
	    		// 		geom1.getCoordinates()[i].getZ()
	    		// );
	    		
                // Line3D tempLine = Line3D.
	    		// Line l = new Line(p1, p2, GeometryOperators3D.TOLERANCE);
	    		// g1Lines.add(l);
	    	}
        }
        return null;
    }

    // public static void main(String[] args) {
    //     Precision.DoubleEquivalence precision = Precision.doubleEquivalenceOfEpsilon(1e-6);
    //     // create a BSP tree representing the unit cube
    //     RegionBSPTree3D tree = Parallelepiped.unitCube(precision).toTree();

    //     // create a sphere centered on the origin
    //     Sphere sphere = Sphere.from(Vector3D.ZERO, 0.65, precision);

    //     // subtract a BSP tree approximation of the sphere containing 512 facets
    //     // from the cube, modifying the cube tree in place
    //     tree.difference(sphere.toTree(3));
    //     RegionBSPTree3D tree1 = sphere.toTree(3);

    //     // compute some properties of the resulting region
    //     double size = tree.getSize(); // 0.11509505362599505
    //     Vector3D centroid = tree.getCentroid(); // (0, 0, 0)

    //     // convert to a triangle mesh
    //     TriangleMesh mesh = tree.toTriangleMesh(precision);

    //     // save as an OBJ file
    //     IO3D.write(mesh, Paths.get("src/main/resources/geoOBJ/cube-minus-sphere.obj"));
    // }

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

        // for (int l=0; l<3; l++){
        //     for (int i=0; i<4; i++){
        //         coordinates[i] = new Coordinate(i, i+1, i+2);
        //     }
        //     polygons.add(gFactory.createPolygon(coordinates));
        //     coordinates = new Coordinate[]{};
        // }
        return polygons;
    } 

    public static void main(String[] args) throws IOException {
        BSPTree tree = new BSPTree();
        List<Polygon> polygons = polygonGen();
        tree.buildBSPTree(polygons);
        // tree.drawBSPTree(tree, new Vector3D(0, 0, 0));
        tree.drawBSPTree(tree);
    }
}