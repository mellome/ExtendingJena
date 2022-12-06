# Load the Python Standard and DesignScript Libraries
import sys
import ctypes
import json


def json_writer(dir, json_file):
    warn_str = "cannot find path or json file doesn't exist."
    res = True
    try: 
        with open(dir, 'w') as fp:
            json.dump(json_file, fp, sort_keys=True, indent=4)
    except:
        res = False
        ctypes.windll.user32.MessageBoxW(0, warn_str, 'Input Exception',1)

    return res


def json_loader(dir):
    data = {}
    warn_str = 'File or File Path does not exist.'

    try:
        with open(dir, 'r') as fp:
            data = json.load(fp)
    except:
        ctypes.windll.user32.MessageBoxW(0, warn_str, 'Input Exception',1)
    return data

if __name__ == "__main__":
    pass
