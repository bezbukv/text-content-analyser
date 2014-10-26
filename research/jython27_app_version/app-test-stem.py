# coding: utf-8

import dals.os_io.io_wrapper as dal
import TestLuceneStemmer

def write_result_file(result_list, fname):
    sets = dal.get_utf8_template()
    sets['howOpen'] = 'w'
    sets['name'] = fname
    dal.list2file(sets, result_list)
    
def read_utf_txt_file(fname):
    sets = dal.get_utf8_template()
    sets['name'] = fname
    return dal.file2list(sets) 

string = 'смотрела'

result = read_utf_txt_file('tmp.txt')
write_result_file(result, 'tmp2.txt')

stemmer = TestLuceneStemmer()
stemmer.stem('tmp.txt')

from subprocess import call
call(["java", "TestLuceneStemmer"])
print 'Done'