package no.hiof.android2018.gruppe11.shrooms;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

public class bottomNavTest extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_nav_test);

        BottomNavigationView bottomNav = findViewById(R.id.botNav);
        bottomNav.setOnNavigationItemSelectedListener(listener);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FeedFragment()).commit();

    }

    private BottomNavigationView.OnNavigationItemSelectedListener listener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    Fragment selectedFragment = null;

                    switch(menuItem.getItemId()){
                        case R.id.nav_feed:
                            selectedFragment = new FeedFragment();
                            break;
                        case R.id.nav_new_post:
                            selectedFragment = new NewPostFragment();
                            break;
                        case R.id.nav_my_posts:
                            selectedFragment = new MyPostFragment();
                            break;
                        case R.id.nav_map:
                            Toast.makeText(bottomNavTest.this, "Opening maps...", Toast.LENGTH_LONG).show();
                            break;
                        case R.id.nav_my_profile:
                            selectedFragment = new MyProfileFragment();
                            break;

                    }
                    if(selectedFragment != null) {
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                    }
                    return true;

                }
            };

}
