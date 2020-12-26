package com.example.endapp.ui.weather

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.endapp.R
import kotlinx.android.synthetic.main.fragment_weather.*

class WeatherFragment : Fragment() {

    companion object {
        fun newInstance() = WeatherFragment()
    }

    private lateinit var viewModel: WeatherViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_weather, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        lateinit var viewModel: WeatherViewModel
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProvider(this,ViewModelProvider.AndroidViewModelFactory(requireActivity().application)).get(WeatherViewModel::class.java)
        viewModel.cities.observe(viewLifecycleOwner, Observer {
            val cities = it
            val adapter = ArrayAdapter<City>(requireActivity(),android.R.layout.simple_list_item_1,cities)
            listView.adapter = adapter
            //引入相应事件 跳转
            listView.setOnItemClickListener { _, _, position, _ ->
                val cityCode = cities[position].city_code
                val intent = Intent(requireActivity(),CityMainActivity::class.java)
                intent.putExtra("city_code",cityCode)//参数
                startActivity(intent)//启动第二个界面
            }
        })
    }
}