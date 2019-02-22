package com.uren.kuranezan.MainFragments.TabNamazVakti.Adapters;

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
import com.uren.kuranezan.Models.PrayerTimeModels.PrayerTimes;
import com.uren.kuranezan.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class ImsakiyeAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<PrayerTimes> prayerTimes;
    private ListItemClickListener listItemClickListener;
    private String[] aylar;
    private String formattedDate = "";
    private int currentDatePosition = 0;

    public ImsakiyeAdapter(Context context) {
        this.mContext = context;
        this.prayerTimes = new ArrayList<PrayerTimes>();
        this.aylar = context.getResources().getStringArray(R.array.months);

        setCurrentDate();
    }

    private void setCurrentDate() {
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        formattedDate = df.format(c);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_imsakiye_item, parent, false);

        RecyclerView.ViewHolder viewHolder = new MyViewHolder(itemView);

        return viewHolder;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ((MyViewHolder) holder).setData(prayerTimes.get(position), position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        LinearLayout llImsakiyeItem;
        TextView txtDay;
        TextView txtMonth;
        TextView txtImsak;
        TextView txtGunes;
        TextView txtOgle;
        TextView txtIkindi;
        TextView txtAksam;
        TextView txtYatsi;

        PrayerTimes prayerTimes;
        String itemName;
        int position;

        public MyViewHolder(View view) {
            super(view);

            llImsakiyeItem = (LinearLayout) view.findViewById(R.id.llImsakiyeItem);
            txtDay = (TextView) view.findViewById(R.id.txtDay);
            txtMonth = (TextView) view.findViewById(R.id.txtMonth);
            txtImsak = (TextView) view.findViewById(R.id.txtImsak);
            txtGunes = (TextView) view.findViewById(R.id.txtGunes);
            txtOgle = (TextView) view.findViewById(R.id.txtOgle);
            txtIkindi = (TextView) view.findViewById(R.id.txtIkindi);
            txtAksam = (TextView) view.findViewById(R.id.txtAksam);
            txtYatsi = (TextView) view.findViewById(R.id.txtYatsi);

            setListeners();
        }

        private void setListeners() {
        }


        public void setData(PrayerTimes prayerTimes, int position) {

            this.position = position;
            this.prayerTimes = prayerTimes;

            if (prayerTimes != null) {

                setDate();
                txtImsak.setText(prayerTimes.getImsak());
                txtGunes.setText(prayerTimes.getGunes());
                txtOgle.setText(prayerTimes.getOgle());
                txtIkindi.setText(prayerTimes.getIkindi());
                txtAksam.setText(prayerTimes.getAksam());
                txtYatsi.setText(prayerTimes.getYatsi());

                if (prayerTimes.getMiladiTarihKisa().equals(formattedDate)) {
                    llImsakiyeItem.setBackgroundColor(mContext.getResources().getColor(R.color.style_color_accent));
                    currentDatePosition = position;
                } else {
                    llImsakiyeItem.setBackgroundColor(mContext.getResources().getColor(R.color.mushaf2));
                }

            }


        }


        private void setDate() {
            int day = Integer.valueOf(prayerTimes.getMiladiTarihKisa().substring(0, 2));
            int monthNum = Integer.valueOf(prayerTimes.getMiladiTarihKisa().substring(3, 5));

            txtDay.setText(String.valueOf(day));
            txtMonth.setText(aylar[monthNum - 1]);
        }

    }

    @Override
    public int getItemCount() {
        return prayerTimes.size();
    }

    public void addAll(List<PrayerTimes> prayerTimes) {

        if (prayerTimes != null) {
            this.prayerTimes.addAll(prayerTimes);
        }
    }

    public void clearList() {
        prayerTimes.clear();
        notifyDataSetChanged();
    }

    public void setListItemClickListener(ListItemClickListener listItemClickListener) {
        this.listItemClickListener = listItemClickListener;
    }

}


