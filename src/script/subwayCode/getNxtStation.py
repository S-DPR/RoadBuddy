import requests, json
from collections import defaultdict

ret = []
with open('./edges.txt', 'r', encoding='utf-8') as f:
    line = ""
    for i in f.readlines():
        l = i.strip().split()
        if len(l) == 1:
            line = l[0]
            continue
        for u, v in [l, l[::-1]]:
            ret.append({
                'line': line,
                'station': u,
                'connect': v
            })
with open('./connInfo.json', 'w', encoding='utf-8') as f:
    json.dump(ret, f, ensure_ascii=False, indent=4)
print(ret)
