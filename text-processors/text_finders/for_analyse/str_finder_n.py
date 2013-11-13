#!/usr/bin/python
#-*- coding: utf-8 -*-
import re
import os
'''
  file : import str_finder as sf
'''

# parse string
def findAndFill(xxl, srcStr, pattern ):
  result = re.finditer(pattern , srcStr)
  for match in result :
	# результаты поиска
    s = match.group()
	
# итоговая строка
    xxl.append(s)
  
  # число совпадений
  return len(xxl)


# Main
def Main( pattern, dirForFound ):
  # поис и среди исходных файлов
  for dir in dirForFound:
    files = os.listdir( dir )

    # И обрабатываем его
    fileList = list()
    for p in files :
      if ( p.find('.asm') != -1 ) or ( p.find('.inc') != -1 ):
        fileList.append( p )
 
    for fname in fileList :
      # открываем файл и разбираем строку
      try:
        f = open( dir+fname, "r" )
        try:
          # Read the entire contents of a file at once.
          string = f.read() 

          # english
          enl = list("")
          enlen = findAndFill( enl, string, pattern )
          if enlen != 0:
            print fname+" : "+str(enlen)
    
          #fwr.write(sWr) # Write a string to a file
        # обязательно будет выполнено
        finally:
          f.close()
          #fwr.close()
      except IOError:
        print 'error'


