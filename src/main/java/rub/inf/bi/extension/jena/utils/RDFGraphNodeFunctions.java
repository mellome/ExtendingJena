package rub.inf.bi.extension.jena.utils;

import java.util.Collection;

import org.apache.jena.graph.Graph;
import org.apache.jena.graph.Node;
import org.apache.jena.graph.NodeFactory;
import org.apache.jena.graph.Triple;
import org.apache.jena.util.iterator.ExtendedIterator;

public class RDFGraphNodeFunctions {

    static public void findChildren(Collection<Node> acc, Graph graph, Node parent)
    {
        ExtendedIterator<Triple> iter = graph.find(parent, Node.ANY, Node.ANY) ;
        while(iter.hasNext())
        {
            Triple t = iter.next();
            Node containerNode = t.getObject() ;
            acc.add(containerNode) ;
        }
    }
    
    static public void findPredicats(Collection<Node> acc, Graph graph, Node parent)
    {
        ExtendedIterator<Triple> iter = graph.find(parent, Node.ANY, Node.ANY) ;
        while(iter.hasNext())
        {
            Triple t = iter.next();
            Node containerNode = t.getPredicate() ;
            acc.add(containerNode) ;
        }
    }
    
    static public void findSpecificChild(Collection<Node> acc, Graph graph, Node parent, Node predicate)
    {
    	
        ExtendedIterator<Triple> iter = graph.find(parent, predicate, Node.ANY) ;
        while(iter.hasNext())
        {
            Triple t = iter.next();
            
            //int hash = t.hashCode(t.getMatchSubject(), t.getMatchPredicate(), t.getMatchObject());
            //System.out.println(hash);
            
            Node containerNode = t.getObject() ;
            acc.add(containerNode) ;
        }
    }
    
    static public void findSpecificPredicat(Collection<Node> acc, Graph graph, Node parent, Node child)
    {
        ExtendedIterator<Triple> iter = graph.find(parent, Node.ANY, child) ;
        while(iter.hasNext())
        {
            Triple t = iter.next();
            Node containerNode = t.getPredicate() ;
            acc.add(containerNode) ;
        }
    }
    
    static public void findParents(Collection<Node> acc, Graph graph, Node child)
    {
        ExtendedIterator<Triple> iter = graph.find(Node.ANY, Node.ANY, child) ;
        while(iter.hasNext())
        {
            Triple t = iter.next();
            Node containerNode = t.getSubject() ;
            acc.add(containerNode) ;
        }
    }

    static public void findSpecificParent(Collection<Node> acc, Graph graph, Node childLiteral,  Node predicate)
    {
        ExtendedIterator<Triple> iter = graph.find(Node.ANY, predicate, childLiteral) ;
        while(iter.hasNext())
        {
            Triple t = iter.next();
            Node containerNode = t.getSubject() ;
            acc.add(containerNode) ;
        }
    }
    
    static public void find(Collection<Node> acc, Graph graph, Node parent, Node predicate,  Node child)
    {
        ExtendedIterator<Triple> iter = graph.find(parent, predicate, child) ;
        while(iter.hasNext())
        {
            Triple t = iter.next();
            Node containerNode = t.getSubject() ;
            acc.add(containerNode) ;
        }
    }
    
}
