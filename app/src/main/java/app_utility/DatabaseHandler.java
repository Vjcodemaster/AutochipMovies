package app_utility;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;


public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "AUTOCHIP_MOVIES";

    private static final String TABLE_ACCOUNT_INFORMATION = "TABLE_ACCOUNT_INFORMATION";

    private static final String KEY_ID = "_id";

    private static final String KEY_ACCOUNT_ODOO_ID = "KEY_ACCOUNT_ODOO_ID";
    private static final String KEY_ACCOUNT_NAME = "KEY_ACCOUNT_NAME";
    private static final String KEY_ACCOUNT_LOGIN_URL = "KEY_ACCOUNT_LOGIN_URL";
    private static final String KEY_USER_ACCOUNT_ID = "KEY_USER_ACCOUNT_ID";
    private static final String KEY_USER_ACCOUNT_PASSWORD = "KEY_USER_ACCOUNT_PASSWORD";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_ACCOUNT_INFORMATION_TABLE = "CREATE TABLE " + TABLE_ACCOUNT_INFORMATION + "("
                + KEY_ID + " INTEGER PRIMARY KEY, "
                + KEY_ACCOUNT_ODOO_ID + " INTEGER, "
                + KEY_ACCOUNT_NAME + " TEXT, "
                + KEY_ACCOUNT_LOGIN_URL + " TEXT, "
                + KEY_USER_ACCOUNT_ID + " TEXT, "
                + KEY_USER_ACCOUNT_PASSWORD + " TEXT)";

        db.execSQL(CREATE_ACCOUNT_INFORMATION_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        //db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECENT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACCOUNT_INFORMATION);

        // Create tables again
        onCreate(db);
    }

    public void addDataToAccountsTable(DataBaseHelper dataBaseHelper) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        //values.put(KEY_ID, dataBaseHelper.getID()); // Contact Name
        values.put(KEY_ACCOUNT_ODOO_ID, dataBaseHelper.get_account_odoo_id());
        values.put(KEY_ACCOUNT_NAME, dataBaseHelper.get_account_name());
        values.put(KEY_ACCOUNT_LOGIN_URL, dataBaseHelper.get_account_login_url()); // Contact Phone

        // Inserting Row
        db.insert(TABLE_ACCOUNT_INFORMATION, null, values);

        db.close(); // Closing database connection
    }

    /*public void addIDAndPasswordToAccountsTable(DataBaseHelper dataBaseHelper) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        //values.put(KEY_ID, dataBaseHelper.getID()); // Contact Name
        values.put(KEY_USER_ACCOUNT_ID, dataBaseHelper.get_user_account_id());
        values.put(KEY_USER_ACCOUNT_PASSWORD, dataBaseHelper.get_user_account_password());

        // Inserting Row
        db.insert(TABLE_ACCOUNT_INFORMATION, null, values);

        db.close(); // Closing database connection
    }*/

    /*public List<DataBaseHelper> getIDPasswordOfAccountsUsingAccountName() {
        List<DataBaseHelper> dataBaseHelperList = new ArrayList<>();
        // Select All Query
        *//*String selectQuery = "SELECT  " + KEY_VEHICLE_ID_ODOO + " FROM " + USER_VEHICLE_TABLE ;*//*
        String selectQuery = "SELECT  * FROM " + TABLE_ACCOUNT_INFORMATION ;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                DataBaseHelper dataBaseHelper = new DataBaseHelper();

                dataBaseHelper.set_account_odoo_id(cursor.getInt(4));
                dataBaseHelper.set_account_name(cursor.getString(5));
                // Adding data to list
                dataBaseHelperList.add(dataBaseHelper);
            } while (cursor.moveToNext());
        }

        // return recent list
        return dataBaseHelperList;
    }*/

    public List<DataBaseHelper> getAllAccountInformation() {
        List<DataBaseHelper> dataBaseHelperList = new ArrayList<>();
        // Select All Query
        /*String selectQuery = "SELECT  " + KEY_VEHICLE_ID_ODOO + " FROM " + USER_VEHICLE_TABLE ;*/
        String selectQuery = "SELECT  * FROM " + TABLE_ACCOUNT_INFORMATION ;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                DataBaseHelper dataBaseHelper = new DataBaseHelper();

                //dataBaseHelper.set_user_account_id(cursor.getInt(0));
                dataBaseHelper.set_user_account_password(cursor.getString(1));
                dataBaseHelper.set_account_login_url(cursor.getString(2));
                // Adding data to list
                dataBaseHelperList.add(dataBaseHelper);
            } while (cursor.moveToNext());
        }

        // return recent list
        return dataBaseHelperList;
    }

    public List<DataBaseHelper> getIDPasswordOfAccountsUsingAccountName(String sKey) {
        List<DataBaseHelper> dataBaseHelperList = new ArrayList<>();
        //ArrayList<String> alTechSpecs = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT " + KEY_USER_ACCOUNT_ID + "," + KEY_USER_ACCOUNT_PASSWORD +" FROM " + TABLE_ACCOUNT_INFORMATION + " WHERE "
                + KEY_ACCOUNT_NAME + "= '" + sKey+"'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                DataBaseHelper dataBaseHelper = new DataBaseHelper();
                dataBaseHelper.set_user_account_id(cursor.getString(0));
                dataBaseHelper.set_user_account_password(cursor.getString(1));
                // Adding data to list
                dataBaseHelperList.add(dataBaseHelper);
                //String s = String.valueOf(dataBaseHelperList.get(cursor.getPosition()).get_individual_product_names());
                //alTechSpecs.add(s);
            } while (cursor.moveToNext());
        }

        // return recent list
        return dataBaseHelperList;
    }

    public int getIdForAccountName(String str) {
        int res;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_ACCOUNT_INFORMATION, new String[]{KEY_ID,
                }, KEY_ACCOUNT_NAME + "=?",
                new String[]{str}, null, null, null, null);
        if ((cursor != null) && (cursor.getCount() > 0)) {
            cursor.moveToFirst();
            res = cursor.getInt(cursor.getColumnIndex(KEY_ID));
        } else {
            res = -1;
        }
        if (cursor != null) {
            cursor.close();
        }
        return res;
    }


    public void updateIDAndPasswordToAccountsTable(DataBaseHelper dataBaseHelper, int KEY_ID) {
        SQLiteDatabase db = this.getWritableDatabase();
        //String column = "last_seen";
        ContentValues values = new ContentValues();
        //values.put(KEY_NAME, dataBaseHelper.getName());
        //values.put(KEY_NUMBER, dataBaseHelper.getPhoneNumber());
        values.put(KEY_USER_ACCOUNT_ID, dataBaseHelper.get_user_account_id());
        values.put(KEY_USER_ACCOUNT_PASSWORD, dataBaseHelper.get_user_account_password());

        // updating row
        //return db.update(TABLE_RECENT, values, column + "last_seen", new String[] {String.valueOf(KEY_ID)});
        db.update(TABLE_ACCOUNT_INFORMATION, values, "_id" + " = " + KEY_ID, null);
        //*//**//*ContentValues data=new ContentValues();
        //data.put("Field1","bob");
        //DB.update(Tablename, data, "_id=" + id, null);*//**//*
    }

    /*public List<DataBaseHelper> getProductsByType(String sType) {
        List<DataBaseHelper> dataBaseHelperList = new ArrayList<>();
        // Select All Query
        //String selectQuery = "SELECT  * FROM " + TABLE_FOOD_BEVERAGES + " WHERE " + KEY_PRODUCT_TYPE + "= '" + sType+"'";
        String selectQuery = "SELECT " + KEY_PRODUCT_ODOO_ID + "," + KEY_PRODUCT_NAME + "," + KEY_PRODUCT_PRICE + " FROM " + TABLE_ACCOUNT_INFORMATION + " WHERE " + KEY_PRODUCT_TYPE + "= '" + sType+"'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                DataBaseHelper dataBaseHelper = new DataBaseHelper();
                dataBaseHelper.set_product_odoo_id(cursor.getInt(0));
                dataBaseHelper.set_product_name(cursor.getString(1));
                dataBaseHelper.set_product_price(cursor.getString(2));
                // Adding data to list
                dataBaseHelperList.add(dataBaseHelper);
            } while (cursor.moveToNext());
        }
        // return recent list
        return dataBaseHelperList;
    }

    public int getIdForStringTablePermanent(String str) {
        int res;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_ACCOUNT_INFORMATION, new String[]{KEY_PRODUCT_ODOO_ID,
                }, KEY_PRODUCT_NAME + "=?",
                new String[]{str}, null, null, null, null);
        if ((cursor != null) && (cursor.getCount() > 0)) {
            cursor.moveToFirst();
            res = cursor.getInt(cursor.getColumnIndex(KEY_PRODUCT_ODOO_ID));
        } else {
            res = -1;
        }
        if (cursor != null) {
            cursor.close();
        }
        return res;
    }*/
}
