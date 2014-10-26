# coding: utf-8
'''
Created on 03.04.2013

@author: кей
'''
import re

def is_key_conetnet_num(key):
    m = re.match(r"(\w+) (\w+)", key)
    m.group(0)       # The entire match
    return False

def is_key_enabled(key_value):
    if key_value == 'and' or \
            is_content_nums(key_value) or \
            key_value == 'a' or \
            key_value == 'the' or \
            key_value == 'that' or \
            key_value == 'than' or \
            key_value == 'you' or \
            '(' in key_value or \
            ')' in key_value or \
            '(' in key_value or \
            len(key_value) < 3 or \
            "'" in key_value or \
            False:
        return False
    return True

def is_content_nums(string):
    if re.findall(r'\d', string):
        return True
    else:
        return False

if __name__=='__main__':
    print is_content_nums('n1')
    print is_content_nums('n')
