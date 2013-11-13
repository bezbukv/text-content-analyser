# coding: utf-8
'''
Created on 03.04.2013

@author: кей
'''
#import nltk
#from nltk.stem.porter import PorterStemmer
import org.tartarus.snowball.ext.russianStemmer as russianStemmer
import java.lang.System as System
import java.lang.String as String

#import java.io.IOError;

def fake_compressor(word):
    return word

def simple_word_splitter(sentence):
    return sentence.split(' ')


def real_english_stemmer(word):
    #print PorterStemmer().stem_word(word)
    return word#PorterStemmer().stem_word(word)
    
if __name__=="__main__":
    #print real_english_stemmer('hellos')
    
    
    word = "смотрела";
    rs_new = russianStemmer()
    rs_new.setCurrent(String(word))
    rs_new.stem()
    print String(rs_new.getCurrent())
    #System.out.println(rs_new.getCurrent());

    word = str("смотрим")
    word = String(word)
    rs_new.setCurrent(word)
    rs_new.stem()
    print String(rs_new.getCurrent())
    print "Done"
