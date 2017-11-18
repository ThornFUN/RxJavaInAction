package com.safframework.study.operator.activity;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;

import com.safframework.injectview.annotations.InjectView;
import com.safframework.study.operator.R;
import com.safframework.study.operator.app.BaseActivity;
import com.safframework.study.operator.fragment.HomeFragment;
import com.safframework.study.operator.menu.MenuManager;
import com.safframework.study.operator.utils.DoubleClickExitUtils;
import com.safframework.tony.common.utils.Preconditions;


public class MainActivity extends BaseActivity {

    @InjectView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    @InjectView(R.id.navigation_view)
    NavigationView navigationView;

    @InjectView(R.id.toolbar)
    Toolbar toolbar;

    private MenuManager menuManager;
    private Fragment mContent;
    private DoubleClickExitUtils doubleClickExitHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        initData();
    }

    private void initViews() {
        initToolbar();
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                if (Preconditions.isNotBlank(menuItem.getTitle())) {
                    toolbar.setTitle(menuItem.getTitle());
                }
                showMenu(menuItem);
                menuItem.setChecked(true);

                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                return true;
            }
        });
    }

    private void initData() {

        doubleClickExitHelper = new DoubleClickExitUtils(this);

        if (mContent == null) {
            menuManager = MenuManager.getInstance(getSupportFragmentManager());
            mContent = new HomeFragment();
        }

        getSupportFragmentManager().beginTransaction().add(R.id.content_frame,mContent, MenuManager.MenuType.HOME.getTitle()).commit();
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }

    private void showMenu(MenuItem menuItem) {

        switch (menuItem.getItemId()) {
            case R.id.drawer_home:
                menuManager.show(MenuManager.MenuType.HOME);
                break;

            case R.id.drawer_create:
                menuManager.show(MenuManager.MenuType.CREATE);
                break;

            case R.id.drawer_combining:
                menuManager.show(MenuManager.MenuType.COMBINING);
                break;

            default:
                break;
        }
    }

    //重写物理按键的返回逻辑(实现返回键跳转到上一页)
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //用户触摸返回键
        if(keyCode == KeyEvent.KEYCODE_BACK){
            doubleClickExitHelper.onKeyDown(keyCode, event);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}