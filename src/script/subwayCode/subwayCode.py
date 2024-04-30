import pandas as pd

# CSV 파일을 읽어오는 코드
data = pd.read_excel('./items.xlsx')

# 필요한 열만 선택
selected_data = data[['LN_NM', 'STIN_CD', 'STIN_NM']]

# 파일로 저장
with open('./result.txt', 'w', encoding='utf-8') as f:
    for index, row in selected_data.iterrows():
        f.write(f"{row['LN_NM']}, {row['STIN_CD']}, {row['STIN_NM']}\n")
