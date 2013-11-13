'''
Created on 27.04.2013

@author: кей
'''


def doc_to_txt(ipdf_file, otxt_file):
    """ 
    
    Returns:
        Нефильтрованный текстовой файл в формате utf-8
    """
    err_code = 0
    err_msg = ''
    tmp_file = ipdf_file+'.tmp'
    istr = None
    ostream = None
    try:
        istr = BufferedInputStream(FileInputStream(File(ipdf_file)));
        parser = AutoDetectParser();
        ostream = FileOutputStream(tmp_file)
        handler = BodyContentHandler(System.out)#ostream)
        
        metadata = Metadata();
        
        # Выдает в stdout text
        parser.parse(istr, handler, metadata, ParseContext());
        
        for name in metadata.names():
           value = metadata.get(name);
           System.out.println("Metadata Name:  " + name);
           System.out.println("Metadata Value: " + value);

        
            # Преобразуем в unicode
        java_in = BufferedReader(FileReader(tmp_file))
        s = String()
        result_utf8 = []
        while True:
            s = java_in.readLine()
            if s == None:
                break
            result_utf8.append(unicode(str(s), 'utf-8'))
            #print s
            #result_utf8.append(s)
    
        sets = dal.get_utf8_template()
        sets['howOpen'] = 'w'
        sets['name'] = otxt_file
        dal.list2file(sets, result_utf8)
       
    except IOException as e:
        err_code = 1
        err_msg = 'Error: io.'
    except TikaException as e:
        e.printStackTrace()
    except SAXException as e:
        e.printStackTrace()
    finally:
        if ostream:
            try:
                istr.close();
            except IOException as e:
                e.printStackTrace();
        if istr:
            try:
                istr.close();
            except IOException as e:
                e.printStackTrace();
    return err_code, err_msg

def pdf_cp1251_to_text_utf8(ipdf_file, otxt_file):
    """ 
    
    Returns:
        Нефильтрованный текстовой файл в формате utf-8
    """
    err_code = 0
    err_msg = ''
    tmp_file = ipdf_file+'.tmp'
    istr = None
    ostream = None
    try:
        istr = BufferedInputStream(FileInputStream(File(ipdf_file)));
        parser = AutoDetectParser();
        ostream = FileOutputStream(tmp_file)
        handler = BodyContentHandler(ostream)
        
        metadata = Metadata();
        
        # Выдает в stdout text
        parser.parse(istr, handler, metadata, ParseContext());
        
            # Преобразуем в unicode
        java_in = BufferedReader(FileReader(tmp_file))
        s = String()
        result_utf8 = []
        while True:
            s = java_in.readLine()
            if s == None:
                break
            result_utf8.append(unicode(str(s), 'utf-8'))
            #print s
            #result_utf8.append(s)
    
        sets = dal.get_utf8_template()
        sets['howOpen'] = 'w'
        sets['name'] = otxt_file
        dal.list2file(sets, result_utf8)
       
    except IOException as e:
        err_code = 1
        err_msg = 'Error: io.'
    except TikaException as e:
        e.printStackTrace()
    except SAXException as e:
        e.printStackTrace()
    finally:
        if ostream:
            try:
                istr.close();
            except IOException as e:
                e.printStackTrace();
        if istr:
            try:
                istr.close();
            except IOException as e:
                e.printStackTrace();
    return err_code, err_msg


# coding: utf-8
# Sys
import os
import json

# Other
from dals.os_io.io_wrapper import list2file
from dals.os_io.io_wrapper import get_utf8_template
import dals.os_io.io_wrapper as dal

# App
import crosscuttings.tools as tools
from crosscuttings.tools import get_app_cfg_by_path
from spiders_extractors._utils import _parse_target_params
from app_utils import remove_forward_and_back_spaces
from spiders_extractors._utils import get_node_name
from spiders_extractors._utils import is_node

# Convertors but how custome 
from spiders_processors.std_to_text_convertors.srt_to_text import std_srt_to_text_line
from spiders_processors.std_to_text_convertors import get_call_map
# std_to_text_map =
# custom_to_text_map =

def _do_tmp_node_folder(node_name, tmp_dir_path):
    try:
        os.mkdir(tmp_dir_path+node_name)
    except WindowsError as e:
        pass
    return tmp_dir_path+node_name+'/'
    
def _save_temp_file(fname, text_content):
    sets = get_utf8_template()
    sets['name'] = fname 
    sets['howOpen'] = 'w'
    list2file(sets, text_content)
    
def extracte_text(url, sparams):
    # Добавляем адрес для послед. сост. обратного индекса
    result = ['url: '+url]
    result.append('')
    
    params = json.loads(sparams)
    
    # TODO(zaqwes): пока рассм. только файлы операционной системы
    if 'external_url' in params:
        return None, "Error: No implement processing external url."
    
    # Файлы файловой системы
    if 'to_text' in params:
        convertor_name = params['to_text']
        call_map = get_call_map()
        text_content = call_map[convertor_name](url)
        result.append(text_content)
        return result
        
    # Обработка по умолчанию
    
    # Сам контент
    # TODO(zaqwes): url for GET может быть разным
    # TODO(zaqwes): Костыль - подходит только для файлов файловой системы
    #   причем пока только текстовых
    extention = url.split('.')[-1]  
    
    text_content = ''
    if extention == 'srt':
        text_content = std_srt_to_text_line(url)
    else:
        print 'Error: No implemented. Recognize only *.srt files. It *.'+extention
    result.append(text_content)
    
    return result

