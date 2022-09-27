package rub.inf.bi.extension.jena.sparql.tunneling;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
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
import org.apache.jena.sparql.core.Var;
import org.apache.jena.sparql.engine.ExecutionContext ;
import org.apache.jena.sparql.engine.QueryIterator;
import org.apache.jena.sparql.engine.binding.Binding;
import org.apache.jena.sparql.engine.binding.BindingFactory;
import org.apache.jena.sparql.engine.iterator.QueryIterPlainWrapper;
import org.apache.jena.sparql.pfunction.PFuncSimple;
import org.locationtech.jts.geom.Coordinate;

import rub.inf.bi.extension.jena.NamespaceManager;
import rub.inf.bi.extension.jena.utils.RDFGraphNodeFunctions;

public class ConvertAlignmentToWKT extends PFuncSimple {

    @Override
    public QueryIterator execEvaluated(Binding binding, Node containerNode, Node predicate, Node member, ExecutionContext execCxt)
    {
    	//Retrieve Graph Information
        Graph graph = execCxt.getActiveGraph() ;
        
        if(member.isVariable()) {
        	// Execute function on graph, using asked existing triple structure
            Node c = queryRepresentationInformation(containerNode, graph);
            
        	 //Creating variable Input to bind results to the asked triple of the pattern: SUBJECT PREDICATE ?VARIABLE 
            List<Binding> bindings = new ArrayList<Binding>() ;
            Var cnVar = Var.alloc(member); //member ist expected to be a variable
            Binding b = BindingFactory.binding(binding, cnVar, c) ;
            bindings.add(b);
            
            //return cIter;
            return new QueryIterPlainWrapper(bindings.iterator()) ;
        }else {
        	System.err.println("Wrong object for CheckPropertyName! Member must be a variable Node.");
        }
        
        //return cIter;
        return null ;
    }
    
    /**
     * Reads an IfcRepresentation and converts found information to WKT Literal.
     * 
     * @param element
     * @param graph
     * @return Node containing a WKT Literal
     */
    static private Node queryRepresentationInformation(Node element, Graph graph)
    {
    	//Example Pattern: inst:IfcShapeRepresentation_65 ifc:items_IfcRepresentation inst:IfcExtrudedAreaSolid_59 .
        Set<Node> curveItems = new HashSet<Node>();
        Node predicat = NodeFactory.createURI(NamespaceManager.getInstance().getNamespace("ifc") + "Axis");
        RDFGraphNodeFunctions.findSpecificChild(curveItems, graph, element, predicat);

        for(Node curveItem : curveItems) {
        
        	//Example Pattern: inst:IfcExtrudedAreaSolid_59 rdf:type ifc:IfcExtrudedAreaSolid .
        	Set<Node> representationTypeList = new HashSet<Node>();
            Node predicat2 = NodeFactory.createURI(NamespaceManager.getInstance().getNamespace("rdf") + "type");
            RDFGraphNodeFunctions.findSpecificChild(representationTypeList, graph, curveItem, predicat2);
            

            for(Node representationType : representationTypeList) {
            	String typeString = representationType.getLiteralValue().toString();
            	
            	//========================================================
                //Derivation of IfcCurve
                if("IfcPolyline".equals(typeString)) {
                	return parsePolyLine(graph, curveItem);
                }

            }
        }

        return NodeFactory.createLiteral("Unknown Representation", XSDDatatype.XSDstring);
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
        Set<Node> pointAxisLists = new HashSet<Node>();
        Node predicat = NodeFactory.createURI(NamespaceManager.getInstance().getNamespace("ifc") + "Points");
        RDFGraphNodeFunctions.findSpecificChild(pointAxisLists, graph, parent, predicat);
        Node pointAxis = pointAxisLists.iterator().next();
        
        Set<Node> ifcCoordinates = new HashSet<Node>();
        RDFGraphNodeFunctions.findPredicats(ifcCoordinates, graph, pointAxis);
        //RDFGraphNodeFunctions.findChildren(pointLists, graph, pointAxis);
        
        HashMap<String, Coordinate> result = new HashMap<String, Coordinate>();
        ifcCoordinates.iterator().forEachRemaining(ifcCoordinate -> {

        	//System.out.println(ifcCoordinate.getLocalName());
        	
            ArrayList<Node> coordinateList = new ArrayList<Node>();
            RDFGraphNodeFunctions.findSpecificChild(coordinateList, graph, pointAxis, ifcCoordinate);
            Node tempCoord = coordinateList.get(0);
            
            
            ArrayList<Node> coordsList = new ArrayList<Node>();
            Node predicat2 = NodeFactory.createURI(NamespaceManager.getInstance().getNamespace("ifc") + "Coordinates");
            RDFGraphNodeFunctions.findSpecificChild(coordsList, graph, tempCoord, predicat2);
            Node coords = coordsList.get(0);
            
            Coordinate c = new Coordinate();
        	
        	String[] coordsStr = coords.getLiteralValue().toString().split(" ");
        	c.setX(Double.parseDouble(coordsStr[0]));
        	c.setY(Double.parseDouble(coordsStr[1]));
        	
        	result.put(ifcCoordinate.getLocalName(), c);
        	
        });
        
        Collection<String> unsorted = result.keySet();
        List<String> sorted = asSortedList(unsorted);
        
        ArrayList<Coordinate> coordinates = new ArrayList<Coordinate>();
        for(String k : sorted) {
        	coordinates.add(result.get(k));
        }
        
        //Wrapping the coordinates into a LineString WKT-literal
	    GeometryWrapper newGeo = GeometryWrapperFactory.createLineString(coordinates, NamespaceManager.getInstance().getNamespace("geo") + "wktLiteral");
		return NodeFactory.createLiteral(newGeo.asLiteral().getLexicalForm(), WKTDatatype.INSTANCE);
    }
	
	public static <T extends Comparable<? super T>> List<T> asSortedList(Collection<T> c) {
	  List<T> list = new ArrayList<T>(c);
	  java.util.Collections.sort(list);
	  return list;
	}
}

