# coding : utf-8

import java.util.ArrayList as ArrayList

def get_string(args):
   return "Hello"
def get_list(args):
    jlist = ArrayList()
    jlist.add(u'String from Jython')
    return jlist


if __name__=='__main__':
    print get_string(None)
    pass
