package com.example.endapp.ui.game.model

class CardMatchingGame(val count: Int)  {
    var score = 0//分值
        private set//外面不能改变

    val cards: MutableList<Card>//初始化牌

    init {
        val deck = Deck()//初始化牌
        cards = mutableListOf()//可以改变
        for (i in 1..count) {
            val card: Card? = deck.drawRandomCard()
            if (card != null) {
                cards.add(card)
            }
        }
    }

    fun resert() {
        val deck = Deck()//初始化牌
        score = 0
        cards.clear()//可以改变
        for (i in 1..count) {
            val card: Card? = deck.drawRandomCard()
            if (card != null) {
                cards.add(card)
            }
        }
    }
    fun cardAtIndex(index: Int): Card {
        return cards.get(index)//数组 下标从0开始
    }

    val MISMATCH_PENALTY = 2
    val MATCH_BONUS = 4
    val COST_TO_CHOOSE = 1

    fun chooseCardAtIndex(index: Int) {//游戏逻辑 传牌
        val card= cardAtIndex(index)//取出这张牌
        if (!card.isMatched) {//是否已经被匹配
            if (card.isChosen) {//是否翻牌
                card.isChosen = false
            } else {
                for (otherCard in cards) {//检查是否有另一张牌已经被翻起来了
                    if (otherCard.isChosen && !otherCard.isMatched) {//匹配
                        val matchScore = card.match(arrayOf(otherCard))
                        if (matchScore > 0) {//加分
                            score += matchScore * MATCH_BONUS
                            otherCard.isMatched = true//匹配
                            card.isMatched = true//匹配
                        } else {//扣分
                            score -= MISMATCH_PENALTY
                            otherCard.isChosen = false//另一张牌翻过去
                        }
                        break//跳出for循环
                    }
                }
                score -= COST_TO_CHOOSE//扣分
                card.isChosen = true//翻牌
            }
        }
    }
}