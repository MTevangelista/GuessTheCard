package com.bcampa.guessthecard

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.random.Random

// todo: add credits
// todo: consider making a better icon
// todo: translate
class MainActivity : AppCompatActivity() {

    var ace: Int? = null
    var selectedCard: Int? = null
    var cards = mutableListOf<ImageView>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        cards.addAll(listOf(imgVwCard1, imgVwCard2, imgVwCard3))
        shuffleCard()

        btnConfirm.setOnClickListener {
            if(selectedCard != null) {
                showResult()
            }
            else {
                Toast.makeText(this, getString(R.string.no_card_selected), Toast.LENGTH_SHORT).show()
            }
        }

        btnRetry.setOnClickListener{ reset() }
    }

    private fun shuffleCard() {
        ace = when (Random.nextInt(1, 4)) {
            1 -> R.id.imgVwCard1
            2 -> R.id.imgVwCard2
            3 -> R.id.imgVwCard3
            else -> R.id.imgVwCard1
        }
    }

    fun selectCard(v: View) {
        selectedCard = v.id
        v.alpha = 0.6F
        cards.forEach {
            if(it != v) {
                it.alpha = 1F
            }
        }
    }

    private fun showResult() {
        val correct = ace == selectedCard
        if(correct) {
            txtVwMessage.text = getString(R.string.victory_message)
        }
        else {
            txtVwMessage.text = getString(R.string.loss_message)
        }
        cards.forEach {
            it.isClickable = false
            if(it.id == selectedCard) {
                it.alpha = 1F
                val drawable = if (correct) R.drawable.card_ace else R.drawable.card_joker
                rotateCard(it, drawable, false)
            }
        }
        txtVwMessage.visibility = View.VISIBLE
        btnRetry.visibility = View.VISIBLE
        btnConfirm.isClickable = false
    }

    private fun reset() {
        cards.forEach {
            it.isClickable = true
            if(it.id == selectedCard) {
                it.alpha = 1F
                rotateCard(it, R.drawable.card_back, true)
            }
        }
        selectedCard = null
        shuffleCard()
        txtVwMessage.visibility = View.INVISIBLE
        btnRetry.visibility = View.INVISIBLE
        btnConfirm.isClickable = true
    }

    private fun rotateCard(img: ImageView, drawable: Int, reverse: Boolean) {
        val angle = if (reverse) -90F else 90F
        img.animate().rotationYBy(angle).setDuration(300).withEndAction {
            img.setImageResource(drawable)
            img.rotationY = if (reverse) 90F else 270F
            img.animate().rotationYBy(angle).setDuration(300).withEndAction {
                img.rotationY = 0F
            }
        }
    }
}
