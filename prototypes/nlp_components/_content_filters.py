# coding: utf-8
'''
Created on 20.03.2013

@author: кей
'''
def _purge_one_sentence(one_sentence):
    """ Обработка для дробления на слова.
    
    Args:
        Единица контента
        
    Returns:
        Чистый набор слов, разделыенных пробелами
    """
    if one_sentence[0] == ' ':
        one_sentence = one_sentence[1:]
    copy_one_sent = one_sentence.lower()
    copy_one_sent = copy_one_sent.replace('.','')
    copy_one_sent = copy_one_sent.replace('?','')
    copy_one_sent = copy_one_sent.replace('!','')
    copy_one_sent = copy_one_sent.replace(',','')
    copy_one_sent = copy_one_sent.replace('"','')
    copy_one_sent = copy_one_sent.replace('-','')
    copy_one_sent = copy_one_sent.replace(':','')
    copy_one_sent = copy_one_sent.replace(';','')
    copy_one_sent = copy_one_sent.replace('[','')
    copy_one_sent = copy_one_sent.replace(']','')
    copy_one_sent = copy_one_sent.replace('=','')
    #copy_one_sent = copy_one_sent.replace('♪','')
    copy_one_sent = copy_one_sent.replace('&','')
    copy_one_sent = copy_one_sent.replace('>','')
    copy_one_sent = copy_one_sent.replace('<','')
    copy_one_sent = copy_one_sent.replace('~','')
    copy_one_sent = copy_one_sent.replace('/','')
    result = copy_one_sent
    result = result.replace('\n', ' ')
    return result