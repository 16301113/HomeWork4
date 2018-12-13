package edu.bjtu.example.sportsdashboard;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenu;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
//import android.app.Fragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.SharedElementCallback;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.TransitionInflater;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.Toast;
import android.view.View;
import android.content.Intent;

import com.sports.sportclub.local_db.DbManager;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import edu.bjtu.example.sportsdashboard.UI.fragment.AnnocementFragment;
import edu.bjtu.example.sportsdashboard.UI.fragment.AppointmentFragment;
import edu.bjtu.example.sportsdashboard.UI.fragment.FavoriteFragment;
import edu.bjtu.example.sportsdashboard.UI.fragment.GridFragment;
import edu.bjtu.example.sportsdashboard.UI.fragment.HomeFragment;
import edu.bjtu.example.sportsdashboard.UI.fragment.RecommendFragment;
import edu.bjtu.example.sportsdashboard.UI.fragment.My;
import edu.bjtu.example.sportsdashboard.UI.fragment.CoachsFragment;
import edu.bjtu.example.sportsdashboard.UI.fragment.SchedulFragment;

import cn.bmob.v3.BmobUser;
//import edu.bjtu.example.sportsdashboard.UI.fragment.VideoPlayFragment;

public class home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private boolean isExist = false;   //判断是否退出
    private String[] titles = {"Home", "Map", "Appointment", "Register", "Login"};
    public static int currentPosition;
    private static final String KEY_CURRENT_POSITION = "com.sports.sportclub.gridtopager.key.currentPosition";

    //处理退出信息的Handler
    @SuppressLint("HandlerLeak")
    Handler exit_handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isExist = false;
        }

    };
    private DrawerLayout drawerLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        if (savedInstanceState != null) {
            currentPosition = savedInstanceState.getInt(KEY_CURRENT_POSITION, 0);
            // Return here to prevent adding additional GridFragments when changing orientation.
            return;
        }
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //设置滑动监听，利用DrawLayout以及ActionBarDrawerToggle
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //设置导航监听
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.navigation_home);

        //设置底部导航按钮
        BottomNavigationView navigation = findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        disableShiftMode(navigation);

        //设置初始片段为HomeFragment
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frame_content, new HomeFragment());
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        //处理左侧导航抽屉点击事件
        int id = item.getItemId();
        int position = -1;
        Fragment fragment = null;
        if (id == R.id.nav_home) {
            // Handle the home action
            fragment = new HomeFragment();
            position = 0;
        } else if (id == R.id.nav_register) {
            DbManager.getDb_M(getApplicationContext()).update("USER",
                    new String[]{"username","password"},new String[]{"admin","admin"},
                    new String[]{"id"},new String[]{"1"});
            Intent intent = new Intent(home.this, register.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.nav_login) {
            DbManager.getDb_M(getApplicationContext()).update("USER",
                    new String[]{"username","password"},new String[]{"admin","admin"},
                    new String[]{"id"},new String[]{"1"});
            Intent intent = new Intent(home.this, MainActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.nav_favorite) {
            Intent intent = new Intent(home.this,MapActivity.class);
            startActivity(intent);
            return true;
        }
        else if (id == R.id.nav_appointment) {
            fragment = new AppointmentFragment();
//            fragment = new VideoPlayFragment();
            position = 4;
        }
        else if (id == R.id.nav_share) {
            Toast.makeText(home.this,"你点击了share",Toast.LENGTH_LONG).show();
//            DrawerLayout drawer = findViewById(R.id.drawer_layout);
//            drawer.closeDrawer(GravityCompat.START);
            return true;
//            fragment = new GridFragment();
//            position = 5;
        }
//        else if (id == R.id.nav_send) {
//            Toast.makeText(home.this,"你点击了send",Toast.LENGTH_LONG).show();
//            DrawerLayout drawer = findViewById(R.id.drawer_layout);
//            drawer.closeDrawer(GravityCompat.START);
//            return true;
//        }

        changeFragment(R.id.frame_content, fragment);
        setActionBarTitle(position, 1);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //设置底端导航按钮响应事件
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    //打开侧滑功能
                    changeFragment(R.id.frame_content, new HomeFragment());
                    ((DrawerLayout) findViewById(R.id.drawer_layout))
                            .setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                    setActionBarTitle(-1, 1);
                    NavigationView navigationView = findViewById(R.id.nav_view);
                    navigationView.setCheckedItem(R.id.navigation_home);
                    return true;
                case R.id.navigation_dashboard:
                    //禁用侧滑功能
                    changeFragment(R.id.frame_content, new RecommendFragment());
                    ((DrawerLayout) findViewById(R.id.drawer_layout))
                            .setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                    setActionBarTitle(-1, 2);
                    return true;
                case R.id.navigation_coaches:
                    //禁用侧滑功能
                    changeFragment(R.id.frame_content, new CoachsFragment());
                    ((DrawerLayout) findViewById(R.id.drawer_layout))
                            .setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                    setActionBarTitle(-1, 3);
                    return true;
                case R.id.navigation_yourself:
                    //禁用侧滑功能
                    changeFragment(R.id.frame_content, new My());
                    ((DrawerLayout) findViewById(R.id.drawer_layout))
                            .setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                    setActionBarTitle(-1, 4);
                    return true;
                case R.id.navigation_schedule:
                    //禁用侧滑功能
                    changeFragment(R.id.frame_content, new SchedulFragment());
                    ((DrawerLayout) findViewById(R.id.drawer_layout))
                            .setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                    setActionBarTitle(-1, 5);
                    return true;
            }
            return false;
        }
    };

    /**
     * 取消底部导航的默认滑动效果
     *
     * @param view
     */
    @SuppressLint("RestrictedApi")
    public static void disableShiftMode(BottomNavigationView view) {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
        try {
            Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
            shiftingMode.setAccessible(true);
            shiftingMode.setBoolean(menuView, false);
            shiftingMode.setAccessible(false);


            for (int i = 0; i < menuView.getChildCount(); i++) {
                BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
                item.setShiftingMode(false);
                item.setChecked(item.getItemData().isChecked());
            }

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 切换片段所用方法
     *
     * @param currentLayout  当前页面布局
     * @param targetFragment 目标片段
     */
    public void changeFragment(int currentLayout, Fragment targetFragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(currentLayout, targetFragment);
        ft.addToBackStack(null);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
    }

    //
    @Override
    public boolean onKeyDown(int keyNode, KeyEvent event) {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.frame_content);
        /*
         *判断当前位置
         *若是处于推荐页，则需要进行页面返回，再处理退出
         * 若是处于其他页面，则在两秒内双击两次返回键退出程序
         */
        if (fragment instanceof RecommendFragment) {
            WebView webView = (WebView) fragment.getView().findViewById(R.id.recommed_web);
            if (webView.canGoBack()) {
                webView.goBack();
            } else if (!isExist) {
                isExist = true;
                Toast.makeText(home.this, "再按一次退出应用", Toast.LENGTH_LONG).show();
                //延迟两秒发送信息给handler
                exit_handler.sendEmptyMessageDelayed(0, 2000);
            } else {
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(0);
            }
        }
        //对其余界面的处理
        else {
            if (!isExist) {
                isExist = true;
                Toast.makeText(home.this, "再按一次退出应用", Toast.LENGTH_LONG).show();
                exit_handler.sendEmptyMessageDelayed(0, 2000);
            } else {
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(0);
            }
        }
        return false;
    }

    private void setActionBarTitle(int position, int page_num) {
        Toolbar toolbar = findViewById(R.id.toolbar);
        if (page_num == 1) {
            if (position == -1) {
                toolbar.setTitle("Home");
                return;
            }
            toolbar.setTitle(titles[position]);
        } else if (page_num == 2) {
            toolbar.setTitle("Recommend");
        } else if (page_num == 3) {
            toolbar.setTitle("Coaches");
        } else if (page_num == 4) {
            toolbar.setTitle("My");
        } else {
            toolbar.setTitle("Schedule");
        }


    }

    public void onClickLogout(View view) {

        BmobUser.logOut();

        DbManager.getDb_M(getApplicationContext()).update("USER",
                new String[]{"username","password"},new String[]{"admin","admin"},
                new String[]{"id"},new String[]{"1"});
        System.out.print("logout");
        Toast.makeText(home.this, "注销成功", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(home.this, MainActivity.class);
        startActivity(intent);
        finish();
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_CURRENT_POSITION, currentPosition);
    }

    public void Favorite(View view) {
        Toast.makeText(home.this, "你点击了收藏", Toast.LENGTH_LONG).show();

    }

    public void Good(View view) {

        Toast.makeText(home.this, "你点击了点赞", Toast.LENGTH_LONG).show();

    }

    public void Back(View view) {
//        long eventTime = SystemClock.uptimeMillis();
//        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.frame_content);
//        fragment.setExitTransition(TransitionInflater.from(fragment.getContext())
//                .inflateTransition(R.transition.grid_exit_transition));
//        setExitSharedElementCallback(
//                new SharedElementCallback() {
//                    @Override
//                    public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
//                        // Locate the ViewHolder for the clicked position.
//                        RecyclerView recyclerView = findViewById(R.id.recycler_view);
//                        RecyclerView.ViewHolder selectedViewHolder = recyclerView
//                                .findViewHolderForAdapterPosition(home.currentPosition);
//                        if (selectedViewHolder == null || selectedViewHolder.itemView == null) {
//                            return;
//                        }
//
//                        // Map the first shared element name to the child ImageView.
//                        sharedElements
//                                .put(names.get(0), selectedViewHolder.itemView.findViewById(R.id.card_image));
//                    }
//                });
//
//        KeyEvent event = new KeyEvent(eventTime, eventTime,
//                0, 4,0,0,10,158,8,257);
////        event.isCtrlPressed()
//        super.onKeyDown(4,event);


        changeFragment(R.id.frame_content, new My());
        ((DrawerLayout) findViewById(R.id.drawer_layout))
                .setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        setActionBarTitle(-1, 5);
    }

}
