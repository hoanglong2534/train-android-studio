package com.btvn.bai3

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.MultiAutoCompleteTextView
import android.widget.TextView
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

        var multiComplete = findViewById<MultiAutoCompleteTextView>(R.id.multiAutoCompleteTextView)

        var listLanguage = listOf("Java", "Kotlin", "Python", "C++", "JavaScript")

        var adapter = ArrayAdapter(this, R.layout.item, listLanguage)
        multiComplete.setAdapter(adapter)
        multiComplete.setTokenizer(MultiAutoCompleteTextView.CommaTokenizer())

    }

    fun handleClick(view : View){
        var cacKyNang = findViewById<TextView>(R.id.textView2)
        cacKyNang.visibility = View.VISIBLE

        var multiComplete = findViewById<MultiAutoCompleteTextView>(R.id.multiAutoCompleteTextView)
        var ansText = multiComplete.text.toString().trim()
        val ansTextFinal = ansText.dropLast(1)
        var textView = findViewById<TextView>(R.id.textView3)
        textView.setText(ansTextFinal)
        textView.visibility = View.VISIBLE

    }
}