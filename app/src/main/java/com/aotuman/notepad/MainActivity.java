package com.aotuman.notepad;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.aotuman.notepad.adapter.callback.OnGroupClickListener;
import com.aotuman.notepad.entry.GroupInfo;
import com.aotuman.notepad.fragment.LeftFragment;
import com.aotuman.notepad.fragment.MainFragment;

public class MainActivity extends AppCompatActivity implements OnGroupClickListener{
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private LeftFragment mLeftFragment;
    private MainFragment mMainFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initActionBar();
        initView();
    }

    private void initView() {
        mMainFragment = (MainFragment) this.getFragmentManager().findFragmentById(R.id.main_fragment);
        mLeftFragment = (LeftFragment) getFragmentManager().findFragmentById(R.id.left_frame);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.mipmap.ic_launcher_round,  /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
        ) {
            public void onDrawerClosed(View view) {
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mLeftFragment.setCityOnClickListion(this);
    }

    private void initActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        mActionBar.setTitle("便签");
        mActionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        //处理其他菜单点击事件
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == 1){
            mMainFragment.initData();
            mLeftFragment.initData();
        }
    }

    @Override
    public void onClick(GroupInfo groupInfo) {
        mMainFragment.initData();
        mDrawerLayout.closeDrawers();
    }
}
