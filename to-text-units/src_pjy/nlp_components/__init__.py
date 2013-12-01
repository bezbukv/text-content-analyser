# coding: utf-8

#from nltk.stem.snowball import RussianStemmer  # No work with utf-8
import org.apache.lucene.morphology.russian.RussianAnalyzer as RussianAnalyzer
#import org.apache.lucene.morphology.russian.RussianAnalyzer
import org.apache.lucene.morphology.english.EnglishLuceneMorphology as EnglishLuceneMorphology


if __name__=='__main__':
    rs = RussianAnalyzer()
    #rs.
    #print rs.stem("полями")
    #print rs.stem("полe")
    #print rs.stem("полей")
    
    luceneMorph = EnglishLuceneMorphology()
    wordBaseForms = luceneMorph.getMorphInfo("usually")
    print wordBaseForms
    print 'Done'