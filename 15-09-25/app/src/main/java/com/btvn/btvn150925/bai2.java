package com.btvn.btvn150925;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class bai2 extends AppCompatActivity {

    private EditText editTextName;
    private Button btnOpenFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bai2);

        editTextName = findViewById(R.id.editTextName);
        btnOpenFragment = findViewById(R.id.btnOpenFragment);

        btnOpenFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editTextName.getText().toString().trim();

                bai2_fragment fragment = bai2_fragment.newInstance(name, "");

                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.fragmentContainer, fragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });
    }
}
