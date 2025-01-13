from flask import Flask, request, jsonify
import os
import mysql.connector

app = Flask(__name__)

# 設置API密鑰
API_KEY = "123456789"

# 連結資料庫
db = mysql.connector.connect(
    host="localhost",
    user="root",
    password="b19980906",
    database="Date_train"
)

# 認證金鑰器
def require_api_key(view_function):
    def decorated_function(*args, **kwargs):
        if request.headers.get('API-Key') and request.headers.get('API-Key') == API_KEY:
            return view_function(*args, **kwargs)
        else:
            return jsonify({'error': 'Unauthorized access'}), 401
    return decorated_function

@app.route('/train_info', methods=['GET'])
@require_api_key
def get_train_info():
    train_number = request.args.get('train_number')
    
    # 查詢車次訊息
    cursor = db.cursor(dictionary=True)
    cursor.execute("SELECT * FROM table1 WHERE train_number = %s", (train_number,))
    train_info = cursor.fetchone()
    #如果沒有資料回傳為空值
    if not train_info:
        return jsonify({}), 200

    
    # 查詢起始站和終點站訊息
    cursor.execute("SELECT * FROM table2 WHERE train_number = %s", (train_number,))
    station_info = cursor.fetchall()
    
    # 查詢途經站點訊息，並按照stopsequence進行排序
    cursor.execute("SELECT * FROM table3 WHERE train_number = %s ORDER BY StopSequence", (train_number,))
    stops_info = cursor.fetchall()
    
    # 查詢車輛訊息
    vehicles = train_info['Vehicles']
    
    # 構造返回數據
    response = {
        'train_number': train_info['train_number'],
        'vehicles': vehicles,
        'start_station': station_info[0]['start_station'],
        'start_time': str(station_info[0]['start_time']),
        'end_station': station_info[0]['end_station'],
        'end_time': str(station_info[0]['end_time']),
        'stops': [{'station': stop['station'], 'arrive_time': str(stop['arrive_time'])} for stop in stops_info]
    }
    
    # 返回資料顯示
    print("API返回数据:", response)
    
    return jsonify(response)

if __name__ == '__main__':
    app.run(debug=True, host='0.0.0.0', port=5000) 
