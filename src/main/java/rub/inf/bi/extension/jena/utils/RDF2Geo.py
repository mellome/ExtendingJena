import sys
import random
import JSONWriterLoader

# Common Geometry Types
POINT = "POINT";
MULTIPOINT = "MULTIPOINT";
LINESTRING = "LINESTRING";
MULTILINESTRING = "MULTILINESTRING";
POLYGON = "POLYGON";
POLYGON_ROTATION = "POLYGON_ROTATION"
MULTIPOLYGON = "MULTIPOLYGON"
MULTIPOLYGON_ROTATION = "MULTIPOLYGON_ROTATION"
SPHERE = "MULTIPOLYGON_SPHERE"
GEOMETRYCOLLECTION = "GEOMETRYCOLLECTION";

# The inputs to this node will be stored as a list in the IN variables.
NUM_MULTIPOLYGON = 10
NUM_POLYGON = 10
NUM_LINESTRING = 10
NUM_POINT = 10

# Input 
dir = "C:\\Users\\yhe\\Documents\\Developer\\Repo\\ExtendingJena\\src\\main\\resources\\JSON\\xxl3DScene.json"
json_file = JSONWriterLoader.json_loader(dir)

# output container
geo_lst = []

geo_pt_lst = []
geo_li_lst = []
geo_pol_lst = []
geo_mPol_lst = []
geo_sph_lst = []

geo_combi_lst = []

for geo_name, geo_data in json_file.items():
    
    if ( geo_name.find('POINT') != -1 or
        geo_name.find('Point') != -1 ):

        pt_data = geo_data
        pt_x = 0.0
        pt_y = 0.0
        pt_z = 0.0
        if (len(pt_data) == 3): # (x, y, z)
            pt_x = pt_data[0]
            pt_y = pt_data[1]
            pt_z = pt_data[2]
            
        if (len(pt_data) == 2): # (x, y)
            pt_x = pt_data[0]
            pt_y = pt_data[1]
            pt_z = 0.0
            
        pt = Point.ByCoordinates(pt_x, pt_y, pt_z)

        geo_data = pt
        geo_name = geo_name.upper()
        geo_type = 'POINT'
        geometry = pt

        geo_pt_obj = (geo_data, geo_name, geo_type, geometry)
        geo_pt_lst.append(geo_pt_obj)
        continue

    if ( geo_name.find('LINESTRING') != -1 or
        geo_name.find('Linestring') != -1 or
        geo_name.find('LINE') != -1 or
        geo_name.find('Line') != -1 ):

        pt_lst = []
        for pt_data in geo_data[0]:
            pt = Point.ByCoordinates(pt_data[0], pt_data[1], pt_data[2])
            pt_lst.append(pt)

        geo_data = pt_lst
        geo_name = geo_name.upper()
        geo_type = 'LINESTRING'
        geometry = PolyCurve.ByPoints(pt_lst, False)

        geo_li_obj = (geo_data, geo_name, geo_type, geometry)
        geo_li_lst.append(geo_li_obj)
        continue

    if ( geo_name.find('SPHERE') != -1 or
        geo_name.find('Sphere') != -1 ):

        face_lst = []
        for face_data in geo_data:

            pt_lst = []
            for vert_data in face_data:
                pt = Point.ByCoordinates(vert_data[0], vert_data[1], vert_data[2])
                pt_lst.append(pt)
            face = Surface.ByPerimeterPoints(pt_lst)
            face_lst.append(face)

        geo_data = face_lst
        geo_name = geo_name.upper()
        geo_type = 'MULTIPOLYGON_SPHERE'
        geometry = face_lst

        geo_sph_obj = (geo_data, geo_name, geo_type, geometry)
        geo_sph_lst.append(geo_sph_obj)
        continue

    if ( geo_name.find('MULTIPOLYGON') != -1 or
        geo_name.find('Multipolygon') != -1  or
        geo_name.find('CUBE') != -1 or
        geo_name.find('Cube') != -1 ):

        face_lst = []
        for face_data in geo_data:

            pt_lst = []
            for vert_data in face_data:
                pt = Point.ByCoordinates(vert_data[0], vert_data[1], vert_data[2])
                pt_lst.append(pt)
                # print()
            face = Surface.ByPerimeterPoints(pt_lst)
            # face = []
            face_lst.append(face)

        geo_data = face_lst
        geo_name = geo_name.upper()
        geo_type = 'MULTIPOLYGON'
        geometry = face_lst

        geo_mPol_obj = (geo_data, geo_name, geo_type, geometry)
        geo_mPol_lst.append(geo_mPol_obj)
        continue

    if ( geo_name.find('POLYGON') != -1 or 
        geo_name.find('Polygon') != -1 ):
        
        pt_lst = []
        for vert_data in geo_data[0]:
            pt = Point.ByCoordinates(vert_data[0], vert_data[1], vert_data[2])
            pt_lst.append(pt)
            # print()
        face = Surface.ByPerimeterPoints(pt_lst)
        face = []

        geo_data = pt_lst
        geo_name = geo_name.upper()
        geo_type = 'POLYGON'
        geometry = Surface.ByPerimeterPoints(pt_lst)
        # geometry = geo_data

        geo_pol_obj = (geo_data, geo_name, geo_type, geometry)
        geo_pol_lst.append(geo_pol_obj)
        continue

    
geo_combi_lst = geo_pt_lst + geo_li_lst + geo_pol_lst + geo_mPol_lst + geo_sph_lst
print(geo_combi_lst)
