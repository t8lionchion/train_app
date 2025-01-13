package com.example.myapplication

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [BlankFragment5.newInstance] factory method to
 * create an instance of this fragment.
 */
class BlankFragment5 : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }
    // 聲明縣市數組及對應的車站數組 //
    private val countyList = arrayOf( "北北基", "桃園","新竹","苗栗","臺中","彰化","南投","雲林","嘉義","臺南","高雄","屏東","宜蘭","花蓮","臺東")
    private val stationMap = mapOf(
        "北北基" to listOf("基隆","三坑","八堵","七堵","百福","五堵","汐止","汐科", "南港","松山","臺北","萬華","板橋","浮洲","樹林","南樹林","山佳","鶯歌","暖暖","四腳亭",
            "八斗子","海科館","瑞芳","侯硐","三貂嶺","大華","十分","望古","嶺腳","平溪","菁桐",
            "牡丹","雙溪","貢寮","福隆").toTypedArray(),
        "桃園" to listOf("桃園","內壢","中壢","埔心","楊梅","富岡","新富").toTypedArray(),
        "新竹" to listOf("北湖","湖口","新豐","竹北",
            "內灣","富貴","合興","九讚頭","橫山","榮華","上員","六家","竹中","新莊","千甲",
            "北新竹","新竹","三姓橋","香山").toTypedArray(),
        "苗栗" to listOf("崎頂","竹南","造橋","豐富","苗栗","南勢","銅鑼","三義",
            "談文","大山","後龍","龍港","白沙屯","新埔","通霄","苑裡").toTypedArray(),
        "臺中" to listOf("泰安","后里","豐原","栗林","潭子","頭家厝","松竹","太原","經武",
            "臺中","五權","大慶","烏日","新烏日","成功",
            "日南","大甲","臺中港","清水","沙鹿","龍井","大肚","追分").toTypedArray(),
        "彰化" to listOf("彰化","花壇","大村","員林","永靖","社頭","田中","二水","源泉").toTypedArray(),
        "南投" to listOf("濁水","龍泉","集集","水里","車埕").toTypedArray(),
        "雲林" to listOf("林內","石榴","斗六","斗南","石龜").toTypedArray(),
        "嘉義" to listOf("大林","民雄","嘉北","嘉義","水上","南靖").toTypedArray(),
        "臺南" to listOf("後壁","新營","柳營","林鳳營","隆田","拔林","善化","南科","新市",
            "永康","大橋","臺南","保安","仁德","中洲","長榮大學","沙崙").toTypedArray(),
        "高雄" to listOf("大湖","路竹","岡山","橋頭","楠梓","新左營","左營","內惟","美術館",
            "鼓山","三塊厝","高雄","民族","科工館","正義","鳳山","後庄","九曲堂").toTypedArray(),
        "屏東" to listOf("六塊厝","屏東","歸來","麟洛","西勢","竹田","潮州","崁頂","南州",
            "鎮安","林邊","佳冬","東海","枋寮","加祿","內獅","枋山").toTypedArray(),
        "宜蘭" to listOf("石城","大里","大溪","龜山","外澳","頭城","頂埔","礁溪","四城","宜蘭","二結",
            "中里","羅東","冬山","新馬","蘇澳新","蘇澳","永樂","東澳","南澳","武塔","漢本").toTypedArray(),
        "花蓮" to listOf("和平","和仁","崇德","新城","景美","北埔","花蓮",
            "吉安","志學","平和","壽豐","豐田","林榮新光","南平","鳳林","萬榮","光復","大富","富源",
            "瑞穗","三民","玉里","東里","東竹","富里").toTypedArray(),
        "臺東" to listOf("池上","海端","關山","瑞和","瑞源","鹿野","山里","臺東",
            "康樂","知本","太麻里","金崙","瀧溪","大武").toTypedArray()
    )
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_blank5, container, false)

        setupCountySpinner(view)
        setupStationSpinner(view)
        setupDateSpinner(view)
        setupTimeEditText(view)
        setupSearchButton(view)
        return view
    }
    private fun setupSearchButton(view: View) {
        val searchButton = view.findViewById<Button>(R.id.button3)
        searchButton.setOnClickListener {
            val start_station = view.findViewById<Spinner>(R.id.spinner9).selectedItem.toString()
            val dateText = view.findViewById<EditText>(R.id.dateEditText).text.toString()
            val timeText = view.findViewById<EditText>(R.id.timeEditText).text.toString()
            // 检查输入是否为空
            if (start_station.isNotEmpty() && dateText.isNotEmpty() && timeText.isNotEmpty() ) {
                // 创建一个包含要传递的数据的 Intent
                val intent = Intent(requireContext(), MainActivity4::class.java).apply {
                    putExtra("start_station", start_station)
                    putExtra("date", dateText)
                    putExtra("time", timeText)
                }
                // 抵達 MainActivity4
                startActivity(intent)
            } else {
                // 如果输入为空，显示提示信息
                Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun setupCountySpinner(view: View) {
        val countySpinner: Spinner = view.findViewById(R.id.spinner7)
        val countyAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, countyList)
        countyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        countySpinner.adapter = countyAdapter

        countySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedCounty = countyList[position]
                val stations = stationMap[selectedCounty] ?: emptyArray()
                val stationSpinner: Spinner = view?.rootView?.findViewById(R.id.spinner9) ?: return
                val stationAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, stations)
                stationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                stationSpinner.adapter = stationAdapter
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }
    }
    private fun setupStationSpinner(view: View) {
        val defaultStations: Array<String> = stationMap[countyList[0]] ?: emptyArray()
        val stationSpinner: Spinner = view.findViewById(R.id.spinner9)
        val stationAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, defaultStations)
        stationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        stationSpinner.adapter = stationAdapter
    }
    private fun setupDateSpinner(view: View) {
        val calendar = Calendar.getInstance()
        val currentYear = calendar.get(Calendar.YEAR)
        val currentMonth = calendar.get(Calendar.MONTH)
        val currentDay = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                // Do something with the selected date
                val selectedDate = "$year/${month + 1}/$dayOfMonth"
                view.findViewById<EditText>(R.id.dateEditText).setText(selectedDate)
            },
            currentYear,
            currentMonth,
            currentDay
        )

        // 设置日期选择范围为 2024-2060 年
        val minYear = 2024
        val maxYear = 2060
        datePickerDialog.datePicker.minDate = calendar.apply { set(minYear, 0, 1) }.timeInMillis
        datePickerDialog.datePicker.maxDate = calendar.apply { set(maxYear, 11, 31) }.timeInMillis

        // 获取隐藏的 EditText
        val dateEditText = view.findViewById<EditText>(R.id.dateEditText)

        // 设置 EditText 点击监听器
        dateEditText.setOnClickListener {
            datePickerDialog.show()
        }
    }
    private fun setupTimeEditText(view: View) {
        val calendar = Calendar.getInstance()
        val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
        val currentMinute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            requireContext(),
            { _, hourOfDay, minute ->
                // Do something with the selected time
                val selectedTime = "$hourOfDay:$minute"
                view.findViewById<EditText>(R.id.timeEditText).setText(selectedTime)
            },
            currentHour,
            currentMinute,
            true // 是否为24小时制
        )

        val timeEditText = view.findViewById<EditText>(R.id.timeEditText)

        timeEditText.setOnClickListener {
            timePickerDialog.show()
        }
    }

        companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment BlankFragment5.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            BlankFragment5().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}