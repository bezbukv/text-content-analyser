# coding: utf-8

import sys
sys.path.append('D:/home/lugansky-igor/jarvis')
sys.path.append('D:/home/lugansky-igor/github-release')

from collections import deque

import emitter_list_files as flist_emitter
import uasio.os_io.io_wrapper as iow

#import chardetect as detector  # NO way

def init():
    # Точки входа
    roots = ['app-server-code/data_processor']#, 'snmp-agents']#,
        #'D:\\home\\lugansky-igor\\github-release\\bale-ccpp']
        
    listOfExtension = ['cpp', 'h', 'cc']
    
    ignoreLists = {}
    ignoreLists['Extentions'] = ['']
    ignoreLists['Dirs'] = ['.\\bin', '.\\.git', '.\\sbox', 'Analysers']
    return roots, listOfExtension, ignoreLists, ignoreLists, ignoreLists
    
    
def _list_is_utf8(list_lines):
    for it in list_lines:
        try:
            # Попытка перевода в обычную строку
            #str(it)
            it.encode("CP1251")
            it.encode("UTF-8")
            #unicode(it, 'cp1251')
            #unicode(it, 'utf_8')
        except UnicodeEncodeError, e:
            print 'UnicodeEncodingError:', e
            return True
        except TypeError, e:
            print 'TypeError:', e
            return True
            
    return False

def _add_coding_head_cc_h(list_lines):
    """ Просто меняет прочитанный список. Поэтому
    фактически идет перезапись файла в любом случае.
    """
    coding_head = "// encoding : utf8"

    # Есть ли маркер
    if list_lines[0] != coding_head:
        queue_readed = deque(list_lines)
        queue_readed.appendleft(coding_head)
        list_lines = list(queue_readed)
    return list_lines
    
def _utf8_list_to_file(list_lines, fname):
    sets = iow.get_utf8_template()
    sets['coding'] = 'utf_8'
    sets['name'] = fname
    sets['howOpen'] = 'w'
    iow.list2file(sets, list_lines)
    
def _utf8_file_to_list(fname):
    sets = iow.get_utf8_template()
    sets['coding'] = 'utf_8'
    sets['name'] = fname
    return iow.file2list(sets)
    
def _read_cp1251_file(fname):
    """ 
    Returns: 
        Возвращает список и ответ на вопрос - угадали ли мы кодировку?
    """
    this_is_it = True
    sets = iow.get_utf8_template()
    sets['coding'] = 'cp1251'
    sets['name'] = fname
    
    # Если не та кодировка - возвращает пустой список
    readed_list = iow.file2list(sets)
    #if not readed_list:
     #   this_is_it = False
        
    # Не все находит! Нужно еще построчно
    if _list_is_utf8(readed_list):
        this_is_it = False
        
    return readed_list, this_is_it
    
def _cp1251_list_to_file(list_lines, fname):
    sets = iow.get_utf8_template()
    sets['name'] = fname
    sets['coding'] = 'cp1251'
    sets['howOpen'] = 'w'
    iow.list2file(sets, list_lines)
            
def list_files_form_cp1251_to_utf8(list_files):
    for fname in file_list:
        if fname:
            print fname
            #print detector.description_of(fname)
            
            readed_list, this_is_it = _read_cp1251_file(fname)
            print "  ",this_is_it#, ' >>> ', fname
            #if not readed_list:
             #   print readed_list
            '''if not this_is_it:
                # Добавляем заголовок если нет его
                # Сперва нужно правильно прочитать!
                readed_list_u = _utf8_file_to_list(fname)
                readed_list_u = _add_coding_head_cc_h(readed_list_u)
                _utf8_list_to_file(readed_list_u, fname+'.java')
                # Не преобразуем и идем дальше по списку файлов
                continue
            print '  ', 'Need conv.'
            # Если прочитался то что-то да будет
            #if readed_list:
            print '    ', 'Need conv. ReaL'
            _cp1251_list_to_file(readed_list, fname+'.bak')
            
            # Danger! Перезапись
            _utf8_list_to_file(readed_list, fname+'.cs')
            print '\t >> Converted', fname
            
        #else:
            # Добавляем заголовок если нет его
            # Сперва нужно правильно прочитать!
            #readed_list_u = _utf8_file_to_list(fname)
            #readed_list_u = _add_coding_head_cc_h(readed_list_u)
            #_utf8_list_to_file(readed_list_u, fname+'.java')
            '''
    
if __name__ == "__main__":
    file_list = flist_emitter.get_list_files(init)
    list_files_form_cp1251_to_utf8(file_list)
    
    
