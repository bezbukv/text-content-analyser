# coding: utf8


from numpy import arange

def get_fake_fx(COUNT_POINTS):
    
    fx = arange(COUNT_POINTS)
    fx = fx[::-1]
    return fx 