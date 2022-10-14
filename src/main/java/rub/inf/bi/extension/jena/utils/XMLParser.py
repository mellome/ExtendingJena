from xml.dom.minidom import parse

dir = "/Users/yhe/Developer/Repo/ExtendingJena/src/main/resources/rdf/query_result.xml"


def xmlParsing2Uri(path=dir):
    """ it retures the list in structure like [[uri1, uri2],...,[uri1, uri2]]"""
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


def xml2GeometryString(path=dir):
    """ it retures the list in structure like [[Geomety1, Geomety2],...,[Geomety1, Geomety2]]"""
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


if __name__ == "__main__":
    print(xml2GeometryString())