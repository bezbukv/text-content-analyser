# coding: utf-8
'''
Created on 16.03.2013

@author: кей
'''



_kContentMarker = '@CODE:'

def extract_interface_name(line):
    begin = line.find(' ')+1
    return line[begin:]

def process_code(mkt, call_map):
    result = []
    for at in mkt:
        if _kContentMarker in at:
            item = at.replace(_kContentMarker,'')
            key = item[:2]
            item = call_map[key](item)
            print item 
            result.append(item)
        else:
            result.append(at)
            
    return result 
