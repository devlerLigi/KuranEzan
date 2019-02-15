package com.uren.kuranezan.MainFragments.TabDiger.SubFragments.DiniBilgiler;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.uren.kuranezan.MainFragments.BaseFragment;
import com.uren.kuranezan.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;

@SuppressLint("ValidFragment")
public class DiniBilgilerContentFragment extends BaseFragment
        implements View.OnClickListener {

    View mView;

    @BindView(R.id.imgLeft)
    ImageView imgLeft;
    @BindView(R.id.txtToolbarTitle)
    TextView txtToolbarTitle;
    @BindView(R.id.textView)
    TextView textView;

    private String title;
    private int rawItem;

    public DiniBilgilerContentFragment(String title, int rawItem) {
        this.title = title;
        this.rawItem = rawItem;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_dini_bilgiler_content, container, false);
            ButterKnife.bind(this, mView);
            setToolbar();
            init();
            setDescription();
        }

        return mView;
    }

    @Override
    public void onStart() {
        getActivity().findViewById(R.id.tabMainLayout).setVisibility(View.GONE);
        super.onStart();
    }

    private void init() {
        imgLeft.setVisibility(View.VISIBLE);
        imgLeft.setOnClickListener(this);
    }

    private void setDescription() {
        textView.setText(readTextFile());
    }

    private void setToolbar() {
        txtToolbarTitle.setText(title);
    }

    public String readTextFile() {
        InputStream inputStream = getResources().openRawResource(rawItem);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        byte buf[] = new byte[1024];
        int len;
        try {
            while ((len = inputStream.read(buf)) != -1) {
                outputStream.write(buf, 0, len);
            }
            outputStream.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return outputStream.toString();
    }


    @Override
    public void onClick(View view) {
        if(view == imgLeft){
            getActivity().onBackPressed();
        }
    }
}