package com.example.teste.Bd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 2; // Incrementar a versão do banco de dados
    private static final String DATABASE_NAME = "UserManager.db";
    private static final String TABLE_USERS = "users";
    private static final String COLUMN_ID = "id"; // Agora representando o número de telefone
    private static final String COLUMN_USERNAME = "username"; // Agora representando o ID
    private static final String COLUMN_PHONE_NUMBER = "phone_number"; // Agora representando o número de telefone

    private static final String COLUMN_VALIDATION_CODE = "codigo_validacao";
    private static final String COLUMN_IS_LOGGED_IN = "is_logged_in";

    // Novos campos para a tabela de códigos de barras
    private static final String TABLE_BARCODES = "barcodes";
    private static final String COLUMN_BARCODE_ID = "barcode_id";
    private static final String COLUMN_BARCODE = "barcode";
    private static final String COLUMN_TIMESTAMP = "timestamp";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS +
                "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY," +
                COLUMN_USERNAME + " TEXT," +
                COLUMN_PHONE_NUMBER + " TEXT," +
                COLUMN_VALIDATION_CODE + " INTEGER," +
                COLUMN_IS_LOGGED_IN + " INTEGER DEFAULT 0" + // 0 for false, 1 for true
                ")";
        db.execSQL(CREATE_USERS_TABLE);

        // Criação da nova tabela para armazenar códigos de barras
        String CREATE_BARCODES_TABLE = "CREATE TABLE " + TABLE_BARCODES +
                "(" +
                COLUMN_BARCODE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_BARCODE + " TEXT," +
                COLUMN_TIMESTAMP + " TEXT" +
                ")";
        db.execSQL(CREATE_BARCODES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BARCODES); // Adicionado para atualizar a tabela de códigos de barras
        onCreate(db);
    }

    public void addUser(String id, String phoneNumber, int validationCode) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, id);
        values.put(COLUMN_PHONE_NUMBER, phoneNumber);
        values.put(COLUMN_VALIDATION_CODE, validationCode);
        db.insert(TABLE_USERS, null, values);
        db.close();
    }

    public String getValidationCode(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_VALIDATION_CODE};
        String selection = COLUMN_USERNAME + " = ?";
        String[] selectionArgs = {id};
        Cursor cursor = db.query(TABLE_USERS, columns, selection, selectionArgs, null, null, null);
        if (cursor.moveToFirst()) {
            String validationCode = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_VALIDATION_CODE));
            cursor.close();
            db.close();
            return validationCode;
        }
        cursor.close();
        db.close();
        return null;
    }

    public void setLoggedIn(String username, boolean isLoggedIn) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_IS_LOGGED_IN, isLoggedIn ? 1 : 0);
        String selection = COLUMN_USERNAME + " = ?";
        String[] selectionArgs = {username};
        db.update(TABLE_USERS, values, selection, selectionArgs);
        db.close();
    }

    public boolean isUserLoggedIn() {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_IS_LOGGED_IN};
        String selection = COLUMN_IS_LOGGED_IN + " = ?";
        String[] selectionArgs = {"1"};
        Cursor cursor = db.query(TABLE_USERS, columns, selection, selectionArgs, null, null, null);
        boolean isLoggedIn = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return isLoggedIn;
    }

    // Novo método para inserir códigos de barras no banco de dados
    public long insertBarcode(String barcode, String timestamp) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_BARCODE, barcode);
        values.put(COLUMN_TIMESTAMP, timestamp);
        long id = db.insert(TABLE_BARCODES, null, values);
        db.close();
        return id;
    }
}