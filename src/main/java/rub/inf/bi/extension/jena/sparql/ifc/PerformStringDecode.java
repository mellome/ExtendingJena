package rub.inf.bi.extension.jena.sparql.ifc;

import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.sparql.expr.NodeValue;
import org.apache.jena.sparql.function.FunctionBase1;

import rub.inf.bi.extension.jena.utils.StringDecoder;

public class PerformStringDecode extends FunctionBase1 {

	@Override
	public NodeValue exec(NodeValue v1) {
		
		String encoded = v1.getString();
		String decoded = StringDecoder.decode(encoded);
        return NodeValue.makeNode(decoded, XSDDatatype.XSDstring);
	}

}
