package com.gilang.dailynotes10120071.main.profile;
//10120071, Gilang Dhiya Ulhaq, IF2, gilang.dhiya19@gmail.com

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.gilang.dailynotes10120071.R;
import com.gilang.dailynotes10120071.main.diary.CategoriActivity;
import com.gilang.dailynotes10120071.main.viewpager.ViewPagerActivity;
import com.gilang.dailynotes10120071.model.FirebaseHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //Initialize And Assign Variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        //Set Home Selected
        bottomNavigationView.setSelectedItemId(R.id.ProfileId);

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
                        final Dialog dialog = new Dialog(ProfileActivity.this);

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
    }

    private void signOut() {
        FirebaseAuth.getInstance().signOut();
        Toast.makeText(this, "Logged out successfully!", Toast.LENGTH_SHORT).show();
        redirectToLogin(); // Redirect to the login page after logout
    }

    private void redirectToLogin() {
        Intent intent = new Intent(ProfileActivity.this, ViewPagerActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
