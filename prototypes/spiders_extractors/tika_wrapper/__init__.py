# coding: utf-8
""" 

Fails:
    Определение языка
"""
# Python2.7
import re
import json

# Java
import java.io.BufferedInputStream as BufferedInputStream
import java.io.File as File
import java.io.FileInputStream as FileInputStream
import java.io.FileOutputStream as FileOutputStream
import java.io.IOException as IOException
import java.io.InputStream as InputStream
import java.io.PrintWriter as PrintWriter
import java.io.FileWriter as FileWriter
import java.io.BufferedWriter as BufferedWriter
import java.lang.String as String
import java.io.BufferedReader as BufferedReader
import java.io.FileReader as FileReader
import java.lang.StringBuilder as StringBuilder
import java.io.ByteArrayOutputStream as ByteArrayOutputStream
import java.io.OutputStream as OutputStream
import java.io.OutputStreamWriter as OutputStreamWriter
import java.net.URL as URL
import java.lang.System as System

# Tika
import org.apache.tika.exception.TikaException as TikaException
import org.apache.tika.metadata.Metadata as Metadata
import org.apache.tika.parser.AutoDetectParser as AutoDetectParser
import org.apache.tika.parser.ParseContext as ParseContext
import org.apache.tika.parser.Parser as Parser
import org.apache.tika.sax.BodyContentHandler as BodyContentHandler
import org.xml.sax.ContentHandler as ContentHandler
import org.xml.sax.SAXException as SAXException
import org.apache.tika.detect.DefaultDetector as DefaultDetector
import org.apache.tika.detect.Detector as Detector
import org.apache.tika.io.TikaInputStream as TikaInputStream
import org.apache.tika.Tika as Tika
import org.apache.tika.language.LanguageIdentifier as LanguageIdentifier
import org.apache.tika.language.ProfilingHandler as ProfilingHandler
#import org.apache.tika.language.ProfilingWriter
import org.apache.tika.language.ProfilingWriter as ProfilingWriter
import org.apache.tika.language.LanguageProfile as LanguageProfile
import org.apache.tika.parser.DelegatingParser as DelegatingParser
import org.apache.tika.sax.TeeContentHandler as TeeContentHandler

# App 
from filters import is_content_letter
from app_utils import printer
# Develop
import dals.local_host.local_host_io_wrapper as dal
   
def _no_space_in_line():
    return True

class TextExtractorFromOdtDocPdf(object):
    """ 
    Извлекает и очищает текст из файла формата *.pdf, *.doc, *.docx, *.odt
    
    
    TODO(zaqwes): Сделать фильтры
        - в предложении только пробелы или только цифры
    """
    _outputstream = None
    _context = None
    _detector = None
    _parser = None
    _metadata = None
    _extractedText = None
    
    def __init__(self):
        self._context = ParseContext()
        self._detector = DefaultDetector()
        self._parser = AutoDetectParser(self._detector)
        self._context.set(Parser, self._parser)
        self._outputstream = ByteArrayOutputStream()
        self._metadata = Metadata()
        
    _enabled_extentons = ['doc', 'docx', 'pdf', 'odt']
        
    def process(self, input_fname, path_to_node):
        def get_fname_for_save(fname):
            fname = fname.replace('\\','/')
            only_fname = fname.split('/')[-1]
            output_fname = path_to_node+'/'+only_fname+'.ptxt'
            return output_fname
        
        def get_fname_for_save_meta(fname):
            fname = fname.replace('\\','/')
            only_fname = fname.split('/')[-1]
            output_fname = path_to_node+'/'+only_fname+'.meta'
            return output_fname
        
        
        def file_is_enabled(fname):
            extention = fname.split('.')[-1]
            if extention in self._enabled_extentons:
                return True
            return False
        
        def purge_line(one_line):
            if not is_content_letter(one_line):
                return None
            result = one_line.replace('\t','')
            return result
        """ 
        Присутствует фильта входных файлов, что упрощает обработку папок с разнородными
        файлами.
        
        TODO(zaqwes): Используется детектор языков от Tika, хотя детектирует правильно, 
            но выдает, что недостаточно. Возможно можно заменить на другую библитеку
        
        Postcond.:
            Текстовой файл с полностью снятым форматированием. Язык не определен.
            
        Trouble:
            - Если программа содержит код
        """

        print 'Processing file : ', input_fname
        if not file_is_enabled(input_fname):
            return None, (1, 'File must *.doc, *.docx, *.pdf, *.odt. File skipped.')
        
        
        input_var = None
        
        output_fname = get_fname_for_save(input_fname)  # Файл временный, он же выходной
        print output_fname
        out_file = File(output_fname)  # Врядли выкенет исключение
        ofile_stream = FileOutputStream(out_file)
        lang = None
        try:
            # TODO(zaqwes): не очень понятно, что здесь происходит
            url = URL
            file_obj = File(input_fname);
            if file_obj.isFile():
                url = file_obj.toURI().toURL();
            else:
                url = URL(input_fname);
            # TODO(zaqwes): не очень понятно, что здесь происходит
              
            # Начинаем обработку
            metadata = {'url':input_fname}
            
            result_utf8 = []#['metadata','']  # формат строгий!!
              
            # На данный момент сохраняем в промежуточный файл на диске, но можно и ускорить
            # например, через отображение на память
            input_var = TikaInputStream.get(url, self._metadata);
            handler = BodyContentHandler(ofile_stream)
            self._parser.parse(input_var, handler, self._metadata, self._context);
            
            if ofile_stream:
                ofile_stream.close()
                
            return 'TEST', (1, 'TEST')
            # Преобразуем в unicode
            java_in = BufferedReader(FileReader(output_fname))
            writer = ProfilingWriter();
            while True:
                s = String()
                s = java_in.readLine()
                print s
                if s == None:
                    break
                
                # Строку нужно подчистить
                one_line = unicode(str(s), 'utf-8')
                one_line = purge_line(one_line)
                if one_line:
                    result_utf8.append(one_line)
                    writer.append(one_line);
                
            identifier = writer.getLanguage();
            lang = identifier.getLanguage()
            metadata['lang'] = lang
            #print identifier.isReasonablyCertain()  # Всегда False
            #System.out.println(identifier.getLanguage());
            
            meta_fname = get_fname_for_save_meta(input_fname)
            # Сохраняем результат 
            dal.write_result_file(result_utf8, output_fname)
            dal.write_result_file([json.dumps(metadata, sort_keys=True, indent=2)], meta_fname)
        
        except IOException as e:
            err_code = 1
            err_msg = 'Error: io.'
            e.printStackTrace()
            return output_fname, err_code, err_msg
        except TikaException as e:
            # Отключит обработку? Нет не отключит, т.к. исключение поймано
            e.printStackTrace()
        except SAXException as e:
            e.printStackTrace()
        finally:
            if input_var:
                try:
                    input_var.close()
                except IOException as e:
                    e.printStackTrace();
                    
        # Подводим итоги
        return ((output_fname, lang), (0, ''))
        
        
    # TODO(zaqwes): русский язык определить не может    
    def parse_with_lang_detection(self, filename):
        """url = URL  # Отрывок не работает
        file = File(filename);
        if file.isFile():
          url = file.toURI().toURL();
        else:
          url = URL(filename);
          
        input = TikaInputStream.get(url, self._metadata);
        handler = BodyContentHandler(self._outputstream);
        
        profiler = ProfilingHandler(self._outputstream);
        #tee = TeeContentHandler(handler, profiler);
        #tee = TeeContentHandler(handler);
        
        self._parser.parse(input, profiler, self._metadata, self._context);
        identifier = profiler.getLanguage();
        if identifier.isReasonablyCertain():
           # metadata.set(Metadata.LANGUAGE, 
            print identifier.getLanguage()#);
            """
            
        pass
        
        # Детектирует неправильно
        profile = LanguageProfile(
            "несложно догадаться, реально угнетающих  ");
        identifier = LanguageIdentifier(profile);
        System.out.println(identifier.getLanguage());
        
        writer = ProfilingWriter();
        writer.append("несложно догадаться, реально угнетающих ситуаций встречается ");
        writer.append("несложно встречается ");
        writer.append("несложно догадаться,  ");
        writer.append("несложно догадаться, реально угнетающих ситуаций встречается ");
        identifier = writer.getLanguage();
        System.out.println(identifier.getLanguage());

        
    def print_string(self):
        extractedText = self._outputstream.toString();
        print extractedText
        



if __name__=='__main__':
    """extractor = TextExtractor()
    extractor.parse_with_lang_detection('t.pdf')
    extractor.process('t.pdf')
    extractor.print_string()"""
    print
    print 'Done'