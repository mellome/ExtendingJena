package rub.inf.bi.examples;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

import org.apache.jena.geosparql.configuration.GeoSPARQLConfig;
import org.apache.jena.geosparql.implementation.index.IndexConfiguration.IndexOption;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.ParameterizedSparqlString;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.impl.ResourceImpl;
import org.apache.jena.rdfconnection.RDFConnection;
import org.apache.jena.rdfconnection.RDFConnectionFactory;
import org.apache.jena.riot.RDFDataMgr;
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
public class RunOGCValidationExamples_byQueryFactory 
{
	private static String ontologiePath = "./resources/ontologies/OGC_VaidationDataset.rdf";
	
	private static String originalQueryPath = "./resources/queries/OGC_Example1_withNameSpaces.ttl";
		
	public static void main(String[] args) {
		
		//Initialising GeoSPARQL Toolkit for Apache Jena
		GeoSPARQLConfig.setup(IndexOption.MEMORY);
		ExtendedFunctionConfig.setup();
		
		//Loading query data
		String originalQuery = "";
		try {
			byte[] encoded = Files.readAllBytes(Paths.get(originalQueryPath));
			originalQuery = new String(encoded, Charset.defaultCharset());
		} catch (Exception e) {
			e.printStackTrace();
		}

		Query q = QueryFactory.create(originalQuery);
		
		//Loading ontologie data
		Model model = ModelFactory.createDefaultModel();
		InputStream in = RDFDataMgr.open( ontologiePath );
		if (in == null) {
		    throw new IllegalArgumentException("File: " + ontologiePath + " not found");
		}
		
		// read the RDF/XML file
		model.read(in, null);

		// write it to standard out
		//model.write(System.out);

		System.out.println("\n==================Result===================");
		try(QueryExecution qexec = QueryExecutionFactory.create(q, model)) {
	        ResultSet results = qexec.execSelect() ;
	        for ( ; results.hasNext() ; )
	        {
	            QuerySolution soln = results.nextSolution() ;
	            RDFNode x = soln.get("f") ;
	            System.out.println(x);
	        }
	    }
		System.out.println("===========================================");
		
	}

}
