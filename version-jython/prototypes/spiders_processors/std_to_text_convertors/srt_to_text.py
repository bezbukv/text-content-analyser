# coding: utf-8
'''
    TODO(zaqwes): Как быть если запуск многопоточный, а файл включен в несколько узлов?
'''
# Sys

# Other
import re
import dals.os_io.io_wrapper as dal



from  nlp_components.filters import is_content_nums

def std_srt_to_text_line(url):
    """ Тотлько для субтитров. """
    sets = dal.get_utf8_template()
    sets['name'] = url
        
    readed_lst, err = dal.efile2list(sets)
    purged_lst = list()
    if readed_lst:
        for at in readed_lst:
            at_copy = at.replace('\r','')
            at_copy = at_copy.replace('\n','')
            if at_copy:
                if not '-->' in at_copy:
                    if not is_content_nums(at_copy):
                        at_copy = at_copy.replace('<i>','')
                        at_copy = at_copy.replace('</i>','')
                        
                        # Добавление
                        purged_lst.append(at_copy)
    
    # Теперь нужно разить на предложения
    one_line = '@@@@'.join(purged_lst)
    
    # Filtration
    one_line = one_line.replace(']', '.').replace('[','')
    one_line = one_line.replace('♪', '')
    
    # TODO(zaqwes): rm links
    one_line = re.sub('\~.*?\~', ' ', one_line)
    one_line = re.sub('\<.*?\</.*?\>', ' ', one_line)
    
    one_line = '\n'.join(one_line.split('@@@@'))
    
    return one_line
