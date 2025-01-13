from flask import Flask, request, jsonify
import mysql.connector

app = Flask(__name__)

# API密鑰
API_KEY = "a123456789"

# 連到資料庫
db = mysql.connector.connect(
    host="localhost",
    user="root",
    password="b19980906",
    database="Date_train"
)
cursor = db.cursor()

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
    station = request.args.get('station')
    time = request.args.get('time')

    try:
        # 使用 SELECT * FROM 的方式查找資料庫資料
        query = f"SELECT table1.train_number, table1.Vehicles, table2.start_station, \
                  table2.start_time, table2.end_station, table2.end_time \
                  FROM table1 \
                  JOIN table3 ON table1.train_number = table3.train_number \
                  JOIN table2 ON table1.train_number = table2.train_number \
                  WHERE table3.station = '{station}' AND table3.arrive_time >= '{time}'"
        
        cursor.execute(query)
        train_info = cursor.fetchall()

        result = []
        for row in train_info:
            result.append({
                'train_number': row[0],
                'Vehicles': row[1],
                'start_station': row[2],
                'start_time': str(row[3]),
                'end_station': row[4],
                'end_time': str(row[5])
            })

        return jsonify(result)
    except Exception as e:
        return jsonify({'error': str(e)}), 500

if __name__ == '__main__':
    app.run(debug=True, host='0.0.0.0', port=5001) 
