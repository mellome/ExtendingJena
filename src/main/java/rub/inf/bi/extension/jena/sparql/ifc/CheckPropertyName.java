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
import rub.inf.bi.extension.jena.utils.RDFGraphNodeFunctions;
import rub.inf.bi.extension.jena.utils.StringDecoder;

public class CheckPropertyName extends PFuncSimple {

    @Override
    public QueryIterator execEvaluated(Binding binding, Node containerNode, Node predicate, Node member, ExecutionContext execCxt)
    {
    	//Retrieve Graph Information
        Graph graph = execCxt.getActiveGraph() ;
        
        if(member.isLiteral()) {
        	// Execute function on graph, using asked existing triple structure
            Node c = checkPropertyName(containerNode, graph, member.getLiteralValue().toString());
            
        	 //Creating variable Input to bind results to the asked triple of the pattern: SUBJECT PREDICATE ?VARIABLE 
            List<Binding> bindings = new ArrayList<Binding>() ;
            Var cnVar = Var.alloc(NodeFactory.createVariable("CheckPropertyNameResult")); //member ist expected to be a variable
            Binding b = BindingFactory.binding(binding, cnVar, c) ;
            bindings.add(b);
            
            //return cIter;
            return new QueryIterPlainWrapper(bindings.iterator()) ;
        }else {
        	System.err.println("Wrong object for CheckPropertyName! Member must be a text literal.");
        }
        
        //return cIter;
        return null ;
    }
    
    static private Node checkPropertyName(Node element, Graph graph, String psetName)
    {
    	//Example Pattern: inst:IfcPropertySet_524344 ifc:hasProperties_IfcPropertySet inst:IfcPropertySingleValue_524343
        Set<Node> properties = new HashSet<Node>();
        Node predicat = NodeFactory.createURI(NamespaceManager.getInstance().getNamespace("ifc") + "hasProperties_IfcPropertySet");
        RDFGraphNodeFunctions.findSpecificChild(properties, graph, element, predicat);

        //Example Pattern: inst:IfcPropertySingleValue_524343 ifc:name_IfcProperty inst:IfcIdentifier_682638 
        for(Node propertie : properties) {
        	Set<Node> propertieIdentifiers = new HashSet<Node>();
            Node predicat2 = NodeFactory.createURI(NamespaceManager.getInstance().getNamespace("ifc") + "name_IfcProperty");
            RDFGraphNodeFunctions.findSpecificChild(propertieIdentifiers, graph, propertie, predicat2);
            
            //Example Pattern: inst:IfcIdentifier_682638 express:hasString "WU"
            for(Node propertieIdentifier : propertieIdentifiers) {
            	Set<Node> sub = new HashSet<Node>();
                Node predicat3 = NodeFactory.createURI(NamespaceManager.getInstance().getNamespace("express") + "hasString");
                RDFGraphNodeFunctions.findSpecificChild(sub, graph, propertieIdentifier, predicat3);
            
                if(sub.size() == 1) {
                	Node identNode = sub.iterator().next();
                	if(identNode.isLiteral()) {
                		String encoded = identNode.getLiteralValue().toString();
                		String decoded = StringDecoder.decode(encoded);
                		if(psetName.equals(decoded)) {
                	        return NodeFactory.createLiteral("true", XSDDatatype.XSDboolean) ;
                		}
                	}
                }
            }
            
        }

        return NodeFactory.createLiteral("false", XSDDatatype.XSDboolean) ;
    }
    
}

