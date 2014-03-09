# coding: utf-8

from crosscuttings.tools import get_app_cfg_by_path

from spiders_processors.custom_to_text_convertors import *
from spiders_processors.std_to_text_convertors.srt_to_text import *

_std_convertors_map = {'std_srt_to_text_line': std_srt_to_text_line}

g_convertors_map = {}

def Init():
    rpt = []
    path = 'App/Spider/to_text_convertors'
    list_names_processors = get_app_cfg_by_path(path)
    for at in list_names_processors:
        if at in _std_convertors_map:
            g_convertors_map[at] = _std_convertors_map[at]
        elif at in g_custom_convertors_map:
            g_convertors_map[at] = g_custom_convertors_map[at]
        else:
            rpt.append("Error: Name not registred - "+at)
    return rpt
    
def get_call_map():
    return g_convertors_map
