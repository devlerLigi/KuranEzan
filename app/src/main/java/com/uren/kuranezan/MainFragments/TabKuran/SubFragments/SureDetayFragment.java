package com.uren.kuranezan.MainFragments.TabKuran.SubFragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.uren.kuranezan.Interfaces.OptionsCallback;
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

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class SureDetayFragment extends BaseFragment
        implements View.OnClickListener {

    View mView;
    private int number;
    private LinearLayoutManager mLayoutManager;
    private AyahAdapter ayahAdapter;

    @BindView(R.id.toolbarLayout)
    Toolbar toolbarLayout;
    @BindView(R.id.txtToolbarTitle)
    TextView txtToolbarTitle;
    @BindView(R.id.imgLeft)
    ClickableImageView imgBack;
    @BindView(R.id.imgRight)
    ClickableImageView imgOptions;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    ArrayList<Ayahs> ayahOriginalList = new ArrayList<Ayahs>();
    ArrayList<Ayahs> ayahTransliterationlList = new ArrayList<Ayahs>();
    ArrayList<Ayahs> ayahTranslationList = new ArrayList<Ayahs>();

    public static SureDetayFragment newInstance(int number) {
        Bundle args = new Bundle();
        args.putInt(ARGS_INSTANCE, number);
        SureDetayFragment fragment = new SureDetayFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public SureDetayFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_suredetay, container, false);
            ButterKnife.bind(this, mView);

            checkBundle();
            setToolbar();
            init();

            initRecyclerView();
            setUpRecyclerView();

        }

        return mView;
    }

    private void checkBundle() {
        Bundle args = getArguments();
        if (args != null) {
            number = (Integer) args.getInt(ARGS_INSTANCE);
        } else {
            getActivity().onBackPressed();
        }
    }

    private void setToolbar() {
        String surahName = getResources().getStringArray(R.array.surah_name)[number];
        txtToolbarTitle.setText(surahName);
    }

    private void init() {
        imgBack.setVisibility(View.VISIBLE);
        imgBack.setOnClickListener(this);
        imgOptions.setVisibility(View.VISIBLE);
        imgOptions.setOnClickListener(this);
        recyclerView.setOnClickListener(this);
    }

    private void initRecyclerView() {
        setLayoutManager();
        setAdapter();
    }

    private void setLayoutManager() {
        mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
    }

    private void setAdapter() {
        ayahAdapter = new AyahAdapter(getContext());
        recyclerView.setAdapter(ayahAdapter);
        //ayahAdapter.setListItemClickListener(this);
    }


    private void setUpRecyclerView() {
        setAyahListOriginal();
        setAyahTransliteration();
        setAyahTranslation();
        ayahAdapter.addAll(ayahOriginalList, ayahTransliterationlList, ayahTranslationList);
    }

    private void setAyahListOriginal() {
        Quran quranOriginal = QuranOriginal.getInstance().getQuranOriginal();
        Surahs[] surahs = quranOriginal.getData().getSurahs();
        Ayahs[] ayahs = surahs[number].getAyahs();

        for (int i = 0; i < ayahs.length; i++) {
            ayahOriginalList.add(ayahs[i]);
        }
    }

    private void setAyahTransliteration() {
        Quran quranTransliteration = QuranTransliteration.getInstance().getQuranTransliteration();
        Surahs[] surahs = quranTransliteration.getData().getSurahs();
        Ayahs[] ayahs = surahs[number].getAyahs();

        for (int i = 0; i < ayahs.length; i++) {
            ayahTransliterationlList.add(ayahs[i]);
        }
    }

    private void setAyahTranslation() {
        Quran quranTranslation = QuranTranslation.getInstance().getQuranTranslation();
        Surahs[] surahs = quranTranslation.getData().getSurahs();
        Ayahs[] ayahs = surahs[number].getAyahs();

        for (int i = 0; i < ayahs.length; i++) {
            ayahTranslationList.add(ayahs[i]);
        }
    }


    @Override
    public void onClick(View view) {

        if (view == imgBack) {
            getActivity().onBackPressed();
        }

        if (view == imgOptions) {
            imgOptionsClicked();
        }

    }

    private void imgOptionsClicked() {
        OptionsHelper.OptionsClicked optionsClicked = OptionsHelper.OptionsClicked.getInstance();
        optionsClicked.setSinglePostItems(mFragmentNavigation);
        optionsClicked.setProfileRefreshCallback(new OptionsCallback() {
            @Override
            public void onLanguageChanged(String language) {

            }

            @Override
            public void onShowTransliterationChanged(boolean isShow) {
                ayahAdapter.showTransliteration(isShow);
            }

            @Override
            public void onShowTranslationChanged(boolean isShow) {
                ayahAdapter.showTranslation(isShow);
            }

            @Override
            public void onFontArabicChanged(String fontType) {

            }

            @Override
            public void onFontSizeArabicChanged(float fontSize) {

            }

            @Override
            public void onFontSizeTransliterationChanged(float fontSize) {

            }

            @Override
            public void onFontSizeTranslationChanged(float fontSize) {

            }
        });

        optionsClicked.startOptionsProcess();

    }
}
