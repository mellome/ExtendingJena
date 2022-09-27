package rub.inf.bi.extension.jena.sparql.ifc;

import org.apache.jena.atlas.logging.Log;
import org.apache.jena.graph.*;
import org.apache.jena.query.QueryBuildException;
import org.apache.jena.sparql.algebra.Algebra;
import org.apache.jena.sparql.algebra.Op;
import org.apache.jena.sparql.algebra.Table;
import org.apache.jena.sparql.algebra.TableFactory;
import org.apache.jena.sparql.algebra.op.OpBGP;
import org.apache.jena.sparql.algebra.op.OpFilter;
import org.apache.jena.sparql.algebra.op.OpJoin;
import org.apache.jena.sparql.algebra.op.OpTable;
import org.apache.jena.sparql.core.BasicPattern;
import org.apache.jena.sparql.core.Var;
import org.apache.jena.sparql.engine.QueryIterator;
import org.apache.jena.sparql.engine.binding.Binding;
import org.apache.jena.sparql.engine.binding.BindingFactory;
import org.apache.jena.sparql.engine.binding.BindingHashMap;
import org.apache.jena.sparql.engine.iterator.*;
import org.apache.jena.sparql.engine.main.QC;
import org.apache.jena.sparql.expr.E_Regex;
import org.apache.jena.sparql.expr.Expr;
import org.apache.jena.sparql.expr.ExprVar;
import org.apache.jena.sparql.pfunction.PFuncSimple;
import org.apache.jena.sparql.pfunction.PropFuncArg;
import org.apache.jena.sparql.pfunction.PropertyFunction;
import org.apache.jena.sparql.engine.ExecutionContext ;
import org.apache.jena.sparql.pfunction.PropertyFunctionFactory;
import org.apache.jena.sparql.syntax.ElementFilter;
import org.apache.jena.sparql.syntax.ElementGroup;
import org.apache.jena.sparql.syntax.ElementTriplesBlock;
import org.apache.jena.sparql.util.IterLib;
import org.apache.jena.sparql.util.NodeUtils;
import org.apache.jena.sparql.util.graph.GraphContainerUtils;
import org.apache.jena.util.iterator.ExtendedIterator;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;

import java.util.*;

public class CheckPropertyPropFunc extends PFuncSimple {
    Node typeNode = null ;      // Null means don't check type.

    public CheckPropertyPropFunc() { this.typeNode = null ; }

    protected CheckPropertyPropFunc(Node typeURI) { this.typeNode = typeURI ; }

    @Override
    public QueryIterator execEvaluated(Binding binding, Node containerNode, Node predicate, Node member, ExecutionContext execCxt)
    {
        System.out.println(containerNode.getClass().getSimpleName());
        Node_URI node = (Node_URI)containerNode;
        
        System.out.println(execCxt.getClass());
        QueryIterator qIter1 = execEvaluatedConcrete(binding, containerNode, predicate, member, execCxt) ;
        QueryIterator qIter2 = execEvaluatedCalc(binding, containerNode, predicate, member, execCxt) ;
        QueryIterConcat concat = new QueryIterConcat(execCxt) ;
        concat.add(qIter1) ;
        concat.add(qIter2) ;
        return concat ;
    }

    // Ask directly.
    private QueryIterator execEvaluatedConcrete(Binding binding, Node containerNode, Node predicate, Node member,
                                                ExecutionContext execCxt)
    {
        Graph graph = execCxt.getActiveGraph() ;
        QueryIterator qIter = new QueryIterTriplePattern(QueryIterRoot.create(execCxt), new Triple(containerNode, predicate, member), execCxt) ;
        return qIter ;
    }

