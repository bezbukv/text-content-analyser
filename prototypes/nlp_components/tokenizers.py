# coding: utf-8
'''
Created on 02.04.2013

@author: кей
'''

import re

def roughly_split_to_sentences(one_line):
    """ Сделано супер просто. Но реально алгоритмы не таки простые. """
    _kInsertConst = '@@@@'
    one_line_with_inserts = ''
    for at in one_line:
        one_line_with_inserts += at
        if at == '.' or at == '!' or at == '?' or at == ']':
            one_line_with_inserts += _kInsertConst
    
    result = []     
    def _purge_str_line(sentence):
        #print sentence.find(' ') 
        sentence = sentence.replace('\t', ' ')
        tmp =  len(re.match(r"\s*", sentence).group())
        
        if sentence[tmp:] != '.':
            result.append(sentence[tmp:])
        #print map(str.isspace,sentence).index(False) 
    # Чистый контент - набор предложений
    sentences_lst = one_line_with_inserts.split(_kInsertConst)
    
    map(_purge_str_line, sentences_lst)
    return result


