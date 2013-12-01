# coding: utf-8


# Std
import re

# Other
import dals
import dals.local_host.local_host_io_wrapper as dal

def printer(msg):
    print msg

def write_result_file(result_list, fname):
    sets = dal.get_utf8_template()
    sets['howOpen'] = 'w'
    sets['name'] = fname
    dal.list2file(sets, result_list)
    
def read_utf_txt_file(fname):
    sets = dal.get_utf8_template()
    sets['name'] = fname
    return dal.file2list(sets) 



def remove_forward_and_back_spaces(line):
    if line:
        return re.sub("^\s+|\n|\r|\s+$", '', line)
    else:
        return None
    
def remove_fandb_spaces_in_tuple(src):
    tmp = []
    for at in src:
        tmp.append(remove_forward_and_back_spaces(at))
        
    return tuple(tmp)

def remove_comments_from_task(raw_target):
    list_without_comments = []
    for line in raw_target:
        tmp_line = remove_forward_and_back_spaces(line.split('#')[0])
        if tmp_line:
            list_without_comments.append(tmp_line)
    return list_without_comments

if __name__=='__main__':
    print 'Done'
    
    text = """ Punkt knows that the periods in Mr. Smith and Johann S. Bach
    do not mark sentence boundaries.  And sometimes sentences
    can start with non-capitalized words.  i is a good variable
    name.
    """
    #import nltk
    #nltk.download()
    
     
    #tokenizer = nltk.data.load('nltk:tokenizers/punkt/english.pickle')
    #tokenizer.tokenize('Hello.  This is a test.  It works!')
""" 
sent_detector = nltk.data.load('tokenizers/punkt/english.pickle')

print '\n-----\n'.join(sent_detector.tokenize(text.strip()))"""