package com.abdullahbalta.androidrealtimedatabase.fragment;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.abdullahbalta.androidrealtimedatabase.R;
import com.abdullahbalta.androidrealtimedatabase.activitiy.MainActivity;
import com.abdullahbalta.androidrealtimedatabase.model.InventorModel;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddNewEntryFragment extends Fragment implements View.OnClickListener {

    private Button save, chooseProf;
    private EditText inputName, inputWiki, inputImage;
    private List<String> pList = new ArrayList<>();
    private InventorModel inventorModel;
    private boolean [] isSelected = {false, false, false, false, false, false, false};
    private Map<String, Object> postValues;


    public AddNewEntryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_add_new_entry, container, false);
        initViews(rootView);
        if(this.getArguments() != null) {
            inventorModel = (InventorModel) this.getArguments().getSerializable("INVENTOR");
            if (inventorModel != null) {
                inputName.setText(inventorModel.getInventor_name());
                inputWiki.setText(inventorModel.getWiki());
                inputImage.setText(inventorModel.getImage());
                pList = inventorModel.getProfession();
            }
        }
        return rootView;
    }

    private void initViews(View view){
        save = (Button)view.findViewById(R.id.apply_btn);
        save.setOnClickListener(this);
        chooseProf = (Button)view.findViewById(R.id.chooseinventor_professions);
        chooseProf.setOnClickListener(this);

        inputName = (EditText)view.findViewById(R.id.input_inventor_name);
        inputWiki = (EditText)view.findViewById(R.id.input_inventor_wiki);
        inputImage = (EditText)view.findViewById(R.id.input_inventor_image_link);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.apply_btn:
                saveEntry();
                break;
            case R.id.chooseinventor_professions:
                chooseProfessions();
                break;
        }
    }

    private void chooseProfessions() {
        final String [] professions = {"Fizikçi", "Kimyacı", "Biyolog", "Filozof", "Tarihçi", "Ressam", "Heykeltraş"};
        AlertDialog.Builder choose = new AlertDialog.Builder(getActivity());
        choose.setMultiChoiceItems(professions, isSelected, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                isSelected[which] = isChecked;
                if(isChecked){
                    pList.add(professions[which]);
                }else if(pList.contains(professions[which])){
                    pList.remove(which);
                }
            }
        });
        choose.show();
    }

    private void saveEntry() {
        if(inventorModel == null) {
            //Yeni Kayıt
            inventorModel = new InventorModel();
            inventorModel.setInventor_id(MainActivity.COUNT++);
            inventorModel.setImage(inputImage.getText().toString());
            inventorModel.setInventor_name(inputName.getText().toString());
            inventorModel.setWiki(inputWiki.getText().toString());
            inventorModel.setProfession(pList);
            FirebaseDatabase.getInstance().getReference().child("inventors").child(inventorModel.getInventor_id() + "").setValue(inventorModel);
            Toast.makeText(getActivity(),"Kaydedildi",Toast.LENGTH_SHORT).show();
            Log.i("SAVE", "saveEntry: Kaydedildi.");
        }else{
            //Güncelle
            inventorModel.setImage(inputImage.getText().toString());
            inventorModel.setInventor_name(inputName.getText().toString());
            inventorModel.setWiki(inputWiki.getText().toString());
            inventorModel.setProfession(pList);
            postValues = inventorModel.toMap();
            FirebaseDatabase.getInstance().getReference().child("inventors").child(inventorModel.getInventor_id() + "").updateChildren(postValues);
            Toast.makeText(getActivity(),"Güncellendi",Toast.LENGTH_SHORT).show();
            Log.i("SAVE", "saveEntry: Güncellendi.");
        }
    }
}
