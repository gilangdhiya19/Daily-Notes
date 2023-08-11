package com.gilang.dailynotes10120071.main.diary;
//10120071, Gilang Dhiya Ulhaq, IF2, gilang.dhiya19@gmail.com

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.gilang.dailynotes10120071.R;
import com.gilang.dailynotes10120071.model.Note;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UpdateActivity extends AppCompatActivity {

    private EditText titleInput;
    private EditText contentInput;
    private Button btn_simpan, btn_kembali;
    private DatabaseReference noteRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        titleInput = findViewById(R.id.title_note);
        contentInput = findViewById(R.id.content_note);
        btn_simpan = findViewById(R.id.btn_submit);
        btn_kembali = findViewById(R.id.btn_back);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = user.getUid();

        noteRef = FirebaseDatabase.getInstance().getReference().child("note").child(userId);

        Intent get_ID = getIntent();
        String ID = get_ID.getStringExtra("id");

        noteRef.child(ID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Note note = dataSnapshot.getValue(Note.class);
                if (note != null) {
                    titleInput.setText(note.judul);
                    contentInput.setText(note.isi);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle the error, if any
            }
        });

        btn_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference updatedNoteRef = noteRef.child(ID);
                updatedNoteRef.child("judul").setValue(titleInput.getText().toString());
                updatedNoteRef.child("isi").setValue(contentInput.getText().toString());

                Toast.makeText(getApplicationContext(), "Berhasil diubah", Toast.LENGTH_LONG).show();
                ListNoteActivity.ln.RefreshListNote();
                finish();
            }
        });

        btn_kembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}

