# Apache Jena

TODO

# Erstellen einer Ontologie 

TODO

# Durchführen von Anfragen mit SPARQL

TODO

# Implementation von Funktionen

TODO

# Inkludierte Erweiterung

Die folgende Liste der Funktionen sind im Rahmen dieses Projekts enthalten. 

## Geometry 3D Operatoren

| Function  | Parameter  | Return Value  | Description | Type | Tested? | Stable?  |
|---|---|---|---|---|---|---|
| LLIntersection3D | LineString, LineString | [xsd:boolean](http://www.w3.org/2001/XMLSchema#boolean)  | Berechnet die Schnittpunkte zwischen zwei WKT Linien objekten. | Geometry | Ja  | Ja  | 
| LLIntersection3DGeometry | LineString, LineString | [geo:MultiPoint](http://www.opengis.net/ont/geosparql#wktLiteral)  | Berechnet die Schnittpunkte zwischen zwei WKT-Linien objekten. | Geometry | Ja  | Ja  | 
| PLIntersection3D | Polygon, LineString | [xsd:boolean](http://www.w3.org/2001/XMLSchema#boolean)  | Interpretiert ein WKT-Polygon aus 3 Punkten als Plane und berechnet die Schnittpunkte zwischen Plane und einer WKT-Linie. | Geometry | Ja  | Ja  | 
| PLIntersection3DGeometry | Polygon, LineString | [geo:MultiPoint](http://www.opengis.net/ont/geosparql#wktLiteral)  | Interpretiert ein WKT-Polygon aus 3 Punkten als Plane und berechnet die Schnittpunkte zwischen Plane und einer WKT-Linie. | Geometry | Ja  | Ja  | 
| PPIntersection3D | Polygon, Polygon | [xsd:boolean](http://www.w3.org/2001/XMLSchema#boolean)  | Ermittelt ob eine Schnittpunkte-Linie zwischen zwei WKT Polygon-Z objekten existieren. | Geometry | Ja  | Ja  |
| PPIntersection3DGeometry | Polygon, Polygon | [geo:MultiPoint](http://www.opengis.net/ont/geosparql#wktLiteral) | Ermittelt die Punkte der Schnittpunkte-Linie zwischen zwei WKT Polygon-Z objekten. | Geometry | Nein  | Nein  |
| TTIntersection3D | MultiPolygon, MultiPolygon | [xsd:boolean](http://www.w3.org/2001/XMLSchema#boolean)  | Ermittelt ob eine Schnittpunkte-Linie zwischen zwei WKT MultiPolygon-Z objekten existieren. | Geometry | Ja  | Ja  |
| TTIntersection3DGeometry | MultiPolygon, MultiPolygon | [geo:MultiPoint](http://www.opengis.net/ont/geosparql#wktLiteral)  | Ermittelt die Punkte der Schnittpunkte-Linie zwischen zwei WKT MultiPolygon-Z objekten. | Geometry | Ja  | Ja  |

## Tunnelbau Operatoren

| Function  | Parameter  | Return Value  | Description | Type | Tested? | Stable?  |
|---|---|---|---|---|---|---|
| HasRadius | LineString, double | [xsd:boolean](http://www.w3.org/2001/XMLSchema#boolean)  | Überprüft ob eine Kurvenführung einen bestimmten Radius mindestens einhällt. | Geometry | Ja  | Ja  | 
| NAME | PARAMETER_A, PARAMETER_B | [xsd:boolean](http://www.w3.org/2001/XMLSchema#boolean)  | Bescshreibung. | TYPE | Ja/Nein  | Ja/Nein  | 
