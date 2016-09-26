package com.babajisoft.sanketc.fragment;

/**
 * Created by s5 on 31/8/16.
 */
import android.database.SQLException;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.babajisoft.sanketc.R;
import com.babajisoft.sanketc.adapter.VotersAdapter;
import com.babajisoft.sanketc.dto.PersonInfoDTO;
import com.babajisoft.sanketc.helper.Databasehelper;
import com.babajisoft.sanketc.helper.ToastHelper;

import java.util.ArrayList;


public class DataEntryFragment extends Fragment{
    ImageButton searchButton;
    EditText input_lastname,input_layout_name,input_layout_middlename;
    String lastName,name,middleName;
    ArrayList<PersonInfoDTO> votersSearchinfo;
    PersonInfoDTO selectedperson;
    VotersAdapter votersAdapter;
    ListView listView;
    Databasehelper myDbHelper ;
    ToggleButton votingstatus;
    Button save;
    int posision;

    public DataEntryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

       View view =inflater.inflate(R.layout.fragment_data_entry, container, false);

        searchButton=(ImageButton)view.findViewById(R.id.searchButton);
        listView=(ListView)view.findViewById(R.id.listview_voters);
        save=(Button)view.findViewById(R.id.save);
        input_lastname=(EditText)view.findViewById(R.id.input_lastname);
        input_layout_name=(EditText)view.findViewById(R.id.input_name);
        input_layout_middlename=(EditText)view.findViewById(R.id.middle_name);
        myDbHelper = new Databasehelper(getActivity());
        votingstatus=(ToggleButton)view.findViewById(R.id.votingstatus);

        try {
            myDbHelper.openDatabase();
        }catch(SQLException sqle){
            throw sqle;
        }
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedperson=null;
                lastName=input_lastname.getText().toString() ==null ? "" : input_lastname.getText().toString();
                name=input_layout_name.getText().toString() ==null ? "" : input_layout_name.getText().toString();
                middleName=input_layout_middlename.getText().toString() ==null ? "" : input_layout_middlename.getText().toString();
                if(lastName.equalsIgnoreCase("") && name.equalsIgnoreCase("") && middleName.equalsIgnoreCase("") ){
                    ToastHelper.showToast(getActivity(),"Please enter Name", Toast.LENGTH_LONG);
                }else {
                    votersSearchinfo = myDbHelper.getSearchedVotersInfoNew(lastName, name, middleName);
                    if (votersSearchinfo != null) {
                        votersAdapter = new VotersAdapter(getActivity(), R.layout.votorsinfolist_item, votersSearchinfo);
                        listView.setAdapter(votersAdapter);
                    }
                }
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                selectedperson=votersSearchinfo.get(position);
                posision=position;
                if(selectedperson.getVotedone()==1){
                    votingstatus.setChecked(true);
                }else{
                    votingstatus.setChecked(false);
                }
                // When clicked, show a toast with the TextView text
/*                Toast.makeText(getApplicationContext(), selectedperson.getFullName(),
                        Toast.LENGTH_SHORT).show();*/
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              if(selectedperson !=null){
                  int status;
                 if(votingstatus.isChecked()){
                     status=1;
                 }else{
                     status=0;
                 }
                  myDbHelper.UpdateVoteDone(selectedperson.getVoterNo(),status);
                  ToastHelper.showToast(getActivity(),"Successfully done",Toast.LENGTH_SHORT);
                  selectedperson.setVotedone(status);
                  votersSearchinfo.set(posision,selectedperson);
                  votersAdapter.notifyDataSetChanged();
              }
            }
        });

        return view;
    }

}