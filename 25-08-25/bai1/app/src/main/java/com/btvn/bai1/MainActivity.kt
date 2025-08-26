package com.btvn.bai1

import android.os.Bundle
import android.view.View
import android.widget.EditText
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
    }

    fun handleCong(view : View) {
        val textNum1 = findViewById<EditText>(R.id.editTextNumber);
        val textNum2 = findViewById<EditText>(R.id.editTextNumber2);
        val textAns = findViewById<TextView>(R.id.textView4)

        val num1 = textNum1.text.toString().toDouble();
        val num2 = textNum2.text.toString().toDouble();
        val ans = num1 + num2;
        textAns.setText(ans.toString())
    }

    fun handleTru(view : View){
        val textNum1 = findViewById<EditText>(R.id.editTextNumber);
        val textNum2 = findViewById<EditText>(R.id.editTextNumber2);
        val textAns = findViewById<TextView>(R.id.textView4)

        val num1 = textNum1.text.toString().toDouble();
        val num2 = textNum2.text.toString().toDouble();
        val ans = num1 - num2;
        textAns.setText(ans.toString())
    }

    fun handleNhan(view : View){
        val textNum1 = findViewById<EditText>(R.id.editTextNumber);
        val textNum2 = findViewById<EditText>(R.id.editTextNumber2);
        val textAns = findViewById<TextView>(R.id.textView4)

        val num1 = textNum1.text.toString().toDouble();
        val num2 = textNum2.text.toString().toDouble();
        val ans = num1 * num2;
        textAns.setText(ans.toString())
    }

    fun handleChia(view : View){
        val textNum1 = findViewById<EditText>(R.id.editTextNumber);
        val textNum2 = findViewById<EditText>(R.id.editTextNumber2);
        val textAns = findViewById<TextView>(R.id.textView4)

        val num1 = textNum1.text.toString().toDouble();
        val num2 = textNum2.text.toString().toDouble();
        val ans = num1 / num2;
        textAns.setText(ans.toString())
    }
}