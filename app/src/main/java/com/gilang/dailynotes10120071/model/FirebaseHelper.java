package com.gilang.dailynotes10120071.model;
//10120071, Gilang Dhiya Ulhaq, IF2, gilang.dhiya19@gmail.com

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseHelper {
    private DatabaseReference mDatabase;

    public FirebaseHelper() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public void addKategori(String id, String kategori) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = user.getUid();
        Kategori k = new Kategori(id, kategori);
        mDatabase.child("kategori").child(userId).child(id).setValue(k);
    }

    public void addNote(String id, String tanggal, String waktu, String judul, String isi, String id_kategori) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = user.getUid();
        Note n = new Note(id, tanggal, waktu, judul, isi, id_kategori);
        mDatabase.child("note").child(userId).child(id).setValue(n);
    }
}
