package com.hackcathon.nica.granestadia.fragments;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hackcathon.nica.granestadia.R;
import com.squareup.picasso.Picasso;

import static android.content.Context.MODE_PRIVATE;


public class FragmentUsuario extends Fragment implements View.OnClickListener{

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private View root;

    private ImageView userImage;
    private TextView userName, userEmail, userID;
    private String name, email, image, id;
    private EditText descUsuario;
    private Button btGuardar;


    public FragmentUsuario() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_usuario, container, false);

        pref = getActivity().getSharedPreferences("MisPreferencias", MODE_PRIVATE);

        editor = pref.edit();

        userImage = root.findViewById(R.id.img_user);
        userName = root.findViewById(R.id.nombreTxt);
        userEmail = root.findViewById(R.id.txt_email);
        descUsuario = root.findViewById(R.id.edt_descrip);
        btGuardar = root.findViewById(R.id.btn_guardar);

        btGuardar.setOnClickListener(this);


        String txtNombre = pref.getString("nombre","name");
        String url_img = pref.getString("url_img","url");
        String correo = pref.getString("email","correo");
        String descrip = pref.getString("user_desc","");

        userName.setText(txtNombre);
        userEmail.setText(correo);
        descUsuario.setText(descrip);

        Picasso.with(getContext())
                .load(url_img)
                .into(userImage);

        return root;
    }

    @Override
    public void onClick(View v) {
        editor.putString("user_desc",descUsuario.getText().toString());
        editor.apply();
        Toast.makeText(getContext(),"Cambios Guadados!",Toast.LENGTH_SHORT).show();

    }

}
