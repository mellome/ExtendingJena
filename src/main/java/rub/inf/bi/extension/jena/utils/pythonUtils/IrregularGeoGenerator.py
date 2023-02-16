import clr
clr.AddReference('ProtoGeometry')
from Autodesk.DesignScript.Geometry import Cylinder
from Autodesk.DesignScript.Geometry import Transform

radius = 5
height = 10
cylinder = Cylinder.ByRadiusHeight(radius, height)
translation = Transform.Translate(5,5,5)
result = cylinder.Transform(translation)