import requests

api_key = "123456789"
api_url = 'http://10.82.0.10:5000/train_info?train_number=112'
headers = {'API-Key': api_key}

response = requests.get(api_url, headers=headers)

if response.status_code == 200:
    data = response.json()
    print(data)
else:
    print("Failed to fetch train info. Status code:", response.status_code)
