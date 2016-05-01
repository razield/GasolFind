package udea.edu.co.gasolfind.ControlFunctions;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
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
import java.util.List;
import java.util.Objects;

import udea.edu.co.gasolfind.BDClass.Gas_Station;
import udea.edu.co.gasolfind.R;

/**
 * Created by Juan Felipe Zuluaga on 7/04/2016.
 */
public class ConvertJSON {
    JSONObject jsonObject;
    double lat;
    double lng;
    public Array array = null;

    /*
    ConverJSON: recibe las ubicaciones actuales del usuario, para capturar la url que contiene
    el json con las coordenadas de los places a buscar en este caso es 'gas_station' "
     */
    public ConvertJSON(double lat, double lng){
        BufferedReader reader = null;
        this.lat = lat;
        this.lng = lng;
        String placesSearchStr = "https://maps.googleapis.com/maps/api/place/nearbysearch/" +
                "json?location="+lat+","+lng+
                "&radius=2000&sensor=true" +
                "&types=gas_station&&establishment"+
                "&key=AIzaSyCHJPP9eHsur6Vu2wuHtyq4SFn_wx7bTME";
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
    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            return null;
        }
    }


    public void get_Markers(GoogleMap mMap, Marker []markers, Gas_Station gas_station) throws JSONException {
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
            String place_id = place_obj.getString("id");
            Log.d("id ", place_id);

            double place_lat = place_obj.getJSONObject("geometry").getJSONObject("location").getDouble("lat");
            double place_lng = place_obj.getJSONObject("geometry").getJSONObject("location").getDouble("lng");
            String place_name = place_obj.getString("name");
            LatLng LatLng = new LatLng(place_lat, place_lng);
            Marker marker = mMap.addMarker(new MarkerOptions().position(LatLng).title(place_name));
            Location location = new Location("");

            location.setLatitude(place_lat);
            location.setLongitude(place_lat);

            Float distance = user_location.distanceTo(location);

            Log.d("distance", String.valueOf(distance));
            String string = "11111";
            marker.setSnippet("Esto es algo raro" + string);

            if(distance < 9044000){
                marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.gas_station_green));
            }else if(distance >= 9044000 && distance < 9045000){
                marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.gas_station_yellow));
            }else{
                marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.gas_station_red));
            }

            markers[i] = marker;
        }
    }

}