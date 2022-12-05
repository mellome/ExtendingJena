from xml.dom.minidom import parse, parseString

dir_mac = "/Users/yhe/Developer/Repo/ExtendingJena/src/main/resources/rdf/cubeSphere.rdf"

def rdfParsing2GeoData(path=dir_mac):
    """ it returns the json file in structure like { "POLYGON1" : [...], 
                                                     "POLYGON2" : [...], 
                                                     "MULTIPOLYGON1" : [...], ...
                                                    }
    """
    geo_dict = {}
    dom = parse(path)
    data = dom.documentElement
    rdf_desc_lst = data.getElementsByTagName("rdf:Description")
    # geo_in_wkt = data.getElementsByTagName("geo:asWKT")

    for i in range(1, len(rdf_desc_lst)): # first description element (root element) should be removed.
        descr_el = rdf_desc_lst[i]
        geo_name_num = descr_el.attributes._attrs['rdf:about'].nodeValue.split('#')[1]
        geo_wkt_data = descr_el.childNodes[1].firstChild.data
        geo_algebra_lst = []

        if ( geo_name_num.find('POINT') != -1 or
            geo_name_num.find('Point') != -1 ):
            
            wkt_data_lst = geo_wkt_data.split("(")[1] # remove title 'POINT'
            vert_str_lst = wkt_data_lst
            vert_str_lst = vert_str_lst.replace( ")", "" ) # remove character ')'

            pt_algebra_lst = []
            pt_str_lst = vert_str_lst.split(" ")

            pt_algebra_lst = [ float(pt_str) for pt_str in pt_str_lst ]
            geo_algebra_lst.append(pt_algebra_lst)
            
        if ( geo_name_num.find('LINESTRING') != -1 or
            geo_name_num.find('Linestring') != -1 or
            geo_name_num.find('LINE') != -1 or
            geo_name_num.find('Line') != -1 ):

            wkt_data_lst = geo_wkt_data.split("((")[1] # remove title 'LINESTRING'
            vert_str_lst = wkt_data_lst
            vert_str_lst = vert_str_lst.replace( ")", "" ) # remove character ')'
            vert_str_lst = vert_str_lst.split(", ")

            line_algebra_lst = []
            for i in range(0, len(vert_str_lst)):

                pt_str_lst = vert_str_lst[i].split(" ")
                line_algebra_lst.append([ float(pt_str) for pt_str in pt_str_lst ])
            geo_algebra_lst.append(line_algebra_lst)

        if ( geo_name_num.find('POLYGON') != -1 or 
            geo_name_num.find('Polygon') != -1 ):

            wkt_data_lst = geo_wkt_data.split("((")[1] # remove title 'POLYGON Z'
            vert_str_lst = wkt_data_lst
            vert_str_lst = vert_str_lst.replace( ")", "" ) # remove character ')'
            vert_str_lst = vert_str_lst.replace( "(", "" ) # remove character '('
            vert_str_lst = vert_str_lst.split(", ")

            face_algebra_lst = []
            for i in range(0, len(vert_str_lst)-1): # last point not included

                pt_str_lst = vert_str_lst[i].split(" ")
                face_algebra_lst.append([ float(pt_str) for pt_str in pt_str_lst ])
            geo_algebra_lst.append(face_algebra_lst)

        if ( geo_name_num.find('MULTIPOLYGON') != -1 or
            geo_name_num.find('Multipolygon') != -1  or
            geo_name_num.find('CUBE') != -1 or
            geo_name_num.find('Cube') != -1 or
            geo_name_num.find('SPHERE') != -1 or
            geo_name_num.find('Sphere') != -1 ):

            wkt_data_lst = geo_wkt_data.split("(((")[1] # remove title 'MULTIPOLYGON Z'
            face_str_lst = wkt_data_lst.split("), ")

            face_algebra_lst = []
            for face_str in face_str_lst:
                vert_str_lst = face_str.replace( ")", "" ) # remove character ')'
                vert_str_lst = vert_str_lst.replace( "(", "" ) # remove character '('
                vert_str_lst = vert_str_lst.split(", ")

                pt_algebra_lst = []
                for i in range(0, len(vert_str_lst)-1): # last point not included

                    pt_str_lst = vert_str_lst[i].split(" ")
                        
                    pt_algebra = [ float(pt_str) for pt_str in pt_str_lst ]
                    pt_algebra_lst.append(pt_algebra)

                face_algebra_lst.append(pt_algebra_lst)
            geo_algebra_lst = face_algebra_lst

        if geo_name_num not in geo_dict.keys():
            geo_dict[geo_name_num] = geo_algebra_lst
    return geo_dict

def xmlParsing2Uri(path=dir_mac):
    """ it returns the list in structure like [[uri1, uri2],...,[uri1, uri2]]"""
    dom = parse(path)
    data = dom.documentElement
    resList = data.getElementsByTagName('result')
    
    res_lst = []
    for res in resList: # handle the <result>...</result>
        ur_lst = []
        for bi in res.getElementsByTagName('binding'): # handle the <binding>...</binding>
            for ur in bi.getElementsByTagName('uri'):
                ur_lst.append(ur.childNodes[0].nodeValue)
        res_lst.append(ur_lst)
        ur_lst = []
    return res_lst


def xml2GeometryString(path=dir_mac):
    """ it returns the list in structure like [[Geomety1, Geomety2],...,[Geomety1, Geomety2]]"""
    dom = parse(path)
    data = dom.documentElement
    resList = data.getElementsByTagName('result')
    seperator = '#'

    res_lst = []
    for res in resList: # handle the <result>...</result>
        ur_lst = []
        for bi in res.getElementsByTagName('binding'): # handle the <binding>...</binding>
            for ur in bi.getElementsByTagName('uri'):
                uri_str = ur.childNodes[0].nodeValue
                geo_index = uri_str.find(seperator) 
                geo_str = uri_str[geo_index+1:]
                ur_lst.append(geo_str)
        res_lst.append(ur_lst)
        ur_lst = []
    return res_lst

def xml2GeoInDynamo(path=dir):
    with open(path, 'r') as file:
        xml_data = file.read()
        dom = parseString(xml_data)
        #dom = parse(xml_data)
        data = dom.documentElement
        resList = data.getElementsByTagName('result')
        seperator = '#'

        res_lst = []
        for res in resList: # handle the <result>...</result>
            ur_lst = []
            for bi in res.getElementsByTagName('binding'): # handle the <binding>...</binding>
                for ur in bi.getElementsByTagName('uri'):
                    uri_str = ur.childNodes[0].nodeValue
                    geo_index = uri_str.find(seperator) 
                    geo_str = uri_str[geo_index+1:]
                    ur_lst.append(geo_str)
            res_lst.append(ur_lst)
            ur_lst = []
    return res_lst

INDEX_EN = ['A','B','C','D'];
INDEX_NUM = ['1','2','3','4'];

def concatenation(geo_name:str, geo_number:int, index = INDEX_EN):
	geo_concat = [geo_name + index[i] for i in geo_number]
	return geo_concat

def flatten(inp_lst):
    out_lst = []
    while True:
        if inp_lst == []:
            break
        for ind, val in enumerate(inp_lst):
            if type(val) == list:
                inp_lst = val + inp_lst[ind+1:]
                break
            else:
                out_lst.append(val)
                inp_lst.pop(ind)
                break
    return out_lst

def inList(res_lst, geo):
    flat_lst = flatten(res_lst)
    if geo in flat_lst:
        return True
    return False


if __name__ == "__main__":
    rdfParsing2GeoData()