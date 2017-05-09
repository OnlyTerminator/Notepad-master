package com.aotuman.notepad;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;

import com.aotuman.notepad.define.IMainView;
import com.aotuman.notepad.entry.GroupInfo;
import com.aotuman.notepad.fragment.LeftFragment;
import com.aotuman.notepad.fragment.MainFragment;
import com.aotuman.notepad.imp.MainPresenter;

import java.util.List;

public class MainActivity extends Activity {
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
//    private LeftFragment mLeftFragment;
    private MainFragment mMainFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        intitActionBar();
        initView();
    }

    private void initView() {
        mMainFragment = (MainFragment) this.getFragmentManager().findFragmentById(R.id.main_fragment);
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
    }

    private void intitActionBar(){
        ActionBar actionBar = getActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle(R.string.actionbar_title);
        actionBar.setIcon(R.mipmap.ic_launcher_round);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mMainFragment.onActivityResult(requestCode,resultCode,data);
    }
}
