# coding: utf-8
'''
Created on 26.04.2013

@author: Igor
'''
import traceback
try:
    import sys
   
    import json
    
    # App
    sys.path.append('../src')
    from crawlers import get_target
    from dals.local_host.local_host_io_wrapper import write_result_file
except:
    formatted_lines = traceback.format_exc().splitlines()
    err_msg = '\n'.join(formatted_lines) 
    print err_msg
    print sys.path
    var = raw_input("Press any key.")
    exit()

def printer(msg):
    print msg

if __name__=='__main__':
    target_fname = 'targets/crawler_schedule.txt'
    spider_target_fname = 'targets/spider_extractor_target'#.txt'
    target = get_target(target_fname, spider_target_fname+'.txt')
    print target
    
    json_target = json.dumps(target, sort_keys=True, indent=2)
    print '\nResult target:'
    print json_target
    
    # Эти настройки и настройки узлов лучше хранить раздельно
    spider_target_fname = spider_target_fname+'.json'
    write_result_file([json_target], spider_target_fname)

    print 'Done'
    var = raw_input("Press any key.")
