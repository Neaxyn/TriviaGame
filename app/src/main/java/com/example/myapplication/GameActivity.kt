package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.classes.*


class GameActivity : AppCompatActivity() {

    private lateinit var receiver: QuestionReponseReceiver
    private lateinit var plateau: PlateauDeJeu
    private lateinit var btnReponse1: Button
    private lateinit var btnReponse2: Button
    private lateinit var btnReponse3: Button
    private lateinit var btnReponse4: Button
    private lateinit var questionText: TextView
    lateinit var tempsText: TextView
    private lateinit var progresText: TextView
    private lateinit var limcoinTextView: TextView
    private lateinit var joueur : Joueur
    lateinit var timer: CountDownTimer
    var duree = 60000
    var tempsSave : Int = 0
    var nextLevel : Boolean = false
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.game_activity)

        plateau = PlateauDeJeu()
        joueur = intent.getParcelableExtra("joueur",Joueur::class.java)!!

        receiver = QuestionReponseReceiver { question ->
            plateau.questionReponse = question // Affecter la question reçue au plateau de jeu des qu'on fait appel à la service
            majPlateau()
        }
        val filter = IntentFilter("com.example.myapplication.QUESTION")
        registerReceiver(receiver, filter, RECEIVER_NOT_EXPORTED)



        questionText = findViewById(R.id.textViewQuestion)

        //initialisation des boutons
        btnReponse1 = findViewById(R.id.buttonReponse1)
        btnReponse1.setOnClickListener {
            verifierReponse(plateau.questionReponse!!, btnReponse1.text.toString())
        }

        btnReponse2 = findViewById(R.id.buttonReponse2)
        btnReponse2.setOnClickListener {
            verifierReponse(plateau.questionReponse!!, btnReponse2.text.toString())
        }

        btnReponse3 = findViewById(R.id.buttonReponse3)
        btnReponse3.setOnClickListener {
            verifierReponse(plateau.questionReponse!!, btnReponse3.text.toString())
        }

        btnReponse4 = findViewById(R.id.buttonReponse4)
        btnReponse4.setOnClickListener {
            verifierReponse(plateau.questionReponse!!, btnReponse4.text.toString())
        }
        progresText = findViewById(R.id.proges)
        tempsText = findViewById(R.id.temps)


        lancerTimer(duree)

        limcoinTextView = findViewById(R.id.limcoins)
        limcoinTextView.text = joueur.limcoins.toString()


        // Lancer le service au démarrage de l'activité
        val intent = Intent(this, TriviaService::class.java)
        startService(intent)


    }

    override fun onDestroy() {//arreter le service et le receiver des qu'on quitte l'activité
        unregisterReceiver(receiver)
        val serviceIntent = Intent(this, TriviaService::class.java)
        stopService(serviceIntent)
        super.onDestroy()
    }

    private fun majPlateau() {// mise a jour du plateau
        btnReponse1.text = plateau.questionReponse?.reponse1
        btnReponse2.text = plateau.questionReponse?.reponse2
        btnReponse3.text = plateau.questionReponse?.reponse3
        btnReponse4.text = plateau.questionReponse?.reponse4
        questionText.text = plateau.questionReponse?.question
        progresText.text = plateau.progression.toString()
    }

    private fun verifierReponse(questionReponse: QuestionReponse, reponse: String) {
        if (questionReponse.verifReponse(reponse)) {//affichage
            Toast.makeText(this, "Bonne réponse!", Toast.LENGTH_SHORT).show()
            plateau.progression ++
            majGame()
            // Lancer le service pour une nouvelle question
            val intent = Intent(this, TriviaService::class.java)
            startService(intent)
        } else {
            Toast.makeText(this, "Mauvaise réponse!", Toast.LENGTH_SHORT).show()
            perdu()
        }
    }

    fun perdu(){//fonction a appeler dans le cas ou le joueur a timeOut ou cliqué sur la mauvaise réponse
        Toast.makeText(this, "Vous avez perdu!", Toast.LENGTH_SHORT).show()

        //on ajoute dans la liste du score le temps et le progres du joueur
        val scoreSave = ScoreJoueur(tempsSave/1000,plateau.progression)
        joueur.scoreJ.listeScore.add(scoreSave)
        //on arrete le timer
        timer.cancel()

        //on retourne a l'activité
        val intent = Intent(this,ModeActivity::class.java)
        intent.putExtra("joueur",joueur)
        startActivity(intent)


    }
    private fun majGame(){
        if (plateau.progression % 11 == 0){ //on verifie si la progression du joueur est  11

            nextLevel = true
            joueur.ajouterLimcoins(200)
            limcoinTextView.text = joueur.limcoins.toString()

            //on arrete le timer et on reduit le temps de 10 secondes et on le relance
            tempsSave -=10000
            timer.cancel()
            lancerTimer(tempsSave)


            //reset de la liste des questions dans le service
            val intentService = Intent(this, TriviaService::class.java)

            intentService.putExtra("resetList",true)//comme le service utilise un liste qui se vide, on le reset
            startService(intentService)
        }
        else if (plateau.progression  % 3 == 0){
            joueur.ajouterLimcoins(30)
            limcoinTextView.text = joueur.limcoins.toString()
        }

    }

    private fun lancerTimer(duree: Int){
        timer = object: CountDownTimer(duree.toLong(), 1000) {//lancement du timer
        override fun onTick(temps: Long) {

            tempsSave = temps.toInt()
            // Convertir les millisecondes restantes en minutes et secondes
            val minutes = temps / 1000 / 60
            val seconds = temps / 1000 % 60

            // Mettre à jour le texte du TextView
            tempsText.text =  String.format("%02d:%02d", minutes, seconds)
        }
            override fun onFinish() {//si le timer est fini le joueur a perdu et on arrete
                perdu()
                timer.cancel()
            }

        }
        timer.start()//lancer le timer
    }
}
