package rub.inf.bi.ui;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

import javax.swing.JFrame;

import org.apache.jena.geosparql.configuration.GeoSPARQLConfig;
import org.apache.jena.geosparql.implementation.index.IndexConfiguration.IndexOption;
import org.apache.jena.graph.Graph;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.DatasetFactory;
import org.apache.jena.query.ParameterizedSparqlString;
import org.apache.jena.query.Query;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.rdfconnection.RDFConnection;
import org.apache.jena.rdfconnection.RDFConnectionFactory;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.RDFFormat;
import org.apache.jena.shacl.ShaclValidator;
import org.apache.jena.shacl.Shapes;
import org.apache.jena.shacl.ValidationReport;
import org.apache.jena.shacl.lib.ShLib;
import org.apache.jena.shacl.validation.ShaclSimpleValidator;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.JFXPanel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import rub.inf.bi.extension.jena.ExtendedFunctionConfig;
import rub.inf.bi.extension.jena.ExtendedPropertyConfig;
import rub.inf.bi.extension.jena.NamespaceManager;

public class DemoFrame extends JFrame {

	private enum EXTENSIONS {
		SPARQL, SHACL
	};

	private JFXPanel contentPanel = null;

	private String source = "";
	private String queryFile = "";

	public DemoFrame() {
		// Initialising GeoSPARQL Toolkit for Apache Jena
		GeoSPARQLConfig.setup(IndexOption.MEMORY);
		ExtendedFunctionConfig.setup();
		ExtendedPropertyConfig.setup();

		this.setTitle("Query RDF-Graph");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(650, 450);
		this.setLocationRelativeTo(null);
		// this.setResizable(false);

		contentPanel = new JFXPanel();
		this.add(contentPanel);

		Platform.runLater(() -> {

			GridPane grid = new GridPane();
			grid.setAlignment(Pos.CENTER);
			grid.setHgap(10);
			grid.setVgap(10);
			grid.setPadding(new Insets(10, 10, 10, 10));

			ColumnConstraints col1 = new ColumnConstraints();
			col1.setPercentWidth(15);
			ColumnConstraints col2 = new ColumnConstraints();
			col2.setPercentWidth(70);
			ColumnConstraints col3 = new ColumnConstraints();
			col3.setPercentWidth(15);
			grid.getColumnConstraints().addAll(col1, col2, col3);

			Scene scene = new Scene(grid);
			contentPanel.setScene(scene);

			Label rdfFormatName = new Label("Type:");
			grid.add(rdfFormatName, 0, 0);

			ArrayList<EXTENSIONS> comboItems2 = new ArrayList<>();
			comboItems2.add(EXTENSIONS.SPARQL);
			comboItems2.add(EXTENSIONS.SHACL);

			ObservableList<EXTENSIONS> options2 = FXCollections.observableArrayList(comboItems2);

			ComboBox<EXTENSIONS> comboBox2 = new ComboBox<EXTENSIONS>(options2);
			comboBox2.setMaxWidth(Double.MAX_VALUE);
			comboBox2.getSelectionModel().select(0);

			grid.add(comboBox2, 1, 0, 2, 1);
			grid.setHgrow(comboBox2, Priority.ALWAYS);

			Label titelName = new Label("RDF:");
			grid.add(titelName, 0, 1);

			TextField titelTextField = new TextField();
			titelTextField.setText(source);
			titelTextField.textProperty().addListener((observable, oldValue, newValue) -> {
				source = newValue;
			});
			grid.add(titelTextField, 1, 1);

			Button buttonIFCFileSelect = new Button("Load Graph");
			buttonIFCFileSelect.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
					FileChooser fileChooser = new FileChooser();
					fileChooser.setInitialDirectory(new File("./src/main/resources/rdf/"));
					fileChooser.setTitle("Open RDF File");
					fileChooser.getExtensionFilters().add(
							new ExtensionFilter("Resource Description Framework", "*.rdf", "*.ttl"));

					File file = fileChooser.showOpenDialog(null);
					if (file != null) {
						source = file.getAbsolutePath();
						titelTextField.setText(source);
					}
				}
			});
			buttonIFCFileSelect.setMaxWidth(Double.MAX_VALUE);
			grid.add(buttonIFCFileSelect, 2, 1);
			grid.setHgrow(buttonIFCFileSelect, Priority.ALWAYS);

			// ===============================================

			Label titelName2 = new Label("SPARQL:");
			grid.add(titelName2, 0, 2);

			TextField titelTextField2 = new TextField();
			titelTextField2.setText(source);
			titelTextField2.textProperty().addListener((observable, oldValue, newValue) -> {
				queryFile = newValue;
			});
			grid.add(titelTextField2, 1, 2);

