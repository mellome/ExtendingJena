package rub.inf.bi.extension.jena.sparql.geometry3D;

import java.util.ArrayList;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.jena.geosparql.implementation.GeometryWrapper;
import org.apache.jena.geosparql.implementation.GeometryWrapperFactory;
import org.apache.jena.geosparql.implementation.datatype.WKTDatatype;
import org.apache.jena.sparql.expr.NodeValue;
import org.apache.jena.sparql.function.FunctionBase2;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.MultiPolygon;

import rub.inf.bi.extension.jena.utils.GeometryOperators3D;
import rub.inf.bi.extension.jena.vocabulary.ExtendedFunctionVocabolary;

public class TTIntersection3DGeometry extends FunctionBase2 {

	@Override
	public NodeValue exec(NodeValue v1, NodeValue v2) {
		
		GeometryWrapper geometry1 = GeometryWrapper.extract(v1);
	    Geometry geom1 = geometry1.getParsingGeometry();
	    
		GeometryWrapper geometry2 = GeometryWrapper.extract(v2);
	    Geometry geom2 = geometry2.getParsingGeometry();
    	
	    ArrayList<Coordinate> coordinates = new ArrayList<Coordinate>();
	    if (geom1 instanceof MultiPolygon && geom2 instanceof MultiPolygon) {
	    	
	    	MultiPolygon mpG1 = (MultiPolygon)geom1;
	    	MultiPolygon mpG2 = (MultiPolygon)geom2;

	    	ArrayList<Vector3D> results = GeometryOperators3D.intersection3D(mpG1, mpG2);
	    	for(Vector3D v : results) {
	    		coordinates.add(new Coordinate(v.getX(), v.getY(), v.getZ()));
	    	}
	    	
	    }
	    
	    GeometryWrapper newGeo = GeometryWrapperFactory.createMultiPoint(coordinates, ExtendedFunctionVocabolary.GEO + "wktLiteral");
	    return NodeValue.makeNode(newGeo.asLiteral().getLexicalForm(), WKTDatatype.INSTANCE);
	}
	

}
