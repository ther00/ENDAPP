package com.example.endapp.ui.game.model

class Card(private var suit: String, private var rank: String, var isChosen:Boolean = false, var isMatched:Boolean = false)  {
    /*suit:花色 rank:牌面大小 isChosen:牌面受否翻过来 isMatched:为真时表示该牌不需要再被处理*/
    companion object {//伴随对象
        val rankStrings= arrayOf("A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K")
        val validSuits = arrayOf("♥", "♦", "♠", "♣")
    }

    override fun toString(): String {
        return suit + rank
    }

    fun match(otherCards: Array<Card>): Int {//匹配方法
        var score = 0
        if (otherCards.size == 1) {//是否是一张牌
            val otherCard = otherCards[0]//取出
            if (otherCard.rank == rank) {//比较大小
                score = 4//分数 应该配置参数，而不是直接写死
            } else if (otherCard.suit == suit) {//比较花色
                score = 1//分数
            }
        }
        return score
    }
}