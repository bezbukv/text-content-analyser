# coding: utf-8
# Sys
import os

# Other
import dals.os_io.io_wrapper as iow

class CursorWriter(object):
    _index_root = None
    def __init__(self, index_folder):
        self._index_root = index_folder
    
    def getListNodes(self):#list<string>
        list_nodes = os.listdir(self._index_root)
        result = []
        for at in list_nodes:
            result.append(at.replace('$$', ' '))
        return result
    
    
    
if __name__ == "__main__":
    index_head = '../../../index'
    cursor_writer = CursorWriter(index_head)
    print cursor_writer.getListNodes()
    print 'Done'