package rub.inf.bi.extension.jena.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.math3.geometry.euclidean.threed.Line;
import org.apache.commons.math3.geometry.euclidean.threed.Plane;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.geometry.euclidean.threed.Euclidean3D;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;

public class GeometryOperators3D {

	public static final double TOLERANCE = 0.1;

	// TODO: why should we create different functions in case of different geometries but not use the general function directely?
	public static boolean contains3D(Plane plane, Vector3D point) {
		return plane.contains(point);
	}

	public static boolean contains3D(Line line, Vector3D point) {
		return line.contains(point);
	}

	// Yifeng
	// =================================================================
	public static boolean contains3D(Geometry geom1, Geometry geom2) {
		return geom1.contains(geom2);
	}

	public static boolean within3D(Geometry geom1, Geometry geom2) {
		return geom1.within(geom2);
	}

	public static boolean disjoint3D(Geometry geom1, Geometry geom2) {
		return geom1.disjoint(geom2);
	}

	public static boolean intersect3D(Geometry geom1, Geometry geom2) {
		return geom1.intersects(geom2);
	}

	public static List<Vector3D> intersectionRR3D(Polygon rectangleA, Polygon rectangleB) {
		List<Vector3D> intersectionPoints = new ArrayList<Vector3D>();

		Plane rectanglePlaneA = null;
		Plane rectanglePlaneB = null;
		if ((rectangleA.getCoordinates().length - 1) >= 3) {

			Coordinate pA1 = rectangleA.getCoordinates()[0];
			Coordinate pB1 = rectangleA.getCoordinates()[1];
			Coordinate pC1 = rectangleA.getCoordinates()[2];

			Vector3D pt1V1 = new Vector3D(pA1.getX(), pA1.getY(), pA1.getZ());
			Vector3D pt1V2 = new Vector3D(pB1.getX(), pB1.getY(), pB1.getZ());
			Vector3D pt1V3 = new Vector3D(pC1.getX(), pC1.getY(), pC1.getZ());

			rectanglePlaneA = new Plane(
					pt1V1,
					pt1V2,
					pt1V3,
					GeometryOperators3D.TOLERANCE);
		}

		if ((rectangleB.getCoordinates().length - 1) >= 3) {

			Coordinate pA2 = rectangleB.getCoordinates()[0];
			Coordinate pB2 = rectangleB.getCoordinates()[1];
			Coordinate pC2 = rectangleB.getCoordinates()[2];

			Vector3D pt2V1 = new Vector3D(pA2.getX(), pA2.getY(), pA2.getZ());
			Vector3D pt2V2 = new Vector3D(pB2.getX(), pB2.getY(), pB2.getZ());
			Vector3D pt2V3 = new Vector3D(pC2.getX(), pC2.getY(), pC2.getZ());

			rectanglePlaneB = new Plane(
					pt2V1,
					pt2V2,
					pt2V3,
					GeometryOperators3D.TOLERANCE);

		}

		if (rectanglePlaneA != null && rectanglePlaneB != null) {
			Line l = GeometryOperators3D.intersection3D(rectanglePlaneA, rectanglePlaneB); // intersection of two planes

			if (l != null){
				List<Vector3D> interT1 = intersection3DLP(l, rectangleA);
				List<Vector3D> interT2 = intersection3DLP(l, rectangleB);
				
				// List<Vector3D> interT1T2 = Stream.concat(interT1.stream(), interT2.stream()).collect(Collectors.toList());  
				// List<Vector3D> commonVectorLst = interT1.stream().filter(interT2::contains).collect(Collectors.toList());
				// if (!commonVectorLst.isEmpty()) {
				// 	intersectionPoints.addAll(interT1);
				// 	intersectionPoints.addAll(interT2);
				// }
				if (interT1.size() > 0 && interT2.size() > 0) { // the commen intersection line must be instersected with the given geometries!!!
					for(Vector3D ipA : interT1){
						if(contains3D(rectangleB, ipA)){
							intersectionPoints.add(ipA);
						}
					}
					for (Vector3D ipB : interT2){
						if(contains3D(rectangleA, ipB)){
							intersectionPoints.add(ipB);
						}
					}
				}
			}
		}
		return intersectionPoints;
	}

