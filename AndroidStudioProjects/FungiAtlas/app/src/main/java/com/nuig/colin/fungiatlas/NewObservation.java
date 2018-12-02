package com.nuig.colin.fungiatlas;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class NewObservation extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference dbRef;
    FirebaseStorage myStorageRef;
    private ArrayList<String> attributesList;
    private HashMap<String, String> attributesMap;
    private Bitmap bitmap;
    private Uri mImageUri = null;
    private StorageReference mStorage;
    private List<Bitmap> bitmaps;
    private static final int CAMERA_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_observation);
        checkCameraPermission();

        database = FirebaseDatabase.getInstance();
        dbRef = database.getReference();
        myStorageRef = FirebaseStorage.getInstance();
        mStorage = myStorageRef.getReference();
        bitmaps = new ArrayList<>();

        Button capFeaturesButton = findViewById(R.id.buttonCapFeatures);
        Button gillFeaturesButton = findViewById(R.id.buttonGillFeatures);
        Button stemFeaturesButton = findViewById(R.id.buttonStemFeatures);
        Button otherFeaturesButton = findViewById(R.id.buttonOtherFeatures);
        Button addPhoto = findViewById(R.id.buttonAddPhoto);
        Button submit = findViewById(R.id.buttonSubmit);

        attributesList = new ArrayList<>();
        attributesMap = new HashMap<>();

        capFeaturesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent capFeaturesPage = new Intent(NewObservation.this, CapFeatures.class);
                startActivity(capFeaturesPage);
            }
        });

        gillFeaturesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gillFeaturesPage = new Intent(NewObservation.this, GillFeatures.class);
                startActivity(gillFeaturesPage);
            }
        });

        stemFeaturesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent stemFeaturesPage = new Intent(NewObservation.this, StemFeatures.class);
                startActivity(stemFeaturesPage);
            }
        });

        otherFeaturesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent otherFeaturesPage = new Intent(NewObservation.this, OtherFeatures.class);
                startActivity(otherFeaturesPage);
            }
        });
/*
        final Spinner veilType = findViewById(R.id.spinnerVeilType);
        final Spinner veilColour = findViewById(R.id.spinnerVeilColour);
        final Spinner ringNumber = findViewById(R.id.spinnerRingNumber);
        final Spinner ringType = findViewById(R.id.spinnerRingType);
*/
        //------------------------------------------------------------------------------------------

/*
        final ArrayAdapter<String> veilTypeAdapter = new ArrayAdapter<String>(
                NewObservation.this,
                android.R.layout.simple_expandable_list_item_1,
                getResources().getStringArray(R.array.veilTypes));
        arrayAdapterCreator(veilTypeAdapter, veilType);

        final ArrayAdapter<String> veilColourAdapter = new ArrayAdapter<String>(
                NewObservation.this,
                android.R.layout.simple_expandable_list_item_1,
                getResources().getStringArray(R.array.veilColours));
        arrayAdapterCreator(veilColourAdapter,veilColour);

        final ArrayAdapter<String> ringNumberAdapter = new ArrayAdapter<String>(
                NewObservation.this,
                android.R.layout.simple_expandable_list_item_1,
                getResources().getStringArray(R.array.ringNumbers));
        arrayAdapterCreator(ringNumberAdapter,ringNumber);

        final ArrayAdapter<String> ringTypeAdapter = new ArrayAdapter<String>(
                NewObservation.this,
                android.R.layout.simple_expandable_list_item_1,
                getResources().getStringArray(R.array.ringTypes));
        arrayAdapterCreator(ringTypeAdapter,ringType);

*/
        //------------------------------------------------------------------------------------------

        addPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    // use standard intent to capture an image
                    Intent cameraPhoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraPhoto, CAMERA_REQUEST_CODE);
                }
                catch (ActivityNotFoundException anfe) {}
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String time = String.valueOf(new Date().getTime());

                if(CapFeatures.getAttributesList() != null) {
                    for (String feature: CapFeatures.getAttributesList()) {
                        attributesList.add(feature);
                    }
                }
                if(GillFeatures.getAttributesList() != null) {
                    for (String feature: GillFeatures.getAttributesList()) {
                        attributesList.add(feature);
                    }
                }
                if(StemFeatures.getAttributesList() != null) {
                    for (String feature: StemFeatures.getAttributesList()) {
                        attributesList.add(feature);
                    }
                }
                if(OtherFeatures.getAttributesList() != null) {
                    for (String feature: OtherFeatures.getAttributesList()) {
                        attributesList.add(feature);
                    }
                }

                HashMap<String, String> capMap = CapFeatures.getAttributesMap();
                HashMap<String, String> stemMap = StemFeatures.getAttributesMap();
                HashMap<String, String> gillMap = GillFeatures.getAttributesMap();
                HashMap<String, String> othersMap = OtherFeatures.getAttributesMap();


                if(CapFeatures.getAttributesMap()!= null) {
                    HashMap tempMap = new HashMap(CapFeatures.getAttributesMap());
                    tempMap.keySet().removeAll(attributesMap.keySet());
                    attributesMap.putAll(tempMap);
                }
                if(GillFeatures.getAttributesMap()!= null) {
                    HashMap tempMap = new HashMap(GillFeatures.getAttributesMap());
                    tempMap.keySet().removeAll(attributesMap.keySet());
                    attributesMap.putAll(tempMap);
                }
                if(StemFeatures.getAttributesMap()!= null) {
                    HashMap tempMap = new HashMap(StemFeatures.getAttributesMap());
                    tempMap.keySet().removeAll(attributesMap.keySet());
                    attributesMap.putAll(tempMap);
                }
                if(OtherFeatures.getAttributesMap()!= null) {
                    HashMap tempMap = new HashMap(OtherFeatures.getAttributesMap());
                    tempMap.keySet().removeAll(attributesMap.keySet());
                    attributesMap.putAll(tempMap);
                }


                if(attributesList.isEmpty()){
                    Toast.makeText(NewObservation.this,"No data entered",
                            Toast.LENGTH_SHORT).show();
                }
                else {

                    for (int i = 0; i < attributesList.size(); i++) {
                        String tag = attributesList.get(i);
                        String type = "Other";
                        if(tag.contains("Cap")){
                            type = "Cap";
                        }
                        else if(tag.contains("Gill")){
                            type = "Gill";
                        }
                        else if(tag.contains("Stalk")){
                            type = "Stem";
                        }
                        dbRef.child("Fungi Attributes:").child(time).child(type).child(tag).
                                setValue(attributesMap.get(tag));
                    }

                    uploadPhotos(time);
                    Toast.makeText(NewObservation.this,"Observation submitted",
                            Toast.LENGTH_SHORT).show();
                    attributesList.clear();
                    attributesMap.clear();
                    bitmaps.clear();
                    finish();
                }
            }
        });
    }

    public boolean checkCameraPermission(){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CAMERA)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("Permission to access camera")
                        .setMessage("Please allow the app to access you camera.")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(NewObservation.this,
                                        new String[]{Manifest.permission.CAMERA},
                                        99);
                            }
                        })
                        .create()
                        .show();
            }
            else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA},
                        99);
            }
            return false;
        }
        else {
            return true;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK){
            bitmap = (Bitmap) data.getExtras().get("data");
            bitmaps.add(bitmap);
            Toast.makeText(NewObservation.this, "Photo ready to be uploaded", Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadPhotos(String time){
        if(!bitmaps.isEmpty()) {
            for (Bitmap bmap : bitmaps) {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] b = stream.toByteArray();
                String newTime = String.valueOf(new Date().getTime());
                mStorage.child("Images").child(time).child(newTime).putBytes(b).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(NewObservation.this, "Photo upload failed", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            Toast.makeText(NewObservation.this, "Photo(s) uploaded", Toast.LENGTH_SHORT).show();
        }
    }
}