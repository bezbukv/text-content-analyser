# coding: utf-8
'''
Важно то, что будет сгенерировано несколько файлов
'''
# App
from _app_reuse import *

def _process_class(line):
    result = '' 
    result = line         
    return result

def _process_interface(line):
    result = '' 
    result = line  
    
    result = result.replace('I:interface', 'public interface /*class*/')  
    result += ' /* implement '+ result.split(' ')[-1] + ' */ {'    
    return result

def _process_method(line):
    result = '' 
    result = line  
    result = result.replace('M:', '') 
    result += ';//hided {}'      
    return result

def _split_to_files(mkt):
    result = []
    kJoiner = "@@@"
    mkt_line = kJoiner.join(mkt)
    for at in mkt_line.split('@File@'):
        if 'I:' in at or 'C:' in at:
            result.append(at.split(kJoiner))
            
    return result

def _get_entity_name(list_lines):
    header = list_lines[0]
    return header.split(' ')[-1]

def to_code(mkt, open_comment_idx, close_comment_idx):
    java_code = []
  
    # Preprocessing
    for at in mkt:
        item = at.replace('#', '//')
        if item.count('"""OPENED:') == 2:  # Однострочный длинный
            item = item.replace('"""OPENED:', '/**')
            item = item[:-3]+'*/'
        else:
            item = item.replace('"""OPENED:', '/**')
        item = item.replace('CLOSED:"""', '*/')
        
        # Обобщаем шаблоны, нужно для генарации отдельных файлов
        item = item.replace('I:', '@File@I:')
        item = item.replace('C:', '@File@C: ')
        #item = item.replace('@CODE:', '')
        
        java_code.append(item)
    
    # Разбиваем на юниты
    units = _split_to_files(java_code) 
    
    result = {}
    for unit in units:   
        # one IT
        call_map = {
            'I:': _process_interface,  
            'C:': _process_class,
            'M:': _process_method }
        java_code = process_code(unit, call_map)
        entity = _get_entity_name(unit)
        java_code = java_code
        
        # Удалить лишние пустные строки снизу
        i = len(java_code)-1
        tmp = java_code
        while True:
            if i == 0:
                break
            if java_code[i]:
                break
            i -= 1
            tmp = java_code[:i+1]
        
        java_code = tmp
        # Закрываем класс
        java_code.append('}  // '+entity)
        result[entity] = java_code

    return result