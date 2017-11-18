package ca.uoit.csci4100u.workplace_app.lib;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class LocalDbHelper extends SQLiteOpenHelper {

    static final int DATABASE_VERSION = 1;
    static final String TABLE_MESSAGES = "Messages";
    static final String TABLE_COMPANIES = "Companies";
    static final String TABLE_CHATS = "Chats";
    static final String DATABASE_NAME = "Workplace";

    static final String CREATE_USERS_TABLE = "CREATE TABLE Users (\n" +
            "   userId VARCHAR(255) PRIMARY KEY,\n" +
            "   companyListId INTEGER NOT NULL\n" +
            ")\n";

    static final String CREATE_USER_COMPANY_TABLE = "CRATE TABLE UserCompany (\n" +
            "   userId VARCHAR(255) NOT NULL,\n" +
            "   companyId VARCHAR(255) NOT NULL,\n" +
            "   PRIMARY KEY (userId, companyId)\n" +
            ")\n";

    static final String CREATE_COMPANIES_TABLE = "CREATE TABLE Companies (\n" +
            "   companyId VARCHAR(255) PRIMARY KEY,\n" +
            "   companyName VARCHAR(255) NOT NULL\n" +
            ")\n";

    public LocalDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_COMPANIES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersionNum, int newVersionNum) {
        /**
         * TODO: Fill this in
         */
    }

    public void createCompany(String companyId, String userId, String companyName) {
        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues newCompany = new ContentValues();
        newCompany.put("companyId", companyId);
        newCompany.put("companyName", companyName);
        database.insert(TABLE_COMPANIES, null, newCompany);
        database.close();
    }

    public boolean checkCompanyExists(String companyId) {
        boolean companyExists = false;
        SQLiteDatabase database = this.getReadableDatabase();
        String query = "Select * from " + TABLE_COMPANIES + " WHERE companyId =?";
        Cursor cursor = database.rawQuery(query, new String[] {companyId});
        if (cursor.moveToFirst()) {
            companyExists = true;
        }
        cursor.close();
        database.close();
        return companyExists;
    }
}