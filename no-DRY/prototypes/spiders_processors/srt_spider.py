# coding: utf-8
'''
Created on 23.04.2013

@author: Igor
'''
from nlp_components.tokenizers import roughly_split_to_sentences

# Java
from spiders_processors.std_to_text_convertors.srt_to_text import std_srt_to_text_line

def plan_to_jobs_convertor_simpler(scheme):
    result = []
    j = 0
    for at in scheme:
        slice = scheme[at]
        for it in slice:
            j += 1
            result.append((at, it, j))
    return result

def plan_to_jobs_convertor(scheme):
    result = []
    for at in scheme:
        slice = scheme[at]
        for it in slice:
            it.append(at)
            result.append(it)
    return result

def fake_crawler_zero():
        # TODO(zaqwes): Во что сереализуется указатель на функцию - Нельзя его сереализовать
    # Можно подставить имя
    """ 
    [node, url, std_srt_to_text_line, roughly_split_to_sentences]
    """
        
    # Запускаем spider-processor
    node_name1 = 'Stenf. courses I'
    node_name2 = 'Stenf. courses II'
    readed_data = {node_name1:[], node_name2:[]}
    
    # Как бы результат работы spider-extractor and crawler
    node1_urls = [
            '../statistic_data/srts/Stenf Algs part I/5 - 1 - Quicksort- Overview (12 min).srt',
            '../statistic_data/srts/Stenf Algs part I/5 - 3 - Correctness of Quicksort [Review - Optional] (11 min).srt']
    
    
    node2_urls = [
            '../statistic_data/srts/Stenf Algs part I/5 - 3 - Correctness of Quicksort [Review - Optional] (11 min).srt', 
            '../statistic_data/srts/Stenf Algs part I/5 - 2 - Partitioning Around a Pivot (25 min).srt',
            '../statistic_data/srts/Stenf Algs part I/5 - 1 - Quicksort- Overview (12 min).srt']
    
    readed_data[node_name1] = node1_urls
    readed_data[node_name2] = node2_urls
    return readed_data

def fake_crawler_one():
    list_files = [
            '../statistic_data/srts/Stenf Algs part I/5 - 1 - Quicksort- Overview (12 min).srt',
            '../statistic_data/srts/Stenf Algs part I/5 - 3 - Correctness of Quicksort [Review - Optional] (11 min).srt',
            '../statistic_data/srts/Stenf Algs part I/5 - 3 - Correctness of Quicksort [Review - Optional] (11 min).srt', 
            '../statistic_data/srts/Stenf Algs part I/5 - 2 - Partitioning Around a Pivot (25 min).srt',
            '../statistic_data/srts/Stenf Algs part I/5 - 1 - Quicksort- Overview (12 min).srt']
        
    # Запускаем spider-processor
    readed_data = {}
    i = 0
    for at in list_files:
        node_name = at.split('/')[-1][:-3]+'_N'+str(i)
        readed_data[node_name] = [at]
        i += 1

    return readed_data
    
def get_scheme_actions_srt(readed_data):
    jobs = plan_to_jobs_convertor_simpler(readed_data)
    
    # Запускаем spider-processor
    def spider_str_processor(job):
        metadata = {'node_name':job[0]}
        node_name = job[0]
        url = job[1]
        number = job[2]
        metadata['url'] = url
        
        result = ['meta', '']
        # Очищаем файлы
        purged_content_file = std_srt_to_text_line(url)
        
        # делем не предложения и определяем язык
        lang = split_to_sentents(purged_content_file, result)
        metadata['lang'] = lang
        
        result[0] = json.dumps(metadata)
        path_to_file = 'tmp/'+node_name+'_N'+str(number)+'.txt'
        write_result_file(result, path_to_file)
        return (node_name, path_to_file)
        
    initial_jobs = map(spider_str_processor, jobs)
    return initial_jobs
