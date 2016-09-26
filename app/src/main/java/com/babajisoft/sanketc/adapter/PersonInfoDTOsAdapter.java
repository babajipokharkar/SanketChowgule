package com.babajisoft.sanketc.adapter;

/**
 * Created by babaji on 4/9/16.
 */
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.babajisoft.sanketc.R;
import com.babajisoft.sanketc.dto.PersonInfoDTO;

import java.util.ArrayList;

// Create the basic adapter extending from RecyclerView.Adapter
// Note that we specify the custom ViewHolder which gives us access to our views
public class PersonInfoDTOsAdapter extends RecyclerView.Adapter<PersonInfoDTOsAdapter.ViewHolder> {

    // Store a member variable for the PersonInfoDTOs
    private ArrayList<PersonInfoDTO> mPersonInfoDTOs;

    // Pass in the PersonInfoDTO array into the constructor
    public PersonInfoDTOsAdapter(ArrayList<PersonInfoDTO> personInfoDTOs) {
        mPersonInfoDTOs = personInfoDTOs;
    }

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        TextView txtSrno ;
        TextView txtFullName;
        TextView txtPartNo;
        TextView txtageSex;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View v) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(v);

            txtSrno     = (TextView) v.findViewById(R.id.srNo);
            txtFullName = (TextView) v.findViewById(R.id.txtFullName);
            txtPartNo   = (TextView) v.findViewById(R.id.partNo);
            txtageSex   = (TextView) v.findViewById(R.id.ageMale);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View PersonInfoDTOView = inflater.inflate(R.layout.votorsinfolist_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(PersonInfoDTOView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        PersonInfoDTO personInfoDTO = mPersonInfoDTOs.get(position);

        TextView textView = viewHolder.txtFullName;
        textView.setText(personInfoDTO.getFullName());
        viewHolder.txtFullName.setText(personInfoDTO.getFullName());
        viewHolder.txtSrno.setText(personInfoDTO.getVibhagNo()+"");
        viewHolder.txtPartNo.setText(personInfoDTO.getPartNo()+"");
        viewHolder.txtageSex.setText(personInfoDTO.getAgeSex());
    }

    @Override
    public int getItemCount() {
        return mPersonInfoDTOs.size();
    }
}