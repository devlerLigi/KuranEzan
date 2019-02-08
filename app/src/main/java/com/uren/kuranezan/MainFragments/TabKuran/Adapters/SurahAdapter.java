package com.uren.kuranezan.MainFragments.TabKuran.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.uren.kuranezan.Interfaces.ListItemClickListener;

import com.uren.kuranezan.R;

import java.util.ArrayList;
import java.util.List;


public class SurahAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<String> surahList;
    private ListItemClickListener listItemClickListener;

    public SurahAdapter(Context context) {
        this.mContext = context;
        this.surahList = new ArrayList<String>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_surah, parent, false);

        RecyclerView.ViewHolder viewHolder = new MyViewHolder(itemView);

        return viewHolder;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        String surahName = surahList.get(position);
        ((MyViewHolder) holder).setData(surahName, position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        LinearLayout llSure;
        TextView txtSurahNumber;
        TextView txtSurahName;

        String surahName;
        int position;

        public MyViewHolder(View view) {
            super(view);

            llSure = (LinearLayout) view.findViewById(R.id.llSure);
            txtSurahNumber = (TextView) view.findViewById(R.id.txtSurahNumber);
            txtSurahName = (TextView) view.findViewById(R.id.txtSurahName);

            setListeners();
        }

        private void setListeners() {

            llSure.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listItemClickListener != null){
                        listItemClickListener.onListItemClick(surahName, position);
                    }
                }
            });

        }


        public void setData(String surahName, int position) {
            this.surahName = surahName;
            this.position = position;

            txtSurahNumber.setText( String.valueOf(position+1) + ". ");
            txtSurahName.setText(surahName);
        }

    }

    @Override
    public int getItemCount() {
        return surahList.size();
    }

    public List<String> getSurahList() {
        return surahList;
    }

    public void addAll(List<String> surahList) {

        if(surahList != null ){
            this.surahList.addAll(surahList);
        }
    }

    public void clearList() {
        surahList.clear();
        notifyDataSetChanged();
    }

    public void setListItemClickListener(ListItemClickListener listItemClickListener) {
        this.listItemClickListener = listItemClickListener;
    }

}


