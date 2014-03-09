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
def Main( pattern ):
	retList = list('')
	# Получаем список файлов в директории
	# Получаем список всего, что есть в директории
	#dirForFound = ["../Generated Files/VDIInit/","../"]
	dirForFound = ["../src/", "../headers/", "../Generated Files/VDIInit/","../"]
  
	# поис и среди исходных файлов
	for dir in dirForFound:
		files = os.listdir( dir )

    # И обрабатываем его
		fileList = list()
		for p in files :
			if ( p.find('.asm') != -1 ) or ( p.find('.inc') != -1 ):
				fileList.append( p )
 
	# выводим на экран
	
		for fname in fileList :
			# открываем файл и разбираем строку
			try:
				f = open( dir+fname, "r" )
				try:
					# Read the entire contents of a file at once.
					string = f.read() 
					enl = list("")
					enlen = findAndFill( enl, string, pattern )
					if enlen != 0:
						oneRes = fname+" : "+str(enlen)
						print oneRes
						retList.append(oneRes)
				# обязательно будет выполнено
				finally:
					f.close()
			except IOError:
				print 'error'
		
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
	return retList


