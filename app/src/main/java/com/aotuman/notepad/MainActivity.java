package com.aotuman.notepad;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;

import com.aotuman.notepad.define.IMainView;
import com.aotuman.notepad.fragment.LeftFragment;
import com.aotuman.notepad.imp.MainPresenter;

public class MainActivity extends Activity implements IMainView {
    private MainPresenter mMainPresenter;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private LeftFragment mLeftFragment;
    public MainActivity() {
        mMainPresenter = new MainPresenter(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
//        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
//        mLeftFragment = (LeftFragment) getSupportFragmentManager().findFragmentById(R.id.left_frame);
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.mipmap.ic_launcher,  /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
        ) {
            public void onDrawerClosed(View view) {
                getActionBar().setTitle("哈哈");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle("嘻嘻");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
// The action bar home/up actionshould open or close the drawer.
// ActionBarDrawerToggle will takecare of this.
        if(mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        //处理其他菜单点击事件
        return super.onOptionsItemSelected(item);
    }
}
