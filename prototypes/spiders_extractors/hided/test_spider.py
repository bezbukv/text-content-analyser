# coding: utf-8
'''
Created on 10.04.2013

@author: кей
'''
""" 
import logging
logger = logging.getLogger('ftpuploader')
hdlr = logging.FileHandler('ftplog.log')
formatter = logging.Formatter('%(asctime)s %(levelname)s %(message)s')
hdlr.setFormatter(formatter)
logger.addHandler(hdlr)
logger.setLevel(logging.INFO)
FTPADDR = "some ftp address"
"""

import unittest
import dis

# 
from spiders import parser_target_for_spider
from spiders import _utils

def printer(msg):
    print msg

class Test(unittest.TestCase):
    def test_parser_target_bad_file(self):
        target_fname = 'test_spider_target.txt_f'
        gen = parser_target_for_spider(target_fname)
        for at in gen:
            self.assertIsNone(at[0], "File no exist")

        
    def test_parser_target(self):
        target_fname = 'test_data/test_spider_target.txt'
        parse_file_rpt = []
        for at in parser_target_for_spider(target_fname):
            print at
            result, err_code, rpt = at
            if rpt:
                parse_file_rpt.append(rpt)
            self.assertIsNotNone(result, rpt)
        print 'Rpt:'
        map(printer, parse_file_rpt)
           
        print  
        g_for_reduce = parser_target_for_spider(target_fname)
        #print dir(_utils)
        
    def test_parser_target_bad_format(self):
        target_fname = 'test_data/test_spider_target_bad.txt'
        gen = parser_target_for_spider(target_fname)
        for at in gen:
            tmp, err_code, rpt = at
            self.assertEqual(err_code, 2, "Ошибка форматирование файла задания")

if __name__ == "__main__":
    #import sys;sys.argv = ['', 'Test.testName']
    unittest.main()