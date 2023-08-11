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
import com.gilang.dailynotes10120071.model.Kategori;
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

public class CategoriActivity extends AppCompatActivity {

    String[] daftar, id_kategori;
    ListView listView;
    private Cursor cursor;
    public static CategoriActivity ma;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categori);

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
                        final Dialog dialog = new Dialog(CategoriActivity.this);

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

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CategoriActivity.this, SaveCategoryActivity.class);
                startActivity(intent);
            }
        });

        ma = this;

        listView = findViewById(R.id.rv_notes);
        MyAdapter adapter = new MyAdapter(new ArrayList<>());
        listView.setAdapter(adapter);
        RefreshList();
    }

    private void signOut() {
        FirebaseAuth.getInstance().signOut();
        Toast.makeText(this, "Logged out successfully!", Toast.LENGTH_SHORT).show();
        redirectToLogin(); // Redirect to the login page after logout
    }

    private void redirectToLogin() {
        Intent intent = new Intent(CategoriActivity.this, ViewPagerActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    public void RefreshList() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = user.getUid();
        DatabaseReference kategoriRef = FirebaseDatabase.getInstance().getReference().child("kategori").child(userId);

        kategoriRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Kategori> kategoriList = new ArrayList<>();
                for (DataSnapshot kategoriSnapshot : dataSnapshot.getChildren()) {
                    Kategori kategori = kategoriSnapshot.getValue(Kategori.class);
                    kategoriList.add(kategori);
                }

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Kategori selectedKategori = kategoriList.get(position); // Get the selected category from the list
                        String selection = selectedKategori.id_kategori;

                        Intent intent = new Intent(CategoriActivity.this, ListNoteActivity.class);
                        intent.putExtra("kategori", selection);
                        startActivity(intent);
                    }
                });

                listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                        Kategori selectedKategori = kategoriList.get(position); // Get the selected category from the list

                        AlertDialog.Builder builder = new AlertDialog.Builder(CategoriActivity.this);
                        builder.setTitle("Delete Kategori");
                        builder.setMessage("Yakin menghapus kategori ini?");
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DatabaseReference kategoriRef = FirebaseDatabase.getInstance().getReference().child("kategori").child(selectedKategori.id_kategori);
                                kategoriRef.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(getApplicationContext(), "Kategori Berhasil Dihapus", Toast.LENGTH_LONG).show();
                                        kategoriList.remove(position); // Remove the deleted item from the list
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


                MyAdapter adapter = new MyAdapter(kategoriList);
                listView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle the error, if any
            }
        });
    }

    public class MyAdapter extends BaseAdapter {
        private List<Kategori> kategoriList;

        public MyAdapter(List<Kategori> kategoriList) {
            this.kategoriList = kategoriList;
        }

        @Override
        public int getCount() {
            return kategoriList.size();
        }

        @Override
        public Object getItem(int position) {
            return kategoriList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = getLayoutInflater().inflate(R.layout.single_view_item, parent, false);

            TextView kategoriTitle = convertView.findViewById(R.id.kategori_title);
            Kategori kategori = kategoriList.get(position);
            kategoriTitle.setText(kategori.kategori);

            return convertView;
        }
    }

}
