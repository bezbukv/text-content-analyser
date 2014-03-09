# coding: utf-8
'''
Created on 11.04.2013

@author: кей
'''

# Other

# App
from spiders import base_spider
from spiders import parser_target_for_spider
from spiders import check_availabel_resourses
from crosscuttings import tools

# Конверторы
from std_to_text_convertors import Init

def printer(msg):
    print msg
    
def run_other():
    # Несколько раз спрашиваем
    print "Исправльте ошибки и попробуйте еще раз."
    cmd = str(raw_input('Продолжить?[y/n]'))
    if cmd == 'y':
        return True
    if cmd == 'n':
        return False
    else:
        print 'Комманда не опознана'
        return False

def main():
    
    # Инициализируем паука
    path = 'App/Spider/targets_folder'
    kSpiderTargetsPath = tools.get_app_cfg_by_path(path)
    
    # Запускаем паука
    # Проверяем файл целей
    target_name = kSpiderTargetsPath+'iron_man_aa_target.txt'
    parse_file_rpt = []
    for at in parser_target_for_spider(target_name):
        result, err_code, rpt = at
        if rpt:
            parse_file_rpt.append(rpt)
            print at[0]
    if parse_file_rpt:
        # Есть замечания
        print 'Rpt Parse file:'
        map(printer, parse_file_rpt)
        if not run_other():
            return
        
    
    # Базовая проверка целостности задания
    all_right, rpt = check_availabel_resourses(target_name)
    if not all_right:
        print 'Rpt Checking pathes:'
        map(printer, rpt)
        if not run_other():
            return
            
    
    # TODO(zaqwes): сделать отчет по преобразованию
    rpt = Init()
    if rpt:
        print 'Rpt init convertors:'
        map(printer, rpt)
    rpt = base_spider(target_name)
    map(printer, rpt)
    
    # Временный индекс построен!
    
    
if __name__=='__main__':
    main()
    print 'Done'