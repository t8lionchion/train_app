import requests

# API端点
API_ENDPOINT = 'http://10.82.0.10:5002/api/search'

# API密钥
API_KEY = 'b123456789'

# 测试数据
data = {
    'start_station': '板橋',
    'end_station': '彰化',
    'specified_time': '04:00'
}

# 设置请求头
headers = {'API-Key': API_KEY}

# 发送POST请求
response = requests.post(API_ENDPOINT, json=data, headers=headers)

# 打印响应
print(response.json())
