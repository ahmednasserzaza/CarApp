package com.example.carapp.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.SimpleAdapter;

import com.example.carapp.RecyclerViewPackage.Car;

import java.util.ArrayList;

public class DatabaseAccess {
    private SQLiteDatabase db;
    private SQLiteOpenHelper openHelper;
    private static DatabaseAccess instance;

    // Start Singleton Design pattern
    private DatabaseAccess(Context context) {
        this.openHelper = new MyDatabase(context);
    }

    public static DatabaseAccess getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseAccess(context);
        }
        return instance;
    }
    // End Singleton Design pattern

    public void open() {
        this.db = this.openHelper.getWritableDatabase();
    }

    public void close() {
        if (db != null) {
            this.db.close();
        }
    }

    // Insert new row in cars table
    public boolean insertNewRow(Car car) {
        ContentValues values = new ContentValues();
        values.put(MyDatabase.COL_MODEL, car.getModel());
        values.put(MyDatabase.COL_COLOR, car.getColor());
        values.put(MyDatabase.COL_IMAGE, car.getImage());
        values.put(MyDatabase.COL_DISTANCE_PER_LETTER, car.getDpl());
        values.put(MyDatabase.COL_DESCRIPTION, car.getDescription());

        Long result = db.insert(MyDatabase.TABLE_NAME, null, values);

        return result != -1;
    }

    // Delete row
    public boolean deleteRow(Car car) {
        String[] args = {String.valueOf(car.getId())};
        int result = db.delete(MyDatabase.TABLE_NAME, "id=?", args);
        return result > 0;
    }

    // Update row
    public boolean updateRow(Car car) {
        ContentValues values = new ContentValues();

        values.put(MyDatabase.COL_MODEL, car.getModel());
        values.put(MyDatabase.COL_COLOR, car.getColor());
        values.put(MyDatabase.COL_IMAGE, car.getImage());
        values.put(MyDatabase.COL_DISTANCE_PER_LETTER, car.getDpl());
        values.put(MyDatabase.COL_DESCRIPTION, car.getDescription());

        String[] args = {String.valueOf(car.getId())};
        int result = db.update(MyDatabase.TABLE_NAME, values, "id=?", args);

        return result > 0;
    }

    // Restore All Rows
    public ArrayList<Car> getAllRows() {
        ArrayList<Car> cars = new ArrayList<>();
        Cursor cursor = db.rawQuery(" SELECT * FROM " + MyDatabase.TABLE_NAME, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex(MyDatabase.COL_ID));
                String model = cursor.getString(cursor.getColumnIndex(MyDatabase.COL_MODEL));
                String color = cursor.getString(cursor.getColumnIndex(MyDatabase.COL_COLOR));
                String image = cursor.getString(cursor.getColumnIndex(MyDatabase.COL_IMAGE));
                double dbl = cursor.getDouble(cursor.getColumnIndex(MyDatabase.COL_DISTANCE_PER_LETTER));
                String description = cursor.getString(cursor.getColumnIndex(MyDatabase.COL_DESCRIPTION));
                Car car = new Car(id, model, color, description, image, dbl);
                cars.add(car);
            }
            while (cursor.moveToNext());
            cursor.close();
        }
        return cars;
    }

    public Car getOneCare(int carId) {
        String[] args = {String.valueOf(carId)};
        Cursor cursor = db.rawQuery(" SELECT * FROM " + MyDatabase.TABLE_NAME + " WHERE " + MyDatabase.COL_ID + " =?", args);
        if (cursor != null && cursor.moveToFirst()) {

            int id = cursor.getInt(cursor.getColumnIndex(MyDatabase.COL_ID));
            String model = cursor.getString(cursor.getColumnIndex(MyDatabase.COL_MODEL));
            String color = cursor.getString(cursor.getColumnIndex(MyDatabase.COL_COLOR));
            double dbl = cursor.getDouble(cursor.getColumnIndex(MyDatabase.COL_DISTANCE_PER_LETTER));
            String image = cursor.getString(cursor.getColumnIndex(MyDatabase.COL_IMAGE));
            String description = cursor.getString(cursor.getColumnIndex(MyDatabase.COL_DESCRIPTION));
            Car car = new Car(id, model, color, description, image, dbl);

            cursor.close();
            return car;
        }
        return null;
    }

    // Search by model
    public ArrayList<Car> searchByModel(String carModelName) {
        ArrayList<Car> cars = new ArrayList<>();

        String[] args = {"%" + carModelName + "%"};
        Cursor cursor = db.rawQuery("SELECT * FROM " + MyDatabase.TABLE_NAME + " WHERE " + MyDatabase.COL_MODEL + " LIKE ? ", args);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex(MyDatabase.COL_ID));
                String model = cursor.getString(cursor.getColumnIndex(MyDatabase.COL_MODEL));
                String color = cursor.getString(cursor.getColumnIndex(MyDatabase.COL_COLOR));
                Double dbl = cursor.getDouble(cursor.getColumnIndex(MyDatabase.COL_DISTANCE_PER_LETTER));
                String image = cursor.getString(cursor.getColumnIndex(MyDatabase.COL_IMAGE));
                String description = cursor.getString(cursor.getColumnIndex(MyDatabase.COL_DESCRIPTION));
                Car car = new Car(id, model, color, description, image, dbl);
                cars.add(car);
            }
            while (cursor.moveToNext());
            cursor.close();
        }
        return cars;
    }

    // return Number of rows
    public Long numberOfRows() {
        return DatabaseUtils.queryNumEntries(db, MyDatabase.TABLE_NAME);
    }

}
