package com.btvn.btvn

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class bai2Activity : AppCompatActivity() {

    lateinit var listView: ListView
    lateinit var ans: TextView
    val data = listOf("Toán", "Lý", "Hóa", "Sinh", "Anh", "GDCD")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_bai2)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        listView = findViewById(R.id.listView)
        ans = findViewById(R.id.textView10)

        val adapter = ArrayAdapter(this, R.layout.checked_layout_bai2, data)
        listView.adapter = adapter
        listView.choiceMode = ListView.CHOICE_MODE_MULTIPLE
    }

    private fun display(s: String) {
        ans.text = s
        ans.visibility = View.VISIBLE
    }

    fun handleConfirm(view: View) {
        val listSubjects = mutableListOf<String>()
        val checkedPos = listView.checkedItemPositions

        for (x in data.indices) {
            if (checkedPos.get(x)) {
                listSubjects.add(data[x])
            }
        }

        display(listSubjects.joinToString(", "))
    }
}
