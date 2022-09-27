package rub.inf.bi.examples;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.jena.geosparql.configuration.GeoSPARQLConfig;
import org.apache.jena.geosparql.implementation.index.IndexConfiguration.IndexOption;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.DatasetFactory;
import org.apache.jena.query.ParameterizedSparqlString;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdfconnection.RDFConnection;
import org.apache.jena.rdfconnection.RDFConnectionFactory;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.sparql.core.DatasetGraph;
import org.apache.jena.sparql.function.FunctionRegistry;
import org.apache.jena.sparql.syntax.ElementSubQuery;
import org.apache.jena.sparql.syntax.syntaxtransform.ElementTransform;
import org.apache.jena.sparql.syntax.syntaxtransform.ElementTransformCleanGroupsOfOne;
import org.apache.jena.sparql.syntax.syntaxtransform.ElementTransformSubst;
import org.apache.jena.sparql.syntax.syntaxtransform.ElementTransformer;
import org.apache.jena.sparql.syntax.syntaxtransform.QueryTransformOps;

import rub.inf.bi.extension.jena.ExtendedFunctionConfig;

/**
 * Hello world!
 *
 */
public class RunQueryingOntologyStorage 
{
	private static String ontologieAPath = "./src/main/resources/ontologies/OntologyStorages/SampleA/Ontology_5fbb7ba3-c335-4aa3-4338-abefefdeb670.rdf";
	private static String ontologieBPath = "./src/main/resources/ontologies/OntologyStorages/SampleA/SpatialOntology.rdf";
	
	
	//private static String queryPath = "./src/main/resources/queries/IFC Ontology Tests/Check_Radius.ttl";
	private static String queryPath = "./src/main/resources/queries/Check_SpatialRelation_Intersection.ttl";
		
	public static void main(String[] args) {
		
		//FunctionRegistry registry = FunctionRegistry.get();
		//registry.
		
		//Initialising GeoSPARQL Toolkit for Apache Jena
		GeoSPARQLConfig.setup(IndexOption.MEMORY);
		ExtendedFunctionConfig.setup();
		
		//Definition of all namespaces used by GeoSPARQL
		HashMap<String, String> prefixes = new HashMap<String, String>();
		prefixes.put("ifc", "http://standards.buildingsmart.org/IFC/DEV/IFC4_1/OWL#");
		prefixes.put("ogc", "http://www.opengis.net/");
		prefixes.put("geo", "http://www.opengis.net/ont/geosparql#");
		prefixes.put("geof", "http://www.opengis.net/def/function/geosparql/");
		prefixes.put("geor", "http://www.opengis.net/def/rule/geosparql/");
		prefixes.put("sf", "http://www.opengis.net/ont/sf#");
		prefixes.put("gml", "http://www.opengis.net/ont/gml#");
		prefixes.put("xsd", "http://www.w3.org/2001/XMLSchema#");
		prefixes.put("rdf", "http://www.w3.org/1999/02/22-rdf-syntax-ns#");
		prefixes.put("rdfs", "http://www.w3.org/2000/01/rdf-schema#");
		prefixes.put("owl", "http://www.w3.org/2002/07/owl#");
		prefixes.put("my", "http://example.org/ApplicationSchema#");
		prefixes.put("uom", "http://www.opengis.net/def/uom/OGC/1.0/");
		prefixes.put("spatial", "http://jena.apache.org/spatial#");
		prefixes.put("spatialF", "http://jena.apache.org/function/spatial#");
		
		//Loading query data
		String query = "";
		try {
			byte[] encoded = Files.readAllBytes(Paths.get(queryPath));
			query = new String(encoded, Charset.defaultCharset());
		} catch (Exception e) {
			e.printStackTrace();
		}

		ParameterizedSparqlString queryStr = new ParameterizedSparqlString();
		queryStr.setNsPrefixes(prefixes);
		queryStr.append(query);

		Query q = queryStr.asQuery();
		
		//Loading ontologie data
		//Dataset dataset = RDFDataMgr.loadDataset(ontologieAPath);
		
		ArrayList<String> uris = new ArrayList<String>();
		uris.add(ontologieAPath);
		uris.add(ontologieBPath);
		
		Dataset ontologySet = DatasetFactory.create(uris);
		
		try(RDFConnection conn = RDFConnectionFactory.connect(ontologySet) ) {

			System.out.println("=================DATABASE==================");
			//System.out.println(conn.fetch().toString());
			System.out.println("===========================================");
		    
			//Executing query and retrieve results
			System.out.println("\n==================Result===================");
			
			ResultSet result = conn.query(q).execSelect();
			
			System.out.println("Result Variables: " + result.getResultVars());
			while(result.hasNext()) {
				System.out.println("Solution: " + result.next().toString());
			}
			
			System.out.println("===========================================");
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
	}

}
