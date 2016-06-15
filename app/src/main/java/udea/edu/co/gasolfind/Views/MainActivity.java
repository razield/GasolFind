package udea.edu.co.gasolfind.Views;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Map;

import udea.edu.co.gasolfind.Fragments.AboutFragment;
import udea.edu.co.gasolfind.Fragments.ListFragment;
import udea.edu.co.gasolfind.Fragments.MapFragment;
import udea.edu.co.gasolfind.R;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener,
        FirebaseAuth.AuthStateListener {

    private ListView listView;

    private ActionBar actionBar;

    private DrawerLayout drawerLayout;

    private FirebaseUser firebaseUser;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Fragment fragment;

        listView = (ListView) findViewById(R.id.left_frame);

        actionBar = getSupportActionBar();

        drawerLayout = (DrawerLayout) findViewById(R.id.container_main);

        firebaseAuth = FirebaseAuth.getInstance();

        firebaseUser = firebaseAuth.getCurrentUser();

        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(getApplicationContext());

        updateMenu();

        if (sharedPreferences.getBoolean("first_time", true)) {
            Intent intent = new Intent(this, InitialActivity.class);

            startActivity(intent);

            return;
        } else if (sharedPreferences.getBoolean("show_map", true)) {
            fragment = new MapFragment();

            listView.setItemChecked(0, true);

            if (actionBar != null) {
                String title = getResources().getStringArray(R.array.options)[0];

                actionBar.setTitle(title);
            }
        } else {
            fragment = new ListFragment();

            listView.setItemChecked(1, true);

            if (actionBar != null) {
                String title = getResources().getStringArray(R.array.options)[1];

                actionBar.setTitle(title);
            }
        }

        FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction().replace(R.id.main_frame, fragment).commit();

        listView.setOnItemClickListener(this);

        firebaseAuth.addAuthStateListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent;
        Fragment fragment = null;

        switch (position) {
            case 0:
                /*
                fragment = new MapFragment();

                break;
                */

                intent = new Intent(this, MapsActivity.class);

                startActivity(intent);

                return;
            case 1:
                fragment = new ListFragment();
                break;
            case 2:
                intent = new Intent(this, SettingsActivity.class);

                startActivity(intent);

                return;
            case 3:
                fragment = new AboutFragment();
                break;
            case 4:
                if (firebaseUser != null) {
                    firebaseAuth.signOut();

                    return;
                } else {
                    intent = new Intent(this, LoginActivity.class);

                    startActivity(intent);

                    return;
                }
        }

        FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction().replace(R.id.main_frame, fragment).commit();

        listView.setItemChecked(position, true);

        String title = getResources().getStringArray(R.array.options)[position];

        if (actionBar != null) {
            actionBar.setTitle(title);
        }

        drawerLayout.closeDrawer(listView);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == 16908332) {
            if (drawerLayout.isDrawerOpen(listView)) {
                drawerLayout.closeDrawer(listView);
            } else {
                drawerLayout.openDrawer(listView);
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        updateMenu();
    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        firebaseUser = firebaseAuth.getCurrentUser();

        updateMenu();
    }

    public void updateMenu() {
        if (firebaseUser == null) {
            listView.setAdapter(ArrayAdapter.createFromResource(this, R.array.options,
                    android.R.layout.simple_spinner_dropdown_item));
        } else {
            listView.setAdapter(ArrayAdapter.createFromResource(this, R.array.options_logged,
                    android.R.layout.simple_spinner_dropdown_item));
        }
    }
}
