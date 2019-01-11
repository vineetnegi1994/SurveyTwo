package com.admin.surveytwo.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.admin.surveytwo.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SecondPost extends AppCompatActivity {

    EditText editText1;
    Button button1;
    DatabaseReference databaseArtists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_post);

        databaseArtists = FirebaseDatabase.getInstance().getReference("artist");


        editText1 = (EditText)findViewById(R.id.secondPost);
        button1 = (Button)findViewById(R.id.secondButton);

        /*button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addArtist();
            }
        });*/


    }

    /*private void addArtist(){

        String name = editText1.getText().toString().trim();
        if(!TextUtils.isEmpty(name)){
             String id = databaseArtists.push().getKey();
             Artist artist = new Artist(id,name);
             databaseArtists.child(id).setValue(artist);
            Toast.makeText(getApplicationContext(), "Artist Added", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(getApplicationContext(), "Enter Name Firest", Toast.LENGTH_SHORT).show();
        }

    }*/

}
