package com.example.endapp.ui.helloworld

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.endapp.R
import kotlinx.android.synthetic.main.fragment_helloworld.*

class HelloworldFragment :Fragment() {

    companion object {
        fun newInstance() = HelloworldFragment()
    }
    private lateinit var viewModel: HelloworldViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_helloworld, container, false)
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        button_hello.setOnClickListener {
            textView_hello.text = resources.getString(R.string.clicked)
        }
    }
}