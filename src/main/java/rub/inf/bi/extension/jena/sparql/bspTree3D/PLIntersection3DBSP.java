package rub.inf.bi.extension.jena.sparql.bspTree3D;

import org.apache.jena.sparql.expr.NodeValue;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.geometry.euclidean.threed.Line;
import org.apache.commons.geometry.euclidean.threed.line.Lines3D;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
// import org.apache.commons.geometry.euclidean.threed.Vector3D;
// import org.apache.commons.math3.util.Precision;
import org.apache.commons.numbers.core.Precision;
import org.apache.commons.numbers.core.Precision.DoubleEquivalence;
import org.apache.jena.geosparql.implementation.GeometryWrapper;
import org.apache.jena.sparql.expr.NodeValue;
import org.apache.jena.sparql.function.FunctionBase2;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;

import rub.inf.bi.extension.jena.utils.BSPTree;
import rub.inf.bi.extension.jena.utils.GeometryOperators3D;

import org.apache.jena.sparql.function.FunctionBase2;

/*
 * MultiPolygon/Polygon -- LineString/Line
 */
public class PLIntersection3DBSP extends FunctionBase2{

    @Override
    public NodeValue exec(NodeValue v1, NodeValue v2) {
        GeometryWrapper geometry1 = GeometryWrapper.extract(v1);
	    Geometry geom1 = geometry1.getParsingGeometry();
	    
		GeometryWrapper geometry2 = GeometryWrapper.extract(v2);
	    Geometry geom2 = geometry2.getParsingGeometry();	    
        // Precision.DoubleEquivalence precision = Precision.doubleEquivalenceOfEpsilon(1e-6);

	    if (geom1 instanceof MultiPolygon && geom2 instanceof LineString) {
            System.out.println("=========================(Multi)Polygon=============================");
            List<Polygon> polygonsLst1 = BSPTree.node2Polygon(v1);
            BSPTree t1 = new BSPTree();
            t1.buildBSPTree(polygonsLst1);
            // try {
            //     t1.traverse(t1, Vector3D.ZERO);
            // } catch (IOException e) {
            //     System.out.println(e);
            // }

            System.out.println("=========================Line(String)=============================");
	    	for(int i = 1; i < geom2.getCoordinates().length; i++) {
	    		
	    		Vector3D p1 = new Vector3D(
	    				geom2.getCoordinates()[i-1].getX(), 
	    				geom2.getCoordinates()[i-1].getY(), 
	    				geom2.getCoordinates()[i-1].getZ()
	    		);
	    		
	    		Vector3D p2 = new Vector3D(
	    				geom2.getCoordinates()[i].getX(), 
	    				geom2.getCoordinates()[i].getY(), 
	    				geom2.getCoordinates()[i].getZ()
	    		);
	    		
	    		// Line l = new Line(p1, p2, GeometryOperators3D.TOLERANCE);
	    		// lines.add(l);
                if(t1.collisionDetect(t1, p1, p2)){
                    System.out.println("(Multi)Polygon -- Line(String) intersection: true");
                    return NodeValue.TRUE;
                }
                System.out.println("========================================================");
	    	}
        }
        System.out.println("(Multi)Polygon -- Line(String) intersection: false");
        return NodeValue.FALSE;
    }
        
    
}
