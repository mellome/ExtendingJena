import json
from xml.dom.minidom import parse, parseString

dir_mac = "/Users/yhe/Developer/Repo/ExtendingJena/src/main/resources/rdf/query_result.xml"
dir_win = 'C:\\Users\\nobita_yhe\\Workspace\\ExtendingJena\\src\\main\\resources\\rdf\\query_result.xml'

def xmlParsing2Uri(path=dir_win):
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


def xml2GeometryString(path=dir_win):
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

def run():
    res_xml_dir = "C:\\Users\\yhe\\Documents\\Developer\\Repo\\ExtendingJena\\src\\main\\resources\\rdf\\query_result.xml"
    res_json_dir = "C:\\Users\\yhe\\Documents\\Developer\\Repo\\ExtendingJena\\src\\main\\resources\\JSON\\query_result.json"

    res = xml2GeoInDynamo(res_xml_dir)
    res_lst = flatten(res)

    res_dict = {}
    for res in res_lst:
        res_dict[res] = res
    
    try:
        with open(res_json_dir, 'w') as fp:
            json.dump(res_dict, fp, sort_keys=True, indent=4)
    except Exception as err:
        print(f"Unexpected {err=}, {type(err)=}")
        raise

if __name__ == "__main__":
    #print(xml2GeometryString())
    # lst = [["Point1","Polygon1"],"Triangle2", ["Line1",["Line2"]]]
    # geo = "Point1"
    # print(inList(lst,geo))
    run()