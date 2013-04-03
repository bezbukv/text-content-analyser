# coding: utf-8
''' 

Происходит дублирование информации - потери в памяти, но это позволяет
  запускать задачи параллельно
'''

# Other
#from pylab import plot
#from pylab import show
#from pylab import grid
import json

# App
from business.nlp_components.tokenizers import roughly_split_to_sentences
from business.mappers import mapper


# Преобразователи ресурса в текст
from business.originators_text_data.srt_to_text import srt_to_text_line
from dals.os_io.io_wrapper import list2file
from dals.os_io.io_wrapper import get_utf8_template

def printer(item):
    print item

def get_scheme_actions():
    # TODO(zaqwes): Во что сереализуется указатель на функцию - Нельзя его сереализовать
    # Можно подставить имя
    
    def one_node_action_fake():
        content_pkge = \
            [
                ['../../../statistic_data/srts/Iron Man AA/Iron Man02x26.srt', 
                srt_to_text_line, 
                roughly_split_to_sentences],  # Дробитель контекста
                ['../../../statistic_data/srts/Iron Man AA/Iron1and8.srt', 
                srt_to_text_line, 
                None]
             ]
        return content_pkge
    
    node_name1 = 'Iron Man AA1'
    node_name2 = 'Iron Man AA2'
    readed_data = {
          node_name1: one_node_action_fake(),
          node_name2: one_node_action_fake()}
    return readed_data

def plan_to_jobs_convertor(scheme):
    result = []
    for at in scheme:
        slice = scheme[at]
        for it in slice:
            it.append(at)
            result.append(it)
    return result

def main():
    print 'Get task plan.'
    scheme = get_scheme_actions()
    
    print 'Split task to job.'
    jobs = plan_to_jobs_convertor(scheme)
    #map(printer, jobs)
    
    print 'Begin Map stage. Wait please...'
    map_stage_results = map(mapper, jobs)
    
    """sets = get_utf8_template()
    sets['name'] = 'tmp.json'
    sets['howOpen'] = 'w'
    list2file(sets, [json.dumps(map_stage_results, sort_keys=True, indent=2)])"""
      
    # Suffle stage
    print 'Begin Suffle stage. Wait please...'
    
    #def printer(value):
    #    for at in value:
    #        for it in value[at]:
    #            print it    
    #map(printer, map_result)
    
    # Выводим
    #index.print_branch(content_item_name)
    '''freq = []
    sorted_findex = index.get_sorted_forward_idx()
    for at in sorted_findex:
        print at
        freq.append(at[1])
    
    x = range(len(freq))'''
    #plot(x, freq)
    #grid()
    #show()
    
    # Сохраняем в индексе  
    #index.save_branch()
    #import sys
    #print sys.argv[0]

if __name__=='__main__':
    print 'Begin'
    main()
    print 'Done'
    
    #print json.dumps(main)  # NO WAY
    
    

