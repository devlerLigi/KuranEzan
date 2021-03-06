package com.uren.kuranezan.MainFragments.TabKuran.Adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.uren.kuranezan.Interfaces.ListItemClickListener;
import com.uren.kuranezan.Models.QuranModels.Ayahs;
import com.uren.kuranezan.R;
import com.uren.kuranezan.Utils.Config;

import java.util.ArrayList;


public class AyahAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private ListItemClickListener listItemClickListener;
    private ArrayList<Ayahs> ayahOriginalList, ayahTransliterationlList, ayahTranslationList;
    boolean showTransliteration, showTranslation;
    int fontSizeArabic, fontSizeTransliteration, fontSizeTranslation;
    String language, fontArabic;

    public AyahAdapter(Context context) {
        this.mContext = context;
        this.ayahOriginalList = new ArrayList<Ayahs>();
        this.ayahTransliterationlList = new ArrayList<Ayahs>();
        this.ayahTranslationList = new ArrayList<Ayahs>();

        setPreferences();
    }

    private void setPreferences() {
        language = Config.lang;
        showTransliteration = Config.showTransliteration;
        showTranslation = Config.showTranslation;
        fontArabic = Config.fontArabic;
        fontSizeArabic = Config.fontSizeArabic;
        fontSizeTransliteration = Config.fontSizeTransliteration;
        fontSizeTranslation = Config.fontSizeTranslation;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_ayah, parent, false);

        RecyclerView.ViewHolder viewHolder = new MyViewHolder(itemView);

        return viewHolder;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Ayahs ayahOriginal = ayahOriginalList.get(position);
        Ayahs ayahTranslatiration = ayahTransliterationlList.get(position);
        Ayahs ayahTranslation = ayahTranslationList.get(position);
        ((MyViewHolder) holder).setData(ayahOriginal, ayahTranslatiration, ayahTranslation, position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        int position;
        Ayahs ayahOriginal, ayahTranslatiration, ayahTranslation;

        LinearLayout llAyah;
        TextView txtAyahNumber;
        TextView txtAyahOriginal, txtAyahTransliteration, txtAyahTranslation;

        public MyViewHolder(View view) {
            super(view);

            llAyah = (LinearLayout) view.findViewById(R.id.llAyah);
            txtAyahNumber = (TextView) view.findViewById(R.id.txtAyahNumber);
            txtAyahOriginal = (TextView) view.findViewById(R.id.txtAyahOriginal);
            txtAyahTransliteration = (TextView) view.findViewById(R.id.txtAyahTransliteration);
            txtAyahTranslation = (TextView) view.findViewById(R.id.txtAyahTranslation);

            setListeners();
        }

        private void setListeners() {

        }


        public void setData(Ayahs ayahOriginal, Ayahs ayahTranslatiration, Ayahs ayahTranslation, int position) {

            this.position = position;
            this.ayahOriginal = ayahOriginal;
            this.ayahTranslatiration = ayahTranslatiration;
            this.ayahTranslation = ayahTranslation;

            String ayahNumDesc = String.valueOf(ayahOriginal.getNumberInSurah()) + ". " + mContext.getResources().getString(R.string.ayah);

            txtAyahNumber.setText(ayahNumDesc);
            txtAyahOriginal.setText(ayahOriginal.getText());
            txtAyahTransliteration.setText(ayahTranslatiration.getText());
            txtAyahTranslation.setText(ayahTranslation.getText());

            setAyahOriginalTypeface();
            setBackgroundColor();
            setOptions();
        }

        private void setBackgroundColor() {
            if (position % 2 == 0) {
                llAyah.setBackgroundColor(ContextCompat.getColor(mContext, R.color.mushaf3));

            } else {
                llAyah.setBackgroundColor(ContextCompat.getColor(mContext, R.color.mushaf2));
            }
        }

        private void setOptions() {

            //Transliteration layout
            if (!showTransliteration) {
                txtAyahTransliteration.setVisibility(View.GONE);
            } else {
                txtAyahTransliteration.setVisibility(View.VISIBLE);
            }
            //Data layout
            if (!showTranslation) {
                txtAyahTranslation.setVisibility(View.GONE);
            } else {
                txtAyahTranslation.setVisibility(View.VISIBLE);
            }

            txtAyahOriginal.setTextSize(fontSizeArabic);
            txtAyahTransliteration.setTextSize(fontSizeTransliteration);
            txtAyahTranslation.setTextSize(fontSizeTranslation);

        }

        private void setAyahOriginalTypeface() {

            try {
                Typeface custom_font = Typeface.createFromAsset(mContext.getAssets(), fontArabic);
                txtAyahOriginal.setTypeface(custom_font);
            } catch (Exception e) {
                Typeface custom_font = Typeface.createFromAsset(mContext.getAssets(), Config.defaultFontArabic);
                txtAyahOriginal.setTypeface(custom_font);
                Toast.makeText(mContext, "font bulunamadi", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }

    }


    @Override
    public int getItemCount() {
        return ayahOriginalList.size();
    }

    public void addAll(ArrayList<Ayahs> ayahOriginalList, ArrayList<Ayahs> ayahTransliterationlList, ArrayList<Ayahs> ayahTranslationList) {

        this.ayahOriginalList = ayahOriginalList;
        this.ayahTransliterationlList = ayahTransliterationlList;
        this.ayahTranslationList = ayahTranslationList;

    }

    public void clearList() {
        ayahOriginalList.clear();
        notifyDataSetChanged();
    }

    public void setListItemClickListener(ListItemClickListener listItemClickListener) {
        this.listItemClickListener = listItemClickListener;
    }

    public void showTransliteration(boolean isShow) {
        this.showTransliteration = isShow;
        notifyDataSetChanged();
    }

    public void showTranslation(boolean isShow) {
        this.showTranslation = isShow;
        notifyDataSetChanged();
    }
    public void updateFontArabic(String fontArabic) {
        this.fontArabic = fontArabic;
        notifyDataSetChanged();
    }
    public void updateFontSizeArabic(int fontSize) {
        this.fontSizeArabic = fontSize;
        notifyDataSetChanged();
    }
    public void updateFontSizeTransliteration(int fontSize) {
        this.fontSizeTransliteration = fontSize;
        notifyDataSetChanged();
    }
    public void updateFontSizeTranslation(int fontSize) {
        this.fontSizeTranslation = fontSize;
        notifyDataSetChanged();
    }

    public void updateLanguage(ArrayList<Ayahs> ayahTranslationList) {
        this.ayahTranslationList = ayahTranslationList;
        notifyDataSetChanged();
    }

    public void updateTransliterationLanguage(ArrayList<Ayahs> ayahTransliterationlList) {
        this.ayahTransliterationlList = ayahTransliterationlList;
        notifyDataSetChanged();
    }
}


