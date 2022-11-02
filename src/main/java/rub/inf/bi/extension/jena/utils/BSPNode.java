package rub.inf.bi.extension.jena.utils;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.geometry.euclidean.threed.Plane;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.CoordinateSequence;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.math.Plane3D;

import com.github.andrewoma.dexx.collection.ArrayList;

/**
 * @author yifeng
 */
public class BSPNode implements Serializable{
    // Structure BSPNode
    //     BSPNode pointer     frontnode, backnode;
    //     Vector              centre, transformed_centre;
    //     List                polygon_list;
    //     Plane               plane;
    //     Vector              boxmin, boxmax
    //     Vector              transformed_boxmin, transformed_boxmax;
    // End Structure
    // TODO: Do not need to consider BoundingBox situation in java. He will be implemented in Dynamo.
    
    BSPTree tree;
    BSPNode front, back;
    
    Polygon divider;
    List<Polygon> polygonSet;
    Plane partition;
    
    public BSPNode(Polygon polygon) {
        this.divider = polygon;
        this.partition = getPlane(polygon);
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




    private void drawBSPNode(){
        System.out.println();
    }

    public class BSPNodePolygon{

    }

}
