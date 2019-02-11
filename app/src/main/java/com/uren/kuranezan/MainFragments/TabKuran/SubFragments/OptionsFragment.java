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
import android.widget.SeekBar;
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

import butterknife.BindArray;
import butterknife.BindDimen;
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

    @BindArray(R.array.arabic_font)
    String[] ARABIC_FONT;
    @BindArray(R.array.arabic_font_code)
    String[] ARABIC_FONT_CODE;

    @BindView(R.id.seekbar1)
    SeekBar seekbar1;
    @BindView(R.id.seekbar2)
    SeekBar seekbar2;
    @BindView(R.id.seekbar3)
    SeekBar seekbar3;
    @BindView(R.id.txtFontSizeArabic)
    TextView txtFontSizeArabic;
    @BindView(R.id.txtFontSizeTransliteration)
    TextView txtFontSizeTransliteration;
    @BindView(R.id.txtFontSizeTranslation)
    TextView txtFontSizeTranslation;

    private static final int REQUEST_TYPE_LANGUAGE = 1;
    private static final int REQUEST_TYPE_FONT_ARABIC = 2;
    private static final int REQUEST_TYPE_FONT_SIZE_ARABIC = 3;
    private static final int REQUEST_TYPE_FONT_SIZE_TRANSLITERATION = 4;
    private static final int REQUEST_TYPE_FONT_SIZE_TRANSLATION = 5;

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

        setSeekbars();


    }

    private void setSeekbars() {

        seekbar1.setProgress(Config.fontSizeArabic);
        txtFontSizeArabic.setTextSize(Config.fontSizeArabic);
        seekbar1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int p = 0;

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
                if (p < 15) {
                    p = 15;
                    seekbar1.setProgress(p);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // TODO Auto-generated method stub
                p = progress;
                txtFontSizeArabic.setTextSize(p);
                Config.fontSizeArabic = p;
                Config.update(getContext());
                OptionsHelper.OptionsClicked.getInstance().onFontSizeArabicChanged(numberOfCallback, p);
            }
        });

        seekbar2.setProgress(Config.fontSizeTransliteration);
        txtFontSizeTransliteration.setTextSize(Config.fontSizeTransliteration);
        seekbar2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int p = 0;

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
                if (p < 12) {
                    p = 15;
                    seekbar2.setProgress(p);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // TODO Auto-generated method stub
                p = progress;
                txtFontSizeTransliteration.setTextSize(p);
                Config.fontSizeTransliteration = p;
                Config.update(getContext());
                OptionsHelper.OptionsClicked.getInstance().onFontSizeTransliterationChanged(numberOfCallback, p);
            }
        });

        seekbar3.setProgress(Config.fontSizeTranslation);
        txtFontSizeTranslation.setTextSize(Config.fontSizeTranslation);
        seekbar3.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int p = 0;

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
                if (p < 15) {
                    p = 15;
                    seekbar3.setProgress(p);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // TODO Auto-generated method stub
                p = progress;
                txtFontSizeTranslation.setTextSize(p);
                Config.fontSizeTranslation = p;
                Config.update(getContext());
                OptionsHelper.OptionsClicked.getInstance().onFontSizeTranslationChanged(numberOfCallback, p);
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


    }


    private void llLanguageClicked() {
        Translations translations = TranslationList.getInstance().getTranslations();
        showRadioDialogBox(getContext(), REQUEST_TYPE_LANGUAGE, translations);
    }

    private void llFontArabicClicked() {
        showRadioDialogBox(getContext(), REQUEST_TYPE_FONT_ARABIC, null);
    }


    /*******************************************************/

    public void showRadioDialogBox(Context context, final int requestType, Translations translations) {

        // custom dialog
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //before
        dialog.setContentView(R.layout.radiobutton_dialog);
        ScrollView scrollView = (ScrollView) dialog.findViewById(R.id.scrollView);
        final RadioGroup rg = (RadioGroup) dialog.findViewById(R.id.radio_group);
        Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);
        Button btnApply = (Button) dialog.findViewById(R.id.btnApply);

        final HashMap<Integer, String> itemMap = new HashMap<Integer, String>();
        if (requestType == REQUEST_TYPE_LANGUAGE) {
            dialog.setTitle(context.getResources().getString(R.string.language));
            int selectedItemPosition = 0;
            for (int i = 0; i < translations.getData().length; i++) {
                Data data = translations.getData()[i];
                String langString = data.getLanguage().toUpperCase() + " " + data.getName();
                RadioButton rb = new RadioButton(context);
                rb.setText(langString);
                rb.setId(i);
                itemMap.put(i, data.getIdentifier());
                rg.addView(rb);

                if (data.getIdentifier().equals(Config.lang)) {
                    rb.setChecked(true);
                }

            }

        } else if (requestType == REQUEST_TYPE_FONT_ARABIC) {
            for (int i = 0; i < ARABIC_FONT.length; i++) {
                RadioButton rb = new RadioButton(context);
                rb.setText(ARABIC_FONT[i]);
                rb.setId(i);
                itemMap.put(i, ARABIC_FONT_CODE[i]);
                rg.addView(rb);

                if (ARABIC_FONT_CODE[i].equals(Config.fontArabic)) {
                    rb.setChecked(true);
                }

            }
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

                if (requestType == REQUEST_TYPE_LANGUAGE) {
                    checkLanguage(itemMap.get(rg.getCheckedRadioButtonId()));
                    dialog.dismiss();
                } else if (requestType == REQUEST_TYPE_FONT_ARABIC) {
                    checkFontArabic(itemMap.get(rg.getCheckedRadioButtonId()));
                    dialog.dismiss();
                } else {

                }

            }
        });


    }

    private void checkLanguage(String selectedLangIdentifier) {
        Toast.makeText(getContext(), selectedLangIdentifier, Toast.LENGTH_LONG).show();
        Config.lang = selectedLangIdentifier;
        Config.update(getContext());
    }

    private void checkFontArabic(String selectedFontIdetifier) {
        Toast.makeText(getContext(), selectedFontIdetifier, Toast.LENGTH_LONG).show();
        Config.fontArabic = selectedFontIdetifier;
        Config.update(getContext());
        OptionsHelper.OptionsClicked.getInstance().onFontArabicChanged(numberOfCallback, selectedFontIdetifier);

    }

}
