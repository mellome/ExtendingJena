package rub.inf.bi.extension.jena.sparql.ifc;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.jena.datatypes.RDFDatatype;
import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.graph.Graph;
import org.apache.jena.graph.Node;
import org.apache.jena.graph.NodeFactory;
import org.apache.jena.graph.Triple;
import org.apache.jena.sparql.core.Var;
import org.apache.jena.sparql.engine.ExecutionContext ;
import org.apache.jena.sparql.engine.QueryIterator;
import org.apache.jena.sparql.engine.binding.Binding;
import org.apache.jena.sparql.engine.binding.BindingFactory;
import org.apache.jena.sparql.engine.iterator.QueryIterPlainWrapper;
import org.apache.jena.sparql.pfunction.PFuncSimple;
import org.apache.jena.util.iterator.ExtendedIterator;

import rub.inf.bi.extension.jena.NamespaceManager;
import rub.inf.bi.extension.jena.utils.IFCOWLtoWKTFunctions;
import rub.inf.bi.extension.jena.utils.RDFGraphNodeFunctions;
import rub.inf.bi.extension.jena.utils.StringDecoder;

public class ConvertRepresentationToWKT extends PFuncSimple {

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
        Set<Node> representationItems = new HashSet<Node>();
        Node predicat = NodeFactory.createURI(NamespaceManager.getInstance().getNamespace("ifc") + "items_IfcRepresentation");
        RDFGraphNodeFunctions.findSpecificChild(representationItems, graph, element, predicat);

        for(Node representationItem : representationItems) {
        	
        	//Example Pattern: inst:IfcExtrudedAreaSolid_59 rdf:type ifc:IfcExtrudedAreaSolid .
        	Set<Node> representationTypeList = new HashSet<Node>();
            Node predicat2 = NodeFactory.createURI(NamespaceManager.getInstance().getNamespace("rdf") + "type");
            RDFGraphNodeFunctions.findSpecificChild(representationTypeList, graph, representationItem, predicat2);
            Node representationType = representationTypeList.iterator().next();

        	//========================================================
            //Derivation of IfcSolidModel
            if("IfcExtrudedAreaSolid".equals(representationType.getLocalName())) {
            	return IFCOWLtoWKTFunctions.parseExtrudeAreaSolid(graph, representationItem);
            }
        	
            if("IfcRevolvedAreaSolid".equals(representationType.getLocalName())) { 
            	//System.out.println("Ist " + representationType.getLocalName());
            }
        	
            if("IfcSurfaceCurveSweptAreaSolid".equals(representationType.getLocalName())) {
            	//System.out.println("Ist " + representationType.getLocalName());
            }
        	
            if("IfcFacetedBrep".equals(representationType.getLocalName())) { 
            	//System.out.println("Ist " + representationType.getLocalName());
            }
        	
            if("IfcFacetedBrepWithVoids".equals(representationType.getLocalName())) {
            	//System.out.println("Ist " + representationType.getLocalName());
            }
            
            if("IfcCsgSolid".equals(representationType.getLocalName())) { 
            	//System.out.println("Ist " + representationType.getLocalName());
            }
        	
        	//========================================================
            //Derivation of IfcPoint
            if("IfcCartesianPoint".equals(representationType.getLocalName())) {
            	//System.out.println("Ist " + representationType.getLocalName());
            }
        	
        	//========================================================
            //Derivation of IfcCurve
            if("IfcPolyline".equals(representationType.getLocalName())) {
            	return IFCOWLtoWKTFunctions.parsePolyLine(graph, representationItem);
            }
            
            //========================================================
            if("IfcBoundingBox".equals(representationType.getLocalName())) {
            	//System.out.println("Ist " + representationType.getLocalName());
            }

            return NodeFactory.createLiteral("Unsupported: " + representationType.getLocalName(), XSDDatatype.XSDstring) ;
        }

        return NodeFactory.createLiteral("Unknown Representation", XSDDatatype.XSDstring) ;
    }
    
}

