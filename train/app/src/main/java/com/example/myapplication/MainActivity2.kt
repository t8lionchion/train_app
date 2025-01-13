package com.example.myapplication
import java.util.concurrent.TimeUnit
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class MainActivity2 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        // 获取传递过来的数据
        val trainNumber = intent.getStringExtra("train_number")


        val date = intent.getStringExtra("date")
        val time = intent.getStringExtra("time")
        findViewById<TextView>(R.id.trainNumberTextView).text = "車次: $trainNumber"
        findViewById<TextView>(R.id.date12).text = "當日日期: $date"
        findViewById<TextView>(R.id.timeTextView).text = "時間: $time"

        // 请求指定火车车次的信息
        fetchTrainInfo(trainNumber)
    }

    private fun fetchTrainInfo(trainNumber: String?) {
        val client = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
        val api_key = "123456789"  // 添加API密钥

        // 检查 trainNumber 是否为空
        val trainNumberParam = if (trainNumber != null) "train_number=$trainNumber" else ""
        Log.d("MainActivity2", "URL Parameter: $trainNumberParam")  // 输出日志
        val url = "http://10.82.0.10:5000/train_info?$trainNumberParam"
        val request = Request.Builder()
            .url(url)
            .addHeader("API-Key", api_key)  // 添加API密钥到请求头
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                response.body?.let {
                    val jsonData = it.string()
                    val jsonObject = JSONObject(jsonData)
                    Log.d("MainActivity2", "JSON Data: $jsonData")
                    runOnUiThread {
                        updateUI(jsonObject)
                    }
                }
            }
        })
    }

    private fun updateUI(jsonObject: JSONObject) {
        try {
            val trainNumberTextView = findViewById<TextView>(R.id.trainNumberTextView)
            trainNumberTextView.text = "車次: ${jsonObject.getInt("train_number")}"

            val vehiclesTextView = findViewById<TextView>(R.id.VehiclesTextView)
            vehiclesTextView.text = "車種: ${jsonObject.getString("vehicles")}"

            val stops = jsonObject.getJSONArray("stops")
            Log.d("MainActivity2", "Number of stops: ${stops.length()}")

            val recyclerView = findViewById<RecyclerView>(R.id.stop_station)
            recyclerView.layoutManager = LinearLayoutManager(this)
            val stations = ArrayList<String>()
            val times = ArrayList<String>()
            for (i in 0 until stops.length()) {
                val stop = stops.getJSONObject(i)
                val station = stop.getString("station")
                val arriveTime = stop.getString("arrive_time")
                stations.add(station)
                times.add(arriveTime)
            }
            recyclerView.adapter = StopsAdapter(stations, times)
        } catch (e: Exception) {
            Log.e("MainActivity2", "Error updating UI", e)
        }
    }


    class StopsAdapter(private val stations: List<String>, private val times: List<String>) :
        RecyclerView.Adapter<StopsAdapter.ViewHolder>() {

        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val stationTextView: TextView = itemView.findViewById(R.id.stationTextView)
            val arriveTimeTextView: TextView = itemView.findViewById(R.id.arriveTimeTextView)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.stop_item, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.stationTextView.text = stations[position]
            holder.arriveTimeTextView.text = times[position]
        }

        override fun getItemCount(): Int {
            return stations.size
        }
    }
}
