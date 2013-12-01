# coding: utf-8

#
import os

# App
import to_text.strs.os_dal as strs

if __name__=='__main__':
    print os.getcwd()
    fname = '../srts-samples/Iron Man AA/Iron1and8.srt'
    result = strs.read_file_and_purge_content(fname)
    
    #for at in result.split('.'):
    #    print at+'.'