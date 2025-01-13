from datetime import datetime, date
import json
import requests
import mysql.connector

mydb = mysql.connector.connect(
    host="localhost",
    user="root",
    password="b19980906",
    database="Date_train"
)


def insert_train_schedule(train_schedule_data, db_connection):
    cursor = db_connection.cursor()

    for train in train_schedule_data:
        train_number = train['TrainInfo']['TrainNo']
        start_station = train['TrainInfo']['StartingStationName']['Zh_tw']
        start_time = train['StopTimes'][0]['ArrivalTime']
        end_station = train['TrainInfo']['EndingStationName']['Zh_tw']
        end_time = train['StopTimes'][-1]['DepartureTime']

        # 插入 table1
        insert_table1_query = "INSERT INTO table1 (train_number, Vehicles) VALUES (%s, %s)"
        cursor.execute(insert_table1_query, (train_number, train['TrainInfo']['TrainTypeName']['Zh_tw']))

        # 插入 table2
        insert_table2_query = "INSERT INTO table2 (train_number, start_station, start_time, end_station, end_time) VALUES (%s, %s, %s, %s, %s)"
        cursor.execute(insert_table2_query, (train_number, start_station, start_time, end_station, end_time))

        # 插入 table3
        for stop in train['StopTimes']:
            station = stop['StationName']['Zh_tw']
            arrive_time = stop['ArrivalTime']
            stop_sequence = stop['StopSequence']
            insert_table3_query = "INSERT INTO table3 (train_number, station, arrive_time, StopSequence) VALUES (%s, %s, %s, %s)"
            cursor.execute(insert_table3_query, (train_number, station, arrive_time, stop_sequence))

    db_connection.commit()


def delete_previous_data(cursor):
    try:
        # 關閉外鍵約束
        cursor.execute("SET FOREIGN_KEY_CHECKS=0")

        # 刪除table2中的資料
        cursor.execute("DELETE FROM table2")

        # 刪除table3中的資料
        cursor.execute("DELETE FROM table3")

        # 刪除table1中的資料
        cursor.execute("DELETE FROM table1")

        # 開啟外鍵約束
        cursor.execute("SET FOREIGN_KEY_CHECKS=1")

        # 提交操作
        mydb.commit()

        return True
    except Exception as e:
        print("刪除資料時出現錯誤:", e)
        return False


def get_access_token(client_id, client_secret):
    # 設置TDX Token URL
    token_url = 'https://tdx.transportdata.tw/auth/realms/TDXConnect/protocol/openid-connect/token'

    # 設置TDX payload參數
    payload = {
        'grant_type': 'client_credentials',
        'client_id': 's0922123-26cba480-9748-42e7',
        'client_secret': 'd4219be5-f1b3-45fc-9353-8e3abf29b881'
    }

    # 發送POST請求獲取訪問令牌
    response = requests.post(token_url, data=payload)

    # 檢查是否成功獲取訪問令牌
    if response.status_code == 200:
        return response.json()['access_token']
    else:
        print("獲取訪問令牌失敗:", response.status_code)
        return None


def fetch_train_schedule(access_token):
    # 設置API URL #可以設置一次資料量$top='你需要的量'
    api_url = 'https://tdx.transportdata.tw/api/basic/v3/Rail/TRA/DailyTrainTimetable/Today?%24top=1500&%24format=JSON'

    # 設置header
    headers = {'Authorization': 'Bearer ' + access_token}

    # 發送GET請求獲取API數據
    response = requests.get(api_url, headers=headers)

    # 檢查是否成功獲取API數據
    if response.status_code == 200:
        return response.json()
    else:
        print("獲取API數據失敗:", response.status_code)
        return None


def main():
    # 刪除先前的資料
    mycursor = mydb.cursor()
    if delete_previous_data(mycursor):
        print("之前資料已刪除")
    else:
        print("失敗")
    # 發送API請求並獲取資料
    access_token = get_access_token('your_client_id', 'your_client_secret')
    if access_token:
        train_schedule_data = fetch_train_schedule(access_token)
        if train_schedule_data:
            # 插入資料到資料庫
            insert_train_schedule(train_schedule_data['TrainTimetables'], mydb)
            print("資料已同步到資料庫")
        else:
            print("無法獲取API資料")
    else:
        print("無法獲取訪問令牌")


if __name__ == "__main__":
    main()
