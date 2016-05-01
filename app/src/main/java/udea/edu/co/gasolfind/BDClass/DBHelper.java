package udea.edu.co.gasolfind.BDClass;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Juan Felipe Zuluaga on 12/04/2016.
 * Clase que crea la Base de datos
 */
public class DBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "gasolfind.database";
    private static final int DB_SCHEME_VERSION = 1;

    public DBHelper(Context context){
        super(context, DB_NAME, null, DB_SCHEME_VERSION);

    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase){
        sqLiteDatabase.execSQL(Gas_Station.CREATE_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion){

    }
}
