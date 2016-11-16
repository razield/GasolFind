package udea.edu.co.gasolfind.Views;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import org.json.JSONObject;

import udea.edu.co.gasolfind.Fragments.AboutFragment;
import udea.edu.co.gasolfind.Fragments.ListFragment;
import udea.edu.co.gasolfind.R;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private ListView listView;
    private ActionBar actionBar;
    private DrawerLayout drawerLayout;
    public static final String TAG = "MyTag";
    private String host = "https://gasolfindapp.herokuapp.com/";
    private Cache cache;
    private Network network;
    private RequestQueue mRequestQueue;

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private LocationManager locMan;

    private Marker userMarker;
    private Marker[] placeMarkers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        Fragment fragment;

        listView = (ListView) findViewById( R.id.left_frame );
        actionBar = getSupportActionBar();
        drawerLayout = (DrawerLayout) findViewById( R.id.container_main );

        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences( getApplicationContext() );

        updateMenu();

        if (sharedPreferences.getBoolean( "show_map", true )) {
            fragment = initMapFragment();
            initGoogleClient();
            listView.setItemChecked( 0, true );
            if (actionBar != null) {
                String title = getResources().getStringArray( R.array.options )[0];
                actionBar.setTitle( title );
            }
        } else {
            fragment = new ListFragment();
            listView.setItemChecked( 1, true );
            if (actionBar != null) {
                String title = getResources().getStringArray( R.array.options )[1];
                actionBar.setTitle( title );
            }
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace( R.id.main_frame, fragment ).commit();
        listView.setOnItemClickListener( this );
        cache = new DiskBasedCache( getCacheDir(), 1024 * 1024 );
        network = new BasicNetwork( new HurlStack() );
        mRequestQueue = new RequestQueue( cache, network );
        mRequestQueue.start();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent;
        Fragment fragment = null;
        SharedPreferences prefs = getSharedPreferences( "MisPreferencias", MODE_PRIVATE );

        switch (position) {
            case 0:
                fragment = initMapFragment();
                break;
            case 1:
                fragment = new ListFragment();
                break;
            case 2:
                intent = new Intent( this, SettingsActivity.class );
                startActivity( intent );
                return;
            case 3:
                fragment = new AboutFragment();
                break;
            case 4:
                intent = new Intent( this, LoginActivity.class );
                startActivity( intent );
                return;
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace( R.id.main_frame, fragment ).commit();
        listView.setItemChecked( position, true );
        String title = getResources().getStringArray( R.array.options )[position];
        if (actionBar != null) {
            actionBar.setTitle( title );
        }
        drawerLayout.closeDrawer( listView );
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == 16908332) {
            if (drawerLayout.isDrawerOpen( listView )) {
                drawerLayout.closeDrawer( listView );
            } else {
                drawerLayout.openDrawer( listView );
            }
        }

        return super.onOptionsItemSelected( item );
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateMenu();

        initGoogleClient();
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
        }
    }

    public void updateMenu() {
        listView.setAdapter( ArrayAdapter.createFromResource( this, R.array.options,
                android.R.layout.simple_spinner_dropdown_item ) );

        listView.setAdapter( ArrayAdapter.createFromResource( this, R.array.options_logged,
                android.R.layout.simple_spinner_dropdown_item ) );
    }

    public void getStations() {
        String url = host + "fuelstation";
        JsonObjectRequest jsObjRequest = new JsonObjectRequest( Request.Method.POST, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                Log.e( "respuesta", response.toString() );
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub
                Log.e( "respuesta", "error" );
            }
        } );

        mRequestQueue.add( jsObjRequest );
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //Para activar mi localización
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.setMyLocationEnabled(true);
        mMap.setInfoWindowAdapter(new Content_View(getApplicationContext()));
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (mLastLocation != null) {
            LatLng myLocation = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 15));
            locMan = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            try {
                // updatePlaces();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    protected SupportMapFragment initMapFragment() {
        SupportMapFragment mapFragment = new SupportMapFragment();
        mapFragment.getMapAsync(this);

        return mapFragment;
    }

    protected void initGoogleClient() {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(Places.GEO_DATA_API)
                    .addApi(Places.PLACE_DETECTION_API)
                    .addApi(LocationServices.API)
                    .addApi(AppIndex.API).build();
        }
    }
}
