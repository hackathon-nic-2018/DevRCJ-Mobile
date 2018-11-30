package com.hackcathon.nica.granestadia.fragments;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hackcathon.nica.granestadia.LoginActivity;
import com.hackcathon.nica.granestadia.MainActivity;
import com.hackcathon.nica.granestadia.R;
import com.squareup.picasso.Picasso;

import static android.content.Context.MODE_PRIVATE;
import static com.hackcathon.nica.granestadia.datos.Info.ServUrlUpdate;


public class FragmentUsuario extends Fragment implements View.OnClickListener{

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private View root;

    private ImageView userImage;
    private TextView userName, userEmail, userID;
    private String name, email, image, id;
    private EditText descUsuario, edadusuario, stCivil;
    private Button btGuardar, btArrendar;
    private String txtNombre, correo, url_img, descrip, FBid,edadUsuario, civilUsuario;


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
        btArrendar = root.findViewById(R.id.btn_arrendar);
        edadusuario = root.findViewById(R.id.edt_edad);
        stCivil = root.findViewById(R.id.edt_civil);

        btGuardar.setOnClickListener(this);
        btArrendar.setOnClickListener(this);


        txtNombre = pref.getString("nombre","name");
        url_img = pref.getString("url_img","url");
        correo = pref.getString("email","correo");
        descrip = pref.getString("user_desc","");
        FBid = pref.getString("Fid","id");
        edadUsuario = pref.getString("edad_usuario","");
        civilUsuario = pref.getString("estadoCivil","");

        userName.setText(txtNombre);
        userEmail.setText(correo);
        descUsuario.setText(descrip);
        edadusuario.setText(edadUsuario);
        stCivil.setText(civilUsuario);

        Picasso.with(getContext())
                .load(url_img)
                .into(userImage);

        root.setFocusableInTouchMode(true);
        root.requestFocus();
        root.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                Log.i("Backs", "keyCode: " + keyCode);
                if( keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                    Log.i("Backs", "onKey Back listener is working!!!");
                    getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    ChangeFragment(new MapFragment());
                    return true;
                }
                return false;
            }
        });

        return root;
    }

    @Override
    public void onClick(View v) {

        if(v.getId()==R.id.btn_guardar){

            editor.putString("user_desc",descUsuario.getText().toString());
            editor.putString("edad_usuario",edadusuario.getText().toString());
            editor.putString("estadoCivil",stCivil.getText().toString());
            editor.apply();
            //actualizarUsuario(txtNombre,url_img,correo,FBid,edadusuario.getText().toString(),stCivil.getText().toString(),descrip);
            Toast.makeText(getContext(),"Cambios Guadados!",Toast.LENGTH_SHORT).show();
        }

        if(v.getId()==R.id.btn_arrendar){
            ChangeFragment(new ArrendarFragment());
        }

    }

    private void actualizarUsuario(String nombre, String foto, String email, String Fid, String edad, String civil, String desctip){


        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                ServUrlUpdate+"nombres="+nombre+"&apellidos="+Fid+"&email="+email+"&foto="+foto+"&edad="+edad+"&estadocivil="+civil+"&descripcion="+desctip,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        //Toast.makeText(LoginActivity.this,"Registro exitoso...",Toast.LENGTH_LONG).show();

                        Log.v("Repuesta: ", response);


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(getContext(),error.toString(),Toast.LENGTH_LONG).show();
                        Log.i("Error: ", error.toString());
                    }
                }){



        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }


    private void ChangeFragment(Fragment fragment){
        FragmentManager fmanager = getFragmentManager();
        assert fmanager != null;
        FragmentTransaction ftransaction = fmanager.beginTransaction();
        ftransaction.replace(R.id.content_frame, fragment).addToBackStack(null).commit();
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).setActionBarTitle("Mi Perfil");
    }
}