    // Ask by finding all the rdf:_N + rdf:type
    private QueryIterator execEvaluatedCalc(Binding binding, Node containerNode, Node predicate, Node member, ExecutionContext execCxt)
    {
        Graph graph = execCxt.getActiveGraph() ;
        if ( ! containerNode.isVariable() )
        {
            // Container a ground term.
            if ( ! GraphContainerUtils.isContainer(execCxt.getActiveGraph(), containerNode, typeNode) )
                return IterLib.noResults(execCxt) ;
            return oneContainer(binding, containerNode, member, execCxt) ;
        }

        // Container a variable.
        Collection<Node> c = null ;

        if ( member.isVariable() )
            c = findContainers(graph, typeNode) ;
        else
            c = findContainingContainers(graph, typeNode, member) ;

        QueryIterConcat cIter = new QueryIterConcat(execCxt) ;
        Var cVar = Var.alloc(containerNode) ;
        for ( Iterator<Node> iter = c.iterator() ; iter.hasNext() ; )
        {
            Node cn = iter.next() ;
            //Binding the container node.
            Binding b = BindingFactory.binding(binding, cVar, cn) ;
            Node m = member ;
            // Special case of ?x rdfs:member ?x
            if ( Var.isVar(member) && member.equals(cVar) )
                m = cn ;

            cIter.add(oneContainer(b, cn, m, execCxt)) ;
        }
        return cIter ;
        //throw new QueryFatalException(Utils.className(this)+": Arg 1 is too hard : "+containerNode) ;
    }

    private QueryIterator oneContainer(Binding binding, Node containerNode, Node member, ExecutionContext execCxt)
    {
        // containerNode is a fixed term
        if ( member.isVariable() )
            return members(binding, containerNode,  Var.alloc(member), execCxt) ;
        else
            return verify(binding, containerNode, member, execCxt) ;
    }

    private QueryIterator members(Binding binding, Node containerNode, Var memberVar, ExecutionContext execCxt)
    {
        // Not necessarily very efficient
        Collection<Node> x = GraphContainerUtils.containerMembers(execCxt.getActiveGraph(), containerNode, typeNode) ;
        if ( x == null )
            // Wrong type.
            return IterLib.noResults(execCxt) ;

        List<Binding> bindings = new ArrayList<Binding>() ;
        for ( Iterator<Node> iter = x.iterator() ; iter.hasNext() ; )
        {
            Node n = iter.next() ;
            Binding b = BindingFactory.binding(binding, memberVar, n) ;
            bindings.add(b) ;
        }

        // Turn into a QueryIterator of extra bindings.
        return new QueryIterPlainWrapper(bindings.iterator(), execCxt) ;
    }

    private QueryIterator verify(Binding binding, Node containerNode, Node member, ExecutionContext execCxt)
    {
        int count = GraphContainerUtils.countContainerMember(execCxt.getActiveGraph(), containerNode, typeNode, member) ;
        return new QueryIterYieldN(count, binding, execCxt) ;
    }

    static private Collection<Node> findContainers(Graph graph, Node typeNode)
    {
        Set<Node> acc = new HashSet<Node>() ;
        if ( typeNode != null )
        {
            findContainers(acc, graph, typeNode) ;
            return acc;
        }
        findContainers(acc, graph, RDF.Bag.asNode()) ;
        findContainers(acc, graph, RDF.Seq.asNode()) ;
        findContainers(acc, graph, RDF.Alt.asNode()) ;
        return acc ;
    }

    static private void findContainers(Collection<Node> acc, Graph graph, Node typeNode)
    {
        ExtendedIterator<Triple> iter = graph.find(Node.ANY, RDF.type.asNode(), typeNode) ;
        while(iter.hasNext())
        {
            Triple t = iter.next();
            Node containerNode = t.getSubject() ;
            acc.add(containerNode) ;
        }
    }

    static private Collection<Node> findContainingContainers(Graph graph, Node typeNode, Node member)
    {
        Collection<Node> acc = new HashSet<Node>() ;
        // Index off the object
        ExtendedIterator<Triple> iter = graph.find(Node.ANY, Node.ANY, member) ;
        while(iter.hasNext())
        {
            Triple t = iter.next();
            Node containerNode = t.getSubject() ;   // Candidate
            if ( GraphContainerUtils.isContainer(graph, containerNode, typeNode) )
                acc.add(containerNode) ;
        }
        return acc ;
    }
}

