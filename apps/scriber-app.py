# coding: utf-8
'''
Created on 27.04.2013

@author: кей
'''
import traceback
try:
    # Std
    import sys
    import  json
    
    # units
    sys.path.append('../src')
    from scriber import scribe_index
    
    import dals.local_host.local_host_io_wrapper as dal
except:
    formatted_lines = traceback.format_exc().splitlines()
    err_msg = '\n'.join(formatted_lines) 
    print err_msg
    print sys.path
    var = raw_input("Press any key.")

def printer(msg):
    print msg

if __name__=='__main__':
    rpt = []
    # Запускаем крулер
    spider_target_fname = 'targets/spider_extractor_target'
    #.txt'
    
    # Задание загружаем из файла
    json_target, err = dal.read_utf_file_to_list_lines(spider_target_fname+'.json')
    if not json_target:
        print 'Failure occure: ', err[1]
        var = raw_input("Press any key.")
        print 'Quit'
        exit()

    target = json.loads(' '.join(json_target))
    # TODO(zaqwes): может задание в json сохранять для передачи разметчику, а то
    #   при любых ошибках нужно заново запускать поиск
    
    rpt = scribe_index(target, spider_target_fname+'.txt')
    if rpt:
        map(printer, rpt)
    
        
    print 'Done'
    var = raw_input("Press any key.")
