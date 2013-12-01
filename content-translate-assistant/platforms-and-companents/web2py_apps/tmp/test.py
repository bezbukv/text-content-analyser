# coding: utf8
from gluon import *
tt = 'asdfasdf'
def ip0(): 
    return current.request.client
    
#-*- coding: utf8 -*-
# Include the Dropbox SDK libraries
import sys
sys.path.append('D:/Dropbox/dropbox-python-sdk-1.5.1')
import webbrowser

# Other
from dropbox import client, rest, session

# App
#import

def doing_anything():
    """ Выполняет некоторые действия
    
    Precond.: 
        Авторизация должна пройти
        
    Returns:
    """
    # Проверка авторизованности?
    # This will fail if the user didn't visit the above URL and hit 'Allow'
    access_token = sess.obtain_access_token(request_token)

    # Все хорошо?
    ''' 'Now that the hard part is done, all you'll need to sign your other 
    API calls is to to pass the session object to DropboxClient and 
    attach the object to your requests.'''
    client = client.DropboxClient(sess)

    f = open('working-draft.txt')
    response = client.put_file('/magnum-opus.txt', f)
    return response

def dropbox_auth():
    # Get your app key and secret from the Dropbox developer website
    APP_KEY = '5pspell9t3vuqlb'
    APP_SECRET = 'kc8isieyc13vdti'
    # ACCESS_TYPE should be 'dropbox' or 'app_folder' as configured for your app
    ACCESS_TYPE = 'app_folder'
    sess = session.DropboxSession(APP_KEY, APP_SECRET, ACCESS_TYPE)
    request_token = sess.obtain_request_token()

    # Нужно как-то разрешить
    url = sess.build_authorize_url(request_token)
    return url


