package rub.inf.bi;

import java.util.HashMap;

import rub.inf.bi.extension.jena.NamespaceManager;
import rub.inf.bi.ui.DemoFrame;

public class Run {

	public static void main(String[] args) {
		
		//Definition of all namespaces in use
		HashMap<String, String> prefixes = new HashMap<String, String>();

		//prefixes.put("ifc", "http://standards.buildingsmart.org/IFC/DEV/IFC4/ADD1/OWL#");
		//prefixes.put("ifc", "http://standards.buildingsmart.org/IFC/DEV/IFC4/ADD2_TC1/OWL#"); //REQUIRD
		prefixes.put("ifc", "http://standards.buildingsmart.org/IFC/DEV/IFC2x3/TC1/OWL#");
		//prefixes.put("ifc", "http://standards.buildingsmart.org/IFC/DEV/IFC4_1/OWL#");
		prefixes.put("geo", "http://www.opengis.net/ont/geosparql#"); //REQUIRD
		prefixes.put("geof", "http://www.opengis.net/def/function/geosparql/"); //REQUIRD
		prefixes.put("rdf", "http://www.w3.org/1999/02/22-rdf-syntax-ns#"); //REQUIRD
		prefixes.put("rdfs", "http://www.w3.org/2000/01/rdf-schema#"); //REQUIRD
		prefixes.put("list", "https://w3id.org/list#"); //REQUIRD
		prefixes.put("owl", "http://www.w3.org/2002/07/owl#"); //REQUIRD
		prefixes.put("my", "https://www.inf.bi.ruhr-uni-bochum.de/jena/#"); //REQUIRD
		

		prefixes.put("ogc", "http://www.opengis.net/");
		prefixes.put("geor", "http://www.opengis.net/def/rule/geosparql/");
		prefixes.put("sf", "http://www.opengis.net/ont/sf#");
		prefixes.put("gml", "http://www.opengis.net/ont/gml#");
		prefixes.put("xsd", "http://www.w3.org/2001/XMLSchema#");
		prefixes.put("uom", "http://www.opengis.net/def/uom/OGC/1.0/");
		prefixes.put("spatial", "http://jena.apache.org/spatial#");
		prefixes.put("spatialF", "http://jena.apache.org/function/spatial#");
		prefixes.put("express", "https://w3id.org/express#");
		NamespaceManager.initialize(prefixes);
		
		DemoFrame frame = new DemoFrame();
		frame.setVisible(true);
	}

}
