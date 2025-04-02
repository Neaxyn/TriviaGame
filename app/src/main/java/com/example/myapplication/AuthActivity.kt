package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import android.util.Log


class AuthActivity : AppCompatActivity() {

    // Creation de la variable pour s'authentifier
    private lateinit var auth: FirebaseAuth


    companion object {
        private const val TAG = "AuthActivity"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.authentify_activity)

        // Initialisation de la variable
        auth = FirebaseAuth.getInstance()

        val emailEditText: EditText = findViewById(R.id.email)
        val passwordEditText: EditText = findViewById(R.id.password)
        val loginBtn: Button = findViewById(R.id.sign_in)

        //initialisation du bouton de connexion
        loginBtn.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            if(email.isEmpty() || password.isEmpty()) {
                if (password.isEmpty()) {
                    passwordEditText.error = "mot de passe vide!"
                }
                if (email.isEmpty()) {
                    emailEditText.error = "Email vide!"
                }
            }
            else {
                signIn(email, password)
            }
        }



        //bouton pour créer son compte
        val registerBtn = findViewById<Button>(R.id.register_btn)
        registerBtn.setOnClickListener{
            Intent(this, RegisterActivity::class.java).also {
                startActivity(it)
            }
        }

    }


    //authentification via firebase
    private fun signIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    //on passe à l'activité suivante si l'authentification est un success
                    Log.d(TAG, "signInWithEmail:success")
                    goToMode()
                } else {
                    // on affiche les messages d'erreur si ça ne marche pas
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext,
                        "Authentication échoué.",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }
    }



    private fun goToMode() {
        val intent = Intent(this, ModeActivity::class.java)
        intent.putExtra("connect",true)
        startActivity(intent)
    }
}
