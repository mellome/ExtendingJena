import rdflib

# Load the RDF file
g = rdflib.Graph()
g.parse("path/to/rdf_file.rdf", format="xml")

# Define the namespace for the RDF vocabulary used in the file
ns = {
    "rdf": "http://www.w3.org/1999/02/22-rdf-syntax-ns#",
    "geo": "http://www.w3.org/2003/01/geo/wgs84_pos#",
}

# Query the RDF graph to extract the WKT strings
query = """
PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
PREFIX geo: <http://www.w3.org/2003/01/geo/wgs84_pos#>
SELECT ?wkt
WHERE {
    ?subject rdf:type geo:SpatialThing .
    ?subject geo:asWKT ?wkt .
}
"""

results = g.query(query, initNs=ns)

# Extract the WKT strings from the query results
wkt_strings = [result[0] for result in results]

# Print the WKT strings
for wkt in wkt_strings:
    print(wkt)