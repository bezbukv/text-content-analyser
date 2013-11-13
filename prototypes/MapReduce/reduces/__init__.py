# coding: utf-8
def base_reducer(suffler_results):
    # Неплохо бы рекурсивную реализацию
    n = len(suffler_results)
    if n == 1:
        return suffler_results
    else:
        A_raw = suffler_results[:n/2]
        B_raw = suffler_results[n/2:]
        A = base_reducer(A_raw)
        B = base_reducer(B_raw)
        D = base_merge(A, B)
        return D



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

def base_merge(A, B):
    """ [{index}, [count_sents, summ_sents_len], url] """
    #print A[1], B[1]
    #asfdasd
    for at in A[0][0]:
        try:
            B[0][0][at]['N'] += A[0][0][at]['N']
            for iat in A[0][0][at]['S']:
                B[0][0][at]['S'].append(iat)
            
            # Сжатие
            tmp = set(B[0][0][at]['S'])
            B[0][0][at]['S'] = list(tmp)
            
        except KeyError as e:
            B[0][0][at] = {'S': [], 'N': 0}
            B[0][0][at]['N'] = A[0][0][at]['N']
            for iat in A[0][0][at]['S']:
                B[0][0][at]['S'].append(iat)
                
            # Сжатие
            tmp = set(B[0][0][at]['S'])
            B[0][0][at]['S'] = list(tmp)
            
    # Сжать списки слов возникшие после работы стеммера
    #print B   
    return B