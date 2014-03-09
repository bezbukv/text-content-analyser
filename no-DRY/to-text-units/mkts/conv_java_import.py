# coding: utf-8
'''
Created on 18.04.2013

@author: кей
'''

import dals.os_io.io_wrapper as dal

def convert_one_line(msg):
    copy_line = msg.split(';')[0]
    if copy_line:
        name = copy_line.split('.')[-1]
        print copy_line+' as '+name

if __name__=='__main__':
    sets = dal.get_utf8_template()
    sets['name'] = 'test_import_to_jy.txt'
    readed = dal.file2list(sets)
    
    map(convert_one_line, readed)
    
    print 'Done'
