package udea.edu.co.gasolfind.BDClass;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;

import com.google.android.gms.maps.model.LatLng;


/**
 * Created by Juan Felipe Zuluaga on 12/04/2016.
 * Modelo DB para  the Gas_Station modo standAlone
 */
public  class Gas_Station  {


    public static final String TABLE_NAME = "gas_station"; // nombre de la tabla
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_LAT = "_lat";
    public static final String COLUMN_LNG = "_lng";
    public static final String COLUMN_NAME = "_name";
    public static final String COLUMN_ADDRESS = "_address";
    public static final String COLUMN_P_GASOLINE = "_p_gasoline";    //premium gasoline
    public static final String COLUMN_R_GASOLINE = "_r_gasoline";    //regular gasoline
    public static final String COLUMN_GAS = "_gas";
    public static final String COLUMN_ACPM = "_acpm";
    public static final String COLUMN_RATE = "_rate";

    public static final String CREATE_TABLE = "create table " + TABLE_NAME + " ("
            + COLUMN_ID + " TEXT not null primary key, "
            + COLUMN_NAME + " TEXT not null, "
            + COLUMN_LAT + " REAL not null, "
            + COLUMN_LNG + " REAL not null, "
            + COLUMN_ADDRESS + " TEXT, "
            + COLUMN_P_GASOLINE + " REAL, "
            + COLUMN_R_GASOLINE + " REAL,"
            + COLUMN_GAS + " REAL, "
            + COLUMN_ACPM + " REAL, "
            + COLUMN_RATE + " REAL);";

    private DBHelper_Gas_Station dbHelper_gas_station;
    private SQLiteDatabase db;

    public Gas_Station(Context context){
        dbHelper_gas_station = new DBHelper_Gas_Station(context);
        db = dbHelper_gas_station.getWritableDatabase();

    }

    private ContentValues generateContentValues(String id, double lat, double lng, String name, String address, float p_pgasoline, float p_rgasoline, float p_gas, float p_acpm, float rate){

        ContentValues values = new ContentValues();

        values.put(COLUMN_ID, id);
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_LAT, lat);
        values.put(COLUMN_LNG, lng);
        values.put(COLUMN_ADDRESS, address);
        values.put(COLUMN_P_GASOLINE, p_pgasoline);
        values.put(COLUMN_R_GASOLINE, p_rgasoline);
        values.put(COLUMN_GAS, p_gas);
        values.put(COLUMN_ACPM, p_acpm);
        values.put(COLUMN_RATE, rate);

        return values;
    }




    public void create(String id, double lat, double lng, String name, String address, float p_pgasoline, float p_rgasoline, float p_gas, float p_acpm, float rate){
        db.insert(TABLE_NAME, null, generateContentValues(id, lat, lng, name, address, p_pgasoline, p_rgasoline, p_gas, p_acpm, rate));
    }

    public void delete(String id) {
        db.delete(TABLE_NAME, COLUMN_ID + "=?", new String[]{id});
    }

    public void update_price_RegularGasoline(String id, float p_rgasoline){
        ContentValues values = new ContentValues();
        values.put(COLUMN_R_GASOLINE, p_rgasoline);

        db.update(TABLE_NAME, values, COLUMN_ID + "=?", new String[]{id});
    }

    public void update_price_PremiumGasoline(String id, float p_pgasoline){
        ContentValues values = new ContentValues();
        values.put(COLUMN_P_GASOLINE, p_pgasoline);

        db.update(TABLE_NAME, values, COLUMN_ID + "=?", new String[]{id});
    }
    public void update_price_ACPM(String id, float acpm){
        ContentValues values = new ContentValues();
        values.put(COLUMN_GAS, acpm);

        db.update(TABLE_NAME, values, COLUMN_ID + "=?", new String[]{id});
    }

    public void update_price_GAS(String id, float gas){
        ContentValues values = new ContentValues();
        values.put(COLUMN_GAS, gas);

        db.update(TABLE_NAME, values, COLUMN_ID + "=?", new String[]{id});
    }


    public Cursor load_For_Location(double lat, double lng){
        String[] columns = new String[]{
                COLUMN_ID,
                COLUMN_NAME,
                COLUMN_LAT,
                COLUMN_LNG,
                COLUMN_ADDRESS,
                COLUMN_P_GASOLINE,
                COLUMN_R_GASOLINE,
                COLUMN_GAS,
                COLUMN_ACPM,
                COLUMN_RATE,
        };
        String query = COLUMN_LAT + "=? AND " + COLUMN_LNG + "=?";

        return db.query(TABLE_NAME, columns, query, new String[]{String.valueOf(lat), String.valueOf(lng)}, null, null, null);
    }

    public Cursor load(String id){
        String[] columns = new String[]{
                COLUMN_ID,
                COLUMN_NAME,
                COLUMN_LAT,
                COLUMN_LNG,
                COLUMN_ADDRESS,
                COLUMN_P_GASOLINE,
                COLUMN_R_GASOLINE,
                COLUMN_GAS,
                COLUMN_ACPM,
                COLUMN_RATE,
        };
        String query = COLUMN_ID + "=?";

        return db.query(TABLE_NAME, columns, query, new String[]{id}, null, null, null);
    }
}