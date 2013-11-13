# coding: utf-8
import re


def is_content_letter(string):
    no_num = '[^0-9 \f\n\r\t\v\-\.\,]'
    if re.findall(no_num, string):
        return True
    else:
        return False
if __name__=='__main__':
    string = 'asfdasdf99 '
    print is_content_letter(string)
    string = '  '
    print is_content_letter(string)
    string = ' 9 '
    print is_content_letter(string)
    print 'Done'