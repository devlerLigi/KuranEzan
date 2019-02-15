package com.uren.kuranezan.MainFragments.TabDiger.SubFragments;

import android.graphics.Bitmap;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

    @BindView(R.id.photoBackImgv)
    ImageView photoBackImgv;
    @BindView(R.id.photoForwardImgv)
    ImageView photoForwardImgv;
    @BindView(R.id.textBackImgv)
    ImageView textBackImgv;
    @BindView(R.id.textForwardImgv)
    ImageView textForwardImgv;
    @BindView(R.id.colorBackImgv)
    ImageView colorBackImgv;
    @BindView(R.id.colorForwardImgv)
    ImageView colorForwardImgv;

    @BindView(R.id.llPhoto)
    LinearLayout llPhoto;
    @BindView(R.id.llText)
    LinearLayout llText;
    @BindView(R.id.llColor)
    LinearLayout llColor;

    @BindView(R.id.photoImgv)
    ImageView photoImgv;
    @BindView(R.id.messageTv)
    TextView messageTv;
    @BindView(R.id.fridayMessageTv)
    TextView fridayMessageTv;
    @BindView(R.id.colorPaletteImgv)
    ImageView colorPaletteImgv;

    @BindView(R.id.seekbarLayout)
    FrameLayout seekbarLayout;
    @BindView(R.id.seekbar)
    SeekBar seekbar;
    @BindView(R.id.finishButton)
    Button finishButton;
    @BindView(R.id.iconPhotoImgv)
    ImageView iconPhotoImgv;
    @BindView(R.id.iconTextImgv)
    ImageView iconTextImgv;

    CumaMesajlariContent cumaMesajlariContent;

    private int messageIndex = 0;
    private int colorIndex = 0;
    private int imageIndex = 0;

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
        llPhoto.setBackground(ShapeUtil.getShape(getResources().getColor(R.color.transparentBlack),
                0, GradientDrawable.RECTANGLE, 15, 0));
        llText.setBackground(ShapeUtil.getShape(getResources().getColor(R.color.transparentBlack),
                0, GradientDrawable.RECTANGLE, 15, 0));
        llColor.setBackground(ShapeUtil.getShape(getResources().getColor(R.color.transparentBlack),
                0, GradientDrawable.RECTANGLE, 15, 0));
        seekbarLayout.setBackground(ShapeUtil.getShape(getResources().getColor(R.color.transparentBlack),
                0, GradientDrawable.RECTANGLE, 15, 0));
        finishButton.setBackground(ShapeUtil.getShape(getResources().getColor(R.color.transparentBlack),
                getResources().getColor(R.color.White), GradientDrawable.RECTANGLE, 15, 2));
    }

    private void setImages(){
        Glide.with(getContext())
                .load(R.drawable.icon_back_white)
                .apply(RequestOptions.fitCenterTransform())
                .into(photoBackImgv);
        Glide.with(getContext())
                .load(R.drawable.icon_photo)
                .apply(RequestOptions.fitCenterTransform())
                .into(iconPhotoImgv);
        Glide.with(getContext())
                .load(R.drawable.icon_forward_white)
                .apply(RequestOptions.fitCenterTransform())
                .into(photoForwardImgv);

        Glide.with(getContext())
                .load(R.drawable.icon_back_white)
                .apply(RequestOptions.fitCenterTransform())
                .into(textBackImgv);
        Glide.with(getContext())
                .load(R.drawable.icon_text)
                .apply(RequestOptions.fitCenterTransform())
                .into(iconTextImgv);
        Glide.with(getContext())
                .load(R.drawable.icon_forward_white)
                .apply(RequestOptions.fitCenterTransform())
                .into(textForwardImgv);

        Glide.with(getContext())
                .load(R.drawable.icon_back_white)
                .apply(RequestOptions.fitCenterTransform())
                .into(colorBackImgv);
        Glide.with(getContext())
                .load(R.drawable.icon_color_palette)
                .apply(RequestOptions.fitCenterTransform())
                .into(colorPaletteImgv);
        Glide.with(getContext())
                .load(R.drawable.icon_forward_white)
                .apply(RequestOptions.fitCenterTransform())
                .into(colorForwardImgv);
    }

    private void init() {
        imgLeft.setVisibility(View.VISIBLE);
        imgLeft.setOnClickListener(this);

        photoBackImgv.setOnClickListener(this);
        photoForwardImgv.setOnClickListener(this);
        textBackImgv.setOnClickListener(this);
        textForwardImgv.setOnClickListener(this);
        colorBackImgv.setOnClickListener(this);
        colorForwardImgv.setOnClickListener(this);
        finishButton.setOnClickListener(this);

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
    }

    private void setSeekbar(){
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (messageTv != null)
                    messageTv.setTextSize(progress);
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

        if (view == photoBackImgv) {
            photoBackImgv.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.image_click));
            setPhotoImage(DECREASE);
        }

        if (view == photoForwardImgv) {
            photoForwardImgv.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.image_click));
            setPhotoImage(INCREASE);
        }

        if (view == textBackImgv) {
            textBackImgv.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.image_click));
            setPhotoMessage(DECREASE);
        }

        if (view == textForwardImgv) {
            textForwardImgv.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.image_click));
            setPhotoMessage(INCREASE);
        }

        if (view == colorBackImgv) {
            colorBackImgv.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.image_click));
            setTextColor(DECREASE);
        }

        if (view == colorForwardImgv) {
            colorForwardImgv.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.image_click));
            setTextColor(INCREASE);
        }

        if(view == finishButton){
            Bitmap bitmap = BitmapConversion.getScreenShot(rlphotoComplete);
            mFragmentNavigation.pushFragment(new CumaMesajiPhotoFragment(bitmap));
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
            if (colorIndex == (cumaMesajlariContent.getFRIDAY_TEXT_COLORS().length - 1))
                colorIndex = 0;
            else
                colorIndex++;
        } else if (type == DECREASE) {
            if (colorIndex == 0)
                colorIndex = cumaMesajlariContent.getFRIDAY_TEXT_COLORS().length - 1;
            else
                colorIndex--;
        }

        int colorCode = cumaMesajlariContent.getFRIDAY_TEXT_COLORS()[colorIndex];

        colorPaletteImgv.setColorFilter(ContextCompat.getColor(getActivity(), colorCode), android.graphics.PorterDuff.Mode.SRC_IN);
        messageTv.setTextColor(getActivity().getResources().getColor(colorCode));
        fridayMessageTv.setTextColor(getActivity().getResources().getColor(colorCode));
    }
}
