package udea.edu.co.gasolfind.Fragments;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import org.json.JSONException;

import java.io.IOException;

import udea.edu.co.gasolfind.BDClass.Gas_Station;
import udea.edu.co.gasolfind.ControlFunctions.ConvertJSON;
import udea.edu.co.gasolfind.R;
import udea.edu.co.gasolfind.Views.Content_View;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment implements  View.OnClickListener, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private int PLACE_PICKER_REQUEST = 1;
    private LocationManager locMan;
    private Marker userMarker;
    private Marker[] placeMarkers;
    Button btn1, btn2, btn3, btn4, btn5;
    ImageView edit1, edit2, edit3, edit4, edit5;
    private Gas_Station gas_station;

    public MapFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getActivity().getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        //Tomando permisos del API
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this.getContext())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(Places.GEO_DATA_API)
                    .addApi(Places.PLACE_DETECTION_API)
                    .addApi(LocationServices.API)
                    .addApi(AppIndex.API).build();
        }
        btn1 = (Button) view.findViewById(R.id.boton1);
        btn2 = (Button) view.findViewById(R.id.boton2);
        btn3 = (Button) view.findViewById(R.id.boton3);
        btn4 = (Button) view.findViewById(R.id.boton4);
        btn5 = (Button) view.findViewById(R.id.boton5);

        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
        btn5.setOnClickListener(this);

        return view;
    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.boton1:
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                break;
            case R.id.boton2:
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                break;
            case R.id.boton3:
                mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                break;
            case R.id.boton4:
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                break;
            case R.id.boton5:
                mMap.setMapType(GoogleMap.MAP_TYPE_NONE);
                break;
        }
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //Para activar mi localización
        if (ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.setMyLocationEnabled(true);
        mMap.setInfoWindowAdapter(new Content_View(this.getContext()));
    }

    public void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW,
                "Maps Page",
                Uri.parse("http://host/path"),
                Uri.parse("android-app://udea.edu.co.gasolfind/http/host/path")
        );
        AppIndex.AppIndexApi.start(mGoogleApiClient, viewAction);
    }

    public void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW,
                "Maps Page",
                Uri.parse("http://host/path"),
                Uri.parse("android-app://udea.edu.co.gasolfind/http/host/path")
        );
        AppIndex.AppIndexApi.end(mGoogleApiClient, viewAction);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == FragmentActivity.RESULT_OK) {
                Place place = PlacePicker.getPlace(this.getContext(), data);
                String toastMsg = String.format("Place: %s", place.getName());
                Toast.makeText(this.getContext(), toastMsg, Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        //Conexion al API
        if (ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            //Llevando mi posición actual al mapa
            LatLng myLocation = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            //Colocando la camara en mi localización
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 15));
            locMan = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            //Cadacierto tiempo esta actualizando la ubicación
            // locMan.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 30000, 100, this);
            try {
                updatePlaces();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void updatePlaces() throws IOException, JSONException {
        if (ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location lastLoc = locMan.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        double lat = lastLoc.getLatitude();
        double lng = lastLoc.getLongitude();
        LatLng lastLatLng = new LatLng(lat, lng);
        if(userMarker != null)userMarker.remove();
        mMap.animateCamera(CameraUpdateFactory.newLatLng(lastLatLng), 3000, null);
        ConvertJSON convertJSON = new ConvertJSON(lat, lng, this.getContext());
        //Recorriendo todo el json, y marcando en el mapa
        convertJSON.get_Markers(mMap, placeMarkers);
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.e("prueba", "Entra al suspend");

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e("prueba", "Entra al conec failed");
    }
}
