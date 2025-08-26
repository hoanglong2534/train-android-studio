package com.btvn.bai2

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
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

        val autoComplete = findViewById<AutoCompleteTextView>(R.id.autoCompleteTextView)

        val listCountries = listOf("Việt Nam", "Thái Lan", "Indonesia", "Singapore", "Malaysia", "Philippines")

        var adapterConuntry = ArrayAdapter(this, R.layout.item_dropdown, listCountries)
        autoComplete.setAdapter(adapterConuntry)

        autoComplete.setOnItemClickListener{parent, view, position ,id ->
            var selecItem = parent.getItemAtPosition(position).toString()
            Toast.makeText(this, "Nhập $selecItem thành công", Toast.LENGTH_SHORT).show()
        }
    }
}