package com.jaktongdan.android.sseuaengnim;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.jaktongdan.android.sseuaengnim.ui.community.CommunityFragment;
import com.jaktongdan.android.sseuaengnim.ui.myPage.MyPageFragment;
import com.jaktongdan.android.sseuaengnim.ui.planner.PlannerFragment;
import com.jaktongdan.android.sseuaengnim.ui.timer.TimerFragment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.Timer;

public class MainActivity extends AppCompatActivity {

    private long backKeyPressedTime = 0;

//    private BottomNavigationView bottomNavigationView;
//    private FragmentManager fragmentManager;
//    private FragmentTransaction fragmentTransaction;
//
//    private PlannerFragment plannerFragment;
//    private TimerFragment timerFragment;
//    private CommunityFragment communityFragment;
//    private MyPageFragment myPageFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        bottomNavigationView = findViewById(R.id.nav_view);
//
//        plannerFragment = new PlannerFragment();
//        timerFragment = new TimerFragment();
//        communityFragment = new CommunityFragment();
//        myPageFragment = new MyPageFragment();
//
//        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                switch (item.getItemId()) {
//                    case R.id.navigationPlanner:
//                        setFragment(0);
//                        break;
//                    case R.id.navigationTimer:
//                        setFragment(1);
//                        break;
//                    case R.id.navigationCommunity :
//                        setFragment(2);
//                        break;
//                    case R.id.navigationMyPage :
//                        setFragment(3);
//                        break;
//                }
//                return true;
//            }
//        });
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigationPlanner, R.id.navigationTimer, R.id.navigationCommunity, R.id.navigationMyPage)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

    }

//    private void setFragment(int n) {
//        fragmentManager = getSupportFragmentManager();
//        fragmentTransaction = fragmentManager.beginTransaction();
//        switch (n) {
//            case 0 :
//                fragmentTransaction.replace(R.id.frame, plannerFragment);
//                fragmentTransaction.commit();
//                break;
//            case 1 :
//                fragmentTransaction.replace(R.id.frame, timerFragment);
//                fragmentTransaction.commit();
//                break;
//            case 2 :
//                fragmentTransaction.replace(R.id.frame, communityFragment);
//                fragmentTransaction.commit();
//                break;
//            case 3 :
//                fragmentTransaction.replace(R.id.frame, myPageFragment);
//                fragmentTransaction.commit();
//                break;
//        }
//    }

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            Toast.makeText(this, "\'뒤로\' 버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            super.onBackPressed();
            moveTaskToBack(true);
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        }

    }

}