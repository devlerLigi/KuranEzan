package com.uren.kuranezan.MainFragments.TabDiger.Adapters;


import android.content.Context;
import android.graphics.PorterDuff;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.uren.kuranezan.MainFragments.TabDiger.SubFragments.Interfaces.DhikrReturnCallback;
import com.uren.kuranezan.MainFragments.TabDiger.Utils.MyZikir;
import com.uren.kuranezan.MainFragments.TabDiger.Utils.TinyDB;
import com.uren.kuranezan.R;

import java.util.ArrayList;

import static com.uren.kuranezan.Constants.StringConstants.MY_ZIKIRS;

public class MyDhikrsAdapter extends RecyclerView.Adapter<MyDhikrsAdapter.MyDhikrsHolder> {

    private Context context;
    private ArrayList<Object> zikirArrayList;
    private DhikrReturnCallback dhikrReturnCallback;

    public MyDhikrsAdapter(Context context, ArrayList<Object> zikirArrayList, DhikrReturnCallback dhikrReturnCallback) {
        this.context = context;
        this.zikirArrayList = zikirArrayList;
        this.dhikrReturnCallback = dhikrReturnCallback;
    }

    @Override
    public MyDhikrsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_dhkir_item, parent, false);
        return new MyDhikrsHolder(itemView);
    }

    public class MyDhikrsHolder extends RecyclerView.ViewHolder {

        int position;
        MyZikir myZikir;
        ImageView settingsImgv;
        TextView zikirNameTv;
        TextView zikirTimeTv;
        TextView zikirNumTv;

        public MyDhikrsHolder(View view) {
            super(view);

            settingsImgv = view.findViewById(R.id.settingsImgv);
            zikirNameTv = view.findViewById(R.id.zikirNameTv);
            zikirTimeTv = view.findViewById(R.id.zikirTimeTv);
            zikirNumTv = view.findViewById(R.id.zikirNumTv);

            settingsImgv.setColorFilter(context.getResources().getColor(R.color.Green), PorterDuff.Mode.SRC_IN);

            settingsImgv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openDhikrMenu();
                }
            });
        }

        public void setData(Object object, int position) {
            this.myZikir = (MyZikir)object;
            this.position = position;

            if (myZikir != null) {
                if (myZikir.getZikirName() != null)
                    zikirNameTv.setText(myZikir.getZikirName());

                if (myZikir.getZikirTime() != null)
                    zikirTimeTv.setText(myZikir.getZikirTime());

                zikirNumTv.setText(Integer.toString(myZikir.getZikirCount()));
            }
        }

        private void openDhikrMenu(){
            PopupMenu popupMenu = new PopupMenu(context, settingsImgv);
            popupMenu.inflate(R.menu.dhikr_item_menu);

            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.continueItem:
                            dhikrReturnCallback.onReturn(myZikir);
                            break;
                        case R.id.deleteItem:
                            deleteDhikr();
                            break;
                    }
                    return false;
                }
            });
            popupMenu.show();
        }

        private void deleteDhikr(){
            TinyDB tinyDB = new TinyDB(context);
            ArrayList<Object> tempList = tinyDB.getListObject(MY_ZIKIRS, MyZikir.class);
            tempList.remove(position);
            zikirArrayList.remove(position);
            tinyDB.putListObject(MY_ZIKIRS, tempList);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, getItemCount());
        }
    }

    @Override
    public void onBindViewHolder(MyDhikrsHolder holder, final int position) {
        Object object = zikirArrayList.get(position);
        holder.setData(object, position);
    }

    @Override
    public int getItemCount() {
        if (zikirArrayList != null)
            return zikirArrayList.size();
        else
            return 0;
    }
}