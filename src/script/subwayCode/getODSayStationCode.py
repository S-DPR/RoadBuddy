import requests, json

apiKey = "+jZahXkzKToGFdeVqZFvO5VNh7ChsQFELMj7JH60I8c"
ret = {}
did = set()
with open('stationID.json', 'w', encoding='utf-8') as cf:
    with open('result.json', 'r', encoding='utf-8') as f:
        for idx, i in enumerate(f.readlines()):
            if not idx%10: print(idx, 'end')
            _, _, _, _, name = i.strip().split(", ")
            if name in did: continue
            did.add(name)
            print(name)
            res = requests.get(
                url='https://api.odsay.com/v1/api/searchStation',
                params={
                    'apiKey': apiKey,
                    'stationName': name,
                    'stationClass': 2,
                    'lang': 0
                }
            ).json()
            for x in res['result']['station']:
                laneName = x['laneName']
                ret[f"{laneName}, {name}"] = x['stationID']
    json.dump(ret, cf, ensure_ascii=False, indent=4)
print(ret)
