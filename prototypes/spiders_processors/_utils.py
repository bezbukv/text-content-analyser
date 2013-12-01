# coding: utf-8
'''
Created on 11.04.2013

@author: кей
'''
# Sys
import re
import json

# App
from app_utils import remove_forward_and_back_spaces
from app_utils import remove_fandb_spaces_in_tuple

def _parse_target_params(str_params):
    rpt = []
    if str_params.count('[') != str_params.count(']'):
        return None, 1, "\tError: [Count '[' != count ']']"

    if str_params.count(':') != str_params.count('[') or \
        str_params.count(':') != str_params.count(']'):
        return None, 1, "\tError: [Format param - [anything : something]]"
    
    params = str_params.replace('[', '')
    params = params.split(']')
    params_map = {}
    for at in params:
        if at:
            pair = remove_forward_and_back_spaces(at)
            key, value = remove_fandb_spaces_in_tuple(tuple(pair.split(':')))
            # Запрещаем второе значени, соотв. ключу
            if key not in params_map:
                params_map[key] = value
            else:
                return None, 1, "\tError: only one params key permitted"

    params_json = json.dumps(params_map)
    return params_json, 0, None

    
def is_node(line):
    line = remove_forward_and_back_spaces(line)
    if line[0] == '[' and line[-1] == ']':
        return True
    else:
        return False
    
def get_node_name(src_node_name): 
    return remove_forward_and_back_spaces(
            src_node_name.replace('[', '').replace(']', ''))
    


            
            
            
            
            
