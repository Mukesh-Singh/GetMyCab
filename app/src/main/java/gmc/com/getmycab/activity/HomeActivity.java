package gmc.com.getmycab.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.support.v4.widget.DrawerLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import gmc.com.getmycab.R;
import gmc.com.getmycab.Utils.AppUtil;
import gmc.com.getmycab.adapters.FragmentPagerAdapter;
import gmc.com.getmycab.base.BaseActivity;
import gmc.com.getmycab.fragments.OneWayTravelFragment;
import gmc.com.getmycab.fragments.OutStationTravelFragment;
import gmc.com.getmycab.gps.GpsTracker;

public class HomeActivity extends BaseActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks ,View.OnClickListener{
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private CharSequence mTitle;
    private RelativeLayout tab1Layout,tab2Layout;
    private TextView tab1Selected,tab2Selected;
    private ViewPager pager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);
       initUi();
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));


    }

    private  void  initUi(){
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        tab1Layout=(RelativeLayout)findViewById(R.id.tab1_layout);
        tab2Layout=(RelativeLayout)findViewById(R.id.tab2_layout);
        tab1Selected=(TextView)findViewById(R.id.tab_one_selectedImage);
        tab2Selected=(TextView)findViewById(R.id.tab_two_selectedImage);
        tab1Layout.setOnClickListener(this);
        tab2Layout.setOnClickListener(this);
        pager=(ViewPager)findViewById(R.id.main_view_pager);
        Fragment []fragments={new OutStationTravelFragment(),new OneWayTravelFragment()};
        FragmentPagerAdapter pagerAdapter= new FragmentPagerAdapter(getSupportFragmentManager(),fragments);
        pager.setAdapter(pagerAdapter);


        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        tabOneSelected(true);
                        tabTwoSelected(false);
                        break;
                    case 1:
                        tabTwoSelected(true);
                        tabOneSelected(false);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });



    }

    @Override
    public void onClick(View v) {
        if (v.getId()==tab1Layout.getId()){
            openTab1();
        }
        else if (v.getId()==tab2Layout.getId())
            openTab2();
    }

   private void openTab1(){
        pager.setCurrentItem(0,true);
    }
    private void openTab2(){
     pager.setCurrentItem(1,true);
    }

    private void tabOneSelected(boolean isSelected){
        if (isSelected){
            tab1Selected.setBackgroundColor(getResources().getColor(R.color.primary));
        }
        else
            tab1Selected.setBackgroundColor(getResources().getColor(android.R.color.white));


    }
    private void tabTwoSelected(boolean isSelected){
        if (isSelected){
            tab2Selected.setBackgroundColor(getResources().getColor(R.color.primary));
        }
        else
            tab2Selected.setBackgroundColor(getResources().getColor(android.R.color.white));
    }


    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments

        switch (position){
            case 0:
                break;
            case 1:
                startActivity(new Intent(this,MyProfileActivity.class));
                break;
            case 2:
                Intent intent= new Intent(this,ContactUsActivity.class);
                intent.putExtra("title","About Us");
                intent.putExtra("url",getResources().getString(R.string.aboutus));
                startActivity(intent);
                break;
            case 3:
                startActivity(new Intent(this,BookingHistoryActivity.class));
                break;
            case 4:
                startActivity(new Intent(this,FeedbackActivity.class));
                break;
            case 5:
                startActivity(new Intent(this,SendLocationActivity.class));
                break;
            case 6:
                sendLocationBySms();
                break;
            case 7:
                Intent intent1= new Intent(this,ContactUsActivity.class);
                intent1.putExtra("title","Contact Us");
                intent1.putExtra("url",getResources().getString(R.string.contactus));
                startActivity(intent1);
                break;

        }
    }

    private void sendLocationBySms() {
        GpsTracker gpsTracker= new GpsTracker(this);
        if (gpsTracker.getIsGPSTrackingEnabled()){
            Intent intent= new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("sms:"));
            String message="Hi, My current location is \n";
            if (gpsTracker .getAddressLine(this)!=null || gpsTracker .getAddressLine(this).equalsIgnoreCase("null") ||gpsTracker .getAddressLine(this).length()!=0)
                message=message+gpsTracker.getAddressLine(this)+"\n";
            if (gpsTracker .getLocality(this)!=null || gpsTracker .getLocality(this).equalsIgnoreCase("null") ||gpsTracker .getLocality(this).length()!=0)
                message=message+gpsTracker.getLocality(this)+"\n";
            intent.putExtra("sms_body",message+" from GetMeCab");

            intent.putExtra("address", "+919015154545");
            startActivity(intent);

        }
        else {
            gpsTracker.showSettingsAlert();
        }

    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        if (!mNavigationDrawerFragment.isDrawerOpen()) {
//            // Only show items in the action bar relevant to this screen
//            // if the drawer is not showing. Otherwise, let the drawer
//            // decide what to show in the action bar.
//            restoreActionBar();
//            return true;
//        }
        MenuInflater inflater= getMenuInflater();
        inflater.inflate(R.menu.home,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.call) {
            AppUtil.callToGetMeCab();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
