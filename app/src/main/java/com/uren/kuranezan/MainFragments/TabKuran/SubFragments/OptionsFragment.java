package com.uren.kuranezan.MainFragments.TabKuran.SubFragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.uren.kuranezan.MainFragments.BaseFragment;
import com.uren.kuranezan.MainFragments.TabKuran.Adapters.AyahAdapter;
import com.uren.kuranezan.MainFragments.TabKuran.JavaClasses.OptionsHelper;
import com.uren.kuranezan.Models.Ayahs;
import com.uren.kuranezan.Models.Quran;
import com.uren.kuranezan.Models.Surahs;
import com.uren.kuranezan.R;
import com.uren.kuranezan.Singleton.QuranOriginal;
import com.uren.kuranezan.Singleton.QuranTranslation;
import com.uren.kuranezan.Singleton.QuranTransliteration;
import com.uren.kuranezan.Utils.ClickableImage.ClickableImageView;
import com.uren.kuranezan.Utils.Config;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class OptionsFragment extends BaseFragment
        implements View.OnClickListener {

    View mView;
    private int numberOfCallback;

    @BindView(R.id.txtToolbarTitle)
    TextView txtToolbarTitle;
    @BindView(R.id.imgLeft)
    ClickableImageView imgBack;

    @BindView(R.id.chkShowTransliteration)
    CheckBox chkShowTransliteration;
    @BindView(R.id.chkShowTranslation)
    CheckBox chkShowTranslation;

    public static OptionsFragment newInstance(int numberOfCallback) {
        Bundle args = new Bundle();
        args.putInt(ARGS_INSTANCE, numberOfCallback);
        OptionsFragment fragment = new OptionsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_options, container, false);
            ButterKnife.bind(this, mView);
            getItemsFromBundle();

            setToolbar();
            init();

        }

        return mView;
    }

    private void getItemsFromBundle() {
        Bundle args = getArguments();
        if (args != null) {
            numberOfCallback = (Integer) args.getInt(ARGS_INSTANCE);
        }
    }

    private void setToolbar() {
        txtToolbarTitle.setText(getString(R.string.options));
    }

    private void init() {
        imgBack.setVisibility(View.VISIBLE);
        imgBack.setOnClickListener(this);
        chkShowTransliteration.setChecked(Config.showTransliteration);
        chkShowTranslation.setChecked(Config.showTranslation);

        chkShowTransliteration.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                Config.showTransliteration = isChecked;
                OptionsHelper.OptionsClicked.getInstance().onShowTransliterationChanged(numberOfCallback, isChecked);
            }
        });

        chkShowTranslation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                Config.showTranslation = isChecked;
                OptionsHelper.OptionsClicked.getInstance().onShowTranslationChanged(numberOfCallback, isChecked);
            }
        });

    }


    @Override
    public void onClick(View view) {

        if (view == imgBack) {
            getActivity().onBackPressed();
        }


    }
}
