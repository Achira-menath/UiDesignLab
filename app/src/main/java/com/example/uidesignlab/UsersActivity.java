package com.example.uidesignlab;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class UsersActivity extends AppCompatActivity {

    private RecyclerView recyclerViewUsers;
    private UserAdapter adapter;

    private Button btnAddJohn;
    private Button btnClearAll;
    private Button btnReadProvider;

    private static final String TAG = "RoomLab";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        recyclerViewUsers = findViewById(R.id.recyclerViewUsers);

        btnAddJohn = findViewById(R.id.btnAddJohn);
        btnClearAll = findViewById(R.id.btnClearAll);
        btnReadProvider = findViewById(R.id.btnReadProvider);

        // Setup RecyclerView
        recyclerViewUsers.setLayoutManager(
                new LinearLayoutManager(this)
        );

        // Setup Adapter
        adapter = new UserAdapter(user -> {

            AppDatabase.databaseExecutor.execute(() -> {

                AppDatabase.getInstance(getApplicationContext())
                        .userDao()
                        .delete(user);

                runOnUiThread(this::loadUsers);

            });

        });

        recyclerViewUsers.setAdapter(adapter);

        // Add John button
        btnAddJohn.setOnClickListener(v -> {

            AppDatabase.databaseExecutor.execute(() -> {

                long id = AppDatabase.getInstance(
                        getApplicationContext()
                ).userDao().insert(new UserRecord("John"));

                Log.d(TAG, "Added John, id = " + id);

                loadUsersFromDatabase();
            });

        });

        // Clear All button
        btnClearAll.setOnClickListener(v -> {

            AppDatabase.databaseExecutor.execute(() -> {

                AppDatabase.getInstance(
                        getApplicationContext()
                ).userDao().deleteAll();

                Log.d(TAG, "All users deleted");

                loadUsersFromDatabase();
            });

        });

        // Read Provider button
        btnReadProvider.setOnClickListener(v -> {

            AppDatabase.databaseExecutor.execute(() -> {

                int count = 0;

                try (Cursor cursor = getContentResolver().query(
                        UserProvider.CONTENT_URI,
                        null,
                        null,
                        null,
                        null
                )) {

                    if (cursor != null) {

                        int nameIndex =
                                cursor.getColumnIndexOrThrow("name");

                        while (cursor.moveToNext()) {

                            String name =
                                    cursor.getString(nameIndex);

                            Log.d(TAG, "Provider user: " + name);

                            count++;
                        }
                    }

                } catch (Exception e) {

                    Log.e(TAG, "Provider error", e);
                }

                final int finalCount = count;

                runOnUiThread(() -> {

                    Toast.makeText(
                            UsersActivity.this,
                            "Provider returned " + finalCount + " rows",
                            Toast.LENGTH_SHORT
                    ).show();

                });

            });

        });

        // Load users when screen opens
        loadUsers();

    }

    // Load users from Room database
    private void loadUsers() {

        AppDatabase.databaseExecutor.execute(this::loadUsersFromDatabase);

    }

    // This method runs on the database background thread
    private void loadUsersFromDatabase() {

        List<UserRecord> users =
                AppDatabase.getInstance(getApplicationContext())
                        .userDao()
                        .getAllUsers();

        Log.d(TAG, "Users found: " + users.size());

        runOnUiThread(() -> {

            adapter.setUsers(users);

        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        loadUsers();
    }
}