			Button buttonRDFFileSelect = new Button("Load Query");
			buttonRDFFileSelect.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
					FileChooser fileChooser = new FileChooser();
					fileChooser.setInitialDirectory(new File("./src/main/resources/sparql/Geometry3D Tests/"));
					fileChooser.setTitle("Open RDF File");
					fileChooser.getExtensionFilters().add(
							new ExtensionFilter("SPARQL or SHACL File", "*.ttl"));

					File file = fileChooser.showOpenDialog(null);
					if (file != null) {
						queryFile = file.getAbsolutePath();
						titelTextField2.setText(queryFile);
					}
				}
			});

			buttonRDFFileSelect.setMaxWidth(Double.MAX_VALUE);
			grid.add(buttonRDFFileSelect, 2, 2);
			grid.setHgrow(buttonRDFFileSelect, Priority.ALWAYS);

			ListView<String> resultListView = new ListView<String>();
			resultListView.setEditable(false);
			resultListView.setStyle("-fx-font: 14 consolas;");

			grid.add(resultListView, 0, 4, 3, 1);
			grid.setHgrow(resultListView, Priority.ALWAYS);
			grid.setVgrow(resultListView, Priority.ALWAYS);

			// ===============================================

			Button buttonConvert = new Button("Query RDF-Graph");
			buttonConvert.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
					if (comboBox2.getSelectionModel().getSelectedItem().equals(EXTENSIONS.SPARQL)) {
						ResultSet result = querySPARQL();
						if (result != null) {
							String[] resultTextSplit = ResultSetFormatter.asText(result).split("\\r?\\n");
							resultListView.setItems(
									FXCollections.observableList(
											Arrays.asList(resultTextSplit)));

						} else {
							String[] resultTextSplit = { "GOT NULL" };
							resultListView.setItems(
									FXCollections.observableList(
											Arrays.asList(resultTextSplit)));
						}
					}
					if (comboBox2.getSelectionModel().getSelectedItem().equals(EXTENSIONS.SHACL)) {

						String report = querySHACL();

						if (report != null) {
							String[] resultTextSplit = report.split("\\r?\\n");
							resultListView.setItems(
									FXCollections.observableList(
											Arrays.asList(resultTextSplit)));

						} else {
							String[] resultTextSplit = { "GOT NULL" };
							resultListView.setItems(
									FXCollections.observableList(
											Arrays.asList(resultTextSplit)));
						}

					}
				}
			});

			buttonConvert.setMaxWidth(Double.MAX_VALUE);
			grid.add(buttonConvert, 1, 3);
			grid.setHgrow(buttonConvert, Priority.ALWAYS);

		});

	}

	private ResultSet querySPARQL() {

		// Loading query data
		String query = "";
		try {
			byte[] encoded = Files.readAllBytes(Paths.get(queryFile));
			query = new String(encoded, Charset.defaultCharset());
		} catch (Exception e) {
			e.printStackTrace();
		}

		ParameterizedSparqlString queryStr = new ParameterizedSparqlString();
		queryStr.setNsPrefixes(
				NamespaceManager.getInstance().getAllPrefixes());
		queryStr.append(query);

		Query q = queryStr.asQuery();

		// Loading ontologie data
		Dataset dataset = RDFDataMgr.loadDataset(source);

		/*
		 * ArrayList<String> uris = new ArrayList<String>();
		 * uris.add(source);
		 * 
		 * Dataset dataset = DatasetFactory.create(source);
		 */
		String resultText = "";
		try (RDFConnection conn = RDFConnectionFactory.connect(dataset)) {

			// System.out.println("=================DATABASE==================");
			// System.out.println(conn.fetch().toString());
			// System.out.println("===========================================");

			// Executing query and retrieve results
			// System.out.println("\n==================Result===================");

			ResultSet result = conn.query(q).execSelect();
			return result;

			// System.out.println("Result Variables: " + result.getResultVars());
			// while(result.hasNext()) {
			// System.out.println("Solution: " + result.next().toString());
			// }

			// System.out.println("===========================================");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}

	private String querySHACL() {
		Graph dataGraph = RDFDataMgr.loadGraph(source);
		Graph shapesGraph = RDFDataMgr.loadGraph(queryFile);

		Shapes shapes = Shapes.parse(shapesGraph);

		ValidationReport report = ShaclValidator.get().validate(shapes, dataGraph);
		// ShLib.printReport(report);

		// FileOutputStream outputStream = new FileOutputStream("./validationlog.txt");
		// RDFDataMgr.write(outputStream, report.getModel(), Lang.TTL);

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		RDFDataMgr.write(outputStream, report.getModel(), Lang.TTL);
		return new String(outputStream.toByteArray());

	}
}
