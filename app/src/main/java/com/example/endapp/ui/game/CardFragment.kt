package com.example.endapp.ui.game

import android.content.res.Configuration
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.example.endapp.R
import com.example.endapp.ui.game.model.CardMatchingGame
import kotlinx.android.synthetic.main.fragment_card.*

class CardFragment : Fragment() {

    companion object {
        fun newInstance() = CardFragment()
        var game: CardMatchingGame = CardMatchingGame(24)
    }



    lateinit var adapter: CardAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_card, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // TODO: Use the ViewModel
        adapter = CardAdapter(game)
        recyclerView.adapter = adapter

        val configure = resources.configuration
        if(configure.orientation == Configuration.ORIENTATION_PORTRAIT){
            recyclerView.layoutManager = GridLayoutManager(requireActivity(),4)
        }else{
            recyclerView.layoutManager = GridLayoutManager(requireActivity(),6)
        }

        adapter.setOnClickListener {
            game.chooseCardAtIndex(it)
            updateUI()
        }
        updateUI()

        reset.setOnClickListener {
            game.resert()
            updateUI()
        }
    }
    fun updateUI(){
        adapter.notifyDataSetChanged()
        score.text = String.format("%s%d","Score:",game.score)
        score.text = "Score:" + game.score
    }
}