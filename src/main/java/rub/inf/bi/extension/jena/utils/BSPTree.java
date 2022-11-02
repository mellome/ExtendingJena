package rub.inf.bi.extension.jena.utils;

import java.util.List;

import org.apache.commons.math3.geometry.euclidean.threed.Plane;
// import org.apache.commons.geometry.euclidean.threed.Plane;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.CoordinateSequence;
import org.locationtech.jts.geom.Polygon;

import de.javagl.obj.ObjWriter;
import de.javagl.obj.ObjReader;
import de.javagl.obj.ObjUtils;
import de.javagl.obj.Obj;
import org.locationtech.jts.geom.GeometryFactory;
import java.util.logging.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
// import org.locationtech.jts.math.Plane3D;
import java.util.ArrayList;
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
    
    Plane partition;
    BSPNode divider; // divider <--> partition
    BSPTree tree, frontTree, backTree;
    List<Polygon> polygonList = new ArrayList<>(), // all the polygons are in the same plane
                  frontList = new ArrayList<>(),
                  backList = new ArrayList<>();
    Logger logger =  Logger.getLogger(this.getClass().getName());
    final String PATH = "/Users/yhe/Developer/Repo/ExtendingJena/src/main/resources/geoOBJ/bspTree.obj";

    public BSPTree() {
        tree = null;
        frontTree = null;
        backTree = null;
        divider = null;
    }

    public void buildBSPTree(List<Polygon> polygons){
        // select the divider/node polygon
        Polygon rootPolygon = getPolygon(polygons);
        divider = new BSPNode(rootPolygon);
        tree = new BSPTree();

        tree.partition = getPlane(rootPolygon);
        tree.polygonList.add(rootPolygon);
        
        while(!polygons.isEmpty()){
            Polygon tempPolygon = getPolygon(polygons);
            int res = classifyPolygon(tree.partition, tempPolygon);
            switch (res){
                case 0: // COINCIDENT   
                    tree.polygonList.add(tempPolygon);
                    break;
                case 1: // FRONT
                    tree.frontList.add(tempPolygon);
                    break;
                case -1: // BACK
                    tree.backList.add(tempPolygon);
                    break;
                default: // SPANNING
                    Polygon[] splitPolygons = splitPolygon(tempPolygon, tree.partition);
                    tree.frontList.add(splitPolygons[0]);
                    tree.backList.add(splitPolygons[1]);
                    break;
            }
        }

        if (!tree.frontList.isEmpty()){
            tree.frontTree = new BSPTree();
            tree.frontTree.buildBSPTree(tree.frontList);
        }
        if (!tree.backList.isEmpty()){
            tree.backTree = new BSPTree();
            tree.backTree.buildBSPTree(tree.backList);
        }
    } 

    private Polygon[] splitPolygon(Polygon polygon, Plane plane){
        Coordinate[] vertexList = polygon.getCoordinates();
        int count = polygon.getNumPoints(); // total number of vertices in the given polygon
        int out_c = 0;
        int in_c = 0;

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

                    double t = - sidePtA / dotProductVector3D(getNormal(plane), vecAB);
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

                    double t = - sidePtA / dotProductVector3D(getNormal(plane), vecAB);
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

        return new Polygon[]{
            createPolygon(frontPtList, out_c), // front polygon
            createPolygon(backPtList, in_c) // back polygon
        };
    }

    public boolean isLeaf(){
        if (tree.frontList.isEmpty() && tree.backList.isEmpty()){
            return true;
        }
        return false;
        // return tree.frontList.isEmpty() && tree.backList.isEmpty() ? true:false;
    }

    public Vector3D getNormal(Plane plane) {
        return new Vector3D(plane.getNormal().getX(), plane.getNormal().getY(), plane.getNormal().getZ());
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
		Polygon randomPolygon = polygons.get(new Random().nextInt(polygons.size()));
        polygons.remove(randomPolygon);
        return randomPolygon;
    }

    public Plane getPlane(Polygon polygon){
        CoordinateSequence seq = polygon.getExteriorRing().getCoordinateSequence();
        Coordinate c1 = seq.getCoordinate(1);
        Coordinate c2 = seq.getCoordinate(2);
        Coordinate c3 = seq.getCoordinate(3);
        return new Plane(
                        new Vector3D(c1.getX(), c1.getY(), c1.getZ()), 
                        new Vector3D(c2.getX(), c2.getY(), c2.getZ()), 
                        new Vector3D(c3.getX(), c3.getY(), c3.getZ()), 0);
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

    private Polygon createPolygon(List<Coordinate> pointList, int c){
        Coordinate[] coordinates = new Coordinate[]{};
        for (int i=0; i<pointList.size(); i++){
            coordinates[i] = pointList.get(i);
        }
        GeometryFactory geometryFactory = new GeometryFactory();
        Polygon polygon = geometryFactory.createPolygon(coordinates);
        return polygon;
    }


    public void drawBSPTree(Vector3D viewPoint) throws IOException{
        
        if (tree.isLeaf()){
            drawTreeContent(tree.divider.node.getCoordinates());
        }

        double side = classifyPoint(tree.partition, viewPoint);
        if(side >= 0.0){ // INFRONT/CONINCIDING 
            tree.frontTree.drawBSPTree(viewPoint);
            drawTreeContent(tree.divider.node.getCoordinates());
            tree.backTree.drawBSPTree(viewPoint);
        }
        else{ // BEHIND/BACK
            tree.backTree.drawBSPTree(viewPoint);
            drawTreeContent(tree.divider.node.getCoordinates());
            tree.frontTree.drawBSPTree(viewPoint);
        }
    }

    private void drawTreeContent(Coordinate[] coordinates) throws IOException{
        InputStream input = new FileInputStream(new File(PATH));
        OutputStream optStream = new FileOutputStream(new File(PATH));
        Obj obj = ObjUtils.convertToRenderable(ObjReader.read(input));;
        
        for (Coordinate c: coordinates){
            obj.addVertex((float)c.getX(), (float)c.getY(), (float)c.getZ());
        }
        ObjWriter.write(obj, optStream);
    }

    //TODO: this can be used for performance enhancement
    private Polygon selectBestDivider(List<Polygon> polygonList){
        Polygon bestPolygon = null;
        return bestPolygon;
    }
}
