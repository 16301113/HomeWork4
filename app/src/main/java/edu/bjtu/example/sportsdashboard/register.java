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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import edu.bjtu.example.sportsdashboard.MainActivity;
import edu.bjtu.example.sportsdashboard.register;
import com.sports.sportclub.api.BmobService;
import com.sports.sportclub.api.Client;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.support.v4.view.GravityCompat.*;

public class register extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        TextView back = findViewById(R.id.backToLoginText);
        back.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(register.this,MainActivity.class);
                startActivity(intent);
            }
        });


    }

    public void Register(View view) {
        //注册功能的实现
        EditText username_get = findViewById(R.id.username);
        EditText password_get = findViewById(R.id.password);
        EditText repassword_get = findViewById(R.id.repassword);
        String username = username_get.getText().toString();
        String password = password_get.getText().toString();
        if (username.length()<=0){
            showmsg("用户名不能为空!");
            return;
        }
        if (password.length()<=0){
            showmsg("密码不能小于6位数");
            return;
        }
        String repassword = repassword_get.getText().toString();

        if(!password.equals(repassword)){
            showmsg("两次输入密码不一致!");
            return;
        }

        //bmob内部封装的注册方法
//        BmobUser new_user = new BmobUser();
//        new_user.setPassword(password);
//        new_user.setUsername(username);
//        new_user.signUp(new SaveListener<BmobUser>() {
//
//            @Override
//            public void done(BmobUser user, BmobException e) {
//                if(e == null){
//                    showmsg("注册成功");
//                    BmobUser.logOut();
//                    jump2login();
//                }
//                else{
//                    showmsg(e.getMessage().toString());
//                }
//            }
//        });

        //创建注册用户模板
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("username",username);
            jsonObject.put("password",password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //将json转为请求体
        RequestBody body = RequestBody.create(
                MediaType.parse("application/json"),jsonObject.toString());

        //使用retrofit发送请求
        BmobService service = Client.retrofit.create(BmobService.class);
        Call<ResponseBody> call = service.postUser(body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if(response.code() == 201){
                    showmsg("注册成功");
                    jump2login();
                }
                else if(response.code() == 400) {
                    showmsg("该用户名已存在");
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                showmsg(t.getMessage());
            }
        });

    }
    //显示信息
    public void showmsg(String msg){
        Toast.makeText(register.this,msg,Toast.LENGTH_LONG).show();
    }
    //跳转到主界面
    public void jump2login(){
        Intent intent = new Intent(register.this,MainActivity.class);
        startActivity(intent);
        finish();
    }
}
