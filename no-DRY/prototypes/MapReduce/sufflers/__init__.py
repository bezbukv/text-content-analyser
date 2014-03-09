# coding: utf-8
def suffler(map_stage_results):
    shuffle_result = {}
    for at in map_stage_results:
        node_name = at[0]
        if node_name not in shuffle_result:
            shuffle_result[node_name] = []
        shuffle_result[node_name].append(list(at[1:]))
    return shuffle_result 