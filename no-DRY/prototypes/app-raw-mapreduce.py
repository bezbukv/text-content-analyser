# coding: utf-8
''' 

Происходит дублирование информации - потери в памяти, но это позволяет
  запускать задачи параллельно
'''

# Other
import json

# App
from MapReduce.mappers import mapper
from MapReduce.sufflers import suffler
from MapReduce.reduces import base_reducer 

# Преобразователи ресурса в текст
import spiders_processors.docs_spider as docs_spider
      
# App 
from app_utils import read_utf_txt_file
from app_utils import write_result_file

def printer(item):
    print item
    
def main():
    # Запускаем краулер
    print
    print 'Run crawler'
    #result_crawler = docs_spider.get_docs()#fake_crawler_one()
    
    print
    print 'Get task plan.'
    jobs = docs_spider.get_docs()#get_scheme_actions_srt(result_crawler)
    #jobs = [jobs[0]]
    print 
    print 'Jobs'
    map(printer, jobs)

    print
    print 'Begin Map stage. Wait please...'
    map_stage_results = map(mapper, jobs)
    
    # Suffle stage
    print
    print 'Begin Suffle stage. Wait please...'
    suffle_stage_results = suffler(map_stage_results)
      
          
    # Reduce
    print
    print 'Begin reduce stage...'
    result_reduce = {}
    resultttt = []
    for at in suffle_stage_results:
        
        one_node = suffle_stage_results[at]
        
        # Проверка слияния
        print at
        print '  Средняя длина предложения (Оценка Sent_Mean):',one_node[0][1][1]*1.0/one_node[0][1][0], 'слов'
            
        result = base_reducer(one_node)
        
        resultttt.append(one_node[0][1][1]*1.0/one_node[0][1][0])
        
        node_index = result[0][0]
        axises = []
        for jat in node_index:
            axises.append((node_index[jat]['N'], jat))
            
        src_list = sorted(
                axises, 
                key=lambda record: record[0],
                reverse=True) 
        tmp = []
        for it in src_list:
            tmp.append({it[0]: it[1]})
        result_reduce[at] = tmp

    # Result MapReduce
    json_result = json.dumps(result_reduce)
    fname = 'indexes/first_index.json'
    write_result_file([json_result], fname)
    #print json_result
    
    for at in resultttt:
        print at, ','

if __name__=='__main__':
    print 'Begin'
    main()
    print 'Done'
    
    

