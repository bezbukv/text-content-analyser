# coding: utf-8

# Other
from pylab import plot
from pylab import show
from pylab import grid

# App
import business.originator_frequency_index.orginator as orginator
from business.to_text import get_list_content_items_from_str 
from business.originators_text_data import IndexCursor

def get_addrs():
    files = [
            '../statistic_data/srts/Iron Man AA/Iron Man02x26.srt', 
            '../statistic_data/srts/Iron Man AA/Iron1and8.srt']
    return files

def main():
    # Просматриваем индекс
    index_root = 'indexes'
    index = IndexCursor(index_root)
    list_nodes = index.get_list_nodes()
    print 'Index map: ', list_nodes
    
    # Типа выбрали ветку
    content_item_name = 'Iron Man AA'
    
    index.assign_branch(content_item_name)
    
    # Получаем пути к субтитрам
    # Сборщик контента - отдельный объект!
    files = None#get_addrs()
    
    # Получить индекс 
    print 'Process files. Wait please...'
    if files:
        for fname in files:
            # Выделяем единицы контента в список
            sentences_lst = get_list_content_items_from_str(fname)
    
            index.process_list_content_sentences(sentences_lst)
    
    # Выводим
    #index.print_branch(content_item_name)
    freq = []
    sorted_findex = index.get_sorted_forward_idx()
    for at in sorted_findex:
        print at
        freq.append(at[1])
    
    x = range(len(freq))
    plot(x, freq)
    grid()
    show()
    
    # Сохраняем в индексе  
    #index.save_branch()
    #import sys
    #print sys.argv[0]

if __name__=='__main__':
    print 'Begin'
    main()
    print 'Done'

