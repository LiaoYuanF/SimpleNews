package com.example.simplenews;

import androidx.appcompat.app.AppCompatActivity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;//弹窗导入包
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*顶部栏*/
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);

        /*侧边栏抽屉*/
        DrawerLayout drawer=findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        //drawer.addDrawerListener();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        /*主页面选择*/
        viewPager = findViewById(R.id.viewPager);
        setupViewPager(viewPager);
        /*当按下底部按钮时，改变显示页面*/
        final BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavView);
        bottomNavigationView.getMenu().getItem(1).setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                //viewPager.setVisibility(View.VISIBLE);
                switch (menuItem.getItemId()) {
                    case R.id.bottomNav_home:
                        viewPager.setCurrentItem(0);
                        return true;
                    case R.id.bottomNav_my:
                        viewPager.setCurrentItem(1);
                        return true;
                    default: break;
                }
                return false;
            }
        });
        /*当滑动页面的时候改变底部按钮的显示*/
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {  //滑动页面后的效果
                bottomNavigationView.getMenu().getItem(position).setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        //按返回键时的作用
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {//左侧边栏是否打开
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.item_favorite) {
            // Handle the subscribe action
            popout("favorite","你正在访问favorite","");
            //TODO: goto favorite
        } else if (id == R.id.item_history) {
            Intent intent = new Intent();
            popout("history","你正在访问history","");

            //TODO: goto history
        } else if (id == R.id.item_setting) {
            Intent intent = new Intent();
            popout("setting","你正在访问favorite","");
            //TODO: goto setting
        } else if (id == R.id.subitem_01) {
            Intent intent = new Intent();
            popout("subitem","你正在访问subitem","");
            //TODO: DO NOTHING
        } else if (id == R.id.subitem_02) {
            popout("subitem","你正在访问subitem","");
            //TODO: DO NOTHING
        } else if (id == R.id.subitem_03) {
            popout("subitem","你正在访问subitem","");
            //TODO: DONOTHING
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void popout(String title,String message,String button){
        new AlertDialog.Builder(
                MainActivity.this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("确定", null)
                .show();
    }

    public void setupViewPager(ViewPager viewPager) {
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        homeFragment homeFragment = new homeFragment();
        myFragment myFragment = new myFragment();
        viewPagerAdapter.addFragment(homeFragment);
        viewPagerAdapter.addFragment(myFragment);
        viewPager.setAdapter(viewPagerAdapter);
        /*默认显示主页面*/
        viewPager.setCurrentItem(1);
    }
}


class ViewPagerAdapter extends FragmentPagerAdapter {
    private final List<Fragment> fragmentList = new ArrayList<Fragment>();
    private Fragment instantFragment;
    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    public void addFragment(Fragment fragment) {
        fragmentList.add(fragment);
    }

    @Override
    public void setPrimaryItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        instantFragment = (Fragment) object;
        super.setPrimaryItem(container, position, object);
    }
    public Fragment getInstantFragment() {
        return instantFragment;
    }
}
