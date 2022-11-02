package rub.inf.bi.extension.jena.utils;

import java.io.Serializable;

import org.apache.commons.geometry.euclidean.threed.Plane;
import org.apache.commons.geometry.euclidean.threed.Vector3D;
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
    BSPNode frontnode, backnode;
    
    Polygon divider;
    Polygon[] polygonSet;
    Plane partition;
    
    public BSPNode(Polygon polygon) {
        divider = polygon;
        partition = 
    }




    private void drawBSPNode(){
        System.out.println();
    }

    
}
