# coding : utf-8
'''
Created on 25.03.2013

@author: кей
'''

import java.util.List as List
import java.util.ArrayList as ArrayList

class JyList(object):  # NO Iterable - это приговор!
    _python_list = []
    def add(self, value):
        self._python_list.append(value)

# Будут котверторы, а не обертки

def java_lst_to_python_lst(java_list):
    """ 
    Если не Jython то можно просто передать со входа на выход
    
    """
    result = []
    for at in java_list:
        result.append(at)
    return result

if __name__=='__main__':
    readed_list = ArrayList()
    
    # Добавление в список
    readed_list.add(u'Test string')
    
    for at in readed_list:
        print type(at), at
        
    # Перепаковка
    python_list = java_lst_to_python_lst(readed_list)
    for at in python_list:
        print type(at), at
