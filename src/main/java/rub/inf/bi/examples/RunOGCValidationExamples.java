package rub.inf.bi.examples;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

import org.apache.jena.geosparql.configuration.GeoSPARQLConfig;
import org.apache.jena.geosparql.implementation.index.IndexConfiguration.IndexOption;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.ParameterizedSparqlString;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdfconnection.RDFConnection;
import org.apache.jena.rdfconnection.RDFConnectionFactory;
import org.apache.jena.riot.RDFDataMgr;
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
public class RunOGCValidationExamples 
{
	//private static String ontologiePath = "./src/main/resources/rdf/OGC_VaidationDataset.rdf";
	//private static String ontologiePath = "./src/main/resources/rdf/GeometryOntology.rdf";
	//private static String ontologiePath = "./src/main/resources/rdf/GeometryOntologyTriangles.rdf";
	private static String ontologiePath = "./src/main/resources/rdf/GroundModelOntology.rdf";
	//private static String ontologiePath = "./src/main/resources/rdf/Ontology_5fbb7ba3-c335-4aa3-4338-abefefdeb670.rdf";
	
	
	//private static String queryPath = "./src/main/resources/queries/OGC Test Examples/OGC_Example_with_selfmade_function.ttl";
	//private static String queryPath = "./src/main/resources/queries/OGC Test Examples/OGC_Example1.ttl";
	//private static String queryPath = "./src/main/resources/queries/OGC Test Examples/OGC_Example2.ttl";
	//private static String queryPath = "./src/main/resources/queries/OGC Test Examples/OGC_Example3.ttl";
	//private static String queryPath = "./src/main/resources/queries/OGC Test Examples/OGC_Example4.ttl";

	/*
	 * Queries testing for 3D Intersection of WKT Geometry 
	 */
	//private static String queryPath = "./src/main/resources/queries/Geometry3D Tests/Test1_LLIntersection3D.ttl";
	//private static String queryPath = "./src/main/resources/queries/Geometry3D Tests/Test2_LLIntersection3D.ttl";
	//private static String queryPath = "./src/main/resources/queries/Geometry3D Tests/Test1_LLIntersection3DGeometry.ttl";
	//private static String queryPath = "./src/main/resources/queries/Geometry3D Tests/Test1_PLIntersection3D.ttl";
	//private static String queryPath = "./src/main/resources/queries/Geometry3D Tests/Test1_PLIntersection3DGeometry.ttl";
	//private static String queryPath = "./src/main/resources/queries/Geometry3D Tests/Test1_PPIntersection3D.ttl";
	//private static String queryPath = "./src/main/resources/queries/Geometry3D Tests/Test1_TTIntersection3D.ttl";
	//private static String queryPath = "./src/main/resources/queries/Geometry3D Tests/Test1_TLIntersection3D.ttl";
	//private static String queryPath = "./src/main/resources/queries/Geometry3D Tests/Test1_TLIntersection3DGeometry.ttl";

	/*
	 * Queries testing IfcAlignment Constraints and Conditions
	 */
	//private static String queryPath = "./src/main/resources/queries/IFC Ontology Tests/Find_alignmentGeometry_of_Specific_Alignment.ttl";
	//private static String queryPath = "./src/main/resources/queries/IFC Ontology Tests/Check_Radius.ttl";
	private static String queryPath = "./src/main/resources/sparql/IFC/CheckProperty.ttl";
		
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
		Dataset dataset = RDFDataMgr.loadDataset(ontologiePath);

		try(RDFConnection conn = RDFConnectionFactory.connect(dataset) ) {

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
