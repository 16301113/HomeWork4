package edu.bjtu.example.sportsdashboard.UI.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.SharedElementCallback;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.sports.sportclub.Adapter.GridAdapter;
import edu.bjtu.example.sportsdashboard.R;
import edu.bjtu.example.sportsdashboard.home;
import com.daimajia.slider.library.Tricks.ViewPagerEx;

import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private ViewPager viewPager;

    private SliderLayout sliderShow;

    private View view;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);

        //图片轮播
        sliderShow = (SliderLayout)view.findViewById(R.id.slider);
        imageSlider();

        return view;
    }

    private void imageSlider(){
        TextSliderView textSliderView1=new TextSliderView(this.getActivity());
        textSliderView1
                .description("slider1")
                .image(R.drawable.background1);

        TextSliderView textSliderView2=new TextSliderView(this.getActivity());
        textSliderView2
                .description("slider2")
                .image(R.drawable.nav_sports);

        TextSliderView textSliderView3=new TextSliderView(this.getActivity());
        textSliderView3
                .description("slider3")
                .image(R.drawable.banner);

        sliderShow.addSlider(textSliderView1);
        sliderShow.addSlider(textSliderView2);
        sliderShow.addSlider(textSliderView3);

        textSliderView1.setOnSliderClickListener(new BaseSliderView.OnSliderClickListener(){
            @Override
            public void onSliderClick(BaseSliderView slider) {

            }
        });

        textSliderView2.setOnSliderClickListener(new BaseSliderView.OnSliderClickListener(){
            @Override
            public void onSliderClick(BaseSliderView slider) {

            }
        });

        textSliderView3.setOnSliderClickListener(new BaseSliderView.OnSliderClickListener(){
            @Override
            public void onSliderClick(BaseSliderView slider) {

            }
        });

        sliderShow.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        sliderShow.setDuration(2000);//停留时间

        //设置AndroidImageslider监听
        sliderShow.addOnPageChangeListener(new ViewPagerEx.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}
