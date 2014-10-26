# coding: utf-8
""" 
1. Получить имена файлов
2. Перевести в текстовые файлы и в указанную папку

"""
# Java
import java.io.File as File

# App
from to_text.tika_wrapper import TextExtractorFromOdtDocPdf
from to_text.tika_wrapper import write_result_file
        
if __name__=='__main__':
    fname = "d:/11832_.pdf"
    fname = "d:/t.doc"
    fname = "d:/letter.pdf"
    ofile = 'u8_3_txt.txt'
    fname = 'tests_data/t.pdf'
    
    #class 
    path_to_node1 = 'D:/home/lugansky-igor/doc_pdf_odt/'
    node_name = 'Test Node'
    tmp_file_root = 'tmp_folder'
    path = File(path_to_node1)
    
    tasks_for_spider_purger = []#'['+node_name+']']
    
    for fname in path.list():
        full_name = path_to_node1+fname
        extractor = TextExtractorFromOdtDocPdf()
        tmp_fname, err_code, err_msg = extractor.process(full_name, tmp_file_root)
        tasks_for_spider_purger.append(tmp_fname[0])
        
    path_to_targets = 'spider-targets/'
    write_result_file(tasks_for_spider_purger, path_to_targets+'_test_task.txt')
    
    print
    print 'Done'
