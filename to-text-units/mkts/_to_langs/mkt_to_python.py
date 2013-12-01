# coding: utf-8

# App
from _app_reuse import *





def _process_method(line):
    result = ''
    # TODO(zaqwes): Отделить комментарии
    
    # Нужно убрать тип данных в сигнатуре
    def _get_types_in_signature(line):
        result = ''
        begin = line.find('(')
        end = line.find(')')
        in_signature = line[begin+1:end]
        purged_signature_str = ''
        if in_signature:
            purged_signature = []
            for at in in_signature.split(','):
                purged_signature.append(at.split(' ')[-1])
                
            purged_signature_str = ', '.join(purged_signature)
            purged_signature_str = 'self, '+purged_signature_str
            result = line.replace(in_signature, purged_signature_str)
        else:
            result = line
            result = result.replace('(', '(self') 
        
        return result
     
    splitted = _get_types_in_signature(line).split(' ')   
    # TODO(zaqwes): 5! Bad!
    ret_type = splitted[4]
    result = '    # @return_type '+ret_type+'\n'
    result += '    def '+ \
        ' '.join(splitted[5:])+ \
        ':\n        pass\n'
    return result

def to_code(mkt):
    ifaces = []
    def _process_class(line):        
        line = line.replace('I:class ','class ')
        
        ifaces.append(extract_interface_name(line))
        result = line+'('+''+extract_interface_name(line)+'):'
        result = result.replace('class I', 'class ')
        return result

    def _process_interface(line):
        line = line.replace('interface ','class ')
        return _process_class(line)

    python_code = []
    for at in mkt:
        tmp = at.replace('OPENED:', '')
        tmp = tmp.replace('CLOSED:', '')
        python_code.append(tmp)

    call_map = {
        'I:': _process_interface,  
        'C:': _process_class,
        'M:': _process_method }
    
    python_code = process_code(python_code, call_map)
           
            
    return python_code, ifaces