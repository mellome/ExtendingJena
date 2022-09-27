package rub.inf.bi.io;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.jena.geosparql.configuration.GeoSPARQLConfig;
import org.apache.jena.geosparql.implementation.GeometryWrapper;
import org.apache.jena.geosparql.implementation.GeometryWrapperFactory;
import org.apache.jena.geosparql.implementation.WKTLiteralFactory;
import org.apache.jena.geosparql.implementation.datatype.WKTDatatype;
import org.apache.jena.geosparql.implementation.index.IndexConfiguration.IndexOption;
import org.apache.jena.ontology.impl.OntModelImpl;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.impl.PropertyImpl;
import org.apache.jena.rdf.model.impl.ResourceImpl;
import org.apache.jena.rdf.model.impl.StatementImpl;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Polygon;


public class RDFConverter {
	
	private OntModelImpl model;
	private HashMap<String, String> prefixes;
	
	public RDFConverter() {
		GeoSPARQLConfig.setup(IndexOption.MEMORY);

		model = (OntModelImpl)ModelFactory.createOntologyModel();
		prefixes = new HashMap<String, String>();
		
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
		
	}
	
	
	public void constructA() {
	
//		GeoSPARQLOperations.applyPrefixes(m);
//		m.setNsPrefix("my", "http://example.org/ApplicationSchema#");

		//GeoSPARQLOperations.applyInferencing(m);
		
		model.setNsPrefixes(prefixes);
		
		//PRINT ALL GEOMETRYDATATTYPES
//		Iterator<RDFDatatype> t = TypeMapper.getInstance().listTypes();
//		while(t.hasNext()) {
//			System.out.println(t.next().toString());
//		}
		
		//Test p = m.createOntResource( Test.class, ResourceFactory., "http://example.org/ApplicationSchema#F" );
		//OntResource res = m.createOntResource("http://example.org/ApplicationSchema#F")

		StatementImpl statementRootA = new StatementImpl(
				new ResourceImpl(prefixes.get("my"),"RootModel"), 
				new PropertyImpl(prefixes.get("sf") + "Polygon"), 
				new ResourceImpl(prefixes.get("my"), "A")
		);
		model.add(statementRootA);

		StatementImpl statementRootB = new StatementImpl(
				new ResourceImpl(prefixes.get("my"),"RootModel"), 
				new PropertyImpl(prefixes.get("sf") + "Polygon"), 
				new ResourceImpl(prefixes.get("my"), "B")
		);
		model.add(statementRootB);
		
		
		//===================================================================
		
		ArrayList<Coordinate> faceACoords = new ArrayList<Coordinate>();
		faceACoords.add(new Coordinate(0.0, 0.0, 4.0));
		faceACoords.add(new Coordinate(0.0, 4.0, 4.0));
		faceACoords.add(new Coordinate(8.0, 8.0, 4.0));
		faceACoords.add(new Coordinate(4.0, 0.0, 4.0));
		faceACoords.add(new Coordinate(0.0, 0.0, 4.0));
			
		GeometryWrapper wrapperA = GeometryWrapperFactory.createPolygon(faceACoords, prefixes.get("geo") + "wktLiteral");
		Polygon faceA = (Polygon)wrapperA.getParsingGeometry();

		
		ArrayList<Coordinate> faceBCoords = new ArrayList<Coordinate>();
		faceBCoords.add(new Coordinate(0.0, 0.0, 4.0));
		faceBCoords.add(new Coordinate(0.0, 4.0, 4.0));
		faceBCoords.add(new Coordinate(4.0, 4.0, 4.0));
		faceBCoords.add(new Coordinate(4.0, 0.0, 4.0));
		faceBCoords.add(new Coordinate(0.0, 0.0, 4.0));
			
		GeometryWrapper wrapperB = GeometryWrapperFactory.createPolygon(faceBCoords, prefixes.get("geo") + "wktLiteral");
		Polygon faceB = (Polygon)wrapperB.getParsingGeometry();

		model.add(new ResourceImpl("http://example.org/ApplicationSchema#","A"), 
			  new PropertyImpl("http://www.opengis.net/ont/geosparql#asWKT"), 
			  wrapperA.getLexicalForm(), 
			  WKTDatatype.INSTANCE
		);
			
		model.add(new ResourceImpl("http://example.org/ApplicationSchema#","B"), 
			  new PropertyImpl("http://www.opengis.net/ont/geosparql#asWKT"), 
			  wrapperB.getLexicalForm(), 
			  WKTDatatype.INSTANCE
		);
			
		
		//===================================================================		
		
		//m.createClass("http://example.org/ApplicationSchema#TEST");
		//m.createBag("http://example.org/ApplicationSchema#TEST");
		//m.createAlt("http://example.org/ApplicationSchema#TEST");
		//m.createReifiedStatement("http://example.org/ApplicationSchema#TEST2", statementA);
		//m.createSeq("http://example.org/ApplicationSchema#TEST2");

	}


