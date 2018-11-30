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

import static android.content.Context.MODE_PRIVATE;
import static com.hackcathon.nica.granestadia.datos.Info.ServUrlAddHabitacion;
import static com.hackcathon.nica.granestadia.datos.Info.ServUrlInsert;


public class ArrendarFragment extends Fragment implements View.OnClickListener {

    private View root;

    private EditText edtDescrip, edtPrecio,edtCondiciones, edtDireccion;
    private Button btnArrendar;

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    private String FBid;


    public ArrendarFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_arrendar, container, false);

        ((MainActivity) getActivity()).setActionBarTitle("Arrendar");

        MainActivity.updatePage("Arrendar");

        pref = getActivity().getSharedPreferences("MisPreferencias", MODE_PRIVATE);
        editor = pref.edit();

        FBid = pref.getString("Fid","");

        edtDireccion = root.findViewById(R.id.edt_direccion);
        edtPrecio = root.findViewById(R.id.edt_precio);
        edtDescrip  = root.findViewById(R.id.edt_descripcion);
        edtCondiciones = root.findViewById(R.id.edt_condiciones);

        btnArrendar  = root.findViewById(R.id.btn_oferta);

        btnArrendar.setOnClickListener(this);

        root.setFocusableInTouchMode(true);
        root.requestFocus();
        root.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                Log.i("Backs", "keyCode: " + keyCode);
                if( keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                    Log.i("Backs", "onKey Back listener is working!!!");
                    getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    ChangeFragment(new FragmentUsuario());
                    return true;
                }
                return false;
            }
        });


        return root;
    }

    private void registrarHabitacion(String descripcion, String precio, String foto1, String foto2, String foto3, String foto4, String condiciones, String estado, String direccion, String idUsuario){


        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                ServUrlAddHabitacion+"descripcion="+descripcion+"&precio="+precio+"&foto1="+foto1+"&foto2="+foto2+"&foto3="+foto3+"&foto4="+foto4+"&condiciones="+condiciones+"&estado="+estado+"&direccion="+direccion+"&idusuario="+idUsuario,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        //Toast.makeText(LoginActivity.this,"Registro exitoso...",Toast.LENGTH_LONG).show();

                        Log.v("Repuesta: ", response);

                        if (response.equals("true")){
                            Toast.makeText(getContext(),"Habitacion Registrada!",Toast.LENGTH_LONG).show();
                            ChangeFragment(new MapFragment());
                        }
                        else {
                            Toast.makeText(getContext(),"Problema en el registro!",Toast.LENGTH_LONG).show();
                        }


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

    @Override
    public void onClick(View v) {
        registrarHabitacion(edtDescrip.getText().toString(), edtPrecio.getText().toString(),"PNG","JPG","ICO","",edtCondiciones.getTextLocale().toString(),"Disponible",edtDireccion.getText().toString(),FBid);
    }

    private void ChangeFragment(Fragment fragment){
        FragmentManager fmanager = getFragmentManager();
        FragmentTransaction ftransaction = fmanager.beginTransaction();
        ftransaction.replace(R.id.content_frame, fragment).addToBackStack(null).commit();
    }
}
