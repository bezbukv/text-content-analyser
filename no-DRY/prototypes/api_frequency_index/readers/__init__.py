# coding: utf-8

# Sys
import os

# No Jython2.5
import json

# Other
from pylab import plot
from pylab import show
from pylab import grid

# App
import dals.os_io.io_wrapper as dal

class BaseReader(object):
    """ Итератрор индекса. Работает с одной веткой, но знает 
        конерь хранилища и его план
        
        Thinks:
            Как будет происходит первое подкл. к индексу? Мы не знаем
            есть ли ветки
    """
    def __init__(self, index_root, init_branch=None):
        self._index_root = index_root

    # Core
    _index_root = None
    _node_cash = None  # кэш узла индекса
    _current_node_name = None
    _kForwardIndexName = 'forward_index.json'
    
    def _get_real_branch_name(self):#string
        real_branch_name = self._index_root+'/'+self._current_node_name.replace(' ', '$$')
        return real_branch_name
 
    def assignNode(self, node_name):#void
        """ Соединяет курсор с узлом. Если узла нет, создается."""
        self._current_node_name = node_name
        self._node_cash = {}
        findex_name = self._get_real_branch_name()+'/'+self._kForwardIndexName
        sets = dal.get_utf8_template()
        sets['name'] = findex_name
        try:
            os.mkdir(self._get_real_branch_name())
            sets['howOpen'] = 'w'
            dal.list2file(sets, ["{}"])
        except OSError as e:
            print 'Node is exist'  
            # Загружаем индекс 
            readed_list = dal.file2list(sets)
            branch_in_json = ' '.join(readed_list)
            
            # TODO(zaqwes): долгая операция(несколько секунд), как быть?
            self._node_cash = json.loads(branch_in_json)
    
    def printNodeContent(self):#void
        node = self._node_cash
        for at in node:
            if node[at] > 1:
                print node[at], at
                
    def getSortedForwardIdx(self):#list<map<string, int>>
        """ 
        
        TODO(zaqwes): Возможно нужно с фильтром
        """
        src_list = []
        for at in self._node_cash:
            src_list.append([at, self._node_cash[at]])
            
        # Сортируем
        src_list = sorted(
                          src_list, 
                          key=lambda record: record[1],
                          reverse=True) 
        return src_list
                  
    #def saveBranchCash(self):#void
    #    to_file = [json.dumps(self._node_cash, sort_keys=True, indent=2)]
    #    sets = dal.get_utf8_template()
    #    sets['name'] = self._get_real_branch_name()+'/'+self._kForwardIndexName
    #    sets['howOpen'] = 'w'
    #    dal.list2file(sets, to_file)
        
    #def _load_branch_in_cash(self):
    #    pass
    
    def getListNodes(self):#list<string>
        list_nodes = os.listdir(self._index_root)
        result = []
        for at in list_nodes:
            result.append(at.replace('$$', ' '))
        return result
    
    def to_filter(self):
        """ Переводит индекс в фильтр """
        return None
    
    def try_merge(self):
        pass
    
    
if __name__=='__main__':
    index_root = '../../../index'
    reader = BaseReader(index_root)
    def printer(item):
        print item
        
    list_nodes = reader.getListNodes()
    map(printer, list_nodes)
    
    # Загружаем узел
    node_name = 'Iron Man AA'
    reader.assignNode(node_name)
    
    # Вывод индекса
    map(printer, reader.getSortedForwardIdx())
    
    
    
