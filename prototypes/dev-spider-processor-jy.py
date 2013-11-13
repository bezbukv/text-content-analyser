# coding: utf-8
'''
Created on 22.04.2013

@author: Igor
'''
# Sys
import json
import os

# App
import dals.local_host.local_host_io_wrapper as dal
from app_utils import printer
from crawlers import kKeyIndexName
from crosscuttings import tools
import nlp_components as nlp

if __name__=='__main__':
    spider_target_fname = 'targets/spider_extractor_target' 
    # Задание загружаем из файла
    json_target, err = dal.read_utf_file_to_list_lines(spider_target_fname+'.json')
    if not json_target:
        print 'Failure occure: ', err[1]
        var = raw_input("Press any key.")
        print 'Quit'
        exit()

    target = json.loads(' '.join(json_target))
    map(printer, target.items())
    index_name = target[kKeyIndexName]
    app_folder, index_path, index_root, tmp_root = tools.get_pathes_complect(index_name)
    print app_folder, index_path, index_root, tmp_root
    
    # TODO(zaqwes): узлы лучше брать из задания
    nodes = [d for d in os.listdir(tmp_root) if os.path.isdir(os.path.join(tmp_root, d))]
    for node in nodes:
        path_to_node = tmp_root+'/'+node
        items = [path_to_node+'/'+d for d in os.listdir(path_to_node) if d.split('.')[-1] == 'ptxt']
        # Для каждого из файлов в узле
        result_data_one = []
        for item in items:
            print item
            content_list, err = dal.read_utf_file_to_list_lines(item)
            print content_list, err
            nlp.split_to_sentents(content_list, result_data_one)
            
        map(printer, result_data_one)
        # Пути к файлам готов
        break
        
        
    # Получаем список узлов
        
    """
    # Каждый файл отдельно
    #
    index_file = 0
    result_ = []
    for txt_fname in list_targets:
        print 'Process file:', txt_fname
        file_content_in_list = read_utf_txt_file(txt_fname)
        meta = file_content_in_list[0]
        content = file_content_in_list[1:]
        
        node_name = txt_fname.split('/')[-1]+'_N'
        #print node_name
        meta_info = json.loads(meta)
        meta_info['node_name'] = node_name
        meta = json.dumps(meta_info)
        #print meta
        
        # Пишем заголовок
        result =[meta, '']
        
        # Токенизируем контент
        split_to_sentents(content, result)
        
        # Записываем результаты
        path_to_tmp_files = 'result_folder/'
        url = path_to_tmp_files+node_name+str(index_file)+'.txt'
        write_result_file(result, url)
        index_file += 1
        
        result_.append((node_name, url))"""
    #return result_

    print 'Done'
