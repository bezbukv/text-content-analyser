# coding: utf-8
'''
Created on 22.04.2013

@author: Igor

'''
import org.apache.lucene.analysis.PorterStemmer
# Java
import java.text.BreakIterator as BreakIterator
import java.util.Locale as Locale
import java.lang.System as System

# App
from to_text.tika_wrapper import read_utf_txt_file
from to_text.tika_wrapper import write_result_file
from to_text.tika_wrapper import printer

def split_to_sentents(text_list, result_list):
    text = ' '.join(text_list)
    bi = BreakIterator.getSentenceInstance();
    bi.setText(text)
    index = 0
    while bi.next() != BreakIterator.DONE:
        sentence = text[index:bi.current()]
        #System.out.println("Sentence: " + sentence)
        result_list.append(sentence)
        index = bi.current();

if __name__=='__main__':
    # Получает задание
    path_to_targets = 'spider-targets/'
    target_file = path_to_targets+'_test_task.txt'
    list_targets = read_utf_txt_file(target_file)
    #map(printer, list_targets)
    
    # Каждый файл отдельно
    #
    index_file = 0
    for txt_fname in list_targets:
        file_content_in_list = read_utf_txt_file(txt_fname)
        meta = file_content_in_list[0]
        content = file_content_in_list[1:]
        
        # Пишем заголовок
        result =[meta, '']
        
        # Токенизируем контент
        split_to_sentents(content, result)
        
        # Записываем результаты
        path_to_tmp_files = 'result_folder/'
        write_result_file(result, path_to_tmp_files+'tmp'+str(index_file)+'.txt')
        index_file += 1

    print 'Done'
