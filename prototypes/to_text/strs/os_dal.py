# coding: utf-8
# Sys
import json
import re

# Other
import os_io.io_wrapper as iow

def _is_content_nums(string):
    pattern = '^\d*?$'
    result = re.finditer(pattern, string)
    for match in result :
        s = match.group()
        return True
    return False

def read_file_and_purge_content(fname):
    """ Пока разбор только файлов субтитров
    
    Returns:
        Содержимое файла в чистом виде одной строкой
        
    Known Issues:
        Сокращения принимаются за коней предложения
    """
    settings = {
        'name':  fname, 
        'howOpen': 'r', 
        'coding': 'cp866' }
        
    readed_lst = iow.file2list(settings)
    purged_lst = list()
    for at in readed_lst:
        at_copy = at.replace('\r','')
        at_copy = at_copy.replace('\n','')
        if at_copy:
            if not '-->' in at_copy:
                if not _is_content_nums(at_copy):
                    at_copy = at_copy.replace('<i>','')
                    at_copy = at_copy.replace('</i>','')
                    purged_lst.append(at_copy)
    
    # Теперь нужно разить на предложения
    one_line = ' '.join(purged_lst)
    return one_line

def get_url_names():
    """ Получение ссылок на контент 
    
    Returns:
        Здесь - список файлов формата *.str
    """
    files = ['srts/Iron Man02x26.srt', 'srts/Iron1and8.srt']
    return files

def save_process_result(process_result):
    # Запаковали. Можно сохранятся
    to_file = [json.dumps(process_result, sort_keys=True, indent=2)]
    
    settings = {
        'name':  'extracted_words.json', 
        'howOpen': 'w', 
        'coding': 'cp866' }
        
    iow.list2file(settings, to_file)