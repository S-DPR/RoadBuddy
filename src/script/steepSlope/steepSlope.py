# pip install requests, selenium

import requests
import time
from selenium import webdriver
from selenium.webdriver.chrome.service import Service
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import Select, WebDriverWait

webdriverPath = rf"C:\Users\Glory\Desktop\codeZip\webdriver\edgedriver_win64\msedgedriver.exe"
webdriverService = Service(webdriverPath)
driver = webdriver.Edge(service=webdriverService)

url = f"https://www.safekorea.go.kr/idsiSFK/neo/sfk/cs/sfc/ass/asseaiList.html?menuSeq=109"
driver.get(url)
WebDriverWait(driver, 30).until(lambda d: d.execute_script('return document.readyState') == 'complete')

selectArea1Element = driver.find_element(By.ID, "sbLawArea1")
selectArea1 = Select(selectArea1Element) # 시도 선택
selectArea1.select_by_value("11")

selectArea2Element = driver.find_element(By.ID, "sbLawArea2")
selectArea2 = Select(selectArea2Element) # 시군구선택
selectArea2Options = selectArea2.options[1:]

searchBtnElement = driver.find_element(By.CLASS_NAME, "search_btn")
pageInputElement = driver.find_element(By.ID, "iptpageinput")
totalPageElement = driver.find_element(By.ID, "tbpagetotal")
pageGoBtnElement = driver.find_element(By.ID, "apagego")

tbodyId = "gen"

apiEndPoint = 'https://naveropenapi.apigw.ntruss.com/map-geocode/v2/geocode'
headers = {
    'Accept': 'application/json',
    'X-NCP-APIGW-API-KEY-ID': '',
    'X-NCP-APIGW-API-KEY': 'olgFrK6W7mFGem0fxWN1zdw4urmZWhWJRqspk8dI'
}

with open("./fail.txt", "w", encoding='utf-8') as ff:
    with open("./allData.txt", "w", encoding='utf-8') as af:
        with open("./results.txt", "w", encoding='utf-8') as f:
            for option in selectArea2Options:
                selectArea2.select_by_value(option.get_attribute('value'))
                driver.execute_script("arguments[0].click();", searchBtnElement)
                time.sleep(1)
                pageTotal = int(totalPageElement.text.strip("/"))
                for i in range(1, pageTotal+1):
                    pageInputElement.clear()
                    pageInputElement.send_keys(f"{i}")
                    pageGoBtnElement.click()
                    time.sleep(1)
                    tableRows = driver.find_elements(By.XPATH, f"//*[@id='{tbodyId}']/tr")
                    for idx in range(1, len(tableRows)):
                        pos = driver.find_element(By.ID, f"gen_{idx}_anc").text
                        name = driver.find_element(By.ID, f"gen_{idx}_nm").text
                        params = {
                            'query': pos
                        }
                        response = requests.get(apiEndPoint, params=params, headers=headers)
                        toJson = response.json()
                        print(toJson)
                        if not toJson['addresses']:
                            ff.write(f"{pos}\n{name}\n")
                            continue
                        info = toJson["addresses"][0]
                        f.write(f"{pos}\n{name}\n{info['x']} {info['y']}\n")
                        af.write(f"{pos}\n{name}\n{toJson}\n")
