package rub.inf.bi.extension.jena.sparql.tunneling;

import org.apache.jena.sparql.expr.NodeValue;
import org.apache.jena.sparql.function.FunctionBase2;

public class AlignmentSegmentHasRadius extends FunctionBase2 {

	@Override
	public NodeValue exec(NodeValue v1, NodeValue v2) {
		
		double radius2 = Double.parseDouble(v2.getNode().getLiteral().getValue().toString());
		double radius1 = Double.parseDouble(v1.getNode().getLiteral().getValue().toString());
		
		if (radius1 < radius2) {
        	return NodeValue.TRUE;
        }
        return NodeValue.FALSE;
	}

}
