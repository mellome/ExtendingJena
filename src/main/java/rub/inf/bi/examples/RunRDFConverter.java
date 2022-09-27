package rub.inf.bi.examples;

import java.io.IOException;

import rub.inf.bi.io.RDFConverter;

public class RunRDFConverter {

	public static void main(String[] args) {

		RDFConverter converter = new RDFConverter();		
		
		try {
			//converter.constructCubeExample("./resources/ontologies/dataset.rdf");
			converter.constructB();
			converter.save("./resources/ontologies/lineDataset.rdf");
//			converter.constructCubeExample("./resources/ontologies/CubeOntology.rdf");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
