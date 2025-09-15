package com.btvn.btvn

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    fun goToBai1(view : View){
        val intent = Intent(this, bai1Activity::class.java)
        startActivity(intent)
    }

    fun goToBai2(view : View){
        val intent = Intent(this, bai2Activity::class.java)
        startActivity(intent)
    }

    fun goToBai3(view: View){
        val intent = Intent(this, bai3Activity::class.java)
        startActivity(intent)
    }
}