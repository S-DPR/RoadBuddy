import pandas as pd
import json

# CSV 파일을 읽어오는 코드
data = pd.read_excel('./items.xlsx')

# 필요한 열만 선택
selected_data = data[['RAIL_OPR_ISTT_CD', 'LN_CD', 'LN_NM', 'STIN_CD', 'STIN_NM']]

# 파일로 저장
ret = []
for idx, row in selected_data.iterrows():
    ret.append({
        'railCd': row['RAIL_OPR_ISTT_CD'],
        'lnCd': row['LN_CD'],
        'stationCd': row['STIN_CD'],
        'line': row['LN_NM'],
        'station': row['STIN_NM']
    })
with open('result.json', 'w', encoding='utf-8') as f:
    json.dump(ret, f, ensure_ascii=False, indent=4)