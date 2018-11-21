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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
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


public class NewObservation extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    FirebaseDatabase database;
    DatabaseReference dbRef;
    FirebaseStorage myStorageRef;
    private List<String> attributesList;
    private Map<String, String> attributesMap;
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

        final Spinner capShape = findViewById(R.id.spinnerCapShape);
        final Spinner capSurface = findViewById(R.id.spinnerCapSurface);
        final Spinner capColour = findViewById(R.id.spinnerCapColour);
        final Spinner bruises = findViewById(R.id.spinnerBruises);
        final Spinner odor = findViewById(R.id.spinnerOdor);
        final Spinner gillAttachment = findViewById(R.id.spinnerGillAttachment);
        final Spinner gillSpacing = findViewById(R.id.spinnerGillSpacing);
        final Spinner gillSize = findViewById(R.id.spinnerGillSize);
        final Spinner gillColour = findViewById(R.id.spinnerGillColour);
        final Spinner stalkShape = findViewById(R.id.spinnerStalkShape);
        final Spinner stalkRoot = findViewById(R.id.spinnerStalkRoot);
        final Spinner stalkSurfaceAboveRing = findViewById(R.id.spinnerStalkSurfaceAboveRing);
        Spinner stalkSurfaceBelowRing = findViewById(R.id.spinnerStalkSurfaceBelowRing);
        final Spinner stalkColourAboveRing = findViewById(R.id.spinnerStalkColourAboveRing);
        final Spinner stalkColourBelowRing = findViewById(R.id.spinnerStalkColourBelowRing);
        final Spinner veilType = findViewById(R.id.spinnerVeilType);
        final Spinner veilColour = findViewById(R.id.spinnerVeilColour);
        final Spinner ringNumber = findViewById(R.id.spinnerRingNumber);
        final Spinner ringType = findViewById(R.id.spinnerRingType);
        final Spinner population = findViewById(R.id.spinnerPopulation);
        final Spinner habitat = findViewById(R.id.spinnerHabitat);

        Button addPhoto = findViewById(R.id.buttonAddPhoto);
        final Button submit = findViewById(R.id.buttonSubmit);

        attributesList = new ArrayList<>();
        attributesMap = new HashMap<>();
        //------------------------------------------------------------------------------------------
        final ArrayAdapter<String> capShapeAdapter = new ArrayAdapter<String>(
                NewObservation.this,
                android.R.layout.simple_expandable_list_item_1,
                getResources().getStringArray(R.array.capShapes));
        arrayAdapterCreator(capShapeAdapter,capShape);

        final ArrayAdapter<String> capSurfaceAdapter = new ArrayAdapter<String>(
                NewObservation.this,
                android.R.layout.simple_expandable_list_item_1,
                getResources().getStringArray(R.array.capSurfaces));
        arrayAdapterCreator(capSurfaceAdapter,capSurface);

        final ArrayAdapter<String> capColourAdapter = new ArrayAdapter<String>(
                NewObservation.this,
                android.R.layout.simple_expandable_list_item_1,
                getResources().getStringArray(R.array.capColours));
        arrayAdapterCreator(capColourAdapter,capColour);

        final ArrayAdapter<String> bruisesAdapter = new ArrayAdapter<String>(
                NewObservation.this,
                android.R.layout.simple_expandable_list_item_1,
                getResources().getStringArray(R.array.bruises));
        arrayAdapterCreator(bruisesAdapter,bruises);

        final ArrayAdapter<String> odorAdapter = new ArrayAdapter<String>(
                NewObservation.this,
                android.R.layout.simple_expandable_list_item_1,
                getResources().getStringArray(R.array.odor));
        arrayAdapterCreator(odorAdapter,odor);

        final ArrayAdapter<String> gillAttachmentAdapter = new ArrayAdapter<String>(
                NewObservation.this,
                android.R.layout.simple_expandable_list_item_1,
                getResources().getStringArray(R.array.gillAttachments));
        arrayAdapterCreator(gillAttachmentAdapter, gillAttachment);

        final ArrayAdapter<String> gillSpacingAdapter = new ArrayAdapter<String>(
                NewObservation.this,
                android.R.layout.simple_expandable_list_item_1,
                getResources().getStringArray(R.array.gillSpacings));
        arrayAdapterCreator(gillSpacingAdapter,gillSpacing);

        final ArrayAdapter<String> gillSizeAdapter = new ArrayAdapter<String>(
                NewObservation.this,
                android.R.layout.simple_expandable_list_item_1,
                getResources().getStringArray(R.array.gillSizes));
        arrayAdapterCreator(gillSizeAdapter,gillSize);

        final ArrayAdapter<String> gillColourAdapter = new ArrayAdapter<String>(
                NewObservation.this,
                android.R.layout.simple_expandable_list_item_1,
                getResources().getStringArray(R.array.gillColours));
        arrayAdapterCreator(gillColourAdapter,gillColour);

        final ArrayAdapter<String> stalkShapeAdapter = new ArrayAdapter<String>(
                NewObservation.this,
                android.R.layout.simple_expandable_list_item_1,
                getResources().getStringArray(R.array.stalkShapes));
        arrayAdapterCreator(stalkShapeAdapter,stalkShape);

        final ArrayAdapter<String> stalkRootAdapter = new ArrayAdapter<String>(
                NewObservation.this,
                android.R.layout.simple_expandable_list_item_1,
                getResources().getStringArray(R.array.stalkRoots));
        arrayAdapterCreator(stalkRootAdapter,stalkRoot);

        final ArrayAdapter<String> stalkSurfaceAboveRingAdapter = new ArrayAdapter<String>(
                NewObservation.this,
                android.R.layout.simple_expandable_list_item_1,
                getResources().getStringArray(R.array.stalkSurfacesAboveRing));
        arrayAdapterCreator(stalkSurfaceAboveRingAdapter, stalkSurfaceAboveRing);

        ArrayAdapter<String> stalkSurfaceBelowRingAdapter = new ArrayAdapter<String>(
                NewObservation.this,
                android.R.layout.simple_expandable_list_item_1,
                getResources().getStringArray(R.array.stalkSurfacesBelowRing));
        arrayAdapterCreator(stalkSurfaceBelowRingAdapter, stalkSurfaceBelowRing);

        final ArrayAdapter<String> stalkColourAboveRingAdapter = new ArrayAdapter<String>(
                NewObservation.this,
                android.R.layout.simple_expandable_list_item_1,
                getResources().getStringArray(R.array.stalkColoursAboveRing));
        arrayAdapterCreator(stalkColourAboveRingAdapter,stalkColourAboveRing);

        final ArrayAdapter<String> stalkColourBelowRingAdapter = new ArrayAdapter<String>(
                NewObservation.this,
                android.R.layout.simple_expandable_list_item_1,
                getResources().getStringArray(R.array.stalkColoursBelowRing));
        arrayAdapterCreator(stalkColourBelowRingAdapter, stalkColourBelowRing);

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
                getResources().getStringArray(R.array.ringNumber));
        arrayAdapterCreator(ringNumberAdapter,ringNumber);

        final ArrayAdapter<String> ringTypeAdapter = new ArrayAdapter<String>(
                NewObservation.this,
                android.R.layout.simple_expandable_list_item_1,
                getResources().getStringArray(R.array.ringType));
        arrayAdapterCreator(ringTypeAdapter,ringType);

        final ArrayAdapter<String> populationAdapter = new ArrayAdapter<String>(
                NewObservation.this,
                android.R.layout.simple_expandable_list_item_1,
                getResources().getStringArray(R.array.population));
        arrayAdapterCreator(populationAdapter,population);

        final ArrayAdapter<String> habitatAdapter = new ArrayAdapter<String>(
                NewObservation.this,
                android.R.layout.simple_expandable_list_item_1,
                getResources().getStringArray(R.array.habitats));
        arrayAdapterCreator(habitatAdapter, habitat);

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
                for(int i=0; i< attributesList.size(); i++) {
                    String tag = attributesList.get(i);
                    dbRef.child("Fungi Attributes:").child(time).child(tag).setValue(attributesMap.get(tag));
                }
                uploadPhotos(time);
                Toast.makeText(NewObservation.this,"Observation submitted", Toast.LENGTH_SHORT).show();
                attributesList.clear();
                attributesMap.clear();
                bitmaps.clear();

                capShape.setAdapter(capShapeAdapter);
                capSurface.setAdapter(capSurfaceAdapter);
                capColour.setAdapter(capColourAdapter);
                bruises.setAdapter(bruisesAdapter);
                odor.setAdapter(odorAdapter);
                gillAttachment.setAdapter(gillAttachmentAdapter);
                gillSpacing.setAdapter(gillSpacingAdapter);
                gillSize.setAdapter(gillSizeAdapter);
                gillColour.setAdapter(gillColourAdapter);
                stalkShape.setAdapter(stalkShapeAdapter);
                stalkRoot.setAdapter(stalkRootAdapter);
                stalkSurfaceAboveRing.setAdapter(stalkSurfaceAboveRingAdapter);
                stalkColourBelowRing.setAdapter(stalkColourBelowRingAdapter);
                stalkColourAboveRing.setAdapter(stalkColourAboveRingAdapter);
                stalkColourBelowRing.setAdapter(stalkColourBelowRingAdapter);
                veilType.setAdapter(veilTypeAdapter);
                veilColour.setAdapter(veilColourAdapter);
                ringNumber.setAdapter(ringNumberAdapter);
                ringType.setAdapter(ringTypeAdapter);
                population.setAdapter(populationAdapter);
                habitat.setAdapter(habitatAdapter);
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = parent.getSelectedItem().toString();
        if(!item.contains("Select")){
            String tag = parent.getTag().toString();
            boolean newTag = true;
            //check if item is new or just being modified
            for (String currentTag: attributesList) {
                if(currentTag == tag){
                    newTag = false;
                }
            }
            Toast.makeText(this, item + " selected from " + tag, Toast.LENGTH_SHORT).show();
            attributesMap.put(tag, item);
            if(newTag == true) {
                attributesList.add(tag);
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    //reduce array adapter code
    private void arrayAdapterCreator(ArrayAdapter adapter, Spinner spinner){
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
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