package com.babajisoft.sample.fragment;

/**
 * Created by s5 on 31/8/16.
 */
import android.app.ProgressDialog;
import android.database.SQLException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.babajisoft.sample.R;
import com.babajisoft.sample.SearchActivity;
import com.babajisoft.sample.adapter.VotersAdapter;
import com.babajisoft.sample.dto.PersonInfoDTO;
import com.babajisoft.sample.helper.Databasehelper;
import com.babajisoft.sample.helper.ToastHelper;

import java.util.ArrayList;


public class CompleteVoteFragment extends Fragment{
    ImageButton searchButton;
    EditText input_FromPart, inputTopart;
    String startfrom, toPart;
    ArrayList<PersonInfoDTO> votersSearchinfo;
    PersonInfoDTO selectedperson;
    VotersAdapter votersAdapter;
    ListView listView;
    Databasehelper myDbHelper ;
    int posision;

    public CompleteVoteFragment() {
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

       View view =inflater.inflate(R.layout.fragment_completevote, container, false);

        searchButton=(ImageButton)view.findViewById(R.id.searchButton);
        listView=(ListView)view.findViewById(R.id.listview_voters);
        input_FromPart =(EditText)view.findViewById(R.id.input_FromPart);
        inputTopart =(EditText)view.findViewById(R.id.inputTopart);
        myDbHelper = new Databasehelper(getActivity());
        try {
            myDbHelper.openDatabase();
        }catch(SQLException sqle){
            throw sqle;
        }
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedperson=null;
                startfrom = input_FromPart.getText().toString() ==null ? "" : input_FromPart.getText().toString();
                toPart = inputTopart.getText().toString() ==null ? "" : inputTopart.getText().toString();

                if(startfrom.equalsIgnoreCase("") || toPart.equalsIgnoreCase("")){
                    ToastHelper.showToast(getActivity(),"Please enter start to end", Toast.LENGTH_LONG);
                }else {
                new GetSerchedResult().execute();
                }
            }
        });
        /*listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                selectedperson=votersSearchinfo.get(position);
                posision=position;

                // When clicked, show a toast with the TextView text
*//*                Toast.makeText(getApplicationContext(), selectedperson.getFullName(),
                        Toast.LENGTH_SHORT).show();*//*
            }
        });*/



        return view;
    }
    ProgressDialog progressDialog;
    private class GetSerchedResult extends AsyncTask<ArrayList<PersonInfoDTO>, Void, ArrayList<PersonInfoDTO>> {
        @Override
        protected void onPreExecute() {
            progressDialog=new ProgressDialog(getActivity());
            progressDialog.setMessage("Please wait a moment...");
            progressDialog.show();
        }
        @Override
        protected ArrayList<PersonInfoDTO> doInBackground(ArrayList<PersonInfoDTO>... params) {
            return myDbHelper.getVotingDoneVotersInfo(Integer.parseInt(startfrom), Integer.parseInt(toPart), 0);
        }

        @Override
        protected void onPostExecute(ArrayList<PersonInfoDTO> result) {
            if(progressDialog != null){
                progressDialog.dismiss();
            }
            if (result != null) {
                try {
                    if (result != null) {
                        votersAdapter = new VotersAdapter(getActivity(), R.layout.votorsinfolist_item, result);
                        listView.setAdapter(votersAdapter);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    ToastHelper.showToast(getActivity(),"Please cehck input", Toast.LENGTH_LONG);

                }
            }
        }
    }
}