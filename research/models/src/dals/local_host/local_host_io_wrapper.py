#-*- coding: utf-8 -*-
""" 
    tested :
        python2.7
        jython2.5
            читает и пишет правильно, но тип текущей локали определят
                не правильно, поэтому выводит на консоль неправильно
    connect : import usaio.io_wrapper

    Класс для работы с файлами на компьютере
"""


import codecs
import sys
import traceback

class LocalHostDALException(Exception):
    def __init__(self, value):
        self.value = value
        
    def __str__(self):
        return str(self.value)

class File():
    _fhandle = None
    
    """ Инициализация дискриптора. Передача по ссылке! """
    def __init__(self, fhandle):
        self._fhandle = fhandle
        # Возможно лучше открыть файл в конструкторе, но может возникнуть исключение!
        
    def __del__(self):
        self._fhandle.close()  # TODO(zaqwes): иногда выдает ошибк (jython)
    
    def to_list(self):
        try:
            
            list_lines = self._fhandle.readlines() 
             
            # Удалить переводы строк
            result = []
            for at in list_lines:
                result.append(at.replace('\r', '').replace('\n', ''))
            return result
        
        except UnicodeDecodeError as e:
            raise LocalHostDALException(e)
            
        except IOError as e:
            raise LocalHostDALException(e)
        
        except ValueError as e:
            raise LocalHostDALException(e)

    def write(self, str):
        try:
            self._fhandle.write(str)
            
        except UnicodeEncodeError, e:
            raise LocalHostDALException(e)
        
        except IOError, e:
            raise LocalHostDALException(e)
        
""" Выдает файловые объекты """
def FabricOpen(settings):
    fname = settings['name']
    how_open = settings['how_open']
    xcoding = settings['coding']
    f = None
    try:
        # создаем реальный файловый объект и передаем его обертке
        f = codecs.open(fname, how_open, encoding=xcoding)
        wrapper = File(f)
        return wrapper
    
    # Скорее всего путь не тот
    except IOError as e:
        raise LocalHostDALException(e)
    
    # Файл Закрывается!!
    #finally:  # Мы передает ссылку, поэтому закрыть нельзя!
    #    if f:
    #        f.close()
   
def get_utf8_template():
    return {'name':  'fname', 'how_open': 'r', 'coding': 'utf_8'}

def file2list_int(sets):
    readed_list = file2list(sets)
    result = []
    try:
        for at in readed_list:
            result.append(int(at))
        return result
        
    except ValueError as e:
        raise LocalHostDALException(e)
        
def file2list(sets):
    try:
        file = FabricOpen(sets)
        list_lines = None
        if file != None:
            list_lines = file.to_list()
            return list_lines, (0, '')
        else:
            return None, (1, 'Error: cant open file')
    
    except LocalHostDALException as e:
        return None, (1, str(e))
    
    except:
        formatted_lines = traceback.format_exc().splitlines()
        err_msg = '\n'.join(formatted_lines)
        #traceback.print_exc(file=sys.s)
        raise LocalHostDALException("Unexpected error:"+err_msg)

def list2file(sets, lst):
    file = FabricOpen(sets)
    if file != None and lst != None:
        file.write('\r\n'.join(lst))
    else :
        print "list2file error occure"

def app_str(sets, string):
    file = FabricOpen(sets)
    if file != None:
        file.write(string+'\r\n')
    else :
        print "app_str error occure"
        
# HIGHER ABSTRACTION
def write_result_file(result_list, fname):
    sets = get_utf8_template()
    sets['how_open'] = 'w'
    sets['name'] = fname
    list2file(sets, result_list)
    
def read_utf_file_to_list_lines(fname):
    sets = get_utf8_template()
    sets['name'] = fname
    readed_list, err = file2list(sets) 
    if err[0]:
        return None, (1, err[1]) 
    else:
        return readed_list, (0, '')
    
    
# RUNNER
if __name__=='__main__':
    import unittest
    
    class TestSequenceFunctions(unittest.TestCase):
        def setUp(self):
            self.seq = range(10)
    
        def test_file_exist(self):
            fname = '__init__.py'
            result, err = read_utf_file_to_list_lines(fname)
            self.assertEqual(err[0], 0, 'File must exist')
            
        def test_file_no_xist(self):
            fname = 'fake.txt'
            result, err = read_utf_file_to_list_lines(fname)
            self.assertEqual(err[0], 1, 'File no must exist')

    unittest.main()
    print 'Done'
