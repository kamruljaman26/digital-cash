package com.xyz.digital_cash.dcash.banner;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.PagerAdapter;
import androidx.appcompat.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.xyz.digital_cash.dcash.R;

import java.util.ArrayList;

public class BannerSliderAdapter extends PagerAdapter {

    private String TAG = BannerSliderAdapter.class.getSimpleName();
    FragmentManager fragmentManager;
    //private ArrayList<BannerSlider> bannerSliderArrayList = new ArrayList<BannerSlider>();
    private ArrayList<String> bannerSliderImages = new ArrayList<String>();
    private LayoutInflater inflater;
    private Context context;

    private String DEFAULT_NEWS_FEED = "1";
    private String POP_UP_DIALOG = "2";
    private String MUV_POINT = "3";
    private String MUV_POINT_OFFER_DETAILS = "4";
    private String URL = "5";
    private String ADD_COUPON = "6";
    private String RIDE_SERVICE = "7";
    private String MUV_SEND = "8";
    private String MUV_STORE = "9";
    private AlertDialog dialog;

    public BannerSliderAdapter(Context context, ArrayList<String> bannerSliderImages) {
        this.context = context;
        this.bannerSliderImages = bannerSliderImages;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return bannerSliderImages.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View myBannerLayout = inflater.inflate(R.layout.layout_image_slider, view, false);

        fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();

        //final BannerSlider bannerSlider = bannerSliderArrayList.get(position);
        Log.d(TAG, " imageLink:: "+position + bannerSliderImages.get(position));


        ImageView myImage = (ImageView) myBannerLayout.findViewById(R.id.imgViewHomePageSlider);

        Glide.with(context).load(bannerSliderImages.get(position)).into(myImage);

       /* try {

            if(position == 0){
                Picasso.with(context).load(R.drawable.test1).into(myImage);
            }else if(position == 1){
                Picasso.with(context).load(R.drawable.test2).into(myImage);
            }else if(position == 2){
                Picasso.with(context).load(R.drawable.test3).into(myImage);
            }else if(position == 3){
                Picasso.with(context).load(R.drawable.test4).into(myImage);
            }else if(position == 4){
                Picasso.with(context).load(R.drawable.test5).into(myImage);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }*/

        view.addView(myBannerLayout, 0);
        return myBannerLayout;
    }


    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }
}