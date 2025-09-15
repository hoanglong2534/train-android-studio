package com.btvn.btvn

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class bai3Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_bai3)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val listView = findViewById<ListView>(R.id.listView)
        val data = listOf("Đọc sách", "Viết báo cáo", "Tập thể dục", "Học Android")
        val adapter = ArrayAdapter(this , R.layout.checked_layout_bai2, data)

        listView.adapter = adapter
        listView.choiceMode = ListView.CHOICE_MODE_MULTIPLE

        listView.setOnItemClickListener { parent, _, position, _ ->
            val item = parent.getItemAtPosition(position).toString()
            val checkedPos = listView.isItemChecked(position)
            if(checkedPos){
                Toast.makeText(this, " $item đã hoàn thành", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this, " $item chưa làm", Toast.LENGTH_SHORT).show()
            }
        }


    }

}