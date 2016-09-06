package com.babajisoft.sample.adapter;
        import android.content.Context;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ArrayAdapter;
        import android.widget.ImageView;
        import android.widget.TextView;

        import com.babajisoft.sample.R;
        import com.babajisoft.sample.dto.PersonInfoDTO;

        import java.util.ArrayList;

        public class VotersAdapter extends ArrayAdapter<PersonInfoDTO> {

                  ArrayList<PersonInfoDTO> votersinfo = new ArrayList<>();

                  public VotersAdapter(Context context, int textViewResourceId, ArrayList<PersonInfoDTO> objects) {
                      super(context, textViewResourceId, objects);
                      votersinfo = objects;
                  }

                  @Override
                  public int getCount() {
                      return super.getCount();
                  }

                  @Override
                  public View getView(int position, View convertView, ViewGroup parent) {

                      View v = convertView;
                      LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                      v = inflater.inflate(R.layout.votorsinfolist_item, null);
                      TextView txtSrno     = (TextView) v.findViewById(R.id.srNo);
                      TextView txtFullName = (TextView) v.findViewById(R.id.txtFullName);
                      TextView txtPartNo   = (TextView) v.findViewById(R.id.partNo);
                      TextView txtageSex   = (TextView) v.findViewById(R.id.ageMale);

                      txtFullName.setText(votersinfo.get(position).getFullName());
                      txtSrno.setText(votersinfo.get(position).getVibhagNo()+"");
                      txtPartNo.setText(votersinfo.get(position).getPartNo()+"");
                      txtageSex.setText(votersinfo.get(position).getAgeSex());

                      return v;

                  }

              }