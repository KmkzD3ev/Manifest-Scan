package com.example.teste;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.teste.Bd.DatabaseHelper;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

public class DatabaseHelperTest {

    @Mock
    private Context mockContext;
    @Mock
    private SQLiteDatabase mockDb;
    @Mock
    private SQLiteOpenHelper mockDbHelper;

    private DatabaseHelper databaseHelper;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(mockContext.getApplicationContext()).thenReturn(mockContext);
        databaseHelper = spy(new DatabaseHelper(mockContext));
        doReturn(mockDb).when(databaseHelper).getWritableDatabase();
        doReturn(mockDb).when(databaseHelper).getReadableDatabase();
    }

    @Test
    public void addUserTest() {
        databaseHelper.addUser(1, "123456789", 1234);
        verify(mockDb).insert(eq("users"), isNull(), any());
    }

    @Test
    public void getUserDetailsTest() {
        databaseHelper.getLoggedInUserDetails();
        verify(mockDb).query(eq("users"), any(), any(), any(), isNull(), isNull(), isNull());
    }

    @Test
    public void updateUserTest() {
        databaseHelper.setLoggedIn(1, true);
        verify(mockDb).update(eq("users"), any(), any(), any());
    }

    @Test
    public void deleteUserTest() {
        databaseHelper.deleteNota(1);
        verify(mockDb).delete(eq("notas"), any(), any());
    }
}