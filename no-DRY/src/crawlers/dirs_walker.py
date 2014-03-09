#-*- coding: utf-8 -*-
""" Нужно получить все
    
    thinks : walk and regex
    
    http://docs.python.org/2/tutorial/errors.html
"""
import os
 
class CrawlerException(Exception):
    def __init__(self, value):
        self.value = value
        
    def __str__(self):
        return str(self.value)

def _check_extension(string, extension_list):
    """ может быть ошибка, хотя маловероятна. Точка вероятность повышает 
    
    TODO(zaqwes): проверка заперщенных расширений
    """
    if not extension_list:
        raise CrawlerException('Error: List extentions is empty.')
    
    for k in extension_list:
        if not k:
            raise CrawlerException('Error: Extention is empty.')
        
        if '.'+k == string[-len(k)-1:].lower():
            return True
    return False

def find_files_down_tree(root, extension_list, ignored_dirs=None):
    """ Получить список файлов заданных типов с полными путями

    Args:
        1. пути - папки
        2. расширения, которые похожи на разрешенные
        3. целые файлы (с путем(1 шт) и без(может быть много))
        4. регулярные выражения - подстроки
        
    Troubles:
        разные типы данных - возвр. знач. и сообщение - но нужно 
            принимать из функции два значения
    """
    
    def on_error_walk(err):      
        # TODO: сделать свой класс обработки ошибок
        raise CrawlerException(err)
    
    result_list = list('')
    # получаем объект для обхода
    # Если корня нет исключение генерируется при доступе
    try:
        getted_list = os.walk(root, onerror=on_error_walk)
        for root, dirs, files in getted_list:
            if files:
                for name in files:
                    if _check_extension(name, extension_list):
                        path_enabled = True
                        if ignored_dirs:
                            for it in ignored_dirs:
                                print it
                                if it in root:
                                    path_enabled = False
                            
                        if path_enabled:
                            slash = '/'
                            full_path = root+slash+name
                            full_path = unicode(str(full_path), 'utf8')
                            #full_path = unicode(str(unicode(str(full_path), 'cp1251')), 'utf8')
                            full_path = full_path.replace('\\','/')
                            result_list.append(full_path)
    except CrawlerException as e:
        return None, (1, str(e))

    # возвращаем что насобирали
    return result_list, (0, '')    # может в питоне и нече, но вообеще-то...?
        # вобщем нужно подумать над обработкой ошибок
        
def find_files_down_tree_roots(roots, extension_list, ignored_dirs=None):
    """ Получить список файлов заданных типов с полными путями

    Args:
        1. пути - папки
        2. расширения, которые похожи на разрешенные
        3. целые файлы (с путем(1 шт) и без(может быть много))
        4. регулярные выражения - подстроки
        
    Troubles:
        разные типы данных - возвр. знач. и сообщение - но нужно 
            принимать из функции два значения
    """
    rpt = []
    result_list = list('')
    for root_path in roots:
        if root_path:
            def on_error_walk(err):      
                # TODO: сделать свой класс обработки ошибок
                raise CrawlerException(err)
            # получаем объект для обхода
            # Если корня нет исключение генерируется при доступе
            try:
                getted_list = os.walk(root_path, onerror=on_error_walk)
                for root, dirs, files in getted_list:
                    root = root.replace('\\', '/')
                    if files:
                        for name in files:
                            if _check_extension(name, extension_list):
                                path_enabled = True
                                if ignored_dirs:
                                    for it in ignored_dirs:
                                        if it in root:
                                            path_enabled = False
                                    
                                if path_enabled:
                                    slash = '/'
                                    full_path = root+slash+name
                                    #full_path = unicode(str(full_path), 'utf8')
                                    
                                    #full_path = unicode(str(unicode(full_path, 'cp1251')), 'utf8')
                                    full_path = full_path.replace('\\','/')
                                    print full_path
                                    result_list.append(full_path)
            except CrawlerException as e:
                rpt.append(str(e))

    # возвращаем что насобирали
    if not rpt:
        return result_list, (0, '')    # может в питоне и нече, но вообеще-то...?
        # вобщем нужно подумать над обработкой ошибок
    else:
        return result_list, (1, rpt)    # может в питоне и нече, но вообеще-то...?


""" How use it """
if __name__ == '__main__':
    def main():
        root = 'D:/doc_pdf_odt'
        #root = 'D:/Dropbox'
        extension_list = ['doc', 'odt']
        
        # поиск
        result_list, err = find_files_down_tree(root, extension_list)
        if err[0]:
            print err[1]
            return

        def printer(msg):
            print '['+msg.split('/')[-1]+']'
            print msg
    
        map(printer, result_list)
    
    main()
    print 'Done'



