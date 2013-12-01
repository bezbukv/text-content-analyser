# coding: utf-8
"""

Notes:
    - Вызов нераелизованного метода интерфейса возвращает None
    - Вызов нереализованного и необъявленного метода вызывает исключение
"""
# Py Std Pkgs
import sys

# Java Std Pkgs
#from java.lang import Math
#from java.lang import System as javasystem

# App
from test_call_jy import IJythonTestInterface
from test_call_jy import JavaImpl
# 
from test_call_jy import Beach
    
# TESTs
def test_jython_impl():
    """ Usige Python impl. Java interface """
    
    class JyImpl(IJythonTestInterface):
        """ Implementation Java interface """
        def getIdx(self):
            return 42
        
        def getList(self):
            return ["Hello"]
    
    python_impl = JyImpl()
    # Getters
    print python_impl.getIdx()
    print python_impl.getList()
    
def test_java_impl():
    """ Usige java implementation interface """
    # Getters
    java_impl = JavaImpl()
    print java_impl.getIdx()
    
    # Возвращает не Python List! А Java List
    list_getted = java_impl.getList()
    print type(list_getted[0])
    
    # Error: У Java-списка нет метода append()
    #list_getted.append("asdf")
    # Перепаковка
    local_list = []
    local_list.append(list_getted[0])
    
    # Mutators
    local_list.append(9)  # Тоже передалось!! Но так лучше не делать
    print local_list
    java_impl.setList(local_list)
    
def test_polymorphic_usige():
    def ImplFabric():  # Это не фабрика, а генератор
        return JyImpl()
    
if __name__=='__main__':
    test_jython_impl()
    test_java_impl()
   

    