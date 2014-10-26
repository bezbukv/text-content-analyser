# coding: utf-8
# Other
import yaml

# App
from app_utils import remove_forward_and_back_spaces
from app_utils import remove_fandb_spaces_in_tuple

def get_app_cfg():
    f = open('./cfgs/app_cfg.yaml', 'r')
    configuration = yaml.load(f)
    f.close()
    return configuration

def get_app_cfg_by_path(path):
    """ 's1/s2/s3' -> ['s1', 's2', 's3']"""
    tuple_settings = tuple(path.split('/'))
    result_settings = remove_fandb_spaces_in_tuple(tuple_settings)
    try:
        tmp = get_app_cfg()
        for at in result_settings:
            tmp = tmp[at]
            
        return tmp
    except KeyError as e:
        return None
 
def get_pathes_complect(index_name):   
    path = 'App/Scriber/app_folder'
    app_folder = get_app_cfg_by_path(path)
    
    # Производные пути
    index_path = app_folder+'/'+index_name
    index_root = index_path+'/index'
    tmp_root = index_path+'/tmp'
    
    return app_folder, index_path, index_root, tmp_root

    

