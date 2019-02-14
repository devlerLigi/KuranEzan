package com.uren.kuranezan.MainFragments.TabImsakiye.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.uren.kuranezan.Interfaces.ListItemClickListener;
import com.uren.kuranezan.Models.LocationModels.Ilce;
import com.uren.kuranezan.Models.LocationModels.Sehir;
import com.uren.kuranezan.Models.LocationModels.Ulke;
import com.uren.kuranezan.R;

import java.util.ArrayList;
import java.util.List;


public class BaseListAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<Object> baseList;
    private ListItemClickListener listItemClickListener;

    public BaseListAdapter(Context context) {
        this.mContext = context;
        this.baseList = new ArrayList<Object>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_base_item, parent, false);

        RecyclerView.ViewHolder viewHolder = new MyViewHolder(itemView);

        return viewHolder;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ((MyViewHolder) holder).setData(baseList.get(position), position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        LinearLayout llItem;
        TextView txtItemName;

        String itemName;
        int position;

        public MyViewHolder(View view) {
            super(view);

            llItem = (LinearLayout) view.findViewById(R.id.llSure);
            txtItemName = (TextView) view.findViewById(R.id.txtItemName);

            setListeners();
        }

        private void setListeners() {


        }


        public void setData(Object object, int position) {

            this.position = position;

            if(object instanceof Ulke){
                Ulke ulke = (Ulke) object;
                setUlke(ulke);
            }else if(object instanceof Sehir){
                Sehir sehir = (Sehir) object;
                setSehir(sehir);
            }else if(object instanceof Ilce){
                Ilce ilce = (Ilce) object;
                setIlce(ilce);
            }else{
                return;
            }


        }

        private void setUlke(Ulke ulke) {
            Log.i("ulke", ulke.toString());
            this.itemName = ulke.getUlkeAdi();
            txtItemName.setText(itemName);
        }

        private void setSehir(Sehir sehir) {
            this.itemName = sehir.getSehirAdi();
            txtItemName.setText(itemName);
        }

        private void setIlce(Ilce ilce) {
            this.itemName = ilce.getIlceAdi();
            txtItemName.setText(itemName);
        }

    }

    @Override
    public int getItemCount() {
        return baseList.size();
    }

    public void addObject(List<Object> objectList) {

        if(baseList != null ){
            this.baseList.addAll(objectList);
        }
    }

    public void clearList() {
        baseList.clear();
        notifyDataSetChanged();
    }

    public void setListItemClickListener(ListItemClickListener listItemClickListener) {
        this.listItemClickListener = listItemClickListener;
    }

}


