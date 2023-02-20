# Load the Python Standard and DesignScript Libraries
import os
import sys
import ctypes
import json
import xml.etree.ElementTree as ET
from xml.dom.minidom import parse, parseString

############ XML Import and Parsing ######################
warn_str = '(XML) File or File Path does not exist.'
res = ''

def xmlParsing2Uri(path):
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

def xml2GeometryString(path):
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

def xml2GeoInDynamo(path):
    with open(path, 'r', encoding='utf-8') as file:
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
    
def flatten(inp_lst):
# Core: 
#		1. traverse the input list 
#		2. concatenate each two adjacent elements 
#		3. store the concatenation into the output list
#		4. remove the non-list element out of the input list
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
    
    res_dict = {}
    for res in out_lst:
        res_dict[res] = res
    return res_dict
   
############ JSON Import and Parsing #####################
def GroundTruthImport(dir):
    warn_str = '<GroundTruth> error occurs while json file was opening.'
    data = {}
    try:
        with open(dir, 'r', encoding='utf-8') as fp:
            data = json.load(fp)
    except:
        ctypes.windll.user32.MessageBoxW(0, dir, 'Input Exception',1)
    return data
    
############ Accuracy Calculation #####################
def JSON_export(json_file_path, json_file_name, json_file_index, json_file_contents):
    
    tmp_json_file_name = json_file_index + json_file_name
    tmp_json_file_path = json_file_path + tmp_json_file_name
    try:
        with open(tmp_json_file_path, "w") as f:
            json.dump(json_file_contents, f, sort_keys=True, indent=4)
    except FileExistsError:
        print(f"File '{json_file_path}' already exists.")
        
######################################################
# MAC path
query_result_file_path = "/Users/yhe/Developer/Repo/ExtendingJena/src/main/resources/rdf/query_result/"
json_ground_truth_file_path = "/Users/yhe/Developer/Repo/ExtendingJena/src/main/resources/JSON/ground_truth/"
json_accuracy_file_path = "/Users/yhe/Developer/Repo/ExtendingJena/src/main/resources/JSON/accuracy/"

res_files = []
groundTruth_files = []

for res_filename in os.listdir(query_result_file_path):
    if res_filename.endswith('.xml'):
        res_files.append(os.path.join(query_result_file_path, res_filename))
        
for gt_filename in os.listdir(json_ground_truth_file_path):
    if gt_filename.endswith('.json'):
        groundTruth_files.append(os.path.join(json_ground_truth_file_path, gt_filename))

accuracy_dict={}
for res_file, gt_file in zip(res_files, groundTruth_files):
    # XML file import and parsing
    query_res = xml2GeoInDynamo(res_file)
    res_dict = flatten(query_res)
    
    # GroundTruth import and parsing
    ground_truth_dict = GroundTruthImport(gt_file)
    res = ""
    last_slash = res_file.rfind("/")
    last_pt = res_file.rfind(".")
    file_name = res_file[last_slash+1:last_pt]
    if len(res_dict) > 0 and len(ground_truth_dict) > 0:
        count = 0
        for res_key in res_dict.keys():
            if res_key in ground_truth_dict.keys():
                count += 1
        ac = count/len(ground_truth_dict)
        accuracy_dict[file_name]=ac
    elif len(res_dict) == 0 and len(ground_truth_dict) == 0:
        accuracy_dict[file_name]=1
    else:
        accuracy_dict[file_name]="NaN"
    JSON_export(json_accuracy_file_path, file_name+".json", "", accuracy_dict)
        