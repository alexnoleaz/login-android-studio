package com.example.loginpage

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.net.toUri

class MainActivity : AppCompatActivity() {
    private lateinit var userHelper: UserDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val txtUsername = findViewById<EditText>(R.id.txtUsername)
        val txtPassword = findViewById<EditText>(R.id.txtPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val btnFb = findViewById<ImageButton>(R.id.btnFb)
        val btnIg = findViewById<ImageButton>(R.id.btnIg)
        val btnX = findViewById<ImageButton>(R.id.btnX)

        userHelper = UserDatabaseHelper(this)

        btnLogin.setOnClickListener{
            val username = txtUsername.text.toString().trim()
            val password = txtPassword.text.toString().trim()

            val result = userHelper.signIn(username,password)
            if(result !=null)
                Toast.makeText(this,"Bienvenido $result",Toast.LENGTH_SHORT).show()
            else Toast.makeText(this,"Credenciales inválidas",Toast.LENGTH_SHORT).show()
        }

        btnFb.setOnClickListener { this.toSocialNetwork("https://www.facebook.com/UPN/?locale=es_LA")}
        btnIg.setOnClickListener { this.toSocialNetwork("https://www.instagram.com/upn/?hl=es-la")}
        btnX.setOnClickListener { this.toSocialNetwork("https://x.com/UPN_Oficial")}
    }

    private fun toSocialNetwork(url: String){
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = url.toUri()
        startActivity(intent)
    }
}