package com.example.moody;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.example.moody.databinding.ActivityMainBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class MainActivity extends AppCompatActivity
{
    ActivityMainBinding binding;
    FirebaseDatabase database;
    public static String loginUserId = "Adhiraj";
    String usrname, pss;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        database = FirebaseDatabase.getInstance();

        //to hide the actionbar
        getSupportActionBar().hide();

        // this is onclicklistener for donthaveacoount textview
        binding.dontHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,RegisterActivity.class);
                startActivity(i);
                MainActivity.this.finish();
            }
        });

        binding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                usrname = binding.usernameLogIn.getText().toString();
                pss = binding.passwordLogIn.getText().toString();

                database.getReference()
                        .child("Users")
                        .child(usrname+pss)
                        .addValueEventListener(new ValueEventListener()
                        {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot)
                            {
                                for(DataSnapshot ds : snapshot.getChildren()) {
                                    regInfo reg = ds.getValue(regInfo.class);

                                    String checkUsr ;
                                    String checkPas ;
                                    checkUsr = reg.getUsername();
                                    checkPas = reg.getPassword();

                                    try
                                    {
                                        if (( checkUsr.equals(usrname) ) && ( checkPas.equals(pss) ))
                                        {
                                            loginUserId = usrname+pss;
                                            Intent i = new Intent(MainActivity.this, IntroductionPageActivity.class);
                                            startActivity(i);
                                            MainActivity.this.finish();
                                        }
                                    }
                                    catch (Exception e)
                                    {
                                        binding.usernameLogIn.setError("Field can not be empty");
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error)
                            {
                            }
                        });
            }
        });

    }
}