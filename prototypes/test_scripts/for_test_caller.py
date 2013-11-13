# coding : utf-8
""" 

Thinks:
  Похоже лучше передавать простейшие типы (строки, числа)
"""
import sys
sys.path.append('./scripts/pkgs')
sys.path.append('c:/jython2.7b1/Lib')
#print sys.executable  # Потытка различить интерпритаторы

# Availabel modules
import codecs
import json  # No in python2.5
import re

# No args
def hello():
    print "Hello world!"
 
def goodbye():
    print "Goodbye cruel world!"

# Full connect
def string_string(seconds):
    """ Делает то то"""
    return seconds
    
# Python runtime error
def err_in_script():
    asdf = 0;
