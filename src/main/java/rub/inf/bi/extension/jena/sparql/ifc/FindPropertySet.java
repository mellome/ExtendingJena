package rub.inf.bi.extension.jena.sparql.ifc;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

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

public class FindPropertySet extends PFuncSimple {

    @Override
    public QueryIterator execEvaluated(Binding binding, Node containerNode, Node predicate, Node member, ExecutionContext execCxt)
    {
    	//Retrieve Graph Information
        Graph graph = execCxt.getActiveGraph() ;
        
        // Execute function on graph, using asked existing triple structure
        Collection<Node> c = findPropertySets(containerNode, graph);
        
        //Creating variable Input to bind results to the asked triple of the pattern: SUBJECT PREDICATE ?VARIABLE 
        Var cnVar = Var.alloc(member); //member ist expected to be a variable
        List<Binding> bindings = new ArrayList<Binding>() ;
        for(Iterator<Node> iter = c.iterator() ; iter.hasNext() ;) {
            Node cn = iter.next();
            Binding b = BindingFactory.binding(binding, cnVar, cn) ;
            bindings.add(b);            
        }
        
        //return cIter;
        return new QueryIterPlainWrapper(bindings.iterator()) ;
    }
    
    static private Collection<Node> findPropertySets(Node element, Graph graph)
    {

        //Example Pattern: inst:IfcRelDefinesByProperties_524347 rdf:type  ifc:IfcRelDefinesByProperties
        Set<Node> parentNodes = new HashSet<Node>();
        Node predicat = NodeFactory.createURI(NamespaceManager.getInstance().getNamespace("rdf") + "type");
        Node object = NodeFactory.createURI(NamespaceManager.getInstance().getNamespace("ifc") + "IfcRelDefinesByProperties");
        RDFGraphNodeFunctions.findSpecificParent(parentNodes, graph, object, predicat);

        //Example Pattern: inst:IfcRelDefinesByProperties_524347 ifc:relatedObjects_IfcRelDefinesByProperties inst:IfcWall_524135
        Set<Node> propRelations = new HashSet<Node>();
        for(Node subject : parentNodes) {
        	Set<Node> sub = new HashSet<Node>();
            Node predicat2 = NodeFactory.createURI(NamespaceManager.getInstance().getNamespace("ifc") + "relatedObjects_IfcRelDefinesByProperties");
            RDFGraphNodeFunctions.find(sub, graph, subject, predicat2, element);
            
            if(sub.size() == 0) { //if result is emtpy, try out old/alternativ notation
            	 Node predicat3 = NodeFactory.createURI(NamespaceManager.getInstance().getNamespace("ifc") + "relatedObjects_IfcRelDefines");
            	 RDFGraphNodeFunctions.find(sub, graph, subject, predicat3, element);
            }
            
            propRelations.addAll(sub);
        }

        //Example Pattern: inst:IfcRelDefinesByProperties_524347 ifc:relatingPropertyDefinition_IfcRelDefinesByProperties inst:IfcPropertySet_524344
        Set<Node> propSets = new HashSet<Node>();
        for(Node subject : propRelations) {
        	Set<Node> sub = new HashSet<Node>();
            Node predicat2 = NodeFactory.createURI(NamespaceManager.getInstance().getNamespace("ifc") + "relatingPropertyDefinition_IfcRelDefinesByProperties");

            RDFGraphNodeFunctions.findSpecificChild(sub, graph, subject, predicat2);
            propSets.addAll(sub);
        }
        
        return propSets ;
    }
    
}

