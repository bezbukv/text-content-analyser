# coding: utf-8

# Java
import org.apache.tika.language.ProfilingWriter as ProfilingWriter
import java.text.BreakIterator as BreakIterator
import java.util.Locale as Locale

# No DRY!!!
def split_to_sentents_ld(text, result_list):
    """ 
    
    Danger:
        Почему-то между буквами добавляются пробелы
    
    """
    bi = BreakIterator.getSentenceInstance();
    bi.setText(text)
    index = 0
    writer = ProfilingWriter()  # для определения языка
    while bi.next() != BreakIterator.DONE:
        sentence = text[index:bi.current()]
        writer.append(sentence)
        result_list.append(sentence)
        index = bi.current()
        
    # Статистику накопили и теперь определяем язык
    identifier = writer.getLanguage();
    lang = identifier.getLanguage()
    return lang

def split_to_sentents(text_list, result_list):
    text = ' '.join(text_list)
    bi = BreakIterator.getSentenceInstance();
    bi.setText(text)
    index = 0
    while bi.next() != BreakIterator.DONE:
        sentence = text[index:bi.current()]
        result_list.append(sentence)
        index = bi.current();