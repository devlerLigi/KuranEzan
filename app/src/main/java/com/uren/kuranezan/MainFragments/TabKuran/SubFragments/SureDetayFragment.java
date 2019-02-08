package com.uren.kuranezan.MainFragments.TabKuran.SubFragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.uren.kuranezan.MainFragments.BaseFragment;
import com.uren.kuranezan.R;
import com.uren.kuranezan.Utils.ClickableImage.ClickableImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;


public class SureDetayFragment extends BaseFragment
        implements View.OnClickListener {

    View mView;
    private int number;

    @BindView(R.id.txtToolbarTitle)
    TextView txtToolbarTitle;
    @BindView(R.id.imgBack)
    ClickableImageView imgBack;

    @BindView(R.id.txtSure)
    TextView txtSure;


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


            setToolbar();
            init();

            checkBundle();
            setSurah();


        }


        return mView;
    }


    private void setSurah() {

        try {
            JSONObject obj = new JSONObject(loadJSONFromAsset());
            JSONObject jsonObject = obj.getJSONObject("data");
            JSONArray jsonArray=  jsonObject.getJSONArray("surahs");

            JSONObject surah = jsonArray.getJSONObject(number);

            JSONArray ayahs = (JSONArray) surah.get("ayahs");
            Log.i("ayet ", ayahs.toString());

            ArrayList<HashMap<String, String>> formList = new ArrayList<HashMap<String, String>>();
            HashMap<String, String> m_li;

            String s = "";

            Toast.makeText(getActivity(), "bitti",
                    Toast.LENGTH_LONG).show();

            for (int i = 0; i < ayahs.length(); i++) {
                JSONObject jo_inside = ayahs.getJSONObject(i);

                String formula_value = jo_inside.getString("text");

                //Add your values in your `ArrayList` as below:
                m_li = new HashMap<String, String>();
                m_li.put("text", formula_value);

                s = s + " " + formula_value;


                formList.add(m_li);
            }


            txtSure.setText(s);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getContext().getResources().openRawResource(R.raw.quran_uthmani);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    private void setToolbar() {
        txtToolbarTitle.setText(getString(R.string.zikirmatik));
    }


    private void init() {
        imgBack.setVisibility(View.VISIBLE);
        imgBack.setOnClickListener(this);
    }

    private void checkBundle() {
        Bundle args = getArguments();
        if (args != null) {
            number = (Integer) args.getInt(ARGS_INSTANCE);
        } else {
            getActivity().onBackPressed();
        }
    }

    @Override
    public void onClick(View view) {

        if (view == imgBack) {
            getActivity().onBackPressed();
        }

    }
}
