from flask import Flask, request, jsonify
import mysql.connector
from requests.exceptions import RequestException
import datetime

app = Flask(__name__)

# 連結資料庫
mydb = mysql.connector.connect(
    host="localhost",
    user="root",
    password="b19980906",
    database="Date_train"
)
mycursor = mydb.cursor()


# API密鑰
API_KEY = "b123456789"
# 認證金鑰器
def require_api_key(view_function):
    def decorated_function(*args, **kwargs):
        if request.headers.get('API-Key') and request.headers.get('API-Key') == API_KEY:
            return view_function(*args, **kwargs)
        else:
            return jsonify({'error': 'Unauthorized access'}), 401
    return decorated_function


@app.route('/api/search', methods=['POST'])
@require_api_key
def search_train():
    try:
        # 獲取前端參數
        start_station = request.json.get('start_station')
        end_station = request.json.get('end_station')
        specified_time = request.json.get('specified_time')
        
        # 查詢資料庫
        query = """
            SELECT t1.train_number, t1.Vehicles, t3.station AS start_station, t3.arrive_time AS start_time,
                   t3_2.station AS end_station, t3_2.arrive_time AS end_time
            FROM table3 t3
            INNER JOIN table3 t3_2 ON t3.train_number = t3_2.train_number
            INNER JOIN table1 t1 ON t1.train_number = t3.train_number
            WHERE t3.station = %s
            AND t3_2.station = %s
            AND t3.arrive_time < t3_2.arrive_time
            AND t3.StopSequence < t3_2.StopSequence
            AND t3.arrive_time >= %s
        """
        mycursor.execute(query, (start_station, end_station, specified_time))

        results = mycursor.fetchall()
        
        # 返回結果
        response = []
        for row in results:
            start_time = (datetime.datetime.now().replace(hour=0, minute=0, second=0) + row[3]).strftime('%H:%M')
            end_time = (datetime.datetime.now().replace(hour=0, minute=0, second=0) + row[5]).strftime('%H:%M')
            
            response.append({
                'train_number': row[0],
                'start_station': start_station,
                'start_time': start_time,
                'end_station': end_station,
                'end_time': end_time
            })
                
        return jsonify(response)
    
    except RequestException as e:
        return jsonify({'error': str(e)}), 500
    
if __name__ == '__main__':
    app.run(debug=True, host='0.0.0.0', port=5002) 
