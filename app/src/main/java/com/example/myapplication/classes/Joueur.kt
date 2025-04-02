package com.example.myapplication.classes

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Joueur(var pseudo: String =" defaut", var limcoins: Int = 0, var scoreJ: ListeScore = ListeScore()) : Parcelable {

    fun ajouterLimcoins(amount: Int) {
        limcoins += amount
    }

    fun retirerLimcoins(amount: Int) {
        limcoins -= amount
    }


}