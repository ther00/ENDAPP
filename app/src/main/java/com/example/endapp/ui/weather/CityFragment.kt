package com.example.endapp.ui.weather

import android.content.Context
import android.content.Intent
import android.content.Intent.EXTRA_CHOOSER_TARGETS
import android.content.Intent.getIntentOld
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.android.volley.toolbox.Volley.newRequestQueue
import com.example.endapp.R
import com.example.endapp.ui.weather.we.Forecast
import com.example.endapp.ui.weather.we.Weather
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.fragment_city.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CityFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CityFragment : Fragment() {
    val baseURL = "http://t.weather.itboy.net/api/weather/city/"
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        val cityCode = getIntentOld("city_code")
        //获取网络
        val queue = Volley.newRequestQueue(this.context)
        //构造一个请求
        val stringRequest = StringRequest(baseURL+cityCode,{
            val gson = Gson()
            //value会自动启动多线程
            //对字符串做解析
            val WeatherType = object : TypeToken<Weather>() {}.type
            val weather = gson.fromJson<Weather>(it,WeatherType)
            textView_city.text = weather.cityInfo.city
            textView_province.text = weather.cityInfo.parent
            textView_wendu.text = weather.data.wendu
            textView_shidu.text = weather.data.shidu
            val firstDay = weather.data.forecast.first()
            when(firstDay.type){
                "晴" -> imageView.setImageResource(R.drawable.sun)
                "阴" -> imageView.setImageResource(R.drawable.cloud)
                "多云" -> imageView.setImageResource(R.drawable.mcloud)
                "阵雨" -> imageView.setImageResource(R.drawable.rain)
                else -> imageView.setImageResource(R.drawable.thunder)
            }
            //适配器
            //val adapter = ArrayAdapter<Forecast>(this,android.R.layout.simple_list_item_1,weather.data.forecast)
            //listView.adapter = adapter

            Log.d("WeatherFragment","${weather.cityInfo.city} ${weather.cityInfo.parent}")
        },{
            Log.d("CityFragment","$it")
        })
        queue.add(stringRequest)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_city, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CityFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CityFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}