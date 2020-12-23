package com.example.endapp.ui.weather

import androidx.lifecycle.ViewModel

import android.app.Application
import android.content.Intent
import android.util.Log
import android.widget.ArrayAdapter
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.endapp.R
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_main.*
import java.io.BufferedReader
import java.io.InputStreamReader
import kotlin.concurrent.thread

class WeatherViewModel(application: Application) : AndroidViewModel(application){
    private val _cities:MutableLiveData<List<City>> = MutableLiveData() //可以改变数据
    val cities:LiveData<List<City>> = _cities//外面可以观察到的

    init {
        thread {//多线程
            val str = readFileFromRaw(R.raw.citycode) //解析JSON文件
            val gson = Gson()
            val CityType = object : TypeToken<List<City>>(){}.type//获得一个类型 内部类
            var cts:List<City> = gson.fromJson(str,CityType)//转成列表对象 获得字符串str转成CityType
            cts = cts.filter {it.city_code != ""}//没有省份 只剩下了城市
            _cities.postValue(cts) //子线程中更新数据用postValue
        }
    }
    fun readFileFromRaw(rawName: Int): String? {
        try {
            val inputReader = InputStreamReader(getApplication<Application>().resources.openRawResource(rawName))
            val bufReader = BufferedReader(inputReader)
            var line: String? = ""
            var result: String? = ""
            while (bufReader.readLine().also({ line = it }) != null) {
                result += line
            }
            return result
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }
}