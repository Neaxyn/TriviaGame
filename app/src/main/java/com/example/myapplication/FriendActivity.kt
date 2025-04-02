package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

import android.widget.Toast
import com.example.myapplication.classes.Joueur
import com.google.firebase.firestore.FirebaseFirestore

class FriendActivity : AppCompatActivity() {
    private lateinit var joueur: Joueur
    private lateinit var saisiePseudo : EditText


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friend)

        joueur = intent.getParcelableExtra("joueur",Joueur::class.java)!!

        saisiePseudo = findViewById(R.id.saisie_pseudo)

        val returnBtn = findViewById<Button>(R.id.retour_btn)
        returnBtn.setOnClickListener {
            retour()
        }

        val addBtn = findViewById<Button>(R.id.add_joueur)
        addBtn.setOnClickListener{
            checkJoueur(saisiePseudo.text.toString())
        }
    }

    private fun checkJoueur(pseudoSaisi :String){
        val db = FirebaseFirestore.getInstance()
        db.collection("users")
            .whereEqualTo("username", pseudoSaisi)
            .get()
            .addOnSuccessListener { documents ->
                if (documents.isEmpty) {
                    Toast.makeText(this, "Aucun joueur trouvé avec ce pseudo", Toast.LENGTH_SHORT).show()
                }
                else {
                    Toast.makeText(this, "Joueur trouvé avec ce pseudo", Toast.LENGTH_SHORT).show()
                }
                //ajouter ami dans la liste et etc code pas fini
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "erreur lors du chargement des doc", Toast.LENGTH_SHORT).show()
            }
    }

    private fun retour(){
        val intent = Intent(this, ModeActivity::class.java)
        intent.putExtra("joueur",joueur)//on fait quand meme passer en intent pour assurer qu'il soit bien initialisée
        startActivity(intent)
    }
}