package udea.edu.co.gasolfind.ControlFunctions;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.support.v4.content.ParallelExecutorCompat;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import udea.edu.co.gasolfind.BDClass.DBHelper_Gas_Station;
import udea.edu.co.gasolfind.BDClass.Gas_Station;
import udea.edu.co.gasolfind.Firebasegasolfind.FirebaseGasolfind;
import udea.edu.co.gasolfind.Interfaces.DBListener;
import udea.edu.co.gasolfind.R;
import udea.edu.co.gasolfind.fbprueba;

/**
 * Created by Juan Felipe Zuluaga on 7/04/2016.
 */
public class ConvertJSON implements DBListener {
    JSONObject jsonObject;
    double lat;
    double lng;
    public Array array = null;
    private Gas_Station gas_station;
    public FirebaseGasolfind firebaseGasolfind;
    public int action = 0;
    public float gasolina, acpm, gas, premium, rate;


    /*
    ConverJSON: recibe las ubicaciones actuales del usuario, para capturar la url que contiene
    el json con las coordenadas de los places a buscar en este caso es 'gas_station' "
     */
    public ConvertJSON(double lat, double lng, Context context){
        gas_station = new Gas_Station(context);
        firebaseGasolfind = new FirebaseGasolfind();
        BufferedReader reader = null;
        this.lat = lat;
        this.lng = lng;
        String placesSearchStr = "https://maps.googleapis.com/maps/api/place/nearbysearch/" +
                "json?location="+lat+","+lng+
                "&radius=1500&sensor=true" +
                "&types=gas_station&&establishment"+
                "&key=AIzaSyCHJPP9eHsur6Vu2wuHtyq4SFn_wx7bTME";
        Log.d("link", placesSearchStr);
        try {
            URL url = new URL(placesSearchStr);
            reader = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuffer buffer = new StringBuffer();
            int read;
            char[] chars = new char[1024];
            while ((read = reader.read(chars)) != -1)
                buffer.append(chars, 0, read);

            jsonObject = new JSONObject(buffer.toString());

        } catch (MalformedURLException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            if (reader != null){
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public JSONArray getJSONArray() throws JSONException {
        JSONArray jsonArray = jsonObject.getJSONArray("results");
        return jsonArray;
    }

    public void get_Markers(GoogleMap mMap, Marker []markers) throws JSONException {
        JSONObject place_obj;
        if (markers != null) {
            for (int i = 0; i < markers.length; i++) {
                if (markers[i] != null) {
                    markers[i].remove();
                }
            }
        }
        markers = new Marker[getJSONArray().length()];

        Location user_location = new Location("user");

        user_location.setLatitude(lat);
        user_location.setLongitude(lng);

        for(int i = 0; i < getJSONArray().length(); i++) {

            place_obj = getJSONArray().getJSONObject(i);
            double place_lat = place_obj.getJSONObject("geometry").getJSONObject("location").getDouble("lat");
            double place_lng = place_obj.getJSONObject("geometry").getJSONObject("location").getDouble("lng");
            String place_id = place_obj.get("place_id").toString();
            String place_name = place_obj.getString("name");
            String place_address = place_obj.getString("vicinity");
            LatLng LatLng = new LatLng(place_lat, place_lng);

            Marker marker = mMap.addMarker(new MarkerOptions().position(LatLng).title(place_name));
            Location location = new Location("");

            location.setLatitude(place_lat);
            location.setLongitude(place_lat);

            Float distance = user_location.distanceTo(location);

            Log.d("distance", String.valueOf(distance));


            Parametros datos = new Parametros(false, place_id, place_name, String.valueOf(place_lat), String.valueOf(place_lng), place_address, "0", "0", "0","0", "0");
            Cursor cursor = gas_station.load(place_id);

            if(cursor.getCount() == 0){
                Log.d("PrimerIF", "En el if");
                action = 0;
                gas_station.create(place_id, place_lat, place_lng, place_name, place_address, 0, 0, 0, 0, 0);
            }
            firebaseGasolfind.existeEstacion(this, datos);

            if(distance <= 9044000){
                marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.gas_station_green));
            }else if(distance > 9044000 && distance <= 9044500){
                marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.gas_station_yellow));
            }else{
                marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.gas_station_red));
            }
            markers[i] = marker;
        }
    }

    @Override
    public void onResult(Parametros datos, boolean existe) {
        // Parametros datos = (Parametros) object;
        // System.out.println("****" + datos.isExisteEstacion());

        if(existe){
            Log.d("En el segundo if", "En el if");

            datos.setGasolina(String.valueOf(firebaseGasolfind.obtenerPrecioRegular(this, datos)));
            datos.setGas(String.valueOf(firebaseGasolfind.obtenerPrecioGas(this, datos)));
            datos.setAcpm(String.valueOf(firebaseGasolfind.obtenerPrecioACPM(this, datos)));
            datos.setPremium(String.valueOf(firebaseGasolfind.obtenerPrecioPremium(this, datos)));
            gas_station.update_price_ACPM(datos.getPlace_id(), Float.parseFloat(datos.getAcpm()));
            gas_station.update_price_PremiumGasoline(datos.getPlace_id(), Float.parseFloat(datos.getPremium()));
            gas_station.update_price_RegularGasoline(datos.getPlace_id(), Float.parseFloat(datos.gasolina));
            gas_station.update_price_GAS(datos.getPlace_id(), Float.parseFloat(datos.getGas()));

        }else{
            Log.d("En el segundo if---", "En el else");
            //Cursor cursor = gas_station.load(place_id);
            //System.out.println("columnas " + cursor.getCount() + "place_id " + place_id);
            //firebaseGasolfind.registrarEstacion(place_id, "0","0",datos.getName(), "11", datos.getAddress(), String.valueOf(datos.getLat()),String.valueOf(datos.getLng()),"24 horas");
            //System.out.println("///// " + datos.getPlace_id());
            //gas_station.create(datos.getPlace_id(), Double.parseDouble(datos.getLat()), Double.parseDouble(datos.getLng()), datos.getName(), datos.getAddress(), 0, 0, 0, 0, 0);
        }
    }

    @Override
    public void onResultGas(Parametros datos, float precio) {

    }

    @Override
    public void onResultACPM(Parametros datos, float precio) {

    }

    @Override
    public void onResultPremium(Parametros datos, float precio) {

    }

    @Override
    public void onResultRegular(Parametros datos, float precio) {

    }
}
