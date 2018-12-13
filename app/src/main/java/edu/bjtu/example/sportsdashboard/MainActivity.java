package edu.bjtu.example.sportsdashboard;

import android.content.Intent;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
//import android.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sports.sportclub.DataModel.User;
import edu.bjtu.example.sportsdashboard.MainActivity;
import edu.bjtu.example.sportsdashboard.register;
import edu.bjtu.example.sportsdashboard.home;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import com.sports.sportclub.api.BmobService;
import com.sports.sportclub.api.Client;

import static android.support.v4.view.GravityCompat.*;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//轮播
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;
import android.widget.ImageView;
import java.util.ArrayList;
import java.util.List;

import android.widget.LinearLayout;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.sports.sportclub.api.isInternet;
import com.sports.sportclub.local_db.DbManager;

public class MainActivity extends AppCompatActivity{
    private BmobUser current_user;
    private DrawerLayout drawerLayout;
    private SliderLayout sliderShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bmob.initialize(this, "24d2dd9a00667b645f55acaef2d11e16");
        current_user = BmobUser.getCurrentUser();
        int ms = 0;
        int mms = 0;
        while (!isInternet.isNetworkAvalible(getApplicationContext())){
            try {
                Thread.sleep(10);
                ms += 10;
                mms += 10;
                if (ms == 3000){
                    break;
                }
                if (mms <100){
                    Toast.makeText(MainActivity.this, "正在检查网络连接。", Toast.LENGTH_LONG).show();
                }else if (mms <200){
                    Toast.makeText(MainActivity.this, "正在检查网络连接。。", Toast.LENGTH_LONG).show();
                }else if (mms <300){
                    Toast.makeText(MainActivity.this, "正在检查网络连接。。。", Toast.LENGTH_LONG).show();
                }else {
                    mms = 0;
                    Toast.makeText(MainActivity.this, "正在检查网络连接。", Toast.LENGTH_LONG).show();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (ms == 3000){
            jump2main();
        }


        if (current_user != null) {
            if (!DbManager.getDb_M(getApplicationContext()).select(new String[]{"username"},new String[]{"User"},
                    new String[]{"id"},new String[]{"1"}).get(0)[0].equalsIgnoreCase("admin")){
                jump2main();
            }
        } else {
            List<String[]> mes = DbManager.getDb_M(getApplicationContext()).select(new String[]{"username","password"},new String[]{"User"},
                    new String[]{"id"},new String[]{"1"});
            System.out.println(mes.get(0)[0]);
            System.out.println(mes.get(0)[1]);
            if (!mes.get(0)[0].equalsIgnoreCase("admin")){
                jump2main();
            }
            //设置下划线
            TextView forget_text = findViewById(R.id.forget_text);
            forget_text.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
            //设置监听
            forget_text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(MainActivity.this, "该功能未开放", Toast.LENGTH_LONG).show();
                }
            });

            TextView signup_text = findViewById(R.id.register_text);
            signup_text.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
            signup_text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, register.class);
                    startActivity(intent);
                }
            });
        }
    }

    public void Login(View view) {
        EditText username_input = findViewById(R.id.username_input);
        EditText password_input = findViewById(R.id.password_input);

        final String username = username_input.getText().toString();
        String password = password_input.getText().toString();

        //使用retrofit实现登录请求
        BmobService service = Client.retrofit.create(BmobService.class);
        Call<ResponseBody> call = service.getUser(username,password);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if(response.code() == 200){
                    DbManager.getDb_M(getApplicationContext()).update("User",new String[]{"username","password"}
                    ,new String[]{username,password},new String[]{"id"},new String[]{"1"});
                    showmsg("登陆成功");
                    jump2main();
                }
                else if(response.code() == 400) {
                    showmsg("用户名或密码错误");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                showmsg(t.getMessage());
            }
        });
    }

    public boolean Validation(User user){

        return false;
    }
    public void showmsg(String str){
        Toast.makeText(MainActivity.this,str,Toast.LENGTH_LONG).show();
    }

    //跳转至主界面
    public void jump2main(){
        Intent intent = new Intent(this,home.class);
        startActivity(intent);
        finish();
    }


}
