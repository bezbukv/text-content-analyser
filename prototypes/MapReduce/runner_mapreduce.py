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
from nlp_components.tokenizers import roughly_split_to_sentences
from MapReduce.mappers import mapper
from MapReduce.sufflers import suffler
from MapReduce.reduces import base_reducer 
from MapReduce.reduces import base_merge


# Преобразователи ресурса в текст
from std_to_text_convertors.srt_to_text import std_srt_to_text_line
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
                ['../../statistic_data/srts/Iron Man AA/Iron Man02x26.srt', 
                std_srt_to_text_line, 
                roughly_split_to_sentences],  # Дробитель контекста
                ['../../statistic_data/srts/Iron Man AA/Iron1and8.srt', 
                std_srt_to_text_line, 
                roughly_split_to_sentences]
             ]
        return content_pkge
    
    def one_node_action_fake2():
        content_pkge = \
            [
                ['../../statistic_data/srts/Iron Man AA/Iron Man02x26.srt', 
                std_srt_to_text_line, 
                roughly_split_to_sentences],  # Дробитель контекста
                ['../../statistic_data/srts/Iron Man AA/Iron Man02x26.srt', 
                std_srt_to_text_line, 
                roughly_split_to_sentences],
                ['../../statistic_data/srts/Iron Man AA/Iron1and8.srt', 
                std_srt_to_text_line, 
                roughly_split_to_sentences]
             ]
        return content_pkge
    
    node_name1 = 'Iron Man AA1'
    node_name2 = 'Iron Man AA2'
    readed_data = {
          node_name1: one_node_action_fake(),
          node_name2: one_node_action_fake2()}
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
    #for at in map_stage_results:
    #    print at[2]
      
    # Suffle stage
    print 'Begin Suffle stage. Wait please...'
    suffle_stage_results = suffler(map_stage_results)
            
    # Reduce
    for at in suffle_stage_results:
        #print at
        one_node = suffle_stage_results[at]
        #print len(one_node)
        
        # Проверка слияния
        #A = len(set(one_node[0][0].keys()) & set(one_node[1][0]))
        #print A
        #, len(one_node[1][0])
        result = base_reducer(one_node)
        pass
        #print len(result[0][0])
        
    
        #sets = get_utf8_template()
        #sets['name'] = 'tmp.json'
        #sets['howOpen'] = 'w'
        #list2file(sets, [json.dumps(suffle_stage_results, sort_keys=True, indent=2)])

if __name__=='__main__':
    print 'Begin'
    main()
    print 'Done'
    
    #print json.dumps(main)  # NO WAY
    
    

