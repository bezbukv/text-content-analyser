# coding: utf-8
'''
Created on 04.04.2013

@author: кей
'''


src_list = [
            [{'a': 1, 'b':1, 'c':2},[1, 2]],
            [{'a': 1, 'b':1, 'c':2},[1, 2]],
            [{'a': 1, 'b':1, 'c':2},[1, 2]],
            [{'a': 1, 'b':1, 'c':2},[1, 2]],
            [{'a': 1, 'b':1, 'c':2},[1, 2]],
            [{'a': 1, 'b':1, 'c':2},[1, 2]],
            [{'a': 1, 'b':1, 'c':2},[1, 2]]
            ]

src_list_d = [
            [{'a': 1, 'b':1, 'c':2}],
            [{'a': 1, 'b':1, 'c':2}],
            [{'a': 1, 'b':1, 'c':2}],
            [{'a': 1, 'b':1, 'c':2}],
            [{'a': 1, 'b':1, 'c':2}],
            [{'a': 1, 'b':1, 'c':2}],
            [{'a': 1, 'b':1, 'c':2}]
            ]

list_int = [1,2,3,4,5,6,7]

def split_and_merge(C):
    n = len(C)
    if n == 1:
        return C
    else:
        A_raw = C[:n/2]
        B_raw = C[n/2:]
        A = split_and_merge(A_raw)
        B = split_and_merge(B_raw)
        D = merge(A, B)
        return D


def merge(A, B):
    for at in A[0][0]:
        B[0][0][at] += A[0][0][at]
       
    # Суммирование списков 
    B[0][1][0] += A[0][1][0]
    B[0][1][1] += A[0][1][1]
    return B


# Run()
print split_and_merge(src_list)

"""
B = [{'a': 1, 'b':1, 'c':2}]
A = [{'a': 1, 'b':1, 'c':2}]
print merge(A, B)"""





