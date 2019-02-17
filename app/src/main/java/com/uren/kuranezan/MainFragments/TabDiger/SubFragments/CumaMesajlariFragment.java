package com.uren.kuranezan.MainFragments.TabDiger.SubFragments;

import android.graphics.Bitmap;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.uren.kuranezan.MainFragments.BaseFragment;
import com.uren.kuranezan.MainFragments.TabDiger.SubFragments.CumaMesajlari.CumaMesajiPhotoFragment;
import com.uren.kuranezan.MainFragments.TabDiger.SubFragments.CumaMesajlari.CumaMesajlariContent;
import com.uren.kuranezan.R;
import com.uren.kuranezan.Utils.AdMobUtil.AdMobUtils;
import com.uren.kuranezan.Utils.BitmapConversion;
import com.uren.kuranezan.Utils.ClickableImage.ClickableImageView;
import com.uren.kuranezan.Utils.ShapeUtil;

import butterknife.BindView;
import butterknife.ButterKnife;


public class CumaMesajlariFragment extends BaseFragment
        implements View.OnClickListener {

    View mView;

    @BindView(R.id.txtToolbarTitle)
    TextView txtToolbarTitle;
    @BindView(R.id.imgLeft)
    ImageView imgLeft;
    @BindView(R.id.rlphotoComplete)
    RelativeLayout rlphotoComplete;
    @BindView(R.id.adView)
    AdView adView;

    @BindView(R.id.llSelection)
    LinearLayout llSelection;

    @BindView(R.id.photoImgv)
    ImageView photoImgv;
    @BindView(R.id.messageTv)
    TextView messageTv;
    @BindView(R.id.fridayMessageTv)
    TextView fridayMessageTv;

    @BindView(R.id.itemBackImgv)
    ImageView itemBackImgv;
    @BindView(R.id.iconSelectionImgv)
    ImageView iconSelectionImgv;
    @BindView(R.id.itemForwardImgv)
    ImageView itemForwardImgv;

    @BindView(R.id.seekbarLayout)
    FrameLayout seekbarLayout;
    @BindView(R.id.seekbar)
    SeekBar seekbar;
    @BindView(R.id.finishButton)
    Button finishButton;

    @BindView(R.id.imgvMore)
    ImageView imgvMore;
    @BindView(R.id.rlMain)
    RelativeLayout rlMain;
    @BindView(R.id.gradientLayout)
    FrameLayout gradientLayout;
    @BindView(R.id.edittext)
    EditText edittext;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    CumaMesajlariContent cumaMesajlariContent;

    private int messageIndex = 0;
    private int textColorIndex = 0;
    private int imageIndex = 0;
    private int themeColorIndex = 0;

    private int selectionId = 0;

    private static final int INCREASE = 0;
    private static final int DECREASE = 1;

    public CumaMesajlariFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_cuma_mesajlari, container, false);
            ButterKnife.bind(this, mView);
            setToolbar();
            setShapes();
            init();
            setImages();
            setSeekbar();
            setItems();
        }

        return mView;
    }

    @Override
    public void onStart() {
        getActivity().findViewById(R.id.tabMainLayout).setVisibility(View.GONE);
        super.onStart();
    }

    private void setToolbar() {
        txtToolbarTitle.setText(getString(R.string.cumaMesajlari));
    }

    private void setShapes() {
        llSelection.setBackground(ShapeUtil.getShape(getResources().getColor(R.color.transparentBlack),
                0, GradientDrawable.RECTANGLE, 15, 0));
        seekbarLayout.setBackground(ShapeUtil.getShape(getResources().getColor(R.color.transparentBlack),
                0, GradientDrawable.RECTANGLE, 15, 0));
        finishButton.setBackground(ShapeUtil.getShape(getResources().getColor(R.color.transparentBlack),
                getResources().getColor(R.color.White), GradientDrawable.RECTANGLE, 15, 2));
        imgvMore.setBackground(ShapeUtil.getShape(getResources().getColor(R.color.transparentBlack),
                0, GradientDrawable.RECTANGLE, 15, 0));
    }

    private void openMoreMenu(){
        PopupMenu popupMenu = new PopupMenu(getContext(), imgvMore);
        popupMenu.inflate(R.menu.cuma_mesaj_menu);

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                selectionId = item.getItemId();
                switch (item.getItemId()) {
                    case R.id.arkaplanImage:
                        photoImgv.setVisibility(View.VISIBLE);
                        rlphotoComplete.setBackgroundColor(getResources().getColor(R.color.White));
                        setSelectionVisibility();
                        setSelectionImage(R.drawable.icon_photo);
                        break;
                    case R.id.duzTema:
                        photoImgv.setVisibility(View.GONE);
                        setSelectionVisibility();
                        setSelectionImage(R.drawable.icon_color_palette);
                        break;

                    case R.id.hazirMessage:
                        edittext.setVisibility(View.GONE);
                        messageTv.setVisibility(View.VISIBLE);
                        setSelectionVisibility();
                        setSelectionImage(R.drawable.icon_text);
                        break;

                    case R.id.mesajColor:
                        setSelectionVisibility();
                        setSelectionImage(R.drawable.icon_text_color);
                        break;

                    case R.id.kendinYaz:
                        edittext.setVisibility(View.VISIBLE);
                        messageTv.setVisibility(View.GONE);
                        break;
                }
                return false;
            }
        });
        popupMenu.show();
    }

    private void setSelectionImage(int itemId){
        Glide.with(getContext())
                .load(itemId)
                .apply(RequestOptions.fitCenterTransform())
                .into(iconSelectionImgv);
    }

    private void setSelectionVisibility() {
        if(llSelection.getVisibility() == View.GONE)
            llSelection.setVisibility(View.VISIBLE);
    }

    private void setImages(){
        Glide.with(getContext())
                .load(R.drawable.icon_more_vertical)
                .apply(RequestOptions.fitCenterTransform())
                .into(imgvMore);
        Glide.with(getContext())
                .load(R.drawable.icon_back_white)
                .apply(RequestOptions.fitCenterTransform())
                .into(itemBackImgv);
        Glide.with(getContext())
                .load(R.drawable.icon_forward_white)
                .apply(RequestOptions.fitCenterTransform())
                .into(itemForwardImgv);
    }

    private void init() {
        imgLeft.setVisibility(View.VISIBLE);
        imgLeft.setOnClickListener(this);
        imgvMore.setOnClickListener(this);
        finishButton.setOnClickListener(this);
        itemBackImgv.setOnClickListener(this);
        itemForwardImgv.setOnClickListener(this);

        MobileAds.initialize(getContext(), getResources().getString(R.string.ADMOB_APP_ID));
        AdMobUtils.loadBannerAd(adView);
        cumaMesajlariContent = new CumaMesajlariContent();
    }

    private void setItems() {
        Glide.with(getContext())
                .load(cumaMesajlariContent.getFRIDAY_IMAGES()[imageIndex])
                .apply(RequestOptions.fitCenterTransform())
                .into(photoImgv);
        messageTv.setText(cumaMesajlariContent.getTR_FRIDAY_MESSAGES()[messageIndex]);
        rlphotoComplete.setBackgroundColor(getResources().getColor(R.color.White));
    }

    private void setSeekbar(){
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (messageTv != null)
                    messageTv.setTextSize(progress);

                if (edittext != null)
                    edittext.setTextSize(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    @Override
    public void onClick(View view) {

        if (view == imgLeft) {
            getActivity().onBackPressed();
        }

        if(view == finishButton){
            progressBar.setVisibility(View.VISIBLE);
            if(edittext.getVisibility() == View.VISIBLE){
                messageTv.setText(edittext.getText().toString());
                edittext.setVisibility(View.GONE);
                messageTv.setVisibility(View.VISIBLE);
            }
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        progressBar.setVisibility(View.GONE);
                        Bitmap bitmap = BitmapConversion.getScreenShot(rlphotoComplete);
                        mFragmentNavigation.pushFragment(new CumaMesajiPhotoFragment(bitmap));
                    } catch (Exception e) {
                        progressBar.setVisibility(View.GONE);
                        e.printStackTrace();
                    }
                }
            }, 1500);
        }

        if(view == imgvMore){
            imgvMore.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.image_click));
            openMoreMenu();
        }

        if(view == itemBackImgv){
            itemBackImgv.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.image_click));

            if(selectionId == R.id.arkaplanImage){
                setPhotoImage(DECREASE);
            }else if(selectionId == R.id.hazirMessage){
                setPhotoMessage(DECREASE);
            } else if(selectionId == R.id.mesajColor){
                setTextColor(DECREASE);
            }else if(selectionId == R.id.duzTema){
                setThemeColor(DECREASE);
            }
        }

        if(view == itemForwardImgv){
            itemForwardImgv.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.image_click));

            if(selectionId == R.id.arkaplanImage){
                setPhotoImage(INCREASE);
            } else if(selectionId == R.id.hazirMessage){
                setPhotoMessage(INCREASE);
            }else if(selectionId == R.id.mesajColor){
                setTextColor(INCREASE);
            }else if(selectionId == R.id.duzTema){
                setThemeColor(INCREASE);
            }
        }
    }

    private void setPhotoImage(int type) {

        if (type == INCREASE) {
            if (imageIndex == (cumaMesajlariContent.getFRIDAY_IMAGES().length - 1))
                imageIndex = 0;
            else
                imageIndex++;
        } else if (type == DECREASE) {
            if (imageIndex == 0)
                imageIndex = cumaMesajlariContent.getFRIDAY_IMAGES().length - 1;
            else
                imageIndex--;
        }

        Glide.with(getContext())
                .load(cumaMesajlariContent.getFRIDAY_IMAGES()[imageIndex])
                .apply(RequestOptions.fitCenterTransform())
                .into(photoImgv);
    }

    private void setPhotoMessage(int type) {

        if (type == INCREASE) {
            if (messageIndex == (cumaMesajlariContent.getTR_FRIDAY_MESSAGES().length - 1))
                messageIndex = 0;
            else
                messageIndex++;
        } else if (type == DECREASE) {
            if (messageIndex == 0)
                messageIndex = cumaMesajlariContent.getTR_FRIDAY_MESSAGES().length - 1;
            else
                messageIndex--;
        }

        messageTv.setText(cumaMesajlariContent.getTR_FRIDAY_MESSAGES()[messageIndex]);
    }

    private void setTextColor(int type){
        if (type == INCREASE) {
            if (textColorIndex == (cumaMesajlariContent.getFRIDAY_TEXT_COLORS().length - 1))
                textColorIndex = 0;
            else
                textColorIndex++;
        } else if (type == DECREASE) {
            if (textColorIndex == 0)
                textColorIndex = cumaMesajlariContent.getFRIDAY_TEXT_COLORS().length - 1;
            else
                textColorIndex--;
        }

        int colorCode = cumaMesajlariContent.getFRIDAY_TEXT_COLORS()[textColorIndex];

        messageTv.setTextColor(getActivity().getResources().getColor(colorCode));
        edittext.setTextColor(getActivity().getResources().getColor(colorCode));
        fridayMessageTv.setTextColor(getActivity().getResources().getColor(colorCode));
    }

    private void setThemeColor(int type){
        if (type == INCREASE) {
            if (themeColorIndex == (cumaMesajlariContent.getFRIDAY_TEXT_COLORS().length - 1))
                themeColorIndex = 0;
            else
                themeColorIndex++;
        } else if (type == DECREASE) {
            if (themeColorIndex == 0)
                themeColorIndex = cumaMesajlariContent.getFRIDAY_TEXT_COLORS().length - 1;
            else
                themeColorIndex--;
        }

        int colorCode = cumaMesajlariContent.getFRIDAY_TEXT_COLORS()[themeColorIndex];

        //photoImgv.setBackgroundColor(getResources().getColor(colorCode));

        photoImgv.setVisibility(View.GONE);
        rlphotoComplete.setBackgroundColor(getResources().getColor(colorCode));
    }
}
