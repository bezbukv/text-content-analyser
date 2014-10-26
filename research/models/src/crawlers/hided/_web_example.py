#-*- coding: utf-8 -*-
''' 
        thinks :
                раз страница такая как при отключении модуля, то не удается создать pipe channal
                        раз так, то разве что может зависнуть поток создания каналов, либо программа просто отключилась
                
        alg.:
                с периодичностью делаем запросы на сервер
                        пришел ответ с 'The'
                                пытаемся перезапустить процесс
                                ставим флаг, что начали перезапуск
                                
        notes: 
                если есть прерывание от клавы, и есть еще потоки, то прервать нужно столько раз, сколько потоков
                
        test : 
                однажды запустился дважды! но обстоятельства до конца не помню
                однажды настройки сбросились в нули
                
        warning :
                с логгированием в один файл не просто, поэтому пока будет смешивание логов из разных мест
'''
import sys
import httplib
import random
import threading
import time
import os

from subprocess import call
import subprocess
import ctypes
import usaio.io_wrapper as io_wr

''' 
import logging, sys

log= logging.getLogger('logmodule')

def someFunc( a, b ):
    log.debug( "someFunc( %d, %d )", a, b )
    try:
        return 2*int(a) + int(b)
    except ValueError, e:
        log.warning( "ValueError in someFunc( %r, %r )", a, b, exc_info=True )

def mainFunc( *args ):
    log.info( "Starting mainFunc" )
    z= someFunc( args[0], args[1] )
    print z
    log.info( "Ending mainFunc" )

if __name__ == "__main__":
    logging.fileConfig( "logmodule_log.init" )
    mainFunc( sys.argv[1:] )
'''

# for logging

LEFT_PAGE_OWN_DOMAIN="/cgi-bin/ajx_lft.exe"     # cgi-скрипт, с пом. кот. определяем целостн. системы
PROCESS_NAME = 'tvct-old.exe'
PATH_TO_LOG = ''

def kill(pid):
    """kill function for Win32"""
    kernel32 = ctypes.windll.kernel32
    handle = kernel32.OpenProcess(1, 0, pid)
    return (0 != kernel32.TerminateProcess(handle, 0))
        


''' '''
class Launcher_FSM:
        _settings = { 'name':  'LogFile.log', 'howOpen': 'a', 'coding': 'cp1251' }      
        
        def _to_log(self, string):
                # файл может быть занят
                io_wr.app_str(self._settings, string)
                
        def run( self ):
                # стартуем сервер
                self._http_serv = httplib.HTTPConnection("127.0.0.1", 80)
                self._to_log('Launcher : server run')
                
                '''
                # запуск потока, похоже потребуется потокозащита
                onTick()
                
                # main-loop главный поток
                while 1:
                        try:
                                time.sleep(1.0)
                                
                                # возможно некоторые действия
                        except:
                                print 'close'
                                g_ticker.cancel()
                                g_ticker_restart.cancel()
                                
                                # закрываем сервер
                                g_http_serv.close()
                                
                                
                                if g_popen.poll() is None:
                                        print "killing process: %d" % g_popen.pid
                                        kill(g_popen.pid)
                                break'''
                                
        # State
        g_start_switch = False
        g_rerun_start = False
        g_rerun_counter = 0
        g_popen = None

        # число непрерывных помыток
        g_num_tries = 0
        
        # global
        _http_serv = None       # объект-сервер
        
        # таймер монитора
        g_ticker = None

        # таймер перезапуска
        g_ticker_restart = None
        
        


def run():
        global g_rerun_start
        global g_popen
        g_rerun_start = True
        try:
                g_popen = subprocess.Popen(['C:/TVCT/'+PROCESS_NAME])
                        # связан с оболочкой
        except:
                print 'force kill'
        

def printText(txt):
        global g_start_switch
        global g_ticker_restart
        global g_rerun_start
        global g_rerun_counter

        lines = txt.split(' ')
        head = lines[0]
        
        print head
        # теперь важно
        if head == 'The':
                if g_start_switch == False:
                        # отключаем процесс
                        print call(["taskkill", "-f",'-im', PROCESS_NAME])
                        
                        g_ticker_restart = threading.Timer(1.0, run)
                        g_ticker_restart.start()

                elif g_rerun_start:
                        print 'g_rerun_counter',g_rerun_counter
                        g_rerun_counter += 1
                        if g_rerun_counter > 10 :
                                print 'need restart system'
                        
                        
                g_start_switch = True
        else:
                g_start_switch = False
                g_rerun_counter = 0
                
def do_request():
        g_http_serv.connect()

        arg_cgi = "?"+"r="+str(random.random())
        g_http_serv.request('GET', LEFT_PAGE_OWN_DOMAIN+arg_cgi)

        response = g_http_serv.getresponse()
        if response.status == httplib.OK:
                
                printText (response.read())

def onTick():
        global g_ticker
        
        # что делаем - отправляем запрос
        do_request()
        
        # еще круг
        g_ticker = threading.Timer(2.0, onTick)
        g_ticker.start()
        
        
if __name__ == '__main__':
        fsm = Launcher_FSM()
        fsm.run()