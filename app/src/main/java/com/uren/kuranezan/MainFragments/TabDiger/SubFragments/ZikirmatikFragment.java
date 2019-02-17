package com.uren.kuranezan.MainFragments.TabDiger.SubFragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.uren.kuranezan.MainFragments.BaseFragment;
import com.uren.kuranezan.MainFragments.TabDiger.SubFragments.Interfaces.DhikrReturnCallback;
import com.uren.kuranezan.MainFragments.TabDiger.Utils.MyZikir;
import com.uren.kuranezan.MainFragments.TabDiger.Utils.TinyDB;
import com.uren.kuranezan.R;
import com.uren.kuranezan.Utils.AdMobUtil.AdMobUtils;
import com.uren.kuranezan.Utils.DialogBoxUtil.DialogBoxUtil;
import com.uren.kuranezan.Utils.DialogBoxUtil.Interfaces.YesNoDialogBoxCallback;
import com.uren.kuranezan.Utils.ShapeUtil;
import com.uren.kuranezan.Utils.ToastMessageUtil;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.uren.kuranezan.Constants.StringConstants.MY_ZIKIRS;

public class ZikirmatikFragment extends BaseFragment {

    View mView;

    @BindView(R.id.vibrationImgv)
    ImageView vibrationImgv;
    @BindView(R.id.nightModeImgv)
    ImageView nightModeImgv;
    @BindView(R.id.themeImgv)
    ImageView themeImgv;
    @BindView(R.id.saveBtn)
    Button saveBtn;
    @BindView(R.id.zikirlerimBtn)
    Button zikirlerimBtn;
    @BindView(R.id.btnZikir)
    View btnZikir;
    @BindView(R.id.adView)
    AdView adView;
    @BindView(R.id.mainFragLayout)
    FrameLayout mainFragLayout;
    @BindView(R.id.tvZikir)
    TextView tvZikir;
    @BindView(R.id.btnReset)
    View btnReset;
    @BindView(R.id.imageView)
    ImageView imageView;

    boolean vibrationEnabled = false;
    boolean nightModeEnabled = false;

    private int currentBgColor = R.color.MediumSeaGreen;
    private MyZikir zikir = null;

    private static final long VIBRATE_TIME  = 150;

    int colorList[] = {
            R.color.green,
            R.color.DodgerBlue,
            R.color.Orange,
            R.color.Red,
            R.color.Yellow,
            R.color.MediumSeaGreen,
            R.color.LightBlue,
            R.color.Sienna,
            R.color.SeaGreen,
            R.color.RoyalBlue,
            R.color.SandyBrown
    };

    public ZikirmatikFragment() {

    }

