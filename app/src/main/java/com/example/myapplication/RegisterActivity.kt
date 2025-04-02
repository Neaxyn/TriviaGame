

package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore

class RegisterActivity : AppCompatActivity() {

    // Création de l'instance utilisateur
    private lateinit var auth: FirebaseAuth

    // Initialisation des valeurs


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Initialisation de la variable
        auth = Firebase.auth

        val usernameEditText: EditText = findViewById(R.id.username)
        val emailEditText: EditText = findViewById(R.id.email)
        val passwordEditText: EditText = findViewById(R.id.password)
        val confirmpasswordEditText: EditText = findViewById(R.id.confirmpassword)
        val button: Button = findViewById(R.id.sign_Up)

        button.setOnClickListener{
            val username = usernameEditText.text.toString()
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            val confirmpassword = confirmpasswordEditText.text.toString()

            if(email.isEmpty() || password.isEmpty() || confirmpassword.isEmpty() || username.isEmpty())
            {
                if (email.isEmpty()) {
                    emailEditText.error= "Email is required!"
                }
                if (password.isEmpty()) {
                    passwordEditText.error = "Password is required!"
                }
                if (confirmpassword.isEmpty()) {
                    confirmpasswordEditText.error = "Password is required!"
                }
                if (username.isEmpty()) {
                    usernameEditText.error = "Username is required!"
                }
            }
            else {
                if(password != confirmpassword){
                    confirmpasswordEditText.error = "Password did not match!"
                }
                else {
                    // Création de l'utilisateur dans le module Authentification
                    auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener{task ->
                        if(task.isSuccessful){
                            val user = hashMapOf(//on crée un hashmap pour stocker dans la db l'email,le pseudo et les limcoins du joueur
                                "username" to username,
                                "email" to email,
                                "limcoins" to 500
                            )
                            val currentUser = auth.currentUser
                            // Création de l'utilisateur dans le module Firestore
                            val db = Firebase.firestore
                            db.collection("users").document(currentUser!!.uid).set(user).addOnSuccessListener {

                                Intent(this, ModeActivity::class.java).also {
                                    startActivity(it)
                                }

                            }.addOnFailureListener{
                                confirmpasswordEditText.error = "Erreur dans le db de firestore."

                            }

                        }
                        else{
                            confirmpasswordEditText.error = "Erreur d'enregistrement du joueur."

                        }

                    }

                }
            }

        }
    }
}