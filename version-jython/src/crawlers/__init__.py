# coding: utf-8
# Sys
import os#.path.exists

# DAL
import dals.local_host.local_host_io_wrapper as local_dal

# Exist API
import crawlers.dirs_walker as os_walker

# App
import app_utils as util

# crosscuttings
import crosscuttings.tools as tools 

kKeyTargetExts = 'extention list'
kKeyIgnoredDir = 'ignored dir'
kKeyRoot = 'root'
kKeyIndexName = 'index name'

def get_target(target_fname, spider_target_fname):
    def printer(msg):
        print msg

    raw_target, err = local_dal.read_utf_file_to_list_lines(target_fname)
    target = get_target_object(raw_target)
    
    print 'Source target: '
    map(printer, target.items())

    rpt = check_crawler_target(target)
    print 
    print 'Rpt:'
    map(printer, rpt)

    # Можно передавать краулеру на посик файлов - DataIsSafe
    # поиск
    print 
    print 'Begin finding'
    roots = target[kKeyRoot]
    extension_list = target[kKeyTargetExts]
    ignored_dirs = target[kKeyIgnoredDir]
    result_list, err = os_walker.find_files_down_tree_roots(roots, extension_list, ignored_dirs)
    if err[0]:
        print err[1]
    print 'End finding'
        
    # Разбираем не узлы
    target_for_spider, rpt = fill_target_for_spider(result_list)
    if rpt:
        print 
        print 'Rpt:'
        map(printer, rpt)
    
    fname = spider_target_fname
    local_dal.write_result_file(target_for_spider, fname)
    
    #print '\nResult target:'
    #map(printer, target.items())
    print
    print 'Nodes and urls write to file - '+fname
    return target

def check_crawler_target(target):
    rpt = []
    # Проверяем задание
    # Разрешенные расширения
    path_to_settings = "App/Spider extractor/auto_detected_extention"
    auto_processed_ext = tools.get_app_cfg_by_path(path_to_settings)
    target_ext = target[kKeyTargetExts]
    for ext in target_ext:
        if ext not in auto_processed_ext:
            rpt.append('File with this extention not automatically processed - *.'+ext)
            rpt.append('  Extention removed. If need then convert file to '+
                       '/'.join(auto_processed_ext))
            target_ext.remove(ext)
            
    # Доствпность корней
    roots = target[kKeyRoot]
    for diro in roots:
        if not os.path.exists(diro):
            roots.remove(diro)
            rpt.append('Root dir no found - '+diro+'. Path removed from target.')
            
    # Доствпность игнорируемых папок
    ignored = target[kKeyIgnoredDir]
    for diro in ignored:
        if not os.path.exists(diro):
            ignored.remove(diro)
            rpt.append('Ignored dir no found - '+diro+'. Path removed from target.')
            
    for diro in ignored:         
        # игнорируемый путь должен исходит из одного из корней
        content = False
        for root in roots:
            if root in diro:
                content = True
            
        if not content:    
            rpt.append('Ignored dir int root path - '+diro+'. Path removed from target.')
            ignored.remove(diro)
            
    return rpt

def get_target_object(raw_target):
    list_without_comments = []
    for line in raw_target:
        tmp_line = util.remove_forward_and_back_spaces(line.split('#')[0])
        if tmp_line:
            list_without_comments.append(tmp_line)
    
    # TODO(zaqwes): очистить от комментов
    target = {}
    def process_one_line(line):
        ptr = line.find(':')
        key = util.remove_forward_and_back_spaces(line[0:ptr])
        value = util.remove_forward_and_back_spaces(line[ptr+1:])
        if key == kKeyRoot:
            if key not in target: 
                target[key] = []
            target[key].append(value)
            target[key] = list(set(target[key]))
            
        elif key == kKeyTargetExts:
            list_ext = value.split(',')
            target[key] = list(util.remove_fandb_spaces_in_tuple(tuple(list_ext)))
        elif key == kKeyIndexName:
            # Тоже список, хотя из одного элемента. Удобно при дальнейшей обработке
            target[key] = [util.remove_forward_and_back_spaces(value)]
        elif key == kKeyIgnoredDir:
            if key not in target: 
                target[key] = []
            target[key].append(value)
            target[key] = list(set(target[key]))
        else:
            print 'No used'
    
    map(process_one_line, list_without_comments)
    return target

def fill_target_for_spider(result_list):
    list_nodes = {}
    target_for_spider = []
    rpt = []
    def filler_target(msg):
        # * - не может быть в пути к файл, поэтому можно будет разделить имя узла и адрес
        file_name = msg.split('/')[-1]
        node_name = '.'.join(file_name.split('.')[:-1])
        if node_name not in list_nodes:
            list_nodes[node_name] = -1
        else:
            rpt.append('Finded eq. named files. File - '+msg)
            
        list_nodes[node_name] += 1
        
        if list_nodes[node_name] != 0:
            node_name += str(list_nodes[node_name])
            
        target_for_spider.append('['+node_name+']* '+msg)

    map(filler_target, result_list)
    return target_for_spider, rpt

def get_node_name(line):
    line = line.split('*')[0]
    line = line.replace('[','')
    node = util.remove_forward_and_back_spaces(line.replace(']',''))
    return node

def get_url(line):
    line = line.split('*')[1]
    node = util.remove_forward_and_back_spaces(line)
    return node

def get_path_tasks(spider_target_fname):
    """ Нужно удалить комментарии """
    jobs_list, err =  local_dal.read_utf_file_to_list_lines(spider_target_fname)
    jobs_list = util.remove_comments_from_task(jobs_list)
    return jobs_list, err