	public void constructB() {
		

		//GeoSPARQLOperations.applyInferencing(m);
		
		model.setNsPrefixes(prefixes);
		
		StatementImpl statementRootA = new StatementImpl(
				new ResourceImpl(prefixes.get("my"),"RootModel"), 
				new PropertyImpl(prefixes.get("sf") + "LineString"), 
				new ResourceImpl(prefixes.get("my"), "A")
		);
		model.add(statementRootA);

		StatementImpl statementRootB = new StatementImpl(
				new ResourceImpl(prefixes.get("my"),"RootModel"), 
				new PropertyImpl(prefixes.get("sf") + "LineString"), 
				new ResourceImpl(prefixes.get("my"), "B")
		);
		model.add(statementRootB);
		
		
		//===================================================================
		//ADD GEOMETRY TO RDF!
		Coordinate[] coord = new Coordinate[2];
		coord[0] = new Coordinate(0.0, 0.0, 1.0);
		coord[1] = new Coordinate(4.0, 4.0, 2.0);
		
		GeometryWrapper wrapper = GeometryWrapperFactory.createLineString(coord, prefixes.get("geo") + "wktLiteral");

		model.add(new ResourceImpl(prefixes.get("my"),"A"), 
			  new PropertyImpl(prefixes.get("geo"), "asWKT"), 
			  wrapper.getLexicalForm(), 
			  WKTDatatype.INSTANCE
		);
		//===================================================================
		
		//===================================================================
		//ADD GEOMETRY TO RDF!
		Coordinate[] coord2 = new Coordinate[2];
		coord2[0] = new Coordinate(4.0, 0.0);
		coord2[1] = new Coordinate(0.0, 4.0);
		
		GeometryWrapper wrapper2 = GeometryWrapperFactory.createLineString(coord2, prefixes.get("geo") + "wktLiteral");

		model.add(new ResourceImpl(prefixes.get("my"),"B"), 
			  new PropertyImpl(prefixes.get("geo") + "asWKT"), 
			  wrapper2.getLexicalForm(), 
			  WKTDatatype.INSTANCE
		);
		//===================================================================

	}


