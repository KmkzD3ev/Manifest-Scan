package com.example.teste.Bd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.teste.Validation.Model.Nota;
import com.example.teste.Validation.Model.User;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 3; // Incrementar a versão para resetar o banco de dados
    private static final String DATABASE_NAME = "UserManager.db";

    // Tabela de usuários
    private static final String TABLE_USERS = "users";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_PHONE_NUMBER = "phone_number";
    private static final String COLUMN_VALIDATION_CODE = "codigo_validacao";
    private static final String COLUMN_IS_LOGGED_IN = "is_logged_in";

    // Tabela de códigos de barras
    private static final String TABLE_BARCODES = "barcodes";
    private static final String COLUMN_BARCODE_ID = "barcode_id";
    private static final String COLUMN_BARCODE = "barcode";
    private static final String COLUMN_TIMESTAMP = "timestamp";

    // Tabela de notas
    private static final String TABLE_NOTAS = "notas";
    private static final String COLUMN_NOTA_ID = "nota_id";
    private static final String COLUMN_NOTA_BARCODE = "nota_barcode";
    private static final String COLUMN_NOTA_WEIGHT = "nota_weight";
    private static final String COLUMN_NOTA_VALUE = "nota_value";
    private static final String COLUMN_NOTA_CHAVE_CONTINGENCIA = "nota_chave_contingencia";

    // Tabela de manifestos
    private static final String TABLE_MANIFESTOS = "manifestos";
    private static final String COLUMN_MANIFESTO_ID = "manifesto_id";
    private static final String COLUMN_MANIFESTO_BARCODE = "barcode";
    private static final String COLUMN_MANIFESTO_TIMESTAMP = "timestamp";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS +
                "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY," +
                COLUMN_PHONE_NUMBER + " TEXT," +
                COLUMN_VALIDATION_CODE + " INTEGER," +
                COLUMN_IS_LOGGED_IN + " INTEGER DEFAULT 0" +
                ")";
        db.execSQL(CREATE_USERS_TABLE);

        String CREATE_BARCODES_TABLE = "CREATE TABLE " + TABLE_BARCODES +
                "(" +
                COLUMN_BARCODE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_BARCODE + " TEXT," +
                COLUMN_TIMESTAMP + " TEXT" +
                ")";
        db.execSQL(CREATE_BARCODES_TABLE);

        String CREATE_NOTAS_TABLE = "CREATE TABLE " + TABLE_NOTAS +
                "(" +
                COLUMN_NOTA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_NOTA_BARCODE + " TEXT," +
                COLUMN_NOTA_WEIGHT + " TEXT," +
                COLUMN_NOTA_VALUE + " TEXT," +
                COLUMN_NOTA_CHAVE_CONTINGENCIA + " TEXT" +
                ")";
        db.execSQL(CREATE_NOTAS_TABLE);

        String CREATE_MANIFESTOS_TABLE = "CREATE TABLE " + TABLE_MANIFESTOS +
                "(" +
                COLUMN_MANIFESTO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_MANIFESTO_BARCODE + " TEXT," +
                COLUMN_MANIFESTO_TIMESTAMP + " TEXT" +
                ")";
        db.execSQL(CREATE_MANIFESTOS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BARCODES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTAS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MANIFESTOS);
        onCreate(db);
    }

    // Adiciona um novo usuário à tabela de usuários
    public void addUser(int id, String phoneNumber, int validationCode) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, id);
        values.put(COLUMN_PHONE_NUMBER, phoneNumber);
        values.put(COLUMN_VALIDATION_CODE, validationCode);
        db.insert(TABLE_USERS, null, values);
        db.close();
    }

    // Recupera o código de validação de um usuário específico
    public String getValidationCode(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_VALIDATION_CODE};
        String selection = COLUMN_ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};
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


    // Atualiza o status de login de um usuário
    public void setLoggedIn(int id, boolean isLoggedIn) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_IS_LOGGED_IN, isLoggedIn ? 1 : 0);
        String selection = COLUMN_ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};
        db.update(TABLE_USERS, values, selection, selectionArgs);
        db.close();
    }

    // Verifica se algum usuário está logado
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

    // Insere um novo código de barras na tabela de códigos de barras
    public long insertBarcode(String barcode, String timestamp) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_BARCODE, barcode);
        values.put(COLUMN_TIMESTAMP, timestamp);
        long id = db.insert(TABLE_BARCODES, null, values);
        db.close();
        return id;
    }

    // Insere uma nova nota na tabela de notas
    public long insertNota(Nota nota) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOTA_BARCODE, nota.getBarcode());
        values.put(COLUMN_NOTA_WEIGHT, nota.getWeight());
        values.put(COLUMN_NOTA_VALUE, nota.getValue());
        values.put(COLUMN_NOTA_CHAVE_CONTINGENCIA, nota.getChaveContingencia());
        long id = db.insert(TABLE_NOTAS, null, values);
        db.close();
        return id;
    }

    // Deleta uma nota específica da tabela de notas
    public void deleteNota(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NOTAS, COLUMN_NOTA_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    // Insere um novo manifesto na tabela de manifestos
    public long insertManifesto(String barcode, String timestamp) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_MANIFESTO_BARCODE, barcode);
        values.put(COLUMN_MANIFESTO_TIMESTAMP, timestamp);
        long id = db.insert(TABLE_MANIFESTOS, null, values);
        db.close();
        return id;
    }

    // Adiciona um método para obter detalhes do usuário (id e phone_number)
    public User getUserDetails() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, new String[]{COLUMN_ID, COLUMN_PHONE_NUMBER}, null, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
            String phoneNumber = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHONE_NUMBER));
            cursor.close();
            return new User(id, phoneNumber);
        }
        if (cursor != null) {
            cursor.close();
        }
        db.close();
        return null;
    }
}