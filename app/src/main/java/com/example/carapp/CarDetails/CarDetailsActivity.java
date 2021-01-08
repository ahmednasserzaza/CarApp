package com.example.carapp.CarDetails;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.carapp.Database.DatabaseAccess;
import com.example.carapp.Home.MainActivity;
import com.example.carapp.R;
import com.example.carapp.RecyclerViewPackage.Car;
import com.google.android.material.textfield.TextInputEditText;

import java.net.URI;

public class CarDetailsActivity extends AppCompatActivity {

    public static final int PIC_IMAGE_REQ_CODE = 0;
    public static final int PIC_IMAGE_REQ_CODE2 = 1;
    public static final int ADD_CAR_RESULT_CODE = 1;
    public static final int EDIT_CAR_RESULT_CODE = 2;
    private Toolbar toolbar;
    private ImageView iv;
    private TextInputEditText et_model, et_color, et_dbl, et_description;
    private DatabaseAccess db;
    public static int carID = -1;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_details);

        initViews();

        Intent intent = getIntent();
        carID = intent.getIntExtra(MainActivity.CAR_KEY, -1);
        db = DatabaseAccess.getInstance(this);
        if (carID == -1) {
            // كود الاضافة
            enableFields();
            clearFields();
        } else {
            // كود العرض
            disableFields();
            db.open();
            Car c = db.getOneCare(carID);
            db.close();

            if (c != null) {
                fillCarToFields(c);
            }
        }

        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getBaseContext(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    String[] mimeTypes = {"image/jpeg", "image/png"};
                    intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
                    startActivityForResult(intent, PIC_IMAGE_REQ_CODE);

                } else {
                    requestStoragePermission();
                }
            }
        });
    }

    private void initViews() {
        toolbar = findViewById(R.id.details_toolbar);
        iv = findViewById(R.id.details_iv);
        et_model = findViewById(R.id.details_model);
        et_color = findViewById(R.id.details_color);
        et_dbl = findViewById(R.id.details_dbl);
        et_description = findViewById(R.id.details_description);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.details_menu, menu);
        MenuItem save = menu.findItem(R.id.details_menu_save);
        MenuItem edit = menu.findItem(R.id.details_menu_edit);
        MenuItem delete = menu.findItem(R.id.details_menu_delete);
        if (carID == -1) {
            // كود الاضافة
            save.setVisible(true);
            edit.setVisible(false);
            delete.setVisible(false);
        } else {
            // كود العرض
            save.setVisible(false);
            edit.setVisible(true);
            delete.setVisible(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        String image = "" , model, color, description;
        double dbl;
        boolean res;
        db.open();
        switch (item.getItemId()) {
            case R.id.details_menu_save:
                model = et_model.getText().toString();
                color = et_color.getText().toString();
                dbl = Double.parseDouble(et_dbl.getText().toString());
                description = et_description.getText().toString();
                if (image != null)
                    image = imageUri.toString();


                Car c = new Car(carID, model, color, description, image, dbl);

                if (carID == -1) {
                    // عمليةاضافة
                    res = db.insertNewRow(c);
                    if (res) {
                        Toast.makeText(this, "Car Added Successfully", Toast.LENGTH_SHORT).show();
                        setResult(ADD_CAR_RESULT_CODE, null);
                        finish();
                    }
                } else {
                    // عملية تعديل
                    res = db.updateRow(c);
                    if (res) {
                        Toast.makeText(this, "Car Edited Successfully", Toast.LENGTH_SHORT).show();
                        setResult(EDIT_CAR_RESULT_CODE, null);
                        finish();
                    }
                }
                return true;

            case R.id.details_menu_edit:
                enableFields();
                MenuItem save = toolbar.getMenu().findItem(R.id.details_menu_save);
                MenuItem edit = toolbar.getMenu().findItem(R.id.details_menu_edit);
                MenuItem delete = toolbar.getMenu().findItem(R.id.details_menu_delete);

                edit.setVisible(false);
                delete.setVisible(false);
                save.setVisible(true);
                return true;

            case R.id.details_menu_delete:
                Car car = new Car(carID);
                res = db.deleteRow(car);
                if (res) {
                    Toast.makeText(this, "deleted Successfully", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK, null);
                    finish();
                }
                return true;
        }
        db.close();
        return false;
    }

    private void disableFields() {
        iv.setEnabled(false);
        et_model.setEnabled(false);
        et_color.setEnabled(false);
        et_dbl.setEnabled(false);
        et_description.setEnabled(false);
    }

    private void enableFields() {
        iv.setEnabled(true);
        et_model.setEnabled(true);
        et_color.setEnabled(true);
        et_dbl.setEnabled(true);
        et_description.setEnabled(true);
    }

    private void clearFields() {
        iv.setImageURI(null);
        et_model.setText("");
        et_color.setText("");
        et_dbl.setText("");
        et_description.setText("");
    }

    private void fillCarToFields(Car c) {
        if (c.getImage() != null && !c.getImage().equals(""))
            iv.setImageURI(Uri.parse(c.getImage()));

        et_model.setText(c.getModel());
        et_color.setText(c.getColor());
        et_dbl.setText(String.valueOf(c.getDpl()));
        et_description.setText(c.getDescription());
    }

    private void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("This permission is needed to set picture from gallery")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(CarDetailsActivity.this,
                                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PIC_IMAGE_REQ_CODE2);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();

        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PIC_IMAGE_REQ_CODE2);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PIC_IMAGE_REQ_CODE ) {
            if (data != null) {
                imageUri = data.getData();
                iv.setImageURI(imageUri);
            }
        }
    }
}