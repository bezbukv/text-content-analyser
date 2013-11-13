# coding: utf-8
'''
Created on 11.05.2013

@author: кей
'''

from dals.local_host.local_host_io_wrapper import file2list
from dals.local_host.local_host_io_wrapper import get_utf8_template

if __name__=="__main__":
    sets = get_utf8_template()
    sets['name'] = '../info/preposition_ru.txt'
    readed = file2list(sets)
    result_set = set(readed[0][0].split(" "))
    
    # Добавляем делее
    sets['name'] = '../info/stop_words_ru.txt'
    readed, err = file2list(sets)
    for line in readed:
        work_copy = line[2:]
        result_set |= set(work_copy.split(" "))

    for it in result_set:
        print '\"'+it+'\", '
    print len(result_set)
    print 'Done'