	/*
	 * Line -- Polygon
	 */
	public static ArrayList<Vector3D> intersection3DLP(Line source, Polygon polygon) {
		ArrayList<Vector3D> intersectionPoints = new ArrayList<Vector3D>();
		int num_edges = polygon.getCoordinates().length;

		// rectangle
		if ( num_edges == 3 ){
			for (int cIt1 = 1; cIt1 < num_edges; cIt1++) {
				Vector3D tempA = new Vector3D(
					polygon.getCoordinates()[cIt1 - 1].getX(),
					polygon.getCoordinates()[cIt1 - 1].getY(),
					polygon.getCoordinates()[cIt1 - 1].getZ());
				Vector3D tempB = new Vector3D(
					polygon.getCoordinates()[cIt1].getX(),
					polygon.getCoordinates()[cIt1].getY(),
					polygon.getCoordinates()[cIt1].getZ());
	
				Line target = new Line(tempA, tempB, GeometryOperators3D.TOLERANCE); // first two edages
				Vector3D interSectionPoint = source.intersection(target);
	
				if (interSectionPoint != null) {
					if (contains3D(tempA, tempB, interSectionPoint)) {
						intersectionPoints.add(interSectionPoint);
					}
				}
			}
		}
		// polygon
		if ( num_edges > 3 ){
			for (int cIt1 = 1; cIt1 < num_edges; cIt1++) {
				Vector3D tempA = new Vector3D(
					polygon.getCoordinates()[cIt1 - 1].getX(),
					polygon.getCoordinates()[cIt1 - 1].getY(),
					polygon.getCoordinates()[cIt1 - 1].getZ());
				Vector3D tempB = new Vector3D(
					polygon.getCoordinates()[cIt1].getX(),
					polygon.getCoordinates()[cIt1].getY(),
					polygon.getCoordinates()[cIt1].getZ());
	
				Line target = new Line(tempA, tempB, GeometryOperators3D.TOLERANCE); // first n-1 edages
				Vector3D interSectionPoint = source.intersection(target);
	
				if (interSectionPoint != null) {
					if (contains3D(tempA, tempB, interSectionPoint)) {
						intersectionPoints.add(interSectionPoint);
					}
				}
			}
		}
		return intersectionPoints;
	}
	
	// =================================================================
	// TOUCH
	// =================================================================
	public static boolean touch3D(Line geom1, Point geom2) {
		Vector3D vGeom2 = new Vector3D( 
			geom2.getCoordinate().getX(), 
			geom2.getCoordinate().getY(), 
			geom2.getCoordinate().getZ());
		return geom1.contains(vGeom2)? true : false;
	}

	public static boolean touch3D(Polygon geom1, Point geom2) {
		Vector3D vGeom2 = new Vector3D( geom2.getCoordinate().getX(), 
										geom2.getCoordinate().getY(), 
										geom2.getCoordinate().getZ());

		for (int cIt1 = 1; cIt1 < geom1.getCoordinates().length; cIt1++) {
			Vector3D tempA = new Vector3D(
				geom1.getCoordinates()[cIt1 - 1].getX(),
				geom1.getCoordinates()[cIt1 - 1].getY(),
				geom1.getCoordinates()[cIt1 - 1].getZ());
			Vector3D tempB = new Vector3D(
				geom1.getCoordinates()[cIt1].getX(),
				geom1.getCoordinates()[cIt1].getY(),
				geom1.getCoordinates()[cIt1].getZ());

			if (contains3D(tempA, tempB, vGeom2)) {
				return true;
			}
		}
		return false;
	}

	public static boolean touch3D(MultiPolygon geom1, Point geom2) {

		for (int i = 0; i < geom1.getNumGeometries(); i++) {
			if (geom1.getGeometryN(i) instanceof Polygon) {
				Polygon tempFace = (Polygon) geom1.getGeometryN(i);
				if (touch3D(tempFace, geom2)) {
					return true;
				}
			}
		}
		return false;
	}

	public static boolean touch3D(Line geom1, Line geom2) {
		// TODO: find a way to extract start- and end-point of a Line object
		return false;
	}

	public static boolean touch3D(Polygon geom1, Line geom2) {
		int intersectedPointsNum = 0;

		for (int cIt1 = 1; cIt1 < geom1.getCoordinates().length; cIt1++) {
			Vector3D tempA = new Vector3D(
				geom1.getCoordinates()[cIt1 - 1].getX(),
				geom1.getCoordinates()[cIt1 - 1].getY(),
				geom1.getCoordinates()[cIt1 - 1].getZ());
			Vector3D tempB = new Vector3D(
				geom1.getCoordinates()[cIt1].getX(),
				geom1.getCoordinates()[cIt1].getY(),
				geom1.getCoordinates()[cIt1].getZ());
			Line tempLine = new Line(tempA, tempB, TOLERANCE);

			if (touch3D(tempLine, geom2)) {
				// TODO: determine the case where a line lays down in the polygon/surface
			}
		}
		return false;
	}

