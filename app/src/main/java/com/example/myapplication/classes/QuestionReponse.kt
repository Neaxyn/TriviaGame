package com.example.myapplication.classes
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class QuestionReponse(//classe pour les questions et reponses associé
    val question: String,
    val reponse1: String,
    val reponse2: String,
    val reponse3: String,
    val reponse4: String,
    val vraiReponse: String,
) : Parcelable

{
    // Méthode pour vérifier si la réponse est correcte
    fun verifReponse(reponse: String): Boolean {
        return reponse == vraiReponse
    }
}

