package com.gilang.dailynotes10120071.main.diary;
//10120071, Gilang Dhiya Ulhaq, IF2, gilang.dhiya19@gmail.com

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.gilang.dailynotes10120071.R;
import com.gilang.dailynotes10120071.model.FirebaseHelper;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

public class NotesActivity extends AppCompatActivity {

    private EditText titleInput;
    private EditText contentInput;
    private Button btn_simpan, btn_kembali;
    Date dt = new Date();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        titleInput = findViewById(R.id.title_note);
        contentInput = findViewById(R.id.content_note);
        btn_simpan = findViewById(R.id.btn_submit);
        btn_kembali = findViewById(R.id.btn_back);

        Intent getKategori = getIntent();
        String kategori = getKategori.getStringExtra("kategori");

        btn_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = titleInput.getText().toString();
                String content = contentInput.getText().toString();
                String tanggal = new SimpleDateFormat("dd-MMM-yyyy").format(dt);
                String waktu = new SimpleDateFormat("HH:mm a").format(dt);

                // You can generate a unique key for the note using push().getKey()
                String noteId = FirebaseDatabase.getInstance().getReference().child("note").push().getKey();

                FirebaseHelper firebaseHelper = new FirebaseHelper();
                firebaseHelper.addNote(noteId, tanggal, waktu, title, content, kategori);

                Toast.makeText(getApplicationContext(), "Berhasil Buat Note!", Toast.LENGTH_LONG).show();
                ListNoteActivity.ln.RefreshListNote();
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
