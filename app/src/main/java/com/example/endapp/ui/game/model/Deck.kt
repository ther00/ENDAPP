package com.example.endapp.ui.game.model

import java.util.Random

class Deck() {//一副牌
    private val cards = mutableListOf<Card>()//私有的 只能赋值一次
    private val r = Random()//随机数
    init {//构造函数
        for (suit in Card.validSuits) {//构造52张牌
            for (rank in Card.rankStrings) {//穷举所有牌面
                val card = Card(suit=suit,rank=rank)
                cards.add(card)
            }
        }
    }

    fun drawRandomCard(): Card? {//从里面随机抽牌
        var randomCard: Card? = null
        if (cards.size > 0) {//只要还有剩余
            randomCard = cards.removeAt(r.nextInt(cards.size))//生成牌面大小的随机数 移除
        }
        return randomCard
    }
}