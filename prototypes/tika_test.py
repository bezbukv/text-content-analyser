# coding : utf-8

from to_text.tika_wrapper import TextExtractor
        
if __name__=='__main__':
    extractor = TextExtractor()
    extractor.process('tests_data/t.pdf')
    extractor.print_string()
    print
    print 'Done'