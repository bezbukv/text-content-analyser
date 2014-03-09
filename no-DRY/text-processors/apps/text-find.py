#-*- coding: utf-8 -*-
#!/usr/bin/python
import TextFinders.DirsWalker as DirsWalker
import TextFinders.FindEngine as FindEngine
import time

# Run()
if __name__=="__main__":
	ticks0 = time.time()
	
	head = '.\\'
	head = head.replace('\\','/')	# для windows
	pattern = 'slot'
	listOfExtension = ['py']
	
	# Ignore
	listOfIgnoreExtention = [ 'pyc', 'pyd' ]
	listOfIgnoreDirectories = list('')	# пока ничего
	ignoreLists = {}
	ignoreLists[ 'Extentions' ] = listOfIgnoreExtention
	ignoreLists[ 'Dirs' ] = listOfIgnoreDirectories
	
	# поиск
	resultList, msg = DirsWalker.findFilesDownTree( head, listOfExtension, ignoreLists)
	
	# список получен, можно его обработать
	# в принципе можно передать указатель на функцию обработки
	#for at in resultList:
	#	print at
		
	# список файлов готов, можно искать текст
	FindEngine.Main(pattern,resultList, head)
	ticks1 = time.time()
	print '\nTimeRun = '+str(ticks1-ticks0)+' sec'
