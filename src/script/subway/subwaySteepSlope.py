import json
import math
from collections import defaultdict


def haversine(lat1, lon1, lat2, lon2):
    R = 6371.0

    lat1 = math.radians(lat1)
    lon1 = math.radians(lon1)
    lat2 = math.radians(lat2)
    lon2 = math.radians(lon2)

    dlat = lat2 - lat1
    dlon = lon2 - lon1

    a = math.sin(dlat / 2) ** 2 + math.cos(lat1) * math.cos(lat2) * math.sin(dlon / 2) ** 2
    c = 2 * math.atan2(math.sqrt(a), math.sqrt(1 - a))
    distance = R * c

    return distance  # km단위


with open(r"C:\Users\Glory\Desktop\codeZip\Git\RoadBuddy\src\script\steepSlope\results.txt", "r",
          encoding='utf-8') as f:
    lst = f.readlines()
    items = [[*map(lambda x: x.strip(), lst[i:i + 3])] for i in range(0, len(lst), 3)]
    for i in items:
        lat, lon = map(float, i.pop().split())
        i.append([lat, lon])

# copyright : https://observablehq.com/@taekie/seoul_subway_station_coordinate
with open(r'C:\Users\Glory\Desktop\codeZip\Git\RoadBuddy\src\script\subway\station_coordinate.json', 'r',
          encoding='utf-8') as file:
    metro = json.load(file)
ret = []
with open(r'./fail.txt', 'w', encoding='utf-8') as ff:
    for i in metro:
        if len(i) != 5:
            ff.write(f"{i}\n")
            continue
        mlat, mlng = float(i['lat']), float(i['lng'])
        steepSlopes = []
        for j in items:
            dist = haversine(j[-1][1], j[-1][0], mlat, mlng)
            if dist > 0.5: continue
            steepSlopes.append({
                'address': j[0],
                'shortAddress': j[1],
                'latitude': j[2][1],
                'longitude': j[2][0]
            })
        jsn = {
            'line': i['line'],
            'name': i['name'],
            'steepSlope': steepSlopes,
        }
        print(jsn)
        ret.append(jsn)
with open('subwaySteepSlope.json', 'w', encoding='utf-8') as f:
    json.dump(ret, f, ensure_ascii=False, indent=4)
