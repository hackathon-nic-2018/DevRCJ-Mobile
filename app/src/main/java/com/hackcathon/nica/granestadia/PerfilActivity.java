package com.hackcathon.nica.granestadia;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class PerfilActivity extends AppCompatActivity {

    private ImageView userImage;
    private TextView userName, userEmail, userID;
    private String name, email, image, id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        userImage = findViewById(R.id.img_user);
        userName = findViewById(R.id.nombreTxt);
        userEmail = findViewById(R.id.txt_email);

        Intent intent = getIntent();

        name = intent.getStringExtra("nombre");
        email = intent.getStringExtra("email");
        id = intent.getStringExtra("id");
        image =intent.getStringExtra("image");

        userName.setText(name);
        userEmail.setText(email);


        Picasso.with(getApplicationContext())
                .load(image)
                .into(userImage);



    }
}