	private GeometryWrapper createCube(double size, Coordinate offset) {
		ArrayList<Coordinate> faceACoords = new ArrayList<Coordinate>();
		faceACoords.add(new Coordinate(0.0 + offset.getX(), 0.0 + offset.getY(), 0.0 + offset.getZ()));
		faceACoords.add(new Coordinate(0.0 + offset.getX(), size + offset.getY(), 0.0 + offset.getZ()));
		faceACoords.add(new Coordinate(size + offset.getX(), size + offset.getY(), 0.0 + offset.getZ()));
		faceACoords.add(new Coordinate(size + offset.getX(), 0.0 + offset.getY(), 0.0 + offset.getZ()));
		faceACoords.add(new Coordinate(0.0 + offset.getX(), 0.0 + offset.getY(), 0.0 + offset.getZ()));
		
		GeometryWrapper wrapperA = GeometryWrapperFactory.createPolygon(faceACoords, prefixes.get("geo") + "wktLiteral");
		Polygon faceA = (Polygon)wrapperA.getParsingGeometry();
		
		ArrayList<Coordinate> faceBCoords = new ArrayList<Coordinate>();
		faceBCoords.add(new Coordinate(0.0 + offset.getX(), 0.0 + offset.getY(), size + offset.getZ()));
		faceBCoords.add(new Coordinate(size + offset.getX(), 0.0 + offset.getY(), size + offset.getZ()));
		faceBCoords.add(new Coordinate(size + offset.getX(), size + offset.getY(), size + offset.getZ()));
		faceBCoords.add(new Coordinate(0.0 + offset.getX(), size + offset.getY(), size + offset.getZ()));
		faceBCoords.add(new Coordinate(0.0 + offset.getX(), 0.0 + offset.getY(), size + offset.getZ()));
		
		GeometryWrapper wrapperB = GeometryWrapperFactory.createPolygon(faceBCoords, prefixes.get("geo") + "wktLiteral");
		Polygon faceB = (Polygon)wrapperB.getParsingGeometry();
		
		ArrayList<Coordinate> faceCCoords = new ArrayList<Coordinate>();
		faceCCoords.add(new Coordinate(0.0 + offset.getX(), 0.0 + offset.getY(), 0.0 + offset.getZ()));
		faceCCoords.add(new Coordinate(0.0 + offset.getX(), size + offset.getY(), 0.0 + offset.getZ()));
		faceCCoords.add(new Coordinate(0.0 + offset.getX(), size + offset.getY(), size + offset.getZ()));
		faceCCoords.add(new Coordinate(0.0 + offset.getX(), 0.0 + offset.getY(), size + offset.getZ()));
		faceCCoords.add(new Coordinate(0.0 + offset.getX(), 0.0 + offset.getY(), 0.0 + offset.getZ()));
		
		GeometryWrapper wrapperC = GeometryWrapperFactory.createPolygon(faceCCoords, prefixes.get("geo") + "wktLiteral");
		Polygon faceC = (Polygon)wrapperC.getParsingGeometry();
		
		ArrayList<Coordinate> faceDCoords = new ArrayList<Coordinate>();
		faceDCoords.add(new Coordinate(size + offset.getX(), 0.0 + offset.getY(), 0.0 + offset.getZ()));
		faceDCoords.add(new Coordinate(size + offset.getX(), size + offset.getY(), 0.0 + offset.getZ()));
		faceDCoords.add(new Coordinate(size + offset.getX(), size + offset.getY(), size + offset.getZ()));
		faceDCoords.add(new Coordinate(size + offset.getX(), 0.0 + offset.getY(), size + offset.getZ()));
		faceDCoords.add(new Coordinate(size + offset.getX(), 0.0 + offset.getY(), 0.0 + offset.getZ()));
		
		GeometryWrapper wrapperD = GeometryWrapperFactory.createPolygon(faceDCoords, prefixes.get("geo") + "wktLiteral");
		Polygon faceD = (Polygon)wrapperD.getParsingGeometry();
		
		ArrayList<Coordinate> faceECoords = new ArrayList<Coordinate>();
		faceECoords.add(new Coordinate(0.0 + offset.getX(), 0.0 + offset.getY(), 0.0 + offset.getZ()));
		faceECoords.add(new Coordinate(size + offset.getX(), 0.0 + offset.getY(), 0.0 + offset.getZ()));
		faceECoords.add(new Coordinate(size + offset.getX(), 0.0 + offset.getY(), size + offset.getZ()));
		faceECoords.add(new Coordinate(0.0 + offset.getX(), 0.0 + offset.getY(), size + offset.getZ()));
		faceECoords.add(new Coordinate(0.0 + offset.getX(), 0.0 + offset.getY(), 0.0 + offset.getZ()));
		
		GeometryWrapper wrapperE = GeometryWrapperFactory.createPolygon(faceECoords, prefixes.get("geo") + "wktLiteral");
		Polygon faceE = (Polygon)wrapperE.getParsingGeometry();
		
		ArrayList<Coordinate> faceFCoords = new ArrayList<Coordinate>();
		faceFCoords.add(new Coordinate(0.0 + offset.getX(), size + offset.getY(), 0.0 + offset.getZ()));
		faceFCoords.add(new Coordinate(size + offset.getX(), size + offset.getY(), 0.0 + offset.getZ()));
		faceFCoords.add(new Coordinate(size + offset.getX(), size + offset.getY(), size + offset.getZ()));
		faceFCoords.add(new Coordinate(0.0 + offset.getX(), size + offset.getY(), size + offset.getZ()));
		faceFCoords.add(new Coordinate(0.0 + offset.getX(), size + offset.getY(), 0.0 + offset.getZ()));
		
		GeometryWrapper wrapperF = GeometryWrapperFactory.createPolygon(faceFCoords, prefixes.get("geo") + "wktLiteral");
		Polygon faceF = (Polygon)wrapperF.getParsingGeometry();
		
		
		ArrayList<Polygon> polygons = new ArrayList<Polygon>();
		polygons.add(faceA);
		polygons.add(faceB);
		polygons.add(faceC);
		polygons.add(faceD);
		polygons.add(faceE);
		polygons.add(faceF);
		
		GeometryWrapper wrapper = GeometryWrapperFactory.createMultiPolygon(polygons, prefixes.get("geo") + "wktLiteral");
		return wrapper;
	}
	
	public void constructCubeExample(String filepath) throws IOException {
		
		model = (OntModelImpl)ModelFactory.createOntologyModel();

		model.setNsPrefixes(prefixes);
		
		StatementImpl statementRootA = new StatementImpl(
				new ResourceImpl(prefixes.get("my"),"RootModel"), 
				new PropertyImpl(prefixes.get("sf") + "MultiPolygon"), 
				new ResourceImpl(prefixes.get("my"), "A")
		);
		model.add(statementRootA);

		StatementImpl statementRootB = new StatementImpl(
				new ResourceImpl(prefixes.get("my"),"RootModel"), 
				new PropertyImpl(prefixes.get("sf") + "MultiPolygon"), 
				new ResourceImpl(prefixes.get("my"), "B")
		);
		model.add(statementRootB);
		
		StatementImpl statementRootC = new StatementImpl(
				new ResourceImpl(prefixes.get("my"),"RootModel"), 
				new PropertyImpl(prefixes.get("sf") + "MultiPolygon"), 
				new ResourceImpl(prefixes.get("my"), "C")
		);
		model.add(statementRootC);
		
		
		//===================================================================
		//ADD GEOMETRY TO RDF!
		
		GeometryWrapper cubeA = this.createCube(4.0, new Coordinate(0.0, 0.0, 0.0));
		model.add(new ResourceImpl(prefixes.get("my"),"A"), 
			  new PropertyImpl(prefixes.get("geo"), "asWKT"), 
			  cubeA.getLexicalForm(), 
			  WKTDatatype.INSTANCE
		);
		
		GeometryWrapper cubeB = this.createCube(4.0, new Coordinate(0.0, 0.0, 8.0));
		model.add(new ResourceImpl(prefixes.get("my"),"B"), 
			  new PropertyImpl(prefixes.get("geo"), "asWKT"), 
			  cubeB.getLexicalForm(), 
			  WKTDatatype.INSTANCE
		);

		GeometryWrapper cubeC = this.createCube(3.0, new Coordinate(0.5, 0.5, 0.5));
		model.add(new ResourceImpl(prefixes.get("my"),"C"), 
			  new PropertyImpl(prefixes.get("geo"), "asWKT"), 
			  cubeC.getLexicalForm(), 
			  WKTDatatype.INSTANCE
		);
		
		save(filepath);
	}
	
	public void save(String filepath) throws IOException {
	    OutputStream out = new FileOutputStream(filepath);
	    model.write(out); //RDFFormat.RDFXML.toString()
		out.close();
	}
	
}
