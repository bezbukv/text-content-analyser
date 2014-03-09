#!/usr/bin/python
#-*- coding: utf-8 -*-
import re
import os
'''
  file : import str_finder_c as sf
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

''' '''
def findEstention( fname, listExt ):
	for at in listExt:
		if '.'+at in fname:
			return True
	return False

''' '''
def Main( pattern, fileList, head ):
	print "Head : "+head
	print "Pattern : "+pattern
	retList = list('')
	resultList = list('')
	# Получаем список файлов в директории
	# выводим на экран
	for fname in fileList :
		# открываем файл и разбираем строку
		try:
			f = open( fname, "r" )
			try:
				# Read the entire contents of a file at once.
				string = f.read() 
				enl = list("")
				enlen = findAndFill( enl, string, pattern )
				if enlen != 0:
					oneRes = "  "+fname+" : "+str(enlen)
					oneRes = oneRes.replace(head,'')
					if len( enl ) > 0 :
						resultList.append(enl)
					retList.append(oneRes)
			# обязательно будет выполнено
			finally:
				f.close()
		except IOError:
			print 'IOError'
		
	# в плане искл. не безопасно?
	# Пишем результаты в файл
	try:
		fw = open('mon.log', 'w')
		try:
			for enu in retList:
				fw.write('; '+enu+'\n')
		finally:
			fw.close()
	except IOError:
		print 'wr. err.'
	return retList, resultList


