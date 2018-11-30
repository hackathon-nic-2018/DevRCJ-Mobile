package com.hackcathon.nica.granestadia;

import android.content.SharedPreferences;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
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

import com.hackcathon.nica.granestadia.fragments.Fragment1;
import com.hackcathon.nica.granestadia.fragments.Fragment2;
import com.hackcathon.nica.granestadia.fragments.Fragment3;
import com.squareup.picasso.Picasso;

import java.util.Objects;


public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navView;
    private Toolbar appbar;
    private TextView nombreUsuario;
    private SharedPreferences pref;
    private ImageView userImage;

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
                            case R.id.menu_seccion_1:
                                fragment = new Fragment1();
                                fragmentTransaction = true;
                                break;
                            case R.id.menu_seccion_2:
                                fragment = new Fragment2();
                                fragmentTransaction = true;
                                break;
                            case R.id.menu_seccion_3:
                                fragment = new Fragment3();
                                fragmentTransaction = true;
                                break;
                            case R.id.menu_opcion_1:
                                Log.i("NavigationView", "Pulsada opción 1");
                                break;
                            case R.id.menu_opcion_2:
                                Log.i("NavigationView", "Pulsada opción 2");
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


}
