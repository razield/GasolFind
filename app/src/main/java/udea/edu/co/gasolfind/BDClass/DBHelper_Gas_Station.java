package udea.edu.co.gasolfind.BDClass;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Juan Felipe Zuluaga on 5/05/2016.
 */
public class DBHelper_Gas_Station extends SQLiteOpenHelper {

    private static final String DB_NAME = "gasStation.sqlite";
    public static final int DB_SCHEME_VERSION = 1;


    public DBHelper_Gas_Station(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public DBHelper_Gas_Station(Context context) {
        super(context, DB_NAME, null, DB_SCHEME_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Gas_Station.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Gas_Station.TABLE_NAME);
        db.execSQL(Gas_Station.CREATE_TABLE);

    }

}
