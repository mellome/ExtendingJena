package rub.inf.bi.extension.jena.utils;

import java.util.List;

import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.geometry.euclidean.threed.Plane;
import rub.inf.bi.extension.jena.utils.*;
// import org.apache.commons.geometry.euclidean.threed.Plane;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.jena.geosparql.implementation.GeometryWrapper;
import org.apache.jena.sparql.expr.NodeValue;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.CoordinateSequence;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Polygon;

import de.javagl.obj.ObjWriter;
import de.javagl.obj.ObjReader;
import de.javagl.obj.ObjUtils;
import de.javagl.obj.Obj;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.MultiPolygon;

import java.util.logging.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.lang.reflect.Array;
// import org.locationtech.jts.math.Plane3D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class BSPTree implements Serializable{
    // Select a polygon P from the list.
    // Make a node N in the BSP tree, and add P to the list of polygons at that node.
    // For each other polygon in the list:
    //     COINCIDENT: If that polygon lies in the plane containing P, add it to the list of polygons at node N.
    //     IN_FRONT_OF: If that polygon is wholly in front of the plane containing P, move that polygon to the list of nodes in front of P.
    //     IN_BACK_OF: If that polygon is wholly behind the plane containing P, move that polygon to the list of nodes behind P.
    //     INTERSECTION: If that polygon is intersected by the plane containing P, split it into two polygons and move them to the respective lists of polygons behind and in front of P.
    // Apply this algorithm to the list of polygons in front of P.
    // Apply this algorithm to the list of polygons behind P.

    // TODO: Do not need to consider BoundingBox situation in java. He will be implemented in Dynamo.
    
    // Plane partition;
    // BSPNode treeNode; // divider <--> partition
    BSPTree parent, frontTree, backTree;
    List<BSPTree> children = new ArrayList<>();
    Plane partition;
    Polygon divider;
    List<Polygon> polygonList = new ArrayList<>(), // all the polygons are in the same plane
                  frontList = new ArrayList<>(),
                  backList = new ArrayList<>();
    Logger logger =  Logger.getLogger(this.getClass().getName());
    // final String PATH = "/Users/yhe/Developer/Repo/ExtendingJena/src/main/resources/geoOBJ/bspTree.obj";

    public BSPTree() {
        frontTree = null;
        backTree = null;
    }

    public BSPTree(BSPTree parentTree) {
        frontTree = null;
        backTree = null;
        setParentTree(parentTree);
    }

    public void setParentTree(BSPTree parentTree){
        // parentTree.addChild(this);
        this.parent = parentTree;
    }

    public void addChild(BSPTree child){
        child.setParentTree(this);
        this.children.add(child);
    }

    public void setFrontChild(BSPTree frontChild){
        frontChild.setParentTree(this);
        this.frontTree = frontChild;
    }

    public void setBackChild(BSPTree backChild){
        backChild.setParentTree(this);
        this.backTree = backChild;
    }

    public void setFrontTree(BSPTree frontTree){
        this.frontTree = frontTree;
    }

    public void setBackTree(BSPTree backTree){
        this.backTree = backTree;
    }

    public void buildBSPTree(List<Polygon> polygons){
        // select the divider/node polygon
        Polygon polygonPartition = getPolygon(polygons);
        divider = polygonPartition;
        partition = getPlane(polygonPartition);
        polygonList.add(polygonPartition);

        while(!polygons.isEmpty()){
            Polygon tempPolygon = getPolygon(polygons);
            int res = classifyPolygon(partition, tempPolygon);
            switch (res){
                case 0: // COINCIDENT   
                    polygonList.add(tempPolygon);
                    break;
                case 1: // FRONT
                    frontList.add(tempPolygon);
                    break;
                case -1: // BACK
                    backList.add(tempPolygon);
                    break;
                default: // SPANNING
                    Polygon[] splitPolygons = splitPolygon(tempPolygon, partition);
                    if(splitPolygons[0] != null){
                        frontList.add(splitPolygons[0]);
                    }
                    if(splitPolygons[1] != null){
                        backList.add(splitPolygons[1]);
                    }
                    break;
            }
        }
        if (!frontList.isEmpty()){
            BSPTree newFrontTree = new BSPTree();
            newFrontTree.buildBSPTree(frontList);
            setFrontChild(newFrontTree);
        }
        if (!backList.isEmpty()){
            BSPTree newBackTree = new BSPTree();
            newBackTree.buildBSPTree(backList);
            setBackChild(newBackTree);
        }
    } 

    private Polygon[] splitPolygon(Polygon polygon, Plane plane){
        Coordinate[] vertexList = Arrays.copyOf(polygon.getCoordinates(), polygon.getCoordinates().length-1); 
        int count = polygon.getNumPoints()-1; // get rid of the duplicate vertice in the last place.
        // int out_c = 0;
        // int in_c = 0;

        double sidePtA, sidePtB;
        Vector3D ptA, ptB;
        List<Coordinate> frontPtList = new ArrayList<>();
        List<Coordinate> backPtList = new ArrayList<>();
        ptA = new Vector3D( vertexList[count-1].getX(), 
                            vertexList[count-1].getY(), 
                            vertexList[count-1].getZ());
        sidePtA = classifyPoint(plane, ptA);

        // start with ptA
        for (int i=0; i<count; i++){
            ptB = new Vector3D(vertexList[i].getX(), vertexList[i].getY(), vertexList[i].getZ());
            sidePtB = classifyPoint(plane, ptB);

            if(sidePtB > 0) { // Point B in the front
                if(sidePtA < 0){ // Point A in the back
                    // p = p0 + tv
                    // p dot n + d = 0
                    // n dot (p1-p0) = 0
                    // t = (n dot p1 - n dot p0) / n dot u
                    // n: normal of p0
                    Vector3D vecAB = new Vector3D(ptB.getX()-ptA.getX(), ptB.getY()-ptA.getY(), ptB.getZ()-ptA.getZ());
                    double t = - classifyPoint(plane, ptA) / dotProductVector3D(getNormal(plane), vecAB);
                    Coordinate intersectedPoint = new Coordinate(
                        ptA.getX() + vecAB.getX()*t,
                        ptA.getY() + vecAB.getY()*t,
                        ptA.getZ() + vecAB.getZ()*t
                    );
                    frontPtList.add(intersectedPoint);
                    backPtList.add(intersectedPoint);
                }
                frontPtList.add(new Coordinate(ptB.getX(), ptB.getY(), ptB.getZ()));
            }
            else if( sidePtB < 0){ // Point B in the back
                if (sidePtA > 0){ // Point A in the front
                    Vector3D vecAB = new Vector3D(ptB.getX()-ptA.getX(), ptB.getY()-ptA.getY(), ptB.getZ()-ptA.getZ());

                    double t = - classifyPoint(plane, ptA) / dotProductVector3D(getNormal(plane), vecAB);
                    Coordinate intersectedPoint = new Coordinate(
                        ptA.getX() + vecAB.getX()*t,
                        ptA.getY() + vecAB.getY()*t,
                        ptA.getZ() + vecAB.getZ()*t
                    );
                    frontPtList.add(intersectedPoint);
                    backPtList.add(intersectedPoint);
                }
                backPtList.add(new Coordinate(ptB.getX(), ptB.getY(), ptB.getZ()));
            }
            else {
                frontPtList.add(new Coordinate(ptB.getX(), ptB.getY(), ptB.getZ()));
                backPtList.add(new Coordinate(ptB.getX(), ptB.getY(), ptB.getZ()));
            }
            ptA = ptB;
            sidePtA = sidePtB;
        }

        Polygon frontPo = null;
        Polygon backPo = null;
        if(frontPtList.size() > 3){
            List <Coordinate> tempFrontPts =frontPtList;
            tempFrontPts.add(frontPtList.get(0));
            frontPo = createPolygon(frontPtList);
        }
        if(backPtList.size() > 3){
            List <Coordinate> tempBackPts =backPtList;
            tempBackPts.add(backPtList.get(0));
            backPo = createPolygon(backPtList);
        }

        return new Polygon[]{
            frontPo, // front polygon
            backPo // back polygon
        };
    }

    public boolean isLeaf(){
        if (frontTree == null && backTree == null){
            return true;
        }
        return false;
        // return tree.frontList.isEmpty() && tree.backList.isEmpty() ? true:false;
    }

    public Vector3D getNormal(Plane plane) {
        // return new Vector3D(plane.getNormal().getX(), plane.getNormal().getY(), plane.getNormal().getZ());
        return plane.getNormal();
    }

    public double dotProductVector3D(Vector3D v1, Vector3D v2){
        return v1.getX()*v2.getX() + v1.getY()*v2.getY() + v1.getZ()*v2.getZ();
    }

    public Vector3D additionVector3D(Vector3D v1, Vector3D v2){
        return new Vector3D(v1.getX()+v2.getX(), v1.getY()+v2.getY(), v1.getZ()+v2.getZ());
    }

    public Vector3D subtractionVector3D(Vector3D v1, Vector3D v2){
        return new Vector3D(v1.getX()-v2.getX(), v1.getY()-v2.getY(), v1.getZ()-v2.getZ());
    }

    public Polygon getPolygon(List<Polygon> polygons){
        int randIndex = new Random().nextInt(polygons.size());
		Polygon randomPolygon = polygons.get(randIndex);
        polygons.remove(randIndex);
        return randomPolygon;
    }

    public Plane getPlane(Polygon polygon) {
        CoordinateSequence seq = polygon.getExteriorRing().getCoordinateSequence();
        Coordinate c1 = seq.getCoordinate(1);
        Coordinate c2 = seq.getCoordinate(2);
        Coordinate c3 = seq.getCoordinate(3);
        Plane p = null;
        try {
            p = new Plane(
                new Vector3D(c1.getX(), c1.getY(), c1.getZ()), 
                new Vector3D(c2.getX(), c2.getY(), c2.getZ()), 
                new Vector3D(c3.getX(), c3.getY(), c3.getZ()), 1.0e-10);
        } catch (Exception e) {
            p = new Plane(
                new Vector3D(c1.getX(), c1.getY(), c1.getZ()), 
                new Vector3D(c2.getX()*2, c2.getY()*2, c2.getZ()*2), 
                new Vector3D(c3.getX()*3, c3.getY()*3, c3.getZ()*3), 1.0e-10);
            System.out.println(e.getMessage());
            System.out.println("c1: " + c1.getX()+ " " + c1.getY() + " " + c1.getZ());
            System.out.println("c2: " + c2.getX()+ " " + c2.getY() + " " + c2.getZ());
            System.out.println("c3: " + c3.getX()+ " " + c3.getY() + " " + c3.getZ());
            System.out.println();
        }
        // return new Plane(
        //                 new Vector3D(c1.getX(), c1.getY(), c1.getZ()), 
        //                 new Vector3D(c2.getX(), c2.getY(), c2.getZ()), 
        //                 new Vector3D(c3.getX(), c3.getY(), c3.getZ()), 1.0e-10);
        return p;
    }


    /**
     * Determines the spatial relationship between a point and a plane.
     * 
     * Plane equation: n dot (p0-p1) = t 
     * p0: point in the plane (x0, y0, z0)
     * p1: input/tested point (x1, y1, z1)
     * n: normal vector
     * 
     *   n dot (p0-p1) = t
     * => a(x0-x1) + b(y0-y1) + c(z0-z1) = t
     * => n dot p0 + d = t
     * 
     * where d=-(ax1+by1+cz1)

     * @param partition
     * @param point3D
     * @return t > 0 || t < 0 : the given point is not on the plane
     *         t == 0 : the given point is on the plane
     */
    private double classifyPoint(Plane partition, Vector3D point3D){
        Vector3D p0 = partition.getOrigin();
        Vector3D p1 = point3D;
        Vector3D n = partition.getNormal();

        return dotProductVector3D(n, subtractionVector3D(p0, p1));
    }

    /* 
     * case 0: // COINCIDENT   
     * case 1: // FRONT
     * case -1: // BACK
     * case t, t!=0, 1, -1: // SPANNING
     */
    private int classifyPolygon(Plane partition, Polygon polygon){
        // List<Vector3D> vertices = new ArrayList<>();
        Coordinate[] vertices =  polygon.getCoordinates();
        final int num = vertices.length;
        int res = 0;
        for(Coordinate c : vertices){
            double tempRes = classifyPoint(partition, new Vector3D(c.getX(), c.getY(), c.getZ()));
            if(tempRes > 0.0){ // FRONT
                res++;
            }
            if(tempRes < 0.0){ // BACK
                res--;
            }
            // COINCIDENT
        }
        if(res == 0){ // COINCIDENT
            return 0;
        }
        else if(res == num){ // FRONT
            return 1;
        }
        else if(res == -num){ // BACK
            return -1;
        }
        return 2; // SPANNING
    }

    private static Polygon createPolygon(List<Coordinate> pointList){
        Coordinate[] coordinates = new Coordinate[pointList.size()]; // first and last point must be the same!!!

        for (int i=0; i<pointList.size(); i++){
            coordinates[i] = pointList.get(i);
        }
        // coordinates[pointList.size()] = pointList.get(0);
        GeometryFactory geometryFactory = new GeometryFactory();
        Polygon polygon = null;
        try {
            polygon = geometryFactory.createPolygon(coordinates);
        } catch (IllegalArgumentException e) {
            System.out.println(e);
            System.out.println(coordinates.toString());
        }
        return polygon;
    }


    public void traverse(BSPTree tree, Vector3D viewPoint) throws IOException{
        if (tree == null){
            return;
        }
        drawTreeContent(tree.polygonList);
        traverse(tree.backTree, viewPoint);
        traverse(tree.frontTree, viewPoint);
    }

    private void drawTreeContent(List<Polygon> polygons) throws IOException{
        for (Polygon p : polygons){
            Coordinate[] coordinates = p.getCoordinates();
            System.out.println(Arrays.toString(coordinates));
        }
    }


    //TODO: this can be used for performance enhancement
    private Polygon selectBestDivider(List<Polygon> polygonList){
        Polygon bestPolygon = null;
        return bestPolygon;
    }

    private BSPTree merge(BSPTree t1, BSPTree t2){
        return null;
    }

    /*
     * This is the naive version of collision detection algorithm.
     * Idea:
     * 1. classify two given polygons if they are intersected.
     */
    public boolean collisionDetect(BSPTree t1, BSPTree t2){
        // in PRE-ORDER
        if( t1 == null || t2 == null ){ // 
            return false;
        }
        if( traverseIntersection(t1, t2.divider) ){ // mid
            return true;
        }
        boolean left = collisionDetect(t1, t2.frontTree); // left
        boolean right = collisionDetect(t1, t2.backTree); // right

        return left || right;
    }

    private boolean traverseIntersection(BSPTree t, Polygon p){
        // in PRE-ORDER
        if( t == null || p == null){ 
            return false;
        }

        List<Vector3D> intersectedPoints = GeometryOperators3D.intersectionRR3D(t.divider, p);
        if(intersectedPoints.size() > 0){ // two polygons has intersection point(s)
            return true;
        }

        // if( classifyPolygon(t.partition, p) == 2 ){ // mid
        //     return true;
        // }
        boolean left = traverseIntersection(t.frontTree, p); // left
        boolean right = traverseIntersection(t.backTree, p); // right
        return left || right;
    }

    /*
     * This is an optimization algorithm.
     */
    private void treeReduce(){

    }

    public static List<Polygon> node2Polygon(NodeValue nValue){
        List<Polygon> poLst = new ArrayList<>();
        // GeometryFactory fact = new GeometryFactory();
        GeometryWrapper geoWrapper = GeometryWrapper.extract(nValue);
        Geometry geom = geoWrapper.getParsingGeometry();

        // node is "POLYGON"
        if(geom instanceof Polygon) {
            poLst.add((Polygon) geom);
            return poLst;
        }

        // node is "MULTIPOLYGON"
        MultiPolygon mpG = (MultiPolygon) geoWrapper.getParsingGeometry();
        int numGeos = mpG.getNumGeometries();
        for (int i=0; i < numGeos; i++){ // Extract Polygons out of MultiPolygon
            try {
                Polygon polygon = (Polygon) mpG.getGeometryN(i);
                poLst.add(polygon);
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        return poLst;
    }
    
    public static void main(String[] args) {
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
        List<Coordinate> c = new ArrayList<>();
        for( Coordinate coo : coordinates1){
            c.add(coo);
        }

        Polygon p = createPolygon(c);
        System.out.println(p);
    }
}
