package com.example.myapplication.classes

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ScoreJoueur (
    val temps: Int,
    val questions:Int,
): Parcelable



@Parcelize
class ListeScore(
    //liste de score par défaut
    var listeScore:MutableList<ScoreJoueur> = mutableListOf(
        ScoreJoueur(0,0),
        ScoreJoueur(0,1),
        ScoreJoueur(0,2),
        ScoreJoueur(0,3),
        )

):Parcelable

fun ListeScore.trierListeScore() {
    //on utilise  tri bulles
    val n = listeScore.size
    val scores = listeScore
    for (i in 0 until n - 1) {
        for (j in 0 until n - 1 - i) {
            val score1 = scores[j]
            val score2 = scores[j + 1]

            // tri en fonction du nombre de bonnes réponses :décroissant
            if (score1.questions < score2.questions) {
                // echange des éléments
                val temp = scores[j]
                scores[j] = scores[j + 1]
                scores[j + 1] = temp
            }
            // si le nombre de bonnes réponses sont égaux, tri en fonction du temps:croissant
            else if (score1.questions == score2.questions && score1.temps > score2.temps) {
                val temp = scores[j]
                scores[j] = scores[j + 1]
                scores[j + 1] = temp
            }
        }
    }
}
