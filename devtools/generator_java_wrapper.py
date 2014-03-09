# coding: utf-8

import regenerator_python_code

if __name__=='__main__':
    source_content = regenerator_python_code.get_file_content('orginator.py')
    regenerator_python_code.generate_python_code(source_content)
    regenerator_python_code.generate_java_code(source_content)

