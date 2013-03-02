#-*- coding: utf-8 -*-
""" Анализирует файлы с субтитрами и ищет частоту употребления слов.
    Каждому слову сопоставлет приложение или нескольно.
    
    Формат хранения данных пусть пока будет json
    
    TODO: Оптимизировать хранение контекста
    TODO: Ужесточить фильтрацию ключей
"""
import sys
import re
import json

# App
import dal.os_dal as dal   # как-то не очень выглядит

_kInsertConst = '@@@@'
_kCountContentItems = 3

def insert(original, new, pos):
  '''Inserts new inside original at pos.'''
  return original[:pos] + new + original[pos:] 
  
def _is_key_enabled(key_value):
    if key_value == 'and' or \
            _is_content_nums(key_value) or \
            key_value == 'a' or \
            False:
        return False
    return True

def _is_content_nums(string):
    pattern = '^\d*?$'
    result = re.finditer(pattern, string)
    for match in result :
        s = match.group()
        return True
    return False
    
def _purge_one_sentence(one_sentence):
    if one_sentence[0] == ' ':
        one_sentence = one_sentence[1:]
    copy_one_sent = one_sentence.lower()
    copy_one_sent = copy_one_sent.replace('.','')
    copy_one_sent = copy_one_sent.replace('?','')
    copy_one_sent = copy_one_sent.replace('!','')
    copy_one_sent = copy_one_sent.replace(',','')
    copy_one_sent = copy_one_sent.replace('"','')
    copy_one_sent = copy_one_sent.replace('-','')
    copy_one_sent = copy_one_sent.replace(':','')
    copy_one_sent = copy_one_sent.replace(';','')
    copy_one_sent = copy_one_sent.replace('[','')
    copy_one_sent = copy_one_sent.replace(']','')
    copy_one_sent = copy_one_sent.replace('=','')
    #copy_one_sent = copy_one_sent.replace('♪','')
    copy_one_sent = copy_one_sent.replace('&','')
    copy_one_sent = copy_one_sent.replace('>','')
    copy_one_sent = copy_one_sent.replace('<','')
    copy_one_sent = copy_one_sent.replace('~','')
    copy_one_sent = copy_one_sent.replace('/','')
    result = copy_one_sent
    return result
    


def main():
    files = ['srts/Iron Man02x26.srt', 'srts/Iron1and8.srt']
    result = {'it':{'num':0, 'sents':[]}}
    for fname in files:
        content_in_one_line = dal.read_file_and_purge_content(fname)
        
        # Единица контента в одно предложение
        one_line_with_inserts = ''
        for at in one_line:
            one_line_with_inserts += at
            if at == '.' or at == '!' or at == '?' or at == ']':
                one_line_with_inserts += _kInsertConst
                
        # Чистый контент - набор предложений
        sentences_lst = one_line_with_inserts.split(_kInsertConst)
        
        # Обработка
        for one_sentence in sentences_lst:
            if one_sentence:
                pure_sentence = _purge_one_sentence(one_sentence)
                
                # Лексические единицы одного предложения
                set_words = pure_sentence.split(' ')
                for at in set_words:
                    if at != ' ':
                        if at:
                            if at in result:
                                if result[at]['num'] < _kCountContentItems+1:
                                    result[at]['sents'].append(one_sentence)
                                result[at]['num'] += 1
                            else:
                                if _is_key_enabled(at):
                                    result[at] = {'num':1, 'sents':[one_sentence]}
    
    to_file = [json.dumps(result, sort_keys=True, indent=2)]
    
    settings = {
        'name':  'extracted_words.json', 
        'howOpen': 'w', 
        'coding': 'cp866' }
        
    iow.list2file(settings, to_file)
    
if __name__ == '__main__':
    main()