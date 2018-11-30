package com.hackcathon.nica.granestadia;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.login.LoginManager;
import com.hackcathon.nica.granestadia.fragments.Fragment1;
import com.hackcathon.nica.granestadia.fragments.Fragment2;
import com.hackcathon.nica.granestadia.fragments.FragmentUsuario;
import com.hackcathon.nica.granestadia.fragments.MapFragment;
import com.squareup.picasso.Picasso;

import java.util.Objects;


public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navView;
    public static Toolbar appbar;
    private TextView nombreUsuario;
    private SharedPreferences pref;
    private ImageView userImage;
    public static String page;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        appbar = findViewById(R.id.appbar);
        setSupportActionBar(appbar);

        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(R.drawable.menu);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        pref = this.getSharedPreferences("MisPreferencias", MODE_PRIVATE);

        String txtNombre = pref.getString("nombre","name");
        String url_img = pref.getString("url_img","url");


        drawerLayout = findViewById(R.id.drawer_layout);

        navView = findViewById(R.id.navview);

        View headerView = navView.getHeaderView(0);

        nombreUsuario = headerView.findViewById(R.id.head_user);

        userImage = headerView.findViewById(R.id.h_userImage);

        nombreUsuario.setText(txtNombre);

        Picasso.with(getApplicationContext())
                .load(url_img)
                .into(userImage);

        navView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {

                        boolean fragmentTransaction = false;
                        Fragment fragment = null;

                        switch (menuItem.getItemId()) {
                            case R.id.menu_mapa:
                                fragment = new MapFragment();
                                fragmentTransaction = true;
                                break;
                            case R.id.menu_favoritos:
                                fragment = new Fragment2();
                                fragmentTransaction = true;
                                break;
                            case R.id.menu_reservaciones:
                                fragment = new Fragment1();
                                fragmentTransaction = true;
                                break;
                            case R.id.menu_usuario:
                                fragment = new FragmentUsuario();
                                fragmentTransaction = true;
                                break;
                            case R.id.menu_salir:
                                Log.i("NavigationView", "Pulsada opción 2");
                                LoginManager.getInstance().logOut();
                                Intent i = new Intent( getApplicationContext(), LoginActivity.class);
                                startActivity(i);
                                break;
                        }

                        if(fragmentTransaction) {
                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.content_frame, fragment)
                                    .commit();

                            menuItem.setChecked(true);
                            getSupportActionBar().setTitle(menuItem.getTitle());
                        }

                        drawerLayout.closeDrawers();

                        return true;
                    }
                });
        ChangeFragment(new MapFragment());

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
            //...
        }

        return super.onOptionsItemSelected(item);
    }

    private void ChangeFragment(Fragment fragment){
        FragmentManager fmanager = getSupportFragmentManager();
        FragmentTransaction ftransaction = fmanager.beginTransaction();
        ftransaction.replace(R.id.content_frame, fragment).addToBackStack(null).commit();
    }



    public void setActionBarTitle(String your_title) {
        getSupportActionBar().setTitle(your_title);
    }

    @Override
    public void onBackPressed() {


        if (page.equals("Mapa")){
            super.onBackPressed();
        }

    }

    public static void updatePage(String pagina){
        page = pagina;
    }


}
