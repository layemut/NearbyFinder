package android.com.semantica.inr;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class dbAdapter {
    public static final String KEY_ROWID = "_id";
    public static final String KEY_VALUE = "value";
    public static final String KEY_DATE = "date";



    private static final String DATABASE_NAME = "past";
    private static final String DATABASE_TABLE = "Entries";
    private static final String DATABASE_TABLE_ROUTE="Routes";
    private static final int DATABASE_VERSION = 1;

    private DbHelper helper;
    private SQLiteDatabase database;
    private final Context context;

    public dbAdapter(Context c) {
        context = c;
    }

    public dbAdapter open() throws SQLException {
        helper = new DbHelper(context);
        database = helper.getWritableDatabase();
        return this;
    }

    public void close() {
        helper.close();
    }

    public long insert(Double sqlvalue) {
        String date = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date());
        ContentValues cv = new ContentValues();
        cv.put(KEY_VALUE, sqlvalue);
        cv.put(KEY_DATE, date);
        return database.insert(DATABASE_TABLE, null, cv);
    }

    public Cursor getAllRecords() {

        return database.query(DATABASE_TABLE, new String[]{KEY_ROWID, KEY_VALUE, KEY_DATE}
                , null, null, null, null, null);
    }

    /*public boolean getARecord() {
        String query = "SELECT * FROM regular WHERE " + KEY_NAME + "='" + sqlName + "' AND " + KEY_PASSWORD + "='" + sqlPass + "'";
        Cursor c = database.rawQuery(query, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }*/

    /*public boolean isNull(){

        String where ="value=?";
        String[] args = new String[]{sqlName};
        Cursor c = database.query(DATABASE_TABLE,null
                ,where, args, null, null, null);
        if(c.moveToFirst()){
            return true;
        }else{
            return false;
        }

    }*/
    /*public void updateARecord(String sqlName, String sqlMail,String sqlPass, String sqlBirth, String sqlPhone, String cName, String cPASS) {
        ContentValues cv = new ContentValues();
        cv.put("name",sqlName);
        cv.put("email",sqlMail);
        cv.put("password",sqlPass);
        cv.put("birthdate",sqlBirth);
        cv.put("phone",sqlPhone);
        String where="name='"+cName+"' AND password='"+cPASS+"'";
        database.update("regular",cv,where,null);
    }*/

    public void deleteAllTable(){

        database.delete(DATABASE_TABLE,null,null);
    }
    /*public void deleteARecord(String sqlName,String sqlPass){
        String where = "name='"+sqlName+"' AND password='"+sqlPass+"'";
        database.delete(DATABASE_TABLE,where,null);
    }*/

    private static class DbHelper extends SQLiteOpenHelper {
        public DbHelper(Context c) {
            super(c, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL("CREATE TABLE " + DATABASE_TABLE + " (" +
                            KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                            KEY_VALUE + " DOUBLE NOT NULL, " +
                            KEY_DATE + " TEXT NOT NULL);"
            );
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
            onCreate(sqLiteDatabase);
        }
    }
}
