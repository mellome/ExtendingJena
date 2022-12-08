# Load the Python Standard and DesignScript Libraries
import sys
import clr
clr.AddReference('ProtoGeometry')
from Autodesk.DesignScript.Geometry import *

# Common Geometry Types
POINT = "POINT";
MULTIPOINT = "MULTIPOINT";
LINESTRING = "LINESTRING";
MULTILINESTRING = "MULTILINESTRING";
POLYGON = "POLYGON";
MULTIPOLYGON = "MULTIPOLYGON";
GEOMETRYCOLLECTION = "GEOMETRYCOLLECTION";

# The inputs to this node will be stored as a list in the IN variables.
NUM_MULTIPOLYGON = 10
NUM_POLYGON = 10
NUM_LINESTRING = 10
NUM_POINT = 10

# Input 
NUM_MULTIPOLYGON = IN[0]
NUM_MULTIPOLYGON = 50
#num = IN[1]

# output container
geo_type_lst = []
geo_lst = []

# Point

# LineString
cl = Arc.ByThreePoints( Point.ByCoordinates(20, 20, 0),
                        Point.ByCoordinates(25, 25, 0),
                        Point.ByCoordinates(40, 10, 0))
geo_lst.append(cl)
# Polygon

# Multipolygon
i=1
for i in range(-NUM_MULTIPOLYGON, NUM_MULTIPOLYGON, 4):
    p1 = Point.ByCoordinates(i, i, 0)
    p2 = Point.ByCoordinates(3+i, 3+i, 3)
    #OUT = Cuboid.ByCorners(p1, p2)
    geo_lst.append(Cuboid.ByCorners(p1, p2))

i=1
for i in range(-NUM_MULTIPOLYGON, NUM_MULTIPOLYGON, 4):
    j = i + 10
    p1 = Point.ByCoordinates(i, j, 0)
    p2 = Point.ByCoordinates(3+i, 3+j, 3)
    #OUT = Cuboid.ByCorners(p1, p2)
    geo_lst.append(Cuboid.ByCorners(p1, p2))



# Assign your output to the OUT variable.
OUT = geo_lst, []

