package com.hackcathon.nica.granestadia;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static com.hackcathon.nica.granestadia.datos.Info.ServUrlInsert;


public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private TextView info;
    private LoginButton loginButton;
    private CallbackManager callbackManager;

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        pref = this.getSharedPreferences("MisPreferencias", MODE_PRIVATE);
        editor = pref.edit();


        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();

        if (isLoggedIn){
            Intent i = new Intent( getApplicationContext(), MainActivity.class);
            startActivity(i);
            finish();
        }
        else {
            //Toast.makeText(this,"No estas logeado",Toast.LENGTH_SHORT).show();
        }

        FacebookSdk.sdkInitialize(getApplicationContext());

        AppEventsLogger.activateApp(this);

        try {
            @SuppressLint("PackageManagerGetSignatures") PackageInfo info = getPackageManager().getPackageInfo(
                    "com.hackcathon.nica.granestadia",
                    PackageManager.GET_SIGNATURES);
            for (android.content.pm.Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException ignored) {

        }


        callbackManager = CallbackManager.Factory.create();

        info = findViewById(R.id.info);
        loginButton = findViewById(R.id.login_button);

        loginButton.setReadPermissions(Arrays.asList(
                "public_profile", "email"));



        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

//                GraphRequest.newMeRequest(
//                        loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
//                            @Override
//                            public void onCompleted(JSONObject me, GraphResponse response) {
//
//
//                                if (response.getError() != null) {
//                                    // handle error
//                                } else {
//                                    // get email and id of the user
//                                    String user_email = me.optString("email");
//                                    String id = me.optString("id");
//                                    String user_name = me.optString("name");
//                                    String link = me.optString("link");
//
//                                    info.setText(user_name + "\n" + link + "\n" + user_email + "\n" + id);
//
//                                }
//                            }
//                        }).executeAsync();

                AccessToken accessToken = loginResult.getAccessToken();
                Profile profile = Profile.getCurrentProfile();

                // Facebook Email address
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(
                                    JSONObject object,
                                    GraphResponse response) {
                                Log.v("LoginActivity Response ", response.toString());



                                try {
                                    String name = object.getString("name");

                                    String FEmail = object.getString("email");

                                    String FEid = object.getString("id");


                                    Log.v("Email = ", " " + FEmail+ "Gender ");

                                    Toast.makeText(getApplicationContext(), "Bienvenido: " + name, Toast.LENGTH_LONG).show();

                                    //info.setText("Nombre: " + name + "\n" + "e-mail: " + FEmail);

                                    editor.putString("nombre",name);
                                    editor.putString("email",FEmail);
                                    editor.putString("url_img","https://graph.facebook.com/" + FEid+ "/picture?type=large");
                                    editor.putString("Fid",FEid);
                                    editor.putBoolean("isLogin",true);
                                    editor.apply();

                                    if (pref.getBoolean("firstrun", true)) {
                                        // Do first run stuff here then set 'firstrun' as false
                                        // using the following line to edit/commit prefs
                                        pref.edit().putBoolean("firstrun", false).apply();
                                        registrarUsuario(name,"https://graph.facebook.com/" + FEid+ "/picture?type=large",FEmail,FEid);
                                    }




                                    Intent i = new Intent( getApplicationContext(), MainActivity.class);
                                    i.putExtra("nombre", name);
                                    i.putExtra("email", FEmail);
                                    i.putExtra("id", FEid);
                                    i.putExtra("image","https://graph.facebook.com/" + FEid+ "/picture?type=large");
                                    startActivity(i);


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender, birthday");
                request.setParameters(parameters);
                request.executeAsync();


            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onCancel() {
                info.setText("Login attempt canceled.");
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onError(FacebookException e) {
                info.setText("Login attempt failed."+e.toString());
            }
        });




    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void registrarUsuario(String nombre, String foto, String email, String Fid){


        StringRequest stringRequest = new StringRequest(Request.Method.GET, ServUrlInsert+"nombres="+nombre+"&apellidos="+Fid+"&email="+email+"&foto="+foto,
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

                        Toast.makeText(LoginActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                        Log.i("Error: ", error.toString());
                    }
                }){



        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }



}


