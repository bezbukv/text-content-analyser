#!/usr/bin/python
#-*- coding: utf-8 -*-
import re
'''
  file : import str_finder as sf
'''

# parse string
def findAndFill( xxl, srcStr, pattern ):
	xxl_l = list()
	result = re.finditer(pattern , srcStr)
	for match in result :
		# результаты поиска
		s = match.group()
		#print s
		# итоговая строка
		xxl_l.append(s)
  
	# число совпадений
	return xxl_l


# Main
def Main( pattern, fileList, listExt ):
	retList = list('')
	dictNew = {}
	# выводим на экран
	
	for fname in fileList :
		# открываем файл и разбираем строку
		try:
			print fname
			f = open( fname, "r" )
			try:
				# Read the entire contents of a file at once.
				string = f.read() 
				enl = list("")
				
				# для всех в списке
				for at in pattern:
					listOut = findAndFill( enl, string, at )
					enlen = len(listOut)
					if enlen != 0:
						oneRes = at#+' : '+fname+" : "+str(len(listOut))
						dictNew[oneRes] = enlen
						#print oneRes
						retList.append(oneRes)

			# обязательно будет выполнено
			finally:
				f.close()
		except IOError:
			print 'error open'
		
	# в плане искл. не безопасно?
	# Пишем результаты в файл
	try:
		fw = open('mon.log', 'a')
		try:
			for enu in retList:
				fw.write('; '+enu+'\n')
		finally:
			fw.write(' ; New File ')
			fw.close()
	except IOError:
		print 'wr. err.'
	return retList, dictNew