	public static boolean touch3D(MultiPolygon geom1, Line geom2) {
		return false;
	}

	public static boolean touch3D(Polygon geom1, Polygon geom2) {
		return false;
	}

	public static boolean touch3D(MultiPolygon geom1, Polygon geom2) {
		return false;
	}

	public static boolean touch3D(MultiPolygon geom1, MultiPolygon geom2) {
		return false;
	}
	// =================================================================

	public static Vector3D intersection3D(Line line, Vector3D start, Vector3D end) {
		Line checkLine = new Line(start, end, GeometryOperators3D.TOLERANCE);
		return intersection3D(line, checkLine);
	}

	public static Vector3D intersection3D(Line lineA, Line lineB) {
		return lineA.intersection(lineB);
	}

	public static Vector3D intersection3D(Plane plane, Vector3D start, Vector3D end) {
		Line checkLine = new Line(start, end, GeometryOperators3D.TOLERANCE);
		return intersection3D(plane, checkLine);
	}

	public static Vector3D intersection3D(Plane plane, Line line) {
		return plane.intersection(line);
	}

	public static Line intersection3D(Plane planeA, Plane planeB) {
		return planeA.intersection(planeB);
	}

	public static ArrayList<Vector3D> intersection3D(MultiPolygon mpG1, LineString line) {
		ArrayList<Vector3D> intersectionPoints = new ArrayList<Vector3D>();
		for (int i = 0; i < mpG1.getNumGeometries(); i++) {
			if (mpG1.getGeometryN(i) instanceof Polygon) {
				Polygon triangle = (Polygon) mpG1.getGeometryN(i);

				ArrayList<Vector3D> intersectionResult = intersection3D(triangle, line);
				intersectionPoints.addAll(intersectionResult);

			}
		}

		return intersectionPoints;

	}

	public static ArrayList<Vector3D> intersection3D(Polygon planeFaceArea, LineString line) {
		ArrayList<Vector3D> intersectionPoints = new ArrayList<Vector3D>();
		Plane planeA = null;
		if ((planeFaceArea.getCoordinates().length - 1) >= 3) {

			Coordinate pA1 = planeFaceArea.getCoordinates()[0];
			Coordinate pB1 = planeFaceArea.getCoordinates()[1];
			Coordinate pC1 = planeFaceArea.getCoordinates()[2];

			Vector3D pt1V1 = new Vector3D(pA1.getX(), pA1.getY(), pA1.getZ());
			Vector3D pt1V2 = new Vector3D(pB1.getX(), pB1.getY(), pB1.getZ());
			Vector3D pt1V3 = new Vector3D(pC1.getX(), pC1.getY(), pC1.getZ());

			planeA = new Plane(
					pt1V1,
					pt1V2,
					pt1V3,
					GeometryOperators3D.TOLERANCE);
		}

		for (int lI = 1; lI < line.getCoordinates().length; lI++) {
			Vector3D tempA = new Vector3D(
					line.getCoordinates()[lI - 1].getX(),
					line.getCoordinates()[lI - 1].getY(),
					line.getCoordinates()[lI - 1].getZ());
			Vector3D tempB = new Vector3D(
					line.getCoordinates()[lI].getX(),
					line.getCoordinates()[lI].getY(),
					line.getCoordinates()[lI].getZ());

			Line target = new Line(tempA, tempB, GeometryOperators3D.TOLERANCE);
			Vector3D interSectionPoint = planeA.intersection(target);

			if (interSectionPoint != null) {
				if (contains3D(tempA, tempB, interSectionPoint) && contains3D(planeFaceArea, interSectionPoint)) { // TODO
																													// check
																													// if
																													// point
																													// is
																													// on
																													// plane
					intersectionPoints.add(interSectionPoint);
				}
			}

		}

		return intersectionPoints;

	}

