package com.uren.kuranezan.MainFragments.TabKuran.SubFragments;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.uren.kuranezan.MainFragments.BaseFragment;
import com.uren.kuranezan.MainFragments.TabKuran.JavaClasses.OptionsHelper;
import com.uren.kuranezan.Models.TranslationModels.Data;
import com.uren.kuranezan.Models.TranslationModels.Translations;
import com.uren.kuranezan.R;
import com.uren.kuranezan.Singleton.TranslationList;
import com.uren.kuranezan.Utils.ClickableImage.ClickableImageView;
import com.uren.kuranezan.Utils.CommonUtils;
import com.uren.kuranezan.Utils.Config;
import com.uren.kuranezan.Utils.DialogBoxUtil.DialogBoxUtil;
import com.uren.kuranezan.Utils.DialogBoxUtil.Interfaces.YesNoDialogBoxCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

    @BindView(R.id.llLanguage)
    LinearLayout llLanguage;
    @BindView(R.id.llShowTransliteration)
    LinearLayout llShowTransliteration;
    @BindView(R.id.llShowTranslation)
    LinearLayout llShowTranslation;
    @BindView(R.id.llFontArabic)
    LinearLayout llFontArabic;
    @BindView(R.id.llFontSizeArabic)
    LinearLayout llFontSizeArabic;
    @BindView(R.id.llFontSizeTransliteration)
    LinearLayout llFontSizeTransliteration;
    @BindView(R.id.llFontSizeTranslation)
    LinearLayout llFontSizeTranslation;

    private static final int REQUEST_TYPE_LANGUAGE = 1;

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

        llLanguage.setOnClickListener(this);
        llShowTransliteration.setOnClickListener(this);
        llShowTranslation.setOnClickListener(this);
        llFontArabic.setOnClickListener(this);
        llFontSizeArabic.setOnClickListener(this);
        llFontSizeTransliteration.setOnClickListener(this);
        llFontSizeTranslation.setOnClickListener(this);

        chkShowTransliteration.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                Config.showTransliteration = isChecked;
                Config.update(getContext());
                OptionsHelper.OptionsClicked.getInstance().onShowTransliterationChanged(numberOfCallback, isChecked);
            }
        });

        chkShowTranslation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                Config.showTranslation = isChecked;
                Config.update(getContext());
                OptionsHelper.OptionsClicked.getInstance().onShowTranslationChanged(numberOfCallback, isChecked);
            }
        });

    }


    @Override
    public void onClick(View view) {

        if (view == imgBack) {
            getActivity().onBackPressed();
        }

        if (view == llLanguage) {
            llLanguageClicked();
        }

        if (view == llShowTransliteration) {
            chkShowTransliteration.setChecked(!chkShowTransliteration.isChecked());
            Config.showTransliteration = chkShowTransliteration.isChecked();
            Config.update(getContext());
            OptionsHelper.OptionsClicked.getInstance().onShowTransliterationChanged(numberOfCallback, chkShowTransliteration.isChecked());
        }

        if (view == llShowTranslation) {
            chkShowTranslation.setChecked(!chkShowTranslation.isChecked());
            Config.showTranslation = chkShowTranslation.isChecked();
            Config.update(getContext());
            OptionsHelper.OptionsClicked.getInstance().onShowTranslationChanged(numberOfCallback, chkShowTranslation.isChecked());
        }

        if (view == llFontArabic) {
            llFontArabicClicked();
        }

        if (view == llFontSizeArabic) {
            llFontSizeArabicClicked();
        }

        if (view == llFontSizeTransliteration) {
            llFontSizeTransliterationClicked();
        }

        if (view == llFontSizeTranslation) {
            llFontSizeTranslationClicked();
        }


    }


    private void llLanguageClicked() {
        List<String> stringList = new ArrayList<>();
        Translations translations = TranslationList.getInstance().getTranslations();
        showRadioDialogBox(getContext(), REQUEST_TYPE_LANGUAGE, translations);
    }

    private void llFontArabicClicked() {

    }

    private void llFontSizeArabicClicked() {

    }

    private void llFontSizeTransliterationClicked() {

    }

    private void llFontSizeTranslationClicked() {
    }


    /*******************************************************/

    public void showRadioDialogBox(Context context, int requestType, Translations translations) {

        // custom dialog
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.radiobutton_dialog);
        ScrollView scrollView = (ScrollView) dialog.findViewById(R.id.scrollView);
        final RadioGroup rg = (RadioGroup) dialog.findViewById(R.id.radio_group);
        Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);
        Button btnApply = (Button) dialog.findViewById(R.id.btnApply);

        final HashMap<Integer, String> langMap = new HashMap<Integer, String>();
        if (requestType == REQUEST_TYPE_LANGUAGE) {
            dialog.setTitle(context.getResources().getString(R.string.language));
            int selectedItemPosition = 0;
            for (int i = 0; i < translations.getData().length; i++) {
                Data data = translations.getData()[i];
                String langString = data.getLanguage().toUpperCase() + " " + data.getName();
                RadioButton rb = new RadioButton(context);
                rb.setText(langString);
                rb.setId(i);
                langMap.put(i, data.getIdentifier());
                rg.addView(rb);

                if (data.getIdentifier().equals(Config.lang)) {
                    rb.setChecked(true);
                }

            }

        } else {

        }

        dialog.show();

        //set Listeners
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();

            }
        });

        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //dialog.dismiss();
                String s = langMap.get(rg.getCheckedRadioButtonId());
                checkLanguage(langMap.get(rg.getCheckedRadioButtonId()));
            }
        });


    }

    private void checkLanguage(String selectedLangIdentifier) {
        Toast.makeText(getContext(), selectedLangIdentifier, Toast.LENGTH_LONG).show();
        Config.lang = selectedLangIdentifier;
        Config.update(getContext());
    }

}
