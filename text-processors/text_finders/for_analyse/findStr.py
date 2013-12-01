#-*- coding: utf-8 -*-
import sys
import os
import str_finder as sf
''' '''

#dirForFound = ["../Generated Files/VDIInit/","../"]
dirForFound = ["../src/"]#, "../headers/"]
listExt = ['asm', 'inc']
listForFind = [
'D4A', #	1	;D4 Pin-Value
'D4B', #	1	;D4 Pout-Value	Uos
'D9AL', #	1	;D9 Usm1
'D9AH', #	1
'D9BL', #	1	;D9 Usm2
'D9BH', #	1
'D8AL', #	1	;D8 UF-Value
'D8AH', #	1
'D8BL', #	1	;D8 Uop-Value
'D8BH', #	1
'Lavadata', # 22+2
'count', # 1
'pLavadata', # 1
'Tempadr', #	1
'ChSum', #	1
'FIRST', #	1
'xxx', #	1
'Stato', #	1
'Potter', #	1
'cta', #	1
'ctb', #	1
'ctl', # 1
'cti', #	1
'ctu', #	1
'ctf', #	1
'ctm', # 1	;memo counter
'ctq', #	1	;quick move
'temp_FSRL', #	1
'temp_FSRH', #	1
'temp_STATUS2', #	1
'temp_FSRL2', #	1
'temp_FSRH2', #	1
'temp_BSR2', #	1
'temp_WREG2', #	1
'TEMP', #	1
'Temp_DAL', # 1
'Temp_DAH', # 1
'temp_DA', # 1
'DAL', #	1		;for DAC'S registers
'DAH', # 1
'DAA', #	1					;Adress for D4',D8',D9
'Lava', #	1					;Lava-Lava command
'KFLAG', # 1					;
'MFLAG', #	1
'SSPFLAGS', #	1				;0x76
'CTDS', # 1
'CTLoop', #	1
'DSFLAG', #	1
'DSTEMP', #	1  ; температура
'DSLoop',
'bitts', # 1
'AddressPointer', #	1
'AddressPointer2', #	1
'Temp', #	1  ;// какой-то важный регистр
'SPI_Temp', #	1
'counter', #	1
'Channel', #	1	; локальная
'Mode', #	1
]

# Получаем список файлов в директории
# Получаем список всего, что есть в директории

# поиск среди исходных файлов
# И обрабатываем его
fileList = list()
for dir in dirForFound:
	files = os.listdir( dir )
	for p in files :
		if findExt( p, listExt ):
			fileList.append( p )
# индивидуальные файлы
fileList = [
	"../src/_v2_KPUP.asm",
	"../src/kpup_low_lewel.asm"
]

summaryList = list()
summaryDict = list()
for file in fileList:
	tmpList = list('')
	tmpList.append( file )
	retList, dictNew = sf.Main( listForFind, tmpList, listExt )
	summaryList.append( retList )
	summaryDict.append( dictNew )  
	
a = set(summaryList[0])
b = set(summaryList[1])
c = a&b
c = list(c)
try:
	fw = open('cross.log', 'w')
	for i in c:
		string = i+' : '
		oneDict = summaryDict[0]
		string += str(oneDict[ i ])+' '
		oneDict = summaryDict[1]
		string += str(oneDict[ i ])
		fw.write(string+'\n')
	#except IOError:
		#print 'wr. err.'
finally:
	fw.close()
