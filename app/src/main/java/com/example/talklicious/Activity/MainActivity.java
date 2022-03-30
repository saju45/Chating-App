package com.example.talklicious.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.talklicious.Adapter.FragmentAdapter;
import com.example.talklicious.R;
import com.example.talklicious.databinding.ActivityMainBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    FragmentManager manager;
    FragmentAdapter adapter;
    ViewPager viewPager;
    TabLayout tabLayout;
    Toolbar toolbar;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


       viewPager=findViewById(R.id.viewpager);
       tabLayout=findViewById(R.id.tablayout);

      toolbar=findViewById(R.id.my_toolbar);
     setSupportActionBar(toolbar);


        auth=FirebaseAuth.getInstance();
        manager= getSupportFragmentManager();
        adapter=new FragmentAdapter(manager);

       viewPager.setAdapter(adapter);
       tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setIcon(R.drawable.ic_baseline_home_24);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_baseline_person_24);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_baseline_chat_24);


        Registerpermission();

    }

    private void Registerpermission() {

        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                ,Manifest.permission.CAMERA)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            // do you work now
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // permission is denied permenantly, navigate user to app settings
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                })
                .onSameThread()
                .check();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu_layout,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.profile:
                Toast.makeText(getApplicationContext(), "profile", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this,SettingActivity2.class));
                break;
            case R.id.logout:
                auth.signOut();
                startActivity(new Intent(MainActivity.this,SignInActivity.class));
                finish();
        }

        return super.onOptionsItemSelected(item);
    }
}