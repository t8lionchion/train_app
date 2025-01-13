import requests

# 指定的車站和時間
station = '臺北'
time = '18:00'

# API端点
url = 'http://10.82.0.10:5001/train_info'

# 設置API密鑰
headers = {'API-Key': 'a123456789'}

# 傳遞參數
params = {'station': station, 'time': time}

# 發送GET請求
response = requests.get(url, headers=headers, params=params)

# 檢查響應狀態碼
if response.status_code == 200:
    # 打印API響應的JSON檔案
    print(response.json())
else:
    # 打印錯誤信息
    print('Error:', response.json())
