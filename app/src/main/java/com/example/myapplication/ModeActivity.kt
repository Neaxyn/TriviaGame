package com.example.myapplication
import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.example.myapplication.classes.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import android.widget.Toast

class ModeActivity : AppCompatActivity() {
    private lateinit var joueur: Joueur
    private lateinit var nb_limcoins: TextView


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mode)

        //initialisation du joueur de base pour eviter les erreur de lateinit
        joueur= Joueur()

        //initialisation des boutons
        val btnModeClassique = findViewById<Button>(R.id.btn_classic)
        btnModeClassique.setOnClickListener {
            modeClassique()
        }

        val btnVoirScore = findViewById<Button>(R.id.score_btn)
        btnVoirScore.setOnClickListener{
            voirScore()
        }

        val decoBtn = findViewById<Button>(R.id.logout)
        decoBtn.setOnClickListener{
            sauvegarderLimcoins()
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this,AuthActivity::class.java)
            startActivity(intent)
        }

        nb_limcoins = findViewById(R.id.nb_limcoins)

        //on recupere la base de donnée si le joueur s'est connecté dans AuthActivity avant
        //sinon on recuperer le parcelable
        val connect = intent.getBooleanExtra("connect",false)
        if (connect){
           recupererJoueur()

        }
        else{
            joueur = intent.getParcelableExtra("joueur",Joueur::class.java)!!
            majLimcoins()
        }


        //lancer le mode défi
        val defibtn = findViewById<Button>(R.id.defi_btn)
        defibtn.setOnClickListener {
           modeDefi()
        }

    }

    private fun majLimcoins(){//mise a jour des limcoins
        nb_limcoins.text = joueur.limcoins.toString()
    }
    private fun modeClassique(){
        if(joueur.limcoins >= 100){//vérifie si le joueur possède les limcoins pour jouer
            joueur.retirerLimcoins(100)
            val intent = Intent(this, GameActivity::class.java)
            intent.putExtra("joueur",joueur)
            startActivity(intent)
        }
        else{//proposer au joueur de lui refiller ses limcoins
            AlertDialog.Builder(this)
                .setTitle("Ruiné!!!")
                .setMessage("Vous n'avez plus de limcoins , voulez vous repartir avec 100 Limcoins?")
                .setPositiveButton("Accepter"){_,_ ->
                    refillerLimcoins()
                }
                .setNegativeButton("Annuler",null)
                .show()

        }
    }

    private fun voirScore(){
        val intent = Intent(this, ScoreActivity::class.java)
        intent.putExtra("joueur",joueur)
        startActivity(intent)
    }

    private fun refillerLimcoins(){
        joueur.ajouterLimcoins(500)
        majLimcoins()
    }

    private fun recupererJoueur(){
        val user = FirebaseAuth.getInstance().currentUser //on essaie d'obtenir l'email de l'utilisateur connecté
        user?.let {

            val email = user.email
            val db = FirebaseFirestore.getInstance()
            db.collection("users")
                .whereEqualTo("email", email)
                .get()//on verifie  essaye d'obtenir le documents qui contient L'UID de l'utilisateur par son mail
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        //on récupere les limcoins et le pseudo du joueur dans le document
                        val username = document.getString("username")
                        val limcoins = document.getLong("limcoins")?.toInt()
                        joueur.pseudo = username!!
                        joueur.limcoins = limcoins!!
                        majLimcoins()
                    }
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(this, "Erreur sur la recuperation du joueur", Toast.LENGTH_SHORT).show()
                }
        }
    }

    fun modeDefi(){
        val intent = Intent(this, FriendActivity::class.java)
        intent.putExtra("joueur",joueur)
        startActivity(intent)
    }

    private fun sauvegarderLimcoins(){
        //meme principe pour recuperer un joueur sauf qu'on fait la mise a jour des limcoins
        val user = FirebaseAuth.getInstance().currentUser
        user?.let {
            val email = user.email
            val db = FirebaseFirestore.getInstance()
            db.collection("users")
                .whereEqualTo("email", email)
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {//mise a jour  du limcoins dans le document approprié
                        val docRef = db.collection("users").document(document.id)
                        docRef.update("limcoins", joueur.limcoins)
                    }
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(this, "peux pas sauvegarder", Toast.LENGTH_SHORT).show()
                }
        }
    }

}
