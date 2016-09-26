package com.babajisoft.sanketc.fragment;

/**
 * Created by s5 on 31/8/16.
 */
import android.app.ProgressDialog;
import android.database.SQLException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.babajisoft.sanketc.R;
import com.babajisoft.sanketc.adapter.PersonInfoDTOsAdapter;
import com.babajisoft.sanketc.adapter.VotersAdapter;
import com.babajisoft.sanketc.dto.PersonInfoDTO;
import com.babajisoft.sanketc.helper.Databasehelper;
import com.babajisoft.sanketc.helper.EndlessRecyclerViewScrollListener;
import com.babajisoft.sanketc.helper.ToastHelper;

import java.util.ArrayList;


public class CompleteVoteFragment extends Fragment{
    ImageButton searchButton;
    EditText input_FromPart, inputTopart;
    String startfrom, toPart;
    ArrayList<PersonInfoDTO> votersSearchinfo;
    PersonInfoDTO selectedperson;
    VotersAdapter votersAdapter;
    RecyclerView listView;
    Databasehelper myDbHelper ;
    int posision;
    PersonInfoDTOsAdapter adapter;

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
        listView=(RecyclerView) view.findViewById(R.id.listview_voters);
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
            return myDbHelper.getVotingDoneVotersInfo(Integer.parseInt(startfrom), Integer.parseInt(toPart), 0,0);
        }

        @Override
        protected void onPostExecute(ArrayList<PersonInfoDTO> result) {
            if(progressDialog != null){
                progressDialog.dismiss();
            }

            if (result != null) {
                try {
                    if (result != null) {
                        votersSearchinfo=result;
                         adapter = new PersonInfoDTOsAdapter(votersSearchinfo);

                     //   votersAdapter = new VotersAdapter(getActivity(), R.layout.votorsinfolist_item, result);
                        listView.setAdapter(adapter);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                        listView.setLayoutManager(linearLayoutManager);
                        listView.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearLayoutManager) {
                            @Override
                            public void onLoadMore(int page, int totalItemsCount) {
                                ArrayList<PersonInfoDTO> voterSearchinfo=myDbHelper.getVotingDoneVotersInfo(Integer.parseInt(startfrom), Integer.parseInt(toPart), 0,page*100);

                                votersSearchinfo.addAll(voterSearchinfo);
                                // listView.setAdapter(adapter);
                                // listView.setAdapter(adapter);
                                int curSize = adapter.getItemCount();
                                adapter.notifyItemRangeInserted(curSize, votersSearchinfo.size() - 1);
                              /*  List<PersonInfoDTO> moreContacts = Contact.createContactsList(10, page);
                                int curSize = adapter.getItemCount();
                                allContacts.addAll(moreContacts);
                                adapter.notifyItemRangeInserted(curSize, allContacts.size() - 1);*/
                            }
                        });
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    ToastHelper.showToast(getActivity(),"Please cehck input", Toast.LENGTH_LONG);

                }
            }
        }
    }
}