#!/usr/bin/python
#-*- coding: utf-8 -*-
import str_finder as sf
import os
'''
  file : 
'''

defines = ['_2Ublock',
'_3Ublock',		
'_3U_polovina_block',
'_Uniplex',		#	;при активировании переключение Дупл/полудупл - внешней перемычкой.	При деактивации режим Дупл/полудупл выбирается в _duplex
'_duplex',		#	;(при НЕактивной _Uniplex)при активировании контроллер на выв. DUX устан. лог 0 (Дуплекс=const)/при деактивации на DUX лог 1(полудупл). От внешних перемычек не зависит 
'bootloader',
'NO_BOARD',
'RawDetData',
'WithDetector',
'otladka',
'fullspeed',
'liqiud600',
'air600',
'air100',
'DVBT2bred_',		#	;задержка отпирания атт (около 6,0 с) при появлении Рвх (_V3_BUM)
'manyBUM_OFF_bDE',	#	;активировать!!!, для многоблочных ПРД (коммутируем передатчик485 для передатчиков, где >1 БУМа,а для 100вт - MAX1485 вкл на передачу постоянно!
'_Umip42V',		#;для перекл порога по МИП на +42(+13%/-15%) V	 +020312
'_Umip48V',		#;для перекл порога по МИП на +48(+13%/-15%) V	 +020312
'_virtual_UM2']	#; виртуальная плата защит ОУ1, для применения в БУМ_FM 2U.

# Main
for i in defines:
	findedList = sf.Main( i )
	try:
		fwr = open('def_log.log', "a")
		
		try:
			fwr.write('\n'+i+':\n') # Write a string to a file
		finally:
			fwr.close()
			
		# поэлементам списка
		for at in findedList:
			fwr = open('def_log.log', "a")
			# цикл записи
			sWr = '  '+at+'\n'
			try:
				fwr.write(sWr) # Write 
			# цикл записи
		

			# обязательно будет выполнено
			finally:
				fwr.close()
	except IOError:
		print 'IOError'


