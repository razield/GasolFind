package udea.edu.co.gasolfind;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
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
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private int PLACE_PICKER_REQUEST = 1;
    //instance variables for Marker icon drawable resources
    private int userIcon, foodIcon, drinkIcon, shopIcon, otherIcon;

    //***************gitHub examble!!!!!!
    //the map
    private GoogleMap theMap;

    //location manager
    private LocationManager locMan;

    //user marker
    private Marker userMarker;

    //places of interest
    private Marker[] placeMarkers;
    //max
    private final int MAX_PLACES = 20;//most returned from google
    //marker options
    private MarkerOptions[] places;

    private boolean updateFinished = true;

    //********************************************************************


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        /*//get drawable IDs***************************************
        userIcon = R.drawable.yellow_point;
        foodIcon = R.drawable.red_point;
        drinkIcon = R.drawable.blue_point;
        shopIcon = R.drawable.green_point;
        otherIcon = R.drawable.purple_point;*/


        //Tomando permisos del API
        if (mGoogleApiClient == null) {
            // ATTENTION: This "addApi(AppIndex.API)"was auto-generated to implement the App Indexing API.
            // See https://g.co/AppIndexing/AndroidStudio for more information.
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(Places.GEO_DATA_API)
                    .addApi(Places.PLACE_DETECTION_API)
                    .addApi(LocationServices.API)
                    .addApi(AppIndex.API).build();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //Como crear un marcador
        /*
            LatLng marca1 = new LatLng(mLastLocation.getLatitude()+0.005, mLastLocation.getLongitude());
            mMap.addMarker(new MarkerOptions().position(marca1).title("Marca 1"));
        */

        //Para activar mi localización
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        //El tipo de mapa
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        //Boton de localización
        mMap.setMyLocationEnabled(true);


    }

    //Guia del Google*************************************
    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Maps Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://udea.edu.co.gasolfind/http/host/path")
        );
        AppIndex.AppIndexApi.start(mGoogleApiClient, viewAction);
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Maps Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://udea.edu.co.gasolfind/http/host/path")
        );
        AppIndex.AppIndexApi.end(mGoogleApiClient, viewAction);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(this, data);
                String toastMsg = String.format("Place: %s", place.getName());
                Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
            }
        }
    }
    //**********************************************************

    @Override
    public void onConnected(Bundle bundle) {
        Log.e(null, "Entra al onConnect");
        //Conexion al API
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            //Llevando mi posición actual al mapa
            LatLng myLocation = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            //Colocando la camara en mi localización
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 15));
            locMan = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            //Cadacierto tiempo esta actualizando la ubicación
            // locMan.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 30000, 100, this);
            try {
                updatePlaces();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void updatePlaces(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location lastLoc = locMan.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        double lat = lastLoc.getLatitude();
        double lng = lastLoc.getLongitude();

        //Creando la posición
        LatLng lastLatLng = new LatLng(lat, lng);

        //removiendo alguna marca existente
        if(userMarker != null)userMarker.remove();
        //Creando y modificando las propiedades de las marcas
        userMarker = mMap.addMarker(new MarkerOptions().position(lastLatLng).title("Aqui estas"));

        //moviendo la localizacion
        mMap.animateCamera(CameraUpdateFactory.newLatLng(lastLatLng), 3000, null);
        //HAciendo el query para el llamado json de los places
       // String placesSearchStr = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=-33.8670522,151.1957362&radius=500&types=food&name=cruise&key=AIzaSyCHJPP9eHsur6Vu2wuHtyq4SFn_wx7bTME";
        String placesSearchStr = "https://maps.googleapis.com/maps/api/place/nearbysearch/" +
                "json?location="+lat+","+lng+
                "&radius=10000&sensor=true" +
                "&types=gas_station"+
                "&key=AIzaSyCHJPP9eHsur6Vu2wuHtyq4SFn_wx7bTME";
        //Link con el json
        Log.d("test", placesSearchStr);

        //Se debe convertir el respond del server a json y leerlo directamente





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
