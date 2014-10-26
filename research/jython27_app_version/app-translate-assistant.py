# coding: utf-8

# Other
from pylab import plot
from pylab import show
from pylab import grid

# App
import business.originator_frequency_index.orginator as orginator
from business.to_text import get_list_content_items_from_str 
from business.originator_frequency_index.orginator import IndexCursor

import dals.os_io.dirs_walker as dir_walker

def get_addrs():
    files = [
            '../statistic_data/srts/Iron Man AA/Iron Man02x26.srt', 
            '../statistic_data/srts/Iron Man AA/Iron1and8.srt']
    return files

def get_addr_stenf_algs():
    result = []
    head = '../statistic_data/srts/Stenf Algs part I'
    extensions_list, dict_ignore_lists = dir_walker.get_template()
    extensions_list = ['srt']
    return dir_walker.find_files_down_tree_PC(head, 
                                             extensions_list, 
                                             dict_ignore_lists)
    

def main(index, content_item_name, files):
    # Создаем ветку, если ее нет и подкл. к ней
    index.assign_branch(content_item_name)
    
    # Получить индекс 
    print 'Process files. Wait please...'
    for fname in files:
        if fname:
            print fname
            # Выделяем единицы контента в список
            sentences_lst = get_list_content_items_from_str(fname)
            index.process_list_content_sentences(sentences_lst)
    
    # Выводим
    freq = []
    sorted_findex = index.get_sorted_forward_idx()
    for at in sorted_findex:
        print at
        freq.append(at[1])
    
    x = range(len(freq))
    plot(x, freq)
    grid()
    show()

if __name__=='__main__':
    print 'Begin'
        # Просматриваем индекс
    index_root = 'indexes'
    index = IndexCursor(index_root)
    list_nodes = index.get_list_nodes()
    print 'Index map: ', list_nodes
    
    # Типа выбрали ветку
    content_item_name = 'Stenf Online courses'#'Iron Man AA'
    
    # Получаем пути к субтитрам
    # Сборщик контента - отдельный объект!
    files, msg = get_addr_stenf_algs()#get_addrs()
    
    main(index, content_item_name, files)
    print get_addr_stenf_algs()
    print 'Done'