    @Override
    public void onStart() {
        getActivity().findViewById(R.id.tabMainLayout).setVisibility(View.GONE);
        super.onStart();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_zikirmatik, container, false);
            ButterKnife.bind(this, mView);
            init();
            setImages();
            setShapes();
            addListeners();
        }

        return mView;
    }

    private void init() {
        MobileAds.initialize(getContext(), getResources().getString(R.string.ADMOB_APP_ID));
        AdMobUtils.loadBannerAd(adView);
    }

    private void setShapes() {
        vibrationImgv.setBackground(ShapeUtil.getShape(getResources().getColor(R.color.black_25_transparent),
                0, GradientDrawable.OVAL, 50, 0));
        nightModeImgv.setBackground(ShapeUtil.getShape(getResources().getColor(R.color.black_25_transparent),
                0, GradientDrawable.OVAL, 50, 0));
        themeImgv.setBackground(ShapeUtil.getShape(getResources().getColor(R.color.black_25_transparent),
                0, GradientDrawable.OVAL, 50, 0));
        saveBtn.setBackground(ShapeUtil.getShape(getResources().getColor(R.color.black_25_transparent),
                0, GradientDrawable.RECTANGLE, 25, 0));
        zikirlerimBtn.setBackground(ShapeUtil.getShape(getResources().getColor(R.color.black_25_transparent),
                0, GradientDrawable.RECTANGLE, 25, 0));
    }

    private void setImages() {

        Glide.with(getContext())
                .load(R.drawable.icon_smartphone)
                .apply(RequestOptions.fitCenterTransform())
                .into(vibrationImgv);

        Glide.with(getContext())
                .load(R.drawable.icon_night_mode_off)
                .apply(RequestOptions.fitCenterTransform())
                .into(nightModeImgv);

        Glide.with(getContext())
                .load(R.drawable.icon_theme)
                .apply(RequestOptions.fitCenterTransform())
                .into(themeImgv);

        Glide.with(getContext())
                .load(R.drawable.zikirmatik)
                .apply(RequestOptions.fitCenterTransform())
                .into(imageView);
    }

    private void addListeners() {
        vibrationImgv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (vibrationEnabled)
                    vibrationEnabled = false;
                else
                    vibrationEnabled = true;

                setVibrationImage();
            }
        });

        nightModeImgv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (nightModeEnabled)
                    nightModeEnabled = false;
                else
                    nightModeEnabled = true;

                setNightMode();
            }
        });

        themeImgv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (nightModeEnabled) {
                    ToastMessageUtil.showToastShort(getContext(), getContext().getResources().getString(R.string.NIGHT_MODE_MESSAGE));
                    return;
                }
                changeBackgroundColor();
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveDhikr();
            }
        });

        zikirlerimBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startMyDhikrsFragment();
            }
        });

        btnZikir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkVibration();

                int sayi = Integer.parseInt(tvZikir.getText().toString());
                sayi++;
                tvZikir.setText(Integer.toString(sayi));
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int zikirNum = Integer.parseInt(tvZikir.getText().toString());

                if (zikirNum == 0) {
                    ToastMessageUtil.showToastShort(getContext(), getResources().getString(R.string.COUNTER_NUM_IS_ZERO));
                    return;
                }

                DialogBoxUtil.showYesNoDialog(getContext(), null, getResources().getString(R.string.WARNING_RESET_COUNTER),
                        new YesNoDialogBoxCallback() {
                            @Override
                            public void yesClick() {
                                tvZikir.setText(Integer.toString(0));
                                zikir = null;
                                saveBtn.setText(getResources().getString(R.string.SAVE));
                            }

                            @Override
                            public void noClick() {

                            }
                        });
            }
        });
    }

    private void startMyDhikrsFragment(){
        mFragmentNavigation.pushFragment(new MyDhikrsFragment(new DhikrReturnCallback() {
            @Override
            public void onReturn(MyZikir myZikir) {
                zikir = myZikir;
                ToastMessageUtil.showToastShort(getContext(), "(" + zikir.getZikirName() + ") " +
                        getResources().getString(R.string.DHIKR_WILL_BE_CONTINUED));
                tvZikir.setText(Integer.toString(zikir.getZikirCount()));
                saveBtn.setText(getResources().getString(R.string.SAVE_ON));
            }
        }));
    }

    private void saveDhikr() {

        if (zikir != null)
            updateExistingDhikr();
        else
            saveNewDhikr();
    }

    private void saveNewDhikr() {
        final EditText edittext = new EditText(getContext());
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        alert.setMessage(getResources().getString(R.string.PLEASE_GIVE_A_NAME));
        alert.setTitle(getResources().getString(R.string.SAVE_TO_LIST));
        alert.setView(edittext);

        alert.setPositiveButton(getResources().getString(R.string.OK), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String zikirName = edittext.getText().toString().trim();
                int zikirNum = Integer.parseInt(tvZikir.getText().toString());

                if (zikirName.trim().isEmpty()) {
                    ToastMessageUtil.showToastShort(getContext(), getResources().getString(R.string.PLEASE_GIVE_A_NAME));
                    return;
                }

                if (zikirNum == 0) {
                    ToastMessageUtil.showToastShort(getContext(), getResources().getString(R.string.COUNTER_NUM_IS_ZERO));
                    return;
                }

                addZikir(zikirName, zikirNum);
            }
        });

        alert.setNegativeButton(getResources().getString(R.string.CANCEL), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

            }
        });

        alert.show();
    }

    private void updateExistingDhikr() {
        TinyDB tinyDB = new TinyDB(getContext());
        ArrayList<Object> tempList = tinyDB.getListObject(MY_ZIKIRS, MyZikir.class);

        int index = 0;
        for (Object object : tempList) {
            MyZikir tempZikir = (MyZikir) object;

            if (tempZikir.getZikirName().trim().equals(zikir.getZikirName().trim())) {
                int zikirNum = Integer.parseInt(tvZikir.getText().toString());
                tempZikir.setZikirCount(zikirNum);
                tempZikir.setZikirTime(convertTime(System.currentTimeMillis()));
                tempList.set(index, tempZikir);
                break;
            }
            index++;
        }
        tinyDB.putListObject(MY_ZIKIRS, tempList);
        ToastMessageUtil.showToastShort(getContext(), getResources().getString(R.string.WAS_RECORDED));
    }

    private void addZikir(String zikirName, int zikirNum) {
        TinyDB tinyDB = new TinyDB(getContext());
        ArrayList<Object> tempList = tinyDB.getListObject(MY_ZIKIRS, MyZikir.class);

        for (Object object : tempList) {
            MyZikir zikir = (MyZikir) object;

            if (zikir.getZikirName().equals(zikirName.trim())) {
                ToastMessageUtil.showToastShort(getContext(), getResources().getString(R.string.THERE_IS_SAME_ZIKIR));
                return;
            }
        }

        MyZikir myZikir = new MyZikir();
        myZikir.setZikirCount(zikirNum);
        myZikir.setZikirName(zikirName);
        myZikir.setZikirTime(convertTime(System.currentTimeMillis()));
        tempList.add(myZikir);
        tinyDB.putListObject(MY_ZIKIRS, tempList);
        tvZikir.setText(Integer.toString(0));
        startMyDhikrsFragment();
    }

    private void setVibrationImage() {
        if (vibrationEnabled) {
            Glide.with(getContext())
                    .load(R.drawable.icon_vibration)
                    .apply(RequestOptions.fitCenterTransform())
                    .into(vibrationImgv);
        } else {
            Glide.with(getContext())
                    .load(R.drawable.icon_smartphone)
                    .apply(RequestOptions.fitCenterTransform())
                    .into(vibrationImgv);
        }
    }

    private void setNightMode() {
        if (nightModeEnabled) {
            Glide.with(getContext())
                    .load(R.drawable.icon_night_mode_on).apply(RequestOptions.fitCenterTransform()).into(nightModeImgv);
            mainFragLayout.setBackgroundColor(getResources().getColor(R.color.Gray));
        } else {
            Glide.with(getContext())
                    .load(R.drawable.icon_night_mode_off).apply(RequestOptions.fitCenterTransform()).into(nightModeImgv);
            mainFragLayout.setBackgroundColor(getResources().getColor(currentBgColor));
        }
    }

    private void checkVibration() {
        if (vibrationEnabled) {
            Vibrator v = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                v.vibrate(VibrationEffect.createOneShot(VIBRATE_TIME, VibrationEffect.DEFAULT_AMPLITUDE));
            } else
                v.vibrate(VIBRATE_TIME);
        }
    }

    private void changeBackgroundColor() {
        Random rand = new Random();

        int randColor = colorList[rand.nextInt(colorList.length)];

        if (randColor == currentBgColor)
            changeBackgroundColor();

        currentBgColor = randColor;
        mainFragLayout.setBackgroundColor(getResources().getColor(currentBgColor));
    }

    public String convertTime(long time){
        Date date = new Date(time);
        Format format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return format.format(date);
    }
}
