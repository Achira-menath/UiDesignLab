package com.example.uidesignlab;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import android.content.Intent;
import android.net.Uri;
import android.widget.Button;
import androidx.core.view.WindowCompat;
import android.content.SharedPreferences;




public class DashboardActivity extends AppCompatActivity
        implements OnMessageSendListener {
    private static final String STORAGE_TAG = "StorageLab";
    private static final String TAG = "LifecycleTracker";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), true);
        setContentView(R.layout.activity_dashboard);
        Log.d(TAG, "Dashboard onCreate");

        if (savedInstanceState == null) {

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(
                            R.id.dynamic_fragment_container,
                            new DetailFragment()
                    )
                    .commit();

        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(R.string.dashboard_title);

        if (savedInstanceState == null) {

            SharedPreferences prefs = getPreferences(MODE_PRIVATE);

            int visits = prefs.getInt("visit_count", 0) + 1;

            SharedPreferences.Editor editor = prefs.edit();

            editor.putInt("visit_count", visits);

            boolean saved = editor.commit();

            Log.d(STORAGE_TAG,
                    "Visit count saved: " + saved + " (" + visits + ")");

            Toast.makeText(
                    this,
                    getString(R.string.visit_count, visits),
                    Toast.LENGTH_SHORT
            ).show();

        }

        String userName = getIntent().getStringExtra("USER_NAME");
        int userAge = getIntent().getIntExtra("USER_AGE", 0);
        TextView txtUserName = findViewById(R.id.txtUserName);
        txtUserName.setText(getString(R.string.hello_user, userName));

        Toast.makeText(
                this,
                "Your age is " + userAge,
                Toast.LENGTH_SHORT
        ).show();

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<String> items = new ArrayList<>();
        items.add("Activity");
        items.add("Service");
        items.add("Broadcast Receiver");
        items.add("Content Provider");
        items.add("Intent");
        items.add("Fragment");
        items.add("View");
        items.add("Layout");
        items.add("Manifest");
        items.add("Adapter");
        items.add("RecyclerView");
        items.add("Menu");
        items.add("santha");
        items.add("perera");
        items.add("sumal");
        MyAdapter myAdapter = new MyAdapter(items, this);
        recyclerView.setAdapter(myAdapter);


        Button btnWebsite = findViewById(R.id.btnWebsite);

        btnWebsite.setOnClickListener(v -> {

            Intent dialIntent = new Intent(
                    Intent.ACTION_DIAL,
                    Uri.parse("tel:0771234567")
            );

            startActivity(dialIntent);

        });


    }
    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "Dashboard onResume");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "Dashboard onDestroy");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dashboard_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_notes) {

            startActivity(
                    new Intent(this, NotesActivity.class)
            );

            return true;

        }

        if (id == R.id.action_settings) {
            Toast.makeText(this, "Settings clicked", Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.action_logout) {
            new AlertDialog.Builder(this)
                    .setTitle("Logout")
                    .setMessage("Are you sure you want to log out?")

                    .setPositiveButton("Yes", (dialog, which) -> {

                        // Clear remembered username
                        getSharedPreferences(
                                MainActivity.PREFS_NAME,
                                MODE_PRIVATE
                        )
                                .edit()
                                .clear()
                                .apply();

                        // Reset Dashboard visit counter
                        getPreferences(MODE_PRIVATE)
                                .edit()
                                .clear()
                                .apply();

                        finish();

                    })

                    .setNegativeButton("No", null)
                    .show();
            return true;

        } else if (id == R.id.action_about) {

            new AlertDialog.Builder(this)
                    .setTitle("About")
                    .setMessage("This is my Lab 03 Android Application.")
                    .setPositiveButton("OK", null)
                    .show();

            return true;
        }

        if (item.getItemId() == R.id.action_users) {

            Intent intent = new Intent(
                    DashboardActivity.this,
                    UsersActivity.class
            );

            startActivity(intent);

            return true;
        }


        if (item.getItemId() == R.id.action_cloud) {

            Intent intent = new Intent(
                    DashboardActivity.this,
                    CloudActivity.class
            );

            startActivity(intent);

            return true;
        }


        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onMessageSend(String message) {

        DetailFragment detailFragment = (DetailFragment)
                getSupportFragmentManager()
                        .findFragmentById(R.id.dynamic_fragment_container);

        if (detailFragment != null) {
            detailFragment.updateMessage(message);
        }

    }

    @Override
    public void onListItemSelected(String text) {

        DetailFragment detailFragment = (DetailFragment)
                getSupportFragmentManager()
                        .findFragmentById(
                                R.id.dynamic_fragment_container
                        );

        if (detailFragment != null) {
            detailFragment.updateMessage(text);
        }

    }

    @Override
    public void onClearMessage() {

        DetailFragment detailFragment = (DetailFragment)
                getSupportFragmentManager()
                        .findFragmentById(
                                R.id.dynamic_fragment_container
                        );

        if (detailFragment != null) {
            detailFragment.updateMessage(
                    "Detail area: no message yet"
            );
        }

    }



}
