package com.gilang.dailynotes10120071.main.diary;
//10120071, Gilang Dhiya Ulhaq, IF2, gilang.dhiya19@gmail.com

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.gilang.dailynotes10120071.R;
import com.gilang.dailynotes10120071.model.FirebaseHelper;
import com.google.firebase.database.FirebaseDatabase;

public class SaveCategoryActivity extends AppCompatActivity {
    private EditText titleInput;
    private EditText contentInput;
    private Button btn_simpan, btn_kembali;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_category);

        titleInput = findViewById(R.id.input_title);
        contentInput = findViewById(R.id.input_content);
        btn_simpan = findViewById(R.id.button_submit);
        btn_kembali = findViewById(R.id.button_back);

        btn_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = titleInput.getText().toString();
                // You can generate a unique key for the category using push().getKey()
                String categoryId = FirebaseDatabase.getInstance().getReference().child("kategori").push().getKey();

                FirebaseHelper firebaseHelper = new FirebaseHelper();
                firebaseHelper.addKategori(categoryId, title);

                Toast.makeText(getApplicationContext(), "Kategori Berhasil Dibuat", Toast.LENGTH_LONG).show();
                CategoriActivity.ma.RefreshList();
                finish();
            }
        });

        btn_kembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                finish();
            }
        });
    }
}