	public static boolean contains3D(Polygon mesh, Vector3D point) {
		Coordinate[] fPoints = mesh.getCoordinates();

		// TODO: accept complex areas, currently only triangles and rectangles are
		// accepted
		boolean result = false;
		if (fPoints.length - 1 >= 3) {
			Vector3D tempA = new Vector3D(
					fPoints[0].getX(),
					fPoints[0].getY(),
					fPoints[0].getZ());
			Vector3D tempB = new Vector3D(
					fPoints[1].getX(),
					fPoints[1].getY(),
					fPoints[1].getZ());
			Vector3D tempC = new Vector3D(
					fPoints[2].getX(),
					fPoints[2].getY(),
					fPoints[2].getZ());

			result = contains3D(tempA, tempB, tempC, point);
			if (fPoints.length - 1 == 4) {
				Vector3D tempD = new Vector3D(
						fPoints[3].getX(),
						fPoints[3].getY(),
						fPoints[3].getZ());
				result = result || contains3D(tempA, tempC, tempD, point);
			}

		}

		return result;
	}

	/*
	 * determine if a vector point in the given triangle
	 */
	public static boolean contains3D(Vector3D trianglePointA, Vector3D trianglePointB, Vector3D trianglePointC,
			Vector3D point) {
		double areaABC = triangleArea(trianglePointA, trianglePointB, trianglePointC);
		double alpha = triangleArea(point, trianglePointB, trianglePointC) * (1.0 / areaABC);
		double beta = triangleArea(point, trianglePointC, trianglePointA) * (1.0 / areaABC);
		double gamma = 1.0 - alpha - beta;

		if ((0.0 <= alpha && alpha <= 1.0) &&
				(0.0 <= beta && beta <= 1.0) &&
				(0.0 <= gamma && gamma <= 1.0) &&
				((alpha + beta + gamma) == 1.0)) {
			return true;
		}

		return false;
	}

	private static double triangleArea(Vector3D pA, Vector3D pB, Vector3D pC) {
		double d1 = pA.distance(pB);
		double d2 = pA.distance(pC);
		return (d1 * d2) / 2.0;
	}

	public static boolean contains3D(Vector3D lineStart, Vector3D lineEnd, Vector3D point) {
		double distance = lineStart.distance(point) + lineEnd.distance(point);
		double sumDistance = lineStart.distance(lineEnd);
		return (distance > (sumDistance - GeometryOperators3D.TOLERANCE)
				&& distance < (sumDistance + GeometryOperators3D.TOLERANCE));
	}

	public static ArrayList<Vector3D> intersection3D(MultiPolygon mpG1, MultiPolygon mpG2) {
		ArrayList<Vector3D> intersectionPoints = new ArrayList<Vector3D>();
		for (int i = 0; i < mpG1.getNumGeometries(); i++) {
			if (mpG1.getGeometryN(i) instanceof Polygon) {
				Polygon triangleA = (Polygon) mpG1.getGeometryN(i);

				for (int j = 0; j < mpG2.getNumGeometries(); j++) {
					if (mpG2.getGeometryN(j) instanceof Polygon) {
						Polygon triangleB = (Polygon) mpG2.getGeometryN(j);

						ArrayList<Vector3D> intersectionResult = intersection3D(triangleA, triangleB);
						if (intersectionResult.size() > 0) {
							intersectionPoints.addAll(intersection3D(triangleA, triangleB));
						}

					}
				}

			}
		}

		return intersectionPoints;

	}

