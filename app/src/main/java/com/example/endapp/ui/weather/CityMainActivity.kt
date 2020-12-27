package com.example.endapp.ui.weather

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.endapp.R
import com.example.endapp.ui.weather.we.Forecast
import com.example.endapp.ui.weather.we.Weather
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_city_main.*

class CityMainActivity : AppCompatActivity() {
    val baseURL = "http://t.weather.itboy.net/api/weather/city/"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_city_main)
        val cityCode = intent.getStringExtra("city_code")//拿到参数
        //获取网络
        val queue = Volley.newRequestQueue(this)//访问网络 volley自动会启动多线程
        //构造一个请求
        val stringRequest = StringRequest(baseURL+cityCode,{
            val gson = Gson()//声明gson对象进行解析
            //对字符串做解析
            val WeatherType = object :TypeToken<Weather>() {}.type //类
            val weather = gson.fromJson<Weather>(it,WeatherType) //返回对象
            textView_city.text = weather.cityInfo.city //城市
            textView_province.text = weather.cityInfo.parent //省会
            textView_wendu.text = weather.data.wendu //温度
            textView_shidu.text = weather.data.shidu //湿度
            val firstDay = weather.data.forecast.first() //显示第一天的天气
            when(firstDay.type){ //图片
                "晴" -> imageView.setImageResource(R.drawable.sun)
                "阴" -> imageView.setImageResource(R.drawable.cloud)
                "多云" -> imageView.setImageResource(R.drawable.mcloud)
                "阵雨" -> imageView.setImageResource(R.drawable.rain)
                else -> imageView.setImageResource(R.drawable.thunder)
            }
            //适配器
            val adapter = ArrayAdapter<Forecast>(this,android.R.layout.simple_list_item_1,weather.data.forecast)
            listView.adapter = adapter

            Log.d("","${weather.cityInfo.city} ${weather.cityInfo.parent}")
        },{
            Log.d("","$it")
        })
        queue.add(stringRequest)//加入字符串请求队列
    }
}