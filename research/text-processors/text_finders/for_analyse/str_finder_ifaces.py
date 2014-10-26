#!/usr/bin/python
#-*- coding: utf-8 -*-
import re
'''
  file : import str_finder_ifaces as sf
'''

# parse string
def findAndFill(xxl_save, srcStr, pattern ):
	xxl = list()
	result = re.finditer(pattern , srcStr)
	for match in result :
		# результаты поиска
		s = match.group()
		# итоговая строка
		xxl.append(s)
  
	# число совпадений
	return len(xxl), xxl


# Main
def Main( pattern, fileList, listExt ):
	retList = list('')
	listAdd = list()
	# выводим на экран
	
	for fname in fileList :
		# открываем файл и разбираем строку
		try:
			f = open( fname, "r" )
			try:
				# Read the entire contents of a file at once.
				string = f.read() 
				enl = list("")
				
				# для всех в списке
				for at in pattern:
					enlen, enl = findAndFill( None, string, at )
					if enlen != 0:
						listAdd.append( fname.split('/')[-1]+' : '+str(enlen) )
						retList.append(enl)

			# обязательно будет выполнено
			finally:
				f.close()
		except IOError:
			print 'error open'
	return retList, listAdd


