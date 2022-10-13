package rub.inf.bi.examples;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.swing.text.Document;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
 
import java.io.StringReader;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.apache.jena.geosparql.configuration.GeoSPARQLConfig;
import org.apache.jena.geosparql.implementation.index.IndexConfiguration.IndexOption;
import org.apache.jena.query.ParameterizedSparqlString;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.RDFWriter;
import org.apache.jena.riot.RDFDataMgr;
import rub.inf.bi.extension.jena.ExtendedFunctionConfig;
import rub.inf.bi.extension.jena.NamespaceManager;

public class RunTopologicalValidationExample {

    // define directories
    private static String ontologiePath = "/Users/yhe/Developer/Repo/ExtendingJena/src/main/resources/rdf/3DScenario.rdf";
    private static String originalQueryPath = "/Users/yhe/Developer/Repo/ExtendingJena/src/main/resources/sparql/Geometry3D Tests/Test3_TopologicalWithin.ttl";
    private static String queryResultPath = "/Users/yhe/Developer/Repo/ExtendingJena/src/main/resources/rdf/query_result.xml";

    public static void main(String[] args) {
        //Definition of all namespaces in use
		HashMap<String, String> prefixes = new HashMap<String, String>();
		prefixes.put("ifc", "http://standards.buildingsmart.org/IFC/DEV/IFC2x3/TC1/OWL#");
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
        NamespaceManager namespaceManager = NamespaceManager.getInstance();
        
        // init
        GeoSPARQLConfig.setup(IndexOption.MEMORY);
        ExtendedFunctionConfig.setup();

        // loading query data
        String originalQuery = "";
        try {
            byte[] encode = Files.readAllBytes(Paths.get(originalQueryPath));
            originalQuery = new String(encode, Charset.defaultCharset());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        ParameterizedSparqlString queryStr = new ParameterizedSparqlString();
        
		queryStr.setNsPrefixes(namespaceManager.getAllPrefixes());
		queryStr.append(originalQuery);
        Query query = queryStr.asQuery();

        // loading ontology data
        Model model = ModelFactory.createDefaultModel();
        InputStream input = RDFDataMgr.open(ontologiePath);

        if (input == null) {
            throw new IllegalArgumentException("File: " + ontologiePath + " not found");
        }

        // read the RDF/XML file
        model.read(input, null);

        System.out.println("\n==================Result===================");
		try(QueryExecution qexec = QueryExecutionFactory.create(query, model)) {
	        ResultSet results = qexec.execSelect() ;
            Boolean isColumnNameStored = false;
            List<String> columnNames = new ArrayList<>();

            //save query results to a .xml file
            String xmlString = ResultSetFormatter.asXMLString(results);
            System.out.println(xmlString);
            try {
                stringToDom(xmlString, queryResultPath);
            } catch (Exception e) {
                e.getStackTrace();
            }

            // RDFWriter rdfWriter = model.getWriter("RDF/XML");
            // rdfWriter.write(model, new FileOutputStream("/Users/yhe/Developer/Repo/ExtendingJena/src/main/resources/rdf/queried_3DScenario.xml"), xmlString);
            
            // print query results
	        // for ( ; results.hasNext() ; )
	        // {
	        //     QuerySolution sol = results.nextSolution() ;
            //     if(isColumnNameStored == false){
            //         // this progress runs only once within the for-loop
            //         sol.varNames().forEachRemaining(columnNames::add);
            //         isColumnNameStored = true;
            //     }
            //     // System.out.println(sol.getResource(columnNames.get(0)) + " | " + sol.getResource(columnNames.get(1)) );

            //     List<RDFNode> nodeList = new ArrayList<>();
            //     for(String var : columnNames){
            //         nodeList.add(sol.get(var));
            //     }

            //     System.out.println(Arrays.toString(nodeList.toArray()));
	        // }
	    }
		System.out.println("===========================================");

    }

    private static void stringToDom(String xmlString, String pathName) throws SAXException, ParserConfigurationException, IOException {
    // Parse the given input
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    DocumentBuilder builder = factory.newDocumentBuilder();
    org.w3c.dom.Document doc = builder.parse(new InputSource(new StringReader(xmlString)));

    // Write the parsed document to an xml file
    TransformerFactory transformerFactory = TransformerFactory.newInstance();
    try {
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        StreamResult result =  new StreamResult(new File(pathName));
        try {
            transformer.transform(source, result);
        } catch (TransformerException e) {
            e.getMessage();
        }
    } catch (TransformerConfigurationException e) {
        e.getStackTrace();
    }
    }
}
