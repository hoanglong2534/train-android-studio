package com.btvn.btvn

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class bai1Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_bai1)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        fun displayAnswer(price : String){
            val textView = findViewById<TextView>(R.id.textView8)
            textView.setText(price)
        }

        val spinner = findViewById<Spinner>(R.id.spinner)

        val data = mapOf("Trẻ em" to "10K", "Người lớn" to "50K", "Sinh viên" to "5K", "Người cao tuổi" to "20K")

        val adapter = ArrayAdapter(this, R.layout.spinner_layout_bai1, data.keys.toList() )

        adapter.setDropDownViewResource(R.layout.spinner_layout_bai1)
        spinner.setAdapter(adapter)

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                parent?.let{
                    val itemSelected = parent.getItemAtPosition(position).toString()
                    val price = data[itemSelected].toString()

                    displayAnswer(price)
                }


            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }

    }
}