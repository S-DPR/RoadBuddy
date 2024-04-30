# # 파이썬 버전 : 3.12
# # escalator.xlsx 파일을 db에 넣기 위한 파이썬 코드
# # postgresql을 사용함을 가정하였으며, 어떻게 쓰든지 상관없음
# # 결과만 같다면 자바로 다시 짜도 됨
# # sql 테이블 이미 만들어놨음을 가정
# # terminal에서 아래 코드 실행
# # pip install pandas openpyxl psycopg2-binary sqlalchemy
#
# import pandas as pd
# from sqlalchemy import create_engine
#
# # 데이터베이스 연결 설정
# id = "postgres"
# password = "qwerty"
# dbname = "roadbuddy"
# engine = create_engine(f'postgresql+psycopg2://{id}:{password}@localhost:5432/{dbname}')
#
# # 엑셀 파일 읽기
# df = pd.read_excel(rf'escalator.xlsx', skiprows=1, engine='openpyxl')
# rows = [row.tolist() for index, row in df[:-1].iterrows()]
# # for row_id, (_, _, line, name, kth_escalator, number, pos, section, section_direction) in enumerate(rows, 1)
# items = [[row_id, *i] for row_id, (_, _, *i) in enumerate(rows, 1)]
# df = pd.DataFrame(items)
# df.columns = ['escalator_id', 'escalator_line', 'escalator_name',
#               'escalator_kth', 'escalator_number', 'escalator_pos',
#               'escalator_section', 'escalator_section_direction']
# print(len(df))
# print(df.to_sql('escalator', schema='rb', con=engine, index=False, if_exists='append'))
# print("Data insertion complete.")
