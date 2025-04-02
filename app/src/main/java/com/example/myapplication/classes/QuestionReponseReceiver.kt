package com.example.myapplication.classes

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class QuestionReponseReceiver( val onReceive: (QuestionReponse) -> Unit) : BroadcastReceiver() {
    //receiver du QuestionReponse passé dans un service
    override fun onReceive(context: Context, intent: Intent) {
        val question = intent.getParcelableExtra("question", QuestionReponse::class.java)

        if (question != null) {//on verifie si la question n'est pas vide pour permettre l'envoi
            onReceive(question)
        }

    }
}