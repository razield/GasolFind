package udea.edu.co.gasolfind.BDClass;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Juan Felipe Zuluaga on 12/04/2016.
 * Modelo DB para  the Gas_Station modo standAlone
 */
public  class Gas_Station {
    private DBHelper dbHelper;
    private SQLiteDatabase sqLiteDatabase;
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


    public void DataBaseManager(Context context) {
        dbHelper = new DBHelper(context);
        sqLiteDatabase = dbHelper.getWritableDatabase();
    }


    private ContentValues getValues_Gas_Station(String id, double lat, double lng, String name, String address, float pgasoline, float rgasoline, float gas, float acpm, float rate){
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_ID, id);
        contentValues.put(COLUMN_NAME, name);
        contentValues.put(COLUMN_LAT, lat);
        contentValues.put(COLUMN_LNG, lng);
        contentValues.put(COLUMN_ADDRESS, address);
        contentValues.put(COLUMN_P_GASOLINE, pgasoline);
        contentValues.put(COLUMN_R_GASOLINE, rgasoline);
        contentValues.put(COLUMN_GAS, gas);
        contentValues.put(COLUMN_ACPM, acpm);
        contentValues.put(COLUMN_RATE, rate);
        return contentValues;
    }
    //Insertando en la BD de Gas_Station
    //INSERT FUNCTIONS:
    public void insert_Gas_Station(String id, String name, double lat, double lng){
        sqLiteDatabase.insert(TABLE_NAME,null, getValues_Gas_Station(id, lat, lng, name, "no address", -1, -1, -1, -1, -1) );
    }

    //UPDATES FUNCTIONS: para actualizar alguno de los datos de la BD
    public void update_Address(String id, String address){
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_ADDRESS, address);
        sqLiteDatabase.update(TABLE_NAME, contentValues, COLUMN_ID + "=?", new String[]{id});
    }
    public void update_PGasoline(String id, float pgasoline){
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_P_GASOLINE, pgasoline);
        sqLiteDatabase.update(TABLE_NAME, contentValues, COLUMN_ID + "=?", new String[]{id});
    }
    public void update_RGasoline(String id, float rgasoline){
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_R_GASOLINE, rgasoline);
        sqLiteDatabase.update(TABLE_NAME, contentValues, COLUMN_ID + "=?", new String[]{id});
    }
    public void update_Gas(String id, float gas){
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_GAS, gas);
        sqLiteDatabase.update(TABLE_NAME, contentValues, COLUMN_ID + "=?", new String[]{id});
    }
    public void update_ACPM(String id, float acpm){
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_ACPM, acpm);
        sqLiteDatabase.update(TABLE_NAME, contentValues, COLUMN_ID + "=?", new String[]{id});
    }
    public void update_Rate(String id, float  rate){
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_RATE, rate);
        sqLiteDatabase.update(TABLE_NAME, contentValues, COLUMN_ID + "=?", new String[]{id});
    }
    public  void delete_Gas_Station(String id) {
        sqLiteDatabase.delete(TABLE_NAME, COLUMN_ID + "=?", new String[]{id});
    }
    public boolean exist_Gas_Station(String id){
        String[] args = new String[] {id};

        Cursor cursor = sqLiteDatabase.query(TABLE_NAME,null,COLUMN_ID +"=?", args,null,null,null );
        if(cursor != null){
            return true;
        }else {
            return false;
        }
    }
    public Cursor gas_Station(String id){
        String[] args = new String[]{id};
        String[] arg = new String[]{COLUMN_NAME, COLUMN_ADDRESS,  COLUMN_P_GASOLINE, COLUMN_R_GASOLINE, COLUMN_GAS, COLUMN_ACPM, COLUMN_RATE};
        return  sqLiteDatabase.query(TABLE_NAME,arg,COLUMN_ID +"=?", args,null,null,null );
    }
}