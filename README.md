# 🚆 火車查詢系統

一個能查詢 **火車路線、車站與車次時刻表** 的系統。
本專案以 **Python + MySQL** 為後端，並搭配 **Kotlin** 開發簡易手機應用程式，串接 API 提供查詢功能。

---

## 📌 功能特色

* 🔍 **火車路線查詢**：提供完整路線資訊
* 🏢 **車站查詢**：搜尋車站相關資訊
* ⏱️ **車次查詢**：查詢即時時刻表與車次資訊
* 📱 **手機應用整合**：以 Kotlin 開發 Android App 串接後端 API

---

## 🛠️ 技術架構

* **後端**：Python（商業邏輯與 API 開發）
* **資料庫**：MySQL（資料儲存與管理）
* **前端 / 手機端**：Kotlin Android App
* **API**：RESTful API 串接後端與手機應用

---

## 📂 專案結構

```
train/
│── API/        # Python 後端 (商業邏輯與 API)
│── API_Test/        # Python 後端 api測試
│── DbMs/       # SQL 腳本與資料庫設計
│── train/     # Kotlin Android 手機應用
│── README.md       # 專案說明文件
```

---

## 🚀 使用方式

### 1️⃣ 下載專案

```bash
git clone https://github.com/your-username/train-app.git
cd train-app
```

### 2️⃣ 建立資料庫

* 將 `schema.sql` 匯入 MySQL

```bash
mysql -u root -p < database/schema.sql
```

### 3️⃣ 啟動後端

```bash
cd backend
python train_number_search_api.py
python train_route_search_api.py
python Use_stop_search_train_nuber_api.py
```

### 4️⃣ 執行手機應用

* 使用 Android Studio 開啟 `mobile-app/`
* 將 API endpoint 設定為後端伺服器網址
* 執行於模擬器或實體裝置

---

## 📊 系統設計

* 資料庫 ER-Model（可放圖在 `/docs/er-diagram.png`）
* API 文件（可補充 Swagger / Postman Collection）

---

## 📜 授權

本專案僅作為 **學習與練習用途**。

---

