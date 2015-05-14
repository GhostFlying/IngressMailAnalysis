package com.ghostflying.portalwaitinglist;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.ghostflying.portalwaitinglist.fragment.PortalListFragment;
import com.ghostflying.portalwaitinglist.model.PortalDetail;
import com.ghostflying.portalwaitinglist.util.SettingUtil;


public class MainActivity extends AppCompatActivity
        implements PortalListFragment.OnFragmentInteractionListener{
    private static final String LIST_FRAGMENT_TAG = "LIST_FRAGMENT";
    private static final String DETAIL_FRAGMENT_TAG = "DETAIL_FRAGMENT";

    String account;
    PortalDetail clickedPortal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // read all settings from storage.
        SettingUtil.getSettings(this);
        // If account is not set, usually user open this first time
        // turn to AuthIntent.
        if ((account = SettingUtil.getAccount()) == null){
            doAuth();
        }
        setContentView(R.layout.activity_main);
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.content_layout, PortalListFragment.newInstance(), LIST_FRAGMENT_TAG)
                .commit();
    }

    private void doAuth() {
        Intent authIntent = new Intent(this, AuthActivity.class);
        authIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(authIntent);
    }

    @Override
    protected void onResume(){
        super.onResume();
        if (account == null){
            if ((account = SettingUtil.getAccount()) == null){
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        if(getFragmentManager().getBackStackEntryCount() != 0) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void doAuthInActivity() {
        doAuth();
    }

    @Override
    public void portalItemClicked(PortalDetail clickedPortal, View clickedView) {
        this.clickedPortal = clickedPortal;
        Intent detail = new Intent(this, DetailActivity.class);
        detail.putExtra(DetailActivity.ARG_CLICKED_PORTAL, clickedPortal);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            clickedView.setTransitionName("title");
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this, clickedView, "title");
            startActivity(detail, options.toBundle());
            return;
        }
        startActivity(detail);
    }
}