def check_availabel_resourses(target_fname):
    """ Проверка доступности ресурсов до запуска паука."""
    rpt = []
    all_right = True
    target_generator = parser_target_for_spider(target_fname)
    for at in target_generator:
        info = at[0]
        head_rpt_msg = 'Name node: ['+info[0]+']\nUrl: ['+info[1]+']\n'
        
        # Проверка ключей
        params = json.loads(info[3])
        available_keys = tools.get_app_cfg()['App']['Spider']['available_keys']
        for at in params:
            if at not in available_keys:
                all_right = False
                rpt.append(head_rpt_msg+"\tError: find desabled key params.")
                
        # Проверка преобразователя
        if 'to_text' in params:
            available_convertors = tools.get_app_cfg()['App']['Spider']['to_text_convertors']
            if params['to_text'] not in available_convertors:
                all_right = False
                rpt.append(head_rpt_msg+"\tError: no registred to-text-convertor - "+
                           params['to_text']+
                           ". May be registred in"+
                           " /app-cfgs/spider_cfg.yaml file.")
            else:
                if 'std_' not in params['to_text'] and 'custom_' not in params['to_text']:
                    all_right = False
                    rpt.append(head_rpt_msg+"\tError: bad name to-text-convertor - "+
                           params['to_text']+
                           ". Must begin with std_ or custom_ prefix.")
        
        # Проверка преобразователя по умолчанию
        url = info[1]
        extenton = url.split('.')[-1]
        if not params:
            auto_detected_urls = tools.get_app_cfg()['App']['Spider']['auto_detected_urls']
            if extenton not in auto_detected_urls:
                all_right = False
                rpt.append(head_rpt_msg+"\tError: url не распознан и пользовательски настройки не задны")
    
        # Проверка доступности ресурса
        # Файл файловой системы
        url_exist = os.path.exists(url)
        if not url_exist:
            all_right = False
            rpt.append(head_rpt_msg+"\tError: url найден в пределах операционной системы."+
                       " Если это сетевой адрес задайте пареметры [external_url: yes get(or post) add params]")
      
            # Проверка доступности сетевого адреса
            if 'external_url' in params:
                all_right = False
                rpt.append(head_rpt_msg+"\tWarning: проверка внешних адресов не реализована")
        # Проверки пройдены
                
    return all_right, rpt

def base_spider(target_fname):
    """ 
    
    Danger:
        Узел записывается поверх, а не добавляется, временные файлы затираются
        Хорошо бы вообще очистить временную папку. Пусть целевой файл паука
        создает все заново.
        
        Пока все
    """
    rpt = []
    target_generator = parser_target_for_spider(target_fname)

    for at in target_generator:
        # TODO(zaqwes): Не очень эфф. но что если обработка распр? А как быть с конф.
        #   файлом если нужно запустить распределенно? Наверное лучше вынести
        path = 'App/Spider/intermedia_storage'
        tmp_dir_path = get_app_cfg_by_path(path)
        if not tmp_dir_path:
            rpt.append("Error: Params app no found - "+path)
            return rpt

        node_name, url, file_idx, params = at[0]
        
        # Строем папку
        path_to_node = _do_tmp_node_folder(node_name, tmp_dir_path)
        
        # Можно заполнять контентом
        text_content = extracte_text(url, params)
        
        # Пишем во временный файл
        tmp_fname = path_to_node+'/tmp'+str(file_idx)+'.txt'
        _save_temp_file(tmp_fname, text_content)
    return rpt



def parser_target_for_spider(target_fname):
    """ 
    
    Thinks:
        А что если файл пустой?
        
    TODO:
        Сделать кастомизацию преобразоватлелей в текст
    """
    sets = dal.get_utf8_template()
    sets['name'] = target_fname
    list_lines, err = dal.efile2list(sets)
    if err[0]:
        rpt = err[1]
        yield None, 1, rpt
        return
   
    # Можно обрабатывать
    list_without_comments = map(
            lambda line: remove_forward_and_back_spaces(line.split('#')[0]), 
            list_lines)
    
    # Удаление пустых строк
    result_job_list = []
    map(lambda line: result_job_list.append(line) if line \
        else None, list_without_comments)

    # В первой информационной строке должно быть имя узла
    if not is_node(result_job_list[0]):
        rpt = 'target_fname: '+target_fname+ \
                '. Неверный формат файла - первое имя узла должно быть до адресов.'+ \
                'Либо файл с заданиями пуст.'
        code_err = 2
        yield None, code_err, rpt
        return
    
    current_node = get_node_name(result_job_list[0])
    i = 0
    nodes = []
    for at in result_job_list:
        if is_node(at):
            current_node = get_node_name(at)
            if current_node not in nodes:
                nodes.append(current_node)
            else:
                code_err = 2
                yield None, code_err, 'Name node: ['+current_node+ \
                        ']\n'+"\tError: Node name need be unic."
            i = 0
        else:
            i += 1
            # Выделяем обработчик
            pos_first_settings_item = at.find('[')          
            if pos_first_settings_item != -1:
                url =  remove_forward_and_back_spaces(
                        at[:pos_first_settings_item])
                params = at[pos_first_settings_item:]
                params, code_err, rpt = _parse_target_params(params)
                if code_err != 0 and rpt:
                    rpt = 'Name node: ['+current_node+']\nUrl: ['+url+']\n'+rpt
                yield (current_node, url, i, params), 0, rpt
            else:
                url =  remove_forward_and_back_spaces(at)
                rpt = None
                yield (current_node, url, i, '{}'), 0, rpt
                
        