	/***
	 * Finds the intersecting points between two triangles in 3D.
	 * 
	 * <p>
	 * Example: <br>
	 * <br>
	 * AAAAAAAAAAAAAAAAAAAA <br>
	 * AAAAAAAAAAAAAAAAAA <br>
	 * AAAAAACAAAAAAAAA <br>
	 * AAAAACCCAAAAAA <br>
	 * AAAACCCCCAAA <br>
	 * AAACCCCCCX <br>
	 * AACCCCCCBBB <br>
	 * ACCCCXBBBBBBB <br>
	 * AAAA <br>
	 * AA <br>
	 * A <br>
	 * <br>
	 * A = TriangleA, B = TriangleB, C = Intersection, X = Result
	 * </p>
	 * 
	 * @param triangleA - The first triangle as Polygon object (must contain a
	 *                  coordinate loop of exactly 4 points!)
	 * @param triangleB - The second triangle as Polygon object (must contain a
	 *                  coordinate loop of exactly 4 points!)
	 * @return ArrayList containing all intersecting points as Vector3D. Result is
	 *         an empty List if no intersection exists.
	 */
	public static ArrayList<Vector3D> intersection3D(Polygon triangleA, Polygon triangleB) {
		ArrayList<Vector3D> intersectionPoints = new ArrayList<Vector3D>();

		Plane trianglePlaneA = null;
		Plane trianglePlaneB = null;
		if ((triangleA.getCoordinates().length - 1) == 3) {

			Coordinate pA1 = triangleA.getCoordinates()[0];
			Coordinate pB1 = triangleA.getCoordinates()[1];
			Coordinate pC1 = triangleA.getCoordinates()[2];

			Vector3D pt1V1 = new Vector3D(pA1.getX(), pA1.getY(), pA1.getZ());
			Vector3D pt1V2 = new Vector3D(pB1.getX(), pB1.getY(), pB1.getZ());
			Vector3D pt1V3 = new Vector3D(pC1.getX(), pC1.getY(), pC1.getZ());

			trianglePlaneA = new Plane(
					pt1V1,
					pt1V2,
					pt1V3,
					GeometryOperators3D.TOLERANCE);
		}

		if ((triangleB.getCoordinates().length - 1) == 3) {

			Coordinate pA2 = triangleB.getCoordinates()[0];
			Coordinate pB2 = triangleB.getCoordinates()[1];
			Coordinate pC2 = triangleB.getCoordinates()[2];

			Vector3D pt2V1 = new Vector3D(pA2.getX(), pA2.getY(), pA2.getZ());
			Vector3D pt2V2 = new Vector3D(pB2.getX(), pB2.getY(), pB2.getZ());
			Vector3D pt2V3 = new Vector3D(pC2.getX(), pC2.getY(), pC2.getZ());

			trianglePlaneB = new Plane(
					pt2V1,
					pt2V2,
					pt2V3,
					GeometryOperators3D.TOLERANCE);

		}

		if (trianglePlaneA != null && trianglePlaneB != null) {
			Line l = GeometryOperators3D.intersection3D(trianglePlaneA, trianglePlaneB);

			ArrayList<Vector3D> interT1 = intersection3D(l, triangleA);
			ArrayList<Vector3D> interT2 = intersection3D(l, triangleB);

			if (interT1.size() > 0 || interT2.size() > 0) { 
				intersectionPoints.addAll(interT1);
				intersectionPoints.addAll(interT2);
			}
		}

		return intersectionPoints;
	}

	/***
	 * Finds the intersecting points of a line and the edges of a Polygon face in
	 * 3D.
	 * 
	 * <p>
	 * Example: <br>
	 * <br>
	 * A <br>
	 * AAA <br>
	 * AAAAA <br>
	 * AAAAAAA <br>
	 * XCCCCCCCXBBBBBBBBB <br>
	 * AAAAAAAAAAA <br>
	 * AAAAAAAAAAAAA <br>
	 * AAAAAAAAAAAAAAA <br>
	 * AAAAAAAAAAAAAAAAA <br>
	 * <br>
	 * A = Mesh, B = Line, C = Intersection, X = Result
	 * </p>
	 * 
	 * @param source - The line as Line object that is expected to have an
	 *               intersection with the mesh edges
	 * @param mesh   - The mesh as Polygon object, only the edges are considered
	 * @return ArrayList containing all intersecting points as Vector3D. Result is
	 *         an empty List if no intersection exists.
	 */
	public static ArrayList<Vector3D> intersection3D(Line source, Polygon mesh) {
		ArrayList<Vector3D> intersectionPoints = new ArrayList<Vector3D>();

		for (int cIt1 = 1; cIt1 < mesh.getCoordinates().length; cIt1++) {
			Vector3D tempA = new Vector3D(
					mesh.getCoordinates()[cIt1 - 1].getX(),
					mesh.getCoordinates()[cIt1 - 1].getY(),
					mesh.getCoordinates()[cIt1 - 1].getZ());
			Vector3D tempB = new Vector3D(
					mesh.getCoordinates()[cIt1].getX(),
					mesh.getCoordinates()[cIt1].getY(),
					mesh.getCoordinates()[cIt1].getZ());

			Line target = new Line(tempA, tempB, GeometryOperators3D.TOLERANCE);
			Vector3D interSectionPoint = source.intersection(target);

			if (interSectionPoint != null) {
				if (contains3D(tempA, tempB, interSectionPoint)) {
					intersectionPoints.add(interSectionPoint);
				}
			}

		}

		return intersectionPoints;
	}

}
