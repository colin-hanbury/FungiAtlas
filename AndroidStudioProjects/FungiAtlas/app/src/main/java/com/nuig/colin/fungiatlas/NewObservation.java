package com.nuig.colin.fungiatlas;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.ArrayMap;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;


public class NewObservation extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    FirebaseDatabase database;
    DatabaseReference myRef;
    private List<String> attributesList;
    private Map<String, String> attributesMap;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_observation);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        Spinner capShape = findViewById(R.id.spinnerCapShape);
        Spinner capSurface = findViewById(R.id.spinnerCapSurface);
        Spinner capColour = findViewById(R.id.spinnerCapColour);
        Spinner bruises = findViewById(R.id.spinnerBruises);
        Spinner odor = findViewById(R.id.spinnerOdor);
        Spinner gillAttachment = findViewById(R.id.spinnerGillAttachment);
        Spinner gillSpacing = findViewById(R.id.spinnerGillSpacing);
        Spinner gillSize = findViewById(R.id.spinnerGillSize);
        Spinner gillColour = findViewById(R.id.spinnerGillColour);
        Spinner stalkShape = findViewById(R.id.spinnerStalkShape);
        Spinner stalkRoot = findViewById(R.id.spinnerStalkRoot);
        Spinner stalkSurfaceAboveRing = findViewById(R.id.spinnerStalkSurfaceAboveRing);
        Spinner stalkSurfaceBelowRing = findViewById(R.id.spinnerStalkSurfaceBelowRing);
        Spinner stalkColourAboveRing = findViewById(R.id.spinnerStalkColourAboveRing);
        Spinner stalkColourBelowRing = findViewById(R.id.spinnerStalkColourBelowRing);
        Spinner veilType = findViewById(R.id.spinnerVeilType);
        Spinner veilColour = findViewById(R.id.spinnerVeilColour);
        Spinner ringNumber = findViewById(R.id.spinnerRingNumber);
        Spinner ringType = findViewById(R.id.spinnerRingType);
        Spinner population = findViewById(R.id.spinnerPopulation);
        Spinner habitat = findViewById(R.id.spinnerHabitat);

        Button submit = findViewById(R.id.buttonSubmit);

        attributesList = new ArrayList<>();
        attributesMap = new ArrayMap<>();


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String time = String.valueOf(new Date().getTime());
                for(int i=0; i< attributesList.size(); i++) {
                    String tag =attributesList.get(i);
                    myRef.child("Fungi Attributes:").child(time).child(tag).setValue(attributesMap.get(tag));
                }
                Toast.makeText(NewObservation.this,"Observation submitted", Toast.LENGTH_SHORT).show();
            }
        });

        ArrayAdapter<String> capShapeAdapter = new ArrayAdapter<String>(
                NewObservation.this,
                android.R.layout.simple_expandable_list_item_1,
                getResources().getStringArray(R.array.capShapes));
        arrayAdapterCreator(capShapeAdapter,capShape);

        ArrayAdapter<String> capSurfaceAdapter = new ArrayAdapter<String>(
                NewObservation.this,
                android.R.layout.simple_expandable_list_item_1,
                getResources().getStringArray(R.array.capSurfaces));
        arrayAdapterCreator(capSurfaceAdapter,capSurface);

        ArrayAdapter<String> capColourAdapter = new ArrayAdapter<String>(
                NewObservation.this,
                android.R.layout.simple_expandable_list_item_1,
                getResources().getStringArray(R.array.capColours));
        arrayAdapterCreator(capColourAdapter,capColour);

        ArrayAdapter<String> bruisesAdapter = new ArrayAdapter<String>(
                NewObservation.this,
                android.R.layout.simple_expandable_list_item_1,
                getResources().getStringArray(R.array.bruises));
        arrayAdapterCreator(bruisesAdapter,bruises);

        ArrayAdapter<String> odorAdapter = new ArrayAdapter<String>(
                NewObservation.this,
                android.R.layout.simple_expandable_list_item_1,
                getResources().getStringArray(R.array.odor));
        arrayAdapterCreator(odorAdapter,odor);

        ArrayAdapter<String> gillAttachmentAdapter = new ArrayAdapter<String>(
                NewObservation.this,
                android.R.layout.simple_expandable_list_item_1,
                getResources().getStringArray(R.array.gillAttachments));
        arrayAdapterCreator(gillAttachmentAdapter, gillAttachment);

        ArrayAdapter<String> gillSpacingAdapter = new ArrayAdapter<String>(
                NewObservation.this,
                android.R.layout.simple_expandable_list_item_1,
                getResources().getStringArray(R.array.gillSpacings));
        arrayAdapterCreator(gillSpacingAdapter,gillSpacing);

        ArrayAdapter<String> gillSizeAdapter = new ArrayAdapter<String>(
                NewObservation.this,
                android.R.layout.simple_expandable_list_item_1,
                getResources().getStringArray(R.array.gillSizes));
        arrayAdapterCreator(gillSizeAdapter,gillSize);

        ArrayAdapter<String> gillColourAdapter = new ArrayAdapter<String>(
                NewObservation.this,
                android.R.layout.simple_expandable_list_item_1,
                getResources().getStringArray(R.array.gillColours));
        arrayAdapterCreator(gillColourAdapter,gillColour);

        ArrayAdapter<String> stalkShapeAdapter = new ArrayAdapter<String>(
                NewObservation.this,
                android.R.layout.simple_expandable_list_item_1,
                getResources().getStringArray(R.array.stalkShapes));
        arrayAdapterCreator(stalkShapeAdapter,stalkShape);

        ArrayAdapter<String> stalkRootAdapter = new ArrayAdapter<String>(
                NewObservation.this,
                android.R.layout.simple_expandable_list_item_1,
                getResources().getStringArray(R.array.stalkRoots));
        arrayAdapterCreator(stalkRootAdapter,stalkRoot);

        ArrayAdapter<String> stalkSurfaceAboveRingAdapter = new ArrayAdapter<String>(
                NewObservation.this,
                android.R.layout.simple_expandable_list_item_1,
                getResources().getStringArray(R.array.stalkSurfacesAboveRing));
        arrayAdapterCreator(stalkSurfaceAboveRingAdapter, stalkSurfaceAboveRing);

        ArrayAdapter<String> stalkSurfaceBelowRingAdapter = new ArrayAdapter<String>(
                NewObservation.this,
                android.R.layout.simple_expandable_list_item_1,
                getResources().getStringArray(R.array.stalkSurfacesBelowRing));
        arrayAdapterCreator(stalkSurfaceBelowRingAdapter, stalkSurfaceBelowRing);

        ArrayAdapter<String> stalkColourAboveRingAdapter = new ArrayAdapter<String>(
                NewObservation.this,
                android.R.layout.simple_expandable_list_item_1,
                getResources().getStringArray(R.array.stalkColoursAboveRing));
        arrayAdapterCreator(stalkColourAboveRingAdapter,stalkColourAboveRing);

        ArrayAdapter<String> stalkColourBelowRingAdapter = new ArrayAdapter<String>(
                NewObservation.this,
                android.R.layout.simple_expandable_list_item_1,
                getResources().getStringArray(R.array.stalkColoursBelowRing));
        arrayAdapterCreator(stalkColourBelowRingAdapter, stalkColourBelowRing);

        ArrayAdapter<String> veilTypeAdapter = new ArrayAdapter<String>(
                NewObservation.this,
                android.R.layout.simple_expandable_list_item_1,
                getResources().getStringArray(R.array.veilTypes));
        arrayAdapterCreator(veilTypeAdapter, veilType);

        ArrayAdapter<String> veilColourAdapter = new ArrayAdapter<String>(
                NewObservation.this,
                android.R.layout.simple_expandable_list_item_1,
                getResources().getStringArray(R.array.veilColours));
        arrayAdapterCreator(veilColourAdapter,veilColour);

        ArrayAdapter<String> ringNumberAdapter = new ArrayAdapter<String>(
                NewObservation.this,
                android.R.layout.simple_expandable_list_item_1,
                getResources().getStringArray(R.array.ringNumber));
        arrayAdapterCreator(ringNumberAdapter,ringNumber);

        ArrayAdapter<String> ringTypeAdapter = new ArrayAdapter<String>(
                NewObservation.this,
                android.R.layout.simple_expandable_list_item_1,
                getResources().getStringArray(R.array.ringType));
        arrayAdapterCreator(ringTypeAdapter,ringType);

        ArrayAdapter<String> populationAdapter = new ArrayAdapter<String>(
                NewObservation.this,
                android.R.layout.simple_expandable_list_item_1,
                getResources().getStringArray(R.array.population));
        arrayAdapterCreator(populationAdapter,population);

        ArrayAdapter<String> habitatAdapter = new ArrayAdapter<String>(
                NewObservation.this,
                android.R.layout.simple_expandable_list_item_1,
                getResources().getStringArray(R.array.habitats));
        arrayAdapterCreator(habitatAdapter, habitat);
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

    public void arrayAdapterCreator(ArrayAdapter adapter, Spinner spinner){
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }
}