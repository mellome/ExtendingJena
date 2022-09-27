package rub.inf.bi.extension.jena.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.geosparql.implementation.GeometryWrapper;
import org.apache.jena.geosparql.implementation.GeometryWrapperFactory;
import org.apache.jena.geosparql.implementation.datatype.WKTDatatype;
import org.apache.jena.graph.Graph;
import org.apache.jena.graph.Node;
import org.apache.jena.graph.NodeFactory;
import org.apache.jena.graph.Triple;
import org.apache.jena.util.iterator.ExtendedIterator;
import org.locationtech.jts.geom.Coordinate;

import rub.inf.bi.extension.jena.NamespaceManager;
import rub.inf.bi.extension.jena.vocabulary.ExtendedFunctionVocabolary;

public class IFCOWLtoWKTFunctions {
	
	private static Node findSpecificChildSimplified(Graph graph, Node parent, String predicate) {
		Set<Node> set = new HashSet<Node>();
        Node predicat = NodeFactory.createURI(predicate);
        RDFGraphNodeFunctions.findSpecificChild(set, graph, parent, predicat);
        
        if(!set.iterator().hasNext()) {
        	return null;
        }
        
        Node result = set.iterator().next();
        return result;
	}

	public static Node parseExtrudeAreaSolid(Graph graph, Node parent){

		ArrayList<Coordinate> result = new ArrayList<>();

        //==============================
        //Parse area dimensions
		
		//Example Pattern: inst:IfcExtrudedAreaSolid_226 ifc:sweptArea_IfcSweptAreaSolid inst:IfcRectangleProfileDef_223 .
        Node currentSweptArea = findSpecificChildSimplified(
        		graph, 
        		parent, 
        		NamespaceManager.getInstance().getNamespace("ifc") + "sweptArea_IfcSweptAreaSolid"
        );
        
        Node currentXDim = findSpecificChildSimplified(
        		graph, 
        		currentSweptArea, 
        		NamespaceManager.getInstance().getNamespace("ifc") + "xDim_IfcRectangleProfileDef"
        );
        
        Node currentXDimValue = findSpecificChildSimplified(
        		graph, 
        		currentXDim, 
        		NamespaceManager.getInstance().getNamespace("express") + "hasDouble"
        );
        double xDimValue = Double.parseDouble(currentXDimValue.getLiteralValue().toString());
        
        
        
        Node currentYDim = findSpecificChildSimplified(
        		graph, 
        		currentSweptArea, 
        		NamespaceManager.getInstance().getNamespace("ifc") + "yDim_IfcRectangleProfileDef"
        );
        
        Node currentYDimValue = findSpecificChildSimplified(
        		graph, 
        		currentYDim, 
        		NamespaceManager.getInstance().getNamespace("express") + "hasDouble"
        );
        double yDimValue = Double.parseDouble(currentYDimValue.getLiteralValue().toString());
        
        //==============================
        
		//Example Pattern: inst:IfcExtrudedAreaSolid_226 ifc:position_IfcSweptAreaSolid inst:IfcAxis2Placement3D_6231 .
        Node currentPosition = findSpecificChildSimplified(
        		graph, 
        		parent, 
        		NamespaceManager.getInstance().getNamespace("ifc") + "position_IfcSweptAreaSolid"
        );
        
		//Example Pattern: inst:IfcExtrudedAreaSolid_226 ifc:extrudedDirection_IfcExtrudedAreaSolid inst:IfcDirection_9 .
        Node currentExtrudedDirection = findSpecificChildSimplified(
        		graph, 
        		parent, 
        		NamespaceManager.getInstance().getNamespace("ifc") + "extrudedDirection_IfcExtrudedAreaSolid"
        );
        
        //Example Pattern: inst:IfcExtrudedAreaSolid_226 ifc:depth_IfcExtrudedAreaSolid inst:IfcPositiveLengthMeasure_43756 .
        Node currentDepth = findSpecificChildSimplified(
        		graph, 
        		parent, 
        		NamespaceManager.getInstance().getNamespace("ifc") + "depth_IfcExtrudedAreaSolid"
        );
        
        //==============================
        //Parse Location
        
        Node currentLocation = findSpecificChildSimplified(
        		graph, 
        		currentPosition, 
        		NamespaceManager.getInstance().getNamespace("ifc") + "location_IfcPlacement"
        );
        
        Node currentCoordinates = findSpecificChildSimplified(
        		graph, 
        		currentLocation, 
        		NamespaceManager.getInstance().getNamespace("ifc") + "coordinates_IfcCartesianPoint"
        );
       
        
        Coordinate coordinate = new Coordinate();
        int index = 0;
        
        boolean nextLengthMeasureExists = true;
        while(nextLengthMeasureExists){
        	 
            Node currentLenghtMeasureList = findSpecificChildSimplified(
            		graph, 
            		currentCoordinates, 
            		NamespaceManager.getInstance().getNamespace("list") + "hasContents"
            );
            
        	Node currentLenghtMeasure = findSpecificChildSimplified(
            		graph, 
            		currentLenghtMeasureList, 
            		NamespaceManager.getInstance().getNamespace("express") + "hasDouble"
            );
        	
            if(index == 0) { coordinate.setX(Double.parseDouble(currentLenghtMeasure.getLiteralValue().toString())); }
            if(index == 1) { coordinate.setY(Double.parseDouble(currentLenghtMeasure.getLiteralValue().toString())); }
            if(index == 2) { coordinate.setZ(Double.parseDouble(currentLenghtMeasure.getLiteralValue().toString())); }
            
            
            Node tempCoordinates = findSpecificChildSimplified(
            		graph, 
            		currentLenghtMeasureList, 
            		NamespaceManager.getInstance().getNamespace("list") + "hasNext"
            );
            
            if(tempCoordinates == null) {            	
            	nextLengthMeasureExists = false;
            }else {
            	currentCoordinates = tempCoordinates; 
            	index++;
            }
        }

        result.add(new Coordinate(
			coordinate.getX() + xDimValue/2, 
			coordinate.getY() + yDimValue/2, 
			coordinate.getZ()
		));
        
        result.add(new Coordinate(
    			coordinate.getX() + xDimValue/2, 
    			coordinate.getY() - yDimValue/2, 
    			coordinate.getZ()
		));
        
        result.add(new Coordinate(
    			coordinate.getX() - xDimValue/2, 
    			coordinate.getY() - yDimValue/2, 
    			coordinate.getZ()
		));
    
        result.add(new Coordinate(
    			coordinate.getX() - xDimValue/2, 
    			coordinate.getY() + yDimValue/2, 
    			coordinate.getZ()
		));	
        result.add(result.get(0));
        
        //==============================
        
        Node currentAxis = findSpecificChildSimplified(
        		graph, 
        		currentPosition, 
        		NamespaceManager.getInstance().getNamespace("ifc") + "axis_IfcAxis2Placement3D"
        );
        
        Node currentRefDirection = findSpecificChildSimplified(
        		graph, 
        		currentPosition, 
        		NamespaceManager.getInstance().getNamespace("ifc") + "refDirection_IfcAxis2Placement3D"
        );
        
		
	    GeometryWrapper newGeo = GeometryWrapperFactory.createPolygon(result, NamespaceManager.getInstance().getNamespace("geo") + "wktLiteral");
		return NodeFactory.createLiteral(newGeo.asLiteral().getLexicalForm(), WKTDatatype.INSTANCE);
    }
    
	/**
	 * Creates a Node containing a LineString WKT-literal based on provided ifcOWL formated IfcPolyLine.
	 * 
	 * @param graph
	 * @param parent
	 * @return
	 */
	public static Node parsePolyLine(Graph graph, Node parent){
        
		//Example Pattern: inst:IfcPolyline_5972 ifc:points_IfcPolyline inst:IfcCartesianPoint_List_45659 .
        Set<Node> pointLists = new HashSet<Node>();
        Node predicat = NodeFactory.createURI(NamespaceManager.getInstance().getNamespace("ifc") + "points_IfcPolyline");
        RDFGraphNodeFunctions.findSpecificChild(pointLists, graph, parent, predicat);
        Node currentPointList = pointLists.iterator().next();
        
        ArrayList<Coordinate> result = new ArrayList<>();
        
        boolean nextPointExists = true;
        while(nextPointExists){
        	//Parse current pointList
        	Set<Node> points = new HashSet<Node>();
            Node predicat2 = NodeFactory.createURI(NamespaceManager.getInstance().getNamespace("list") + "hasContents");
            RDFGraphNodeFunctions.findSpecificChild(points, graph, currentPointList, predicat2);
            Node cartesianPoint = points.iterator().next();
            
            //Retrieve value of pointList
            Set<Node> coordinates = new HashSet<Node>();
            Node predicat4 = NodeFactory.createURI(NamespaceManager.getInstance().getNamespace("ifc") + "coordinates_IfcCartesianPoint");
            RDFGraphNodeFunctions.findSpecificChild(coordinates, graph, cartesianPoint, predicat4);
            Node currentLengthMeasures = coordinates.iterator().next();
            
            Coordinate coordinate = new Coordinate();
            int index = 0;
            
            boolean nextLengthMeasureExists = true;
            while(nextLengthMeasureExists){
            	//Parse current lengthMeasurement
            	Set<Node> measures = new HashSet<Node>();
                Node predicat5 = NodeFactory.createURI(NamespaceManager.getInstance().getNamespace("list") + "hasContents");
                RDFGraphNodeFunctions.findSpecificChild(measures, graph, currentLengthMeasures, predicat5);
                Node measure = measures.iterator().next();
            	
                //Retrieve value of lengthMeasurement
                Set<Node> measureValues = new HashSet<Node>();
                Node predicat7 = NodeFactory.createURI(NamespaceManager.getInstance().getNamespace("express") + "hasDouble");
                RDFGraphNodeFunctions.findSpecificChild(measureValues, graph, measure, predicat7);
                Node measureValue = measureValues.iterator().next();
                
                if(index == 0) { coordinate.setX(Double.parseDouble(measureValue.getLiteralValue().toString())); }
                if(index == 1) { coordinate.setY(Double.parseDouble(measureValue.getLiteralValue().toString())); }
                if(index == 2) { coordinate.setZ(Double.parseDouble(measureValue.getLiteralValue().toString())); }
                
                //Check if next exists and set next lengthMeasurement element
                Set<Node> tempLengthMeasures = new HashSet<Node>();
                Node predicat6 = NodeFactory.createURI(NamespaceManager.getInstance().getNamespace("list") + "hasNext");
                RDFGraphNodeFunctions.findSpecificChild(tempLengthMeasures, graph, currentLengthMeasures, predicat6);
                
                if(tempLengthMeasures.size() <= 0) {            	
                	nextLengthMeasureExists = false;
                }else {
                	currentLengthMeasures = tempLengthMeasures.iterator().next(); 
                	index++;
                }
            }
            result.add(coordinate);
            
            //Check if next exists and set next pointList element
            Set<Node> tempPointLists = new HashSet<Node>();
            Node predicat3 = NodeFactory.createURI(NamespaceManager.getInstance().getNamespace("list") + "hasNext");
            RDFGraphNodeFunctions.findSpecificChild(tempPointLists, graph, currentPointList, predicat3);
            
            if(tempPointLists.size() <= 0) {            	
            	nextPointExists = false;
            }else {
            	currentPointList = tempPointLists.iterator().next();            	
            }
            
        }
        
        //Wrapping the coordinates into a LineString WKT-literal
	    GeometryWrapper newGeo = GeometryWrapperFactory.createLineString(result, NamespaceManager.getInstance().getNamespace("geo") + "wktLiteral");
		return NodeFactory.createLiteral(newGeo.asLiteral().getLexicalForm(), WKTDatatype.INSTANCE);
    }
}
