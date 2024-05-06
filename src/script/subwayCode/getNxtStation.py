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

# with open('stationID.json', 'r', encoding='utf-8') as f:
#     data = json.load(f)
# print(data)
# apiKey = "+jZahXkzKToGFdeVqZFvO5VNh7ChsQFELMj7JH60I8c"
# ret = []
# def send_response():
#     with open('./response.txt', 'w', encoding='utf-8') as rf:
#         with open('./connInfo.json', 'w', encoding='utf-8') as cf:
#             for name, id in [*data.items()]:
#                 line, station = name.split(", ")
#                 res = requests.get(
#                     url='https://api.odsay.com/v1/api/subwayStationInfo',
#                     params={
#                         'apiKey': apiKey,
#                         'stationID': id,
#                         'lang': 0
#                     }
#                 ).json()['result']
#                 rf.write(f"{line}, {station}, {res}\n")
#                 prv, nxt = [], []
#                 if res['prevOBJ']:
#                     for item in res['prevOBJ']['station']:
#                         nStation = item['stationName']
#                         nLaneName = item['laneName'].replace('수도권 ', '')
#                         prv.append({
#                             'station': nStation,
#                             'line': nLaneName
#                         })
#                 if res['nextOBJ']:
#                     for item in res['nextOBJ']['station']:
#                         nStation = item['stationName']
#                         nLaneName = item['laneName'].replace('수도권 ', '')
#                         nxt.append({
#                             'station': nStation,
#                             'line': nLaneName
#                         })
#                 ret.append({
#                     'station': station,
#                     'line': line,
#                     'prv': prv,
#                     'nxt': nxt
#                 })
#             json.dump(ret, cf, ensure_ascii=False, indent=4)
#
# def read_response():
#     with open('./response.txt', 'r', encoding='utf-8') as rf:
#         with open('./connInfo.json', 'w', encoding='utf-8') as cf:
#             for i in rf.readlines():
#                 line, station, res = i.split(", ", 2)
#                 id = data[f'{line}, {station}']
#                 print(res)
#                 res = json.loads(res)
#                 prv, nxt = [], []
#                 if res['prevOBJ']:
#                     for item in res['prevOBJ']['station']:
#                         nStation = item['stationName']
#                         nLaneName = item['laneName'].replace('수도권 ', '')
#                         prv.append({
#                             'station': nStation,
#                             'line': nLaneName
#                         })
#                 if res['nextOBJ']:
#                     for item in res['nextOBJ']['station']:
#                         nStation = item['stationName']
#                         nLaneName = item['laneName'].replace('수도권 ', '')
#                         nxt.append({
#                             'station': nStation,
#                             'line': nLaneName
#                         })
#                 ret.append({
#                     'station': station,
#                     'line': line,
#                     'prv': prv,
#                     'nxt': nxt
#                 })
#             json.dump(ret, cf, ensure_ascii=False, indent=4)
# read_response()
