package com.gilang.dailynotes10120071.main.diary;
//10120071, Gilang Dhiya Ulhaq, IF2, gilang.dhiya19@gmail.com

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.gilang.dailynotes10120071.R;
import com.gilang.dailynotes10120071.main.profile.ProfileActivity;
import com.gilang.dailynotes10120071.main.viewpager.ViewPagerActivity;
import com.gilang.dailynotes10120071.model.Note;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ListNoteActivity extends AppCompatActivity {

    ListView listView;
    String kategori;
    public static ListNoteActivity ln;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_note);

        //Initialize And Assign Variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        //Set Home Selected
        bottomNavigationView.setSelectedItemId(R.id.CatatanHarian);

        //Perform ItemSelectedListener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.ProfileId:
                        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.CatatanHarian:
                        startActivity(new Intent(getApplicationContext(), CategoriActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.About:
                        final Dialog dialog = new Dialog(ListNoteActivity.this);

                        //Memasang Title / Judul pada Custom Dialog
                        dialog.setTitle("About");

                        //Memasang Desain Layout untuk Custom Dialog
                        dialog.setContentView(R.layout.custom_design_dialog);

                        //Memasang Listener / Aksi saat tombol OK di Klik
                        Button DialogButton = dialog.findViewById(R.id.ok);
                        DialogButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });

                        dialog.show();
                        return true;

                    case R.id.Logout:
                        signOut();
                        return true;
                }

                return false;
            }
        });

        Intent getKategori = getIntent();
        kategori = getKategori.getStringExtra("kategori");

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListNoteActivity.this, NotesActivity.class);
                intent.putExtra("kategori", kategori);
                startActivity(intent);
            }
        });

        ln = this;

        listView = findViewById(R.id.listNote);
        MyAdapter adapter = new MyAdapter(new ArrayList<>());
        listView.setAdapter(adapter);

        RefreshListNote();
    }

    private void signOut() {
        FirebaseAuth.getInstance().signOut();
        Toast.makeText(this, "Logged out successfully!", Toast.LENGTH_SHORT).show();
        redirectToLogin(); // Redirect to the login page after logout
    }

    private void redirectToLogin() {
        Intent intent = new Intent(ListNoteActivity.this, ViewPagerActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    public void RefreshListNote() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = user.getUid();
        DatabaseReference noteRef = FirebaseDatabase.getInstance().getReference().child("note").child(userId);

        noteRef.orderByChild("id_kategori").equalTo(kategori).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Note> notes = new ArrayList<>();
                for (DataSnapshot noteSnapshot : dataSnapshot.getChildren()) {
                    Note note = noteSnapshot.getValue(Note.class);
                    notes.add(note);
                }

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Note selectedNote = notes.get(position); // Get the selected note from the list
                        String selection = selectedNote.id;

                        Intent intent = new Intent(ListNoteActivity.this, UpdateActivity.class);
                        intent.putExtra("id", selection);
                        startActivity(intent);
                    }
                });

                listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                        Note selectedNote = notes.get(position); // Get the selected note from the list

                        AlertDialog.Builder builder = new AlertDialog.Builder(ListNoteActivity.this);
                        builder.setTitle("Delete Note");
                        builder.setMessage("Yakin menghapus note ini?");
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DatabaseReference noteRef = FirebaseDatabase.getInstance().getReference().child("note").child(selectedNote.id);
                                noteRef.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(getApplicationContext(), "Note Berhasil Dihapus", Toast.LENGTH_LONG).show();
                                        notes.remove(position); // Remove the deleted note from the list
                                        ((MyAdapter) listView.getAdapter()).notifyDataSetChanged(); // Notify the adapter
                                    }
                                });
                            }
                        });
                        builder.setNegativeButton("No", null);
                        builder.create().show();

                        return true;
                    }
                });


                MyAdapter adapter = new MyAdapter(notes);
                listView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle the error, if any
            }
        });
    }

    public class MyAdapter extends BaseAdapter {
        private List<Note> notes;

        public MyAdapter(List<Note> notes) {
            this.notes = notes;
        }

        @Override
        public int getCount() {
            return notes.size();
        }

        @Override
        public Object getItem(int position) {
            return notes.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = getLayoutInflater().inflate(R.layout.item_notes, parent, false);

            TextView noteTitle = convertView.findViewById(R.id.text_title);
            TextView noteDesc = convertView.findViewById(R.id.text_content);
            TextView noteDate = convertView.findViewById(R.id.text_date);
            TextView noteTime = convertView.findViewById(R.id.text_time);

            Note note = notes.get(position);

            noteDate.setText(note.tanggal);
            noteTime.setText(note.waktu);
            noteTitle.setText(note.judul);
            noteDesc.setText(note.isi);

            return convertView;
        }
    }

}
