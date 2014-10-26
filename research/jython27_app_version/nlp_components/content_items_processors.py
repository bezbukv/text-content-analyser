# coding: utf-8
'''
Created on 03.04.2013

@author: кей
'''
import re

from nlp_components._content_filters import _purge_one_sentence
from nlp_components.filters import is_key_enabled
from nlp_components.word_level_processors import simple_word_splitter
from nlp_components.word_level_processors import fake_compressor
from nlp_components.word_level_processors import real_english_stemmer

# Processors
def process_list_content_sentences(list_s_sentences, lang):#void
    """ Получает список предложений и заполняет узел индекса. """
    one_node_hash = {}
    summ_sents_len = None
    count_sents = None
    count_sents = len(list_s_sentences) 
    summ_sents_len = 0
    
    # Обработка
    for  sentence in list_s_sentences:
        def add_new_record(key_name):
            if is_key_enabled(key_name):                          
                # Все что соответсвует слову
                one_node_hash[key_name] = {}
                
                # Формируем структуру, соотв. одному корню слова
                one_node_hash[key_name]['N'] = 1
                one_node_hash[key_name]['S'] = []  # Не сереализутеся
                one_node_hash[key_name]['S'].append(at)
                return True
            return False
        
        def edit_exist_node(key_name):
            one_node_hash[key_name]['N'] += 1
            if at not in one_node_hash[key_name]['S']:
                one_node_hash[key_name]['S'].append(at)
                
        # Сама обработка   
        if sentence:
            len_one_sent = 0
            pure_sentence = _purge_one_sentence(sentence)

            # Лексические единицы одного предложения
            set_words = simple_word_splitter(pure_sentence)
            
            
            for at in set_words:
                if at != ' ' and at:
                    # Обрабатываем один ключ
                    if lang == 'en':
                        compressed_key = real_english_stemmer(at)
                        #print 'en'
                    else:
                        compressed_key = fake_compressor(at)
                    # Уже включен
                    if compressed_key in one_node_hash:
                        added = edit_exist_node(compressed_key)
                        if added:
                            len_one_sent += 1
                    else:
                        add_new_record(compressed_key)
                        len_one_sent += 1
                        
            summ_sents_len += len_one_sent
                                       
    return one_node_hash, (count_sents, summ_sents_len)



# Processors
def process_list_content_sentences_real(list_s_sentences, tokenizer):#void
    """ Получает список предложений и заполняет узел индекса. """
    one_node_hash = {}
    summ_sents_len = None
    count_sents = None
    if tokenizer:
        count_sents = len(list_s_sentences) 
        summ_sents_len = 0
    
    # Обработка
    for  sentence in list_s_sentences:
        def add_new_record(key_name):
            if is_key_enabled(key_name):                          
                # Все что соответсвует слову
                one_node_hash[key_name] = {}
                
                # Формируем структуру, соотв. одному корню слова
                one_node_hash[key_name]['N'] = 1
                one_node_hash[key_name]['S'] = []  # Не сереализутеся
                one_node_hash[key_name]['S'].append(at)
        
        def edit_exist_node(key_name):
            one_node_hash[key_name]['N'] += 1
            if at not in one_node_hash[key_name]['S']:
                one_node_hash[key_name]['S'].append(at)
                
        # Сама обработка   
        if sentence:
            pure_sentence = _purge_one_sentence(sentence)

            # Лексические единицы одного предложения
            set_words = simple_word_splitter(pure_sentence)
            if tokenizer:
                summ_sents_len += len(set_words)
            
            for at in set_words:
                if at != ' ' and at:
                    # Обрабатываем один ключ
                    #compressed_key = fake_compressor(at)
                    compressed_key = real_english_stemmer(at)
                    if compressed_key in one_node_hash:
                        edit_exist_node(compressed_key)
                    else:
                        add_new_record(compressed_key)
                                       
    return one_node_hash, (count_sents, summ_sents_len)
