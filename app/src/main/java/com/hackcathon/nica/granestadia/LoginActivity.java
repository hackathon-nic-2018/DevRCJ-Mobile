package com.hackcathon.nica.granestadia;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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


public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private TextView info;
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private ImageView userImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

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

        userImg = findViewById(R.id.img_user2);



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

                                    Intent i = new Intent( getApplicationContext(), PerfilActivity.class);
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

}


