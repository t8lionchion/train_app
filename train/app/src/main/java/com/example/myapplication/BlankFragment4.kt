package com.example.myapplication

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import java.util.Calendar

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [BlankFragment4.newInstance] factory method to
 * create an instance of this fragment.
 */
class BlankFragment4 : Fragment() {
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
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_blank4, container, false)

        setupDateSpinner(view)
        setupTimeEditText(view)
        setupSearchButton(view)

        return view
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
                view.findViewById<EditText>(R.id.dateEditText3).setText(selectedDate)
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
        val dateEditText = view.findViewById<EditText>(R.id.dateEditText3)

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
                view.findViewById<EditText>(R.id.timeEditText3).setText(selectedTime)
            },
            currentHour,
            currentMinute,
            true // 是否为24小时制
        )

        val timeEditText = view.findViewById<EditText>(R.id.timeEditText3)

        timeEditText.setOnClickListener {
            timePickerDialog.show()
        }
    }
    private fun setupSearchButton(view: View) {
        val searchButton = view.findViewById<Button>(R.id.button2)
        searchButton.setOnClickListener {
            val trainText = view.findViewById<EditText>(R.id.trainText3).text.toString()
            val dateText = view.findViewById<EditText>(R.id.dateEditText3).text.toString()
            val timeText = view.findViewById<EditText>(R.id.timeEditText3).text.toString()

            // 检查输入是否为空
            if (trainText.isNotEmpty() && dateText.isNotEmpty() && timeText.isNotEmpty()) {
                // 创建一个包含要传递的数据的 Intent
                val intent = Intent(requireContext(), MainActivity2::class.java).apply {
                    putExtra("train_number", trainText)
                    putExtra("date", dateText)
                    putExtra("time", timeText)
                }
                // 启动 MainActivity2
                startActivity(intent)
            } else {
                // 如果输入为空，显示提示信息
                Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment BlankFragment4.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            BlankFragment4().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}