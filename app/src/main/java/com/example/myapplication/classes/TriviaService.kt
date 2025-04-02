package com.example.myapplication.classes

import android.app.Service
import android.content.Intent
import android.os.IBinder
import kotlin.random.Random

class TriviaService : Service() {
    private var liste = ListeQuestions()
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        if (intent?.getBooleanExtra("resetList", false) == true) {
            liste = ListeQuestions() // Réinitialiser la Liste de qustions si on doit reset
        }else {

            //on tire un nombre au hasard entre 0 et la taille de la liste qui sera l'indice à retirer
            // ensuite on retire la question grace cette indice
            //et on envoie sous forme de broadcast la question
            val max = liste.listeQuestionsReponses.size
            val randomNumber = Random.nextInt(max)
            val question = liste.tirageNumero(randomNumber)
            liste.listeQuestionsReponses.remove(question)
            val broadcastIntent = Intent("com.example.myapplication.QUESTION")
            broadcastIntent.putExtra("question", question)
            sendBroadcast(broadcastIntent)

        }
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }
}
