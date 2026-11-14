
package com.example.uidesignlab;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import androidx.appcompat.app.AlertDialog;

public class CloudActivity extends AppCompatActivity {

    EditText editUserId, editName, editRole;
    EditText editEmail;
    Button btnSaveUser, btnUpdateRole, btnDeleteUser;
    TextView txtCloudData;

    private DatabaseReference databaseReference;
    private ValueEventListener usersListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cloud);

        // Connect XML views
        editUserId = findViewById(R.id.editUserId);
        editName = findViewById(R.id.editName);
        editRole = findViewById(R.id.editRole);

        btnSaveUser = findViewById(R.id.btnSaveUser);
        btnUpdateRole = findViewById(R.id.btnUpdateRole);
        btnDeleteUser = findViewById(R.id.btnDeleteUser);

        txtCloudData = findViewById(R.id.txtCloudData);
        editEmail = findViewById(R.id.editEmail);

        // Get Firebase database reference
        databaseReference = FirebaseDatabase.getInstance()
                .getReference();

        // Create Firebase data listener
        usersListener = new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                StringBuilder data = new StringBuilder();

                if (!snapshot.exists()) {
                    txtCloudData.setText(R.string.no_users);
                    return;
                }

                // Read each user from Firebase
                for (DataSnapshot child : snapshot.getChildren()) {

                    User user = child.getValue(User.class);

                    if (user != null) {

                        String userId = child.getKey();

                        data.append(userId)
                                .append(": ")
                                .append(user.getName())
                                .append(" (")
                                .append(user.getRole())
                                .append(")")
                                .append("\nEmail: ")
                                .append(user.getEmail())
                                .append("\n\n");
                    }
                }

                if (data.length() == 0) {
                    txtCloudData.setText(R.string.no_users);
                } else {
                    txtCloudData.setText(data.toString());
                }

                Log.d("CloudLab", "Data changed in Firebase");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Log.w("CloudLab",
                        "Failed to read users",
                        error.toException());

                Toast.makeText(
                        CloudActivity.this,
                        "Failed to read cloud data",
                        Toast.LENGTH_SHORT
                ).show();
            }
        };

        // Save button
        btnSaveUser.setOnClickListener(v -> saveUser());
        // Update Role button
        btnUpdateRole.setOnClickListener(v -> updateRole());
        // Delete button
        btnDeleteUser.setOnClickListener(v -> deleteUser());
    }

    // Start listening when screen starts
    @Override
    protected void onStart() {
        super.onStart();

        databaseReference.child("users")
                .addValueEventListener(usersListener);
    }

    // Stop listening when screen stops
    @Override
    protected void onStop() {
        super.onStop();

        databaseReference.child("users")
                .removeEventListener(usersListener);
    }

    // Save user to Firebase
    private void saveUser() {

        String name = editName.getText().toString().trim();
        String role = editRole.getText().toString().trim();
        String email = editEmail.getText().toString().trim();

        if (name.isEmpty() || role.isEmpty() || email.isEmpty()) {

            Toast.makeText(this,
                    "Please fill all fields",
                    Toast.LENGTH_SHORT).show();

            return;
        }

        String newId = databaseReference
                .child("users")
                .push()
                .getKey();

        if (newId == null) {

            Toast.makeText(this,
                    "Could not generate User ID",
                    Toast.LENGTH_SHORT).show();

            return;
        }

        User newUser = new User(name, role, email);

        databaseReference.child("users")
                .child(newId)
                .setValue(newUser)
                .addOnSuccessListener(unused -> {

                    Toast.makeText(CloudActivity.this,
                            "User saved. ID: " + newId,
                            Toast.LENGTH_LONG).show();

                    editUserId.setText(newId);

                })
                .addOnFailureListener(e -> {

                    Toast.makeText(CloudActivity.this,
                            "Failed to save user",
                            Toast.LENGTH_SHORT).show();

                    Log.w("CloudLab", "Save failed", e);
                });
    }

    private void updateRole() {

        String userId = editUserId.getText().toString().trim();
        String role = editRole.getText().toString().trim();

        if (userId.isEmpty() || role.isEmpty()) {

            Toast.makeText(this,
                    "Please enter User ID and Role",
                    Toast.LENGTH_SHORT).show();

            return;
        }

        databaseReference.child("users")
                .child(userId)
                .get()
                .addOnSuccessListener(snapshot -> {

                    if (snapshot.exists()) {

                        java.util.Map<String, Object> updateMap =
                                new java.util.HashMap<>();

                        updateMap.put("role", role);

                        databaseReference.child("users")
                                .child(userId)
                                .updateChildren(updateMap)
                                .addOnSuccessListener(unused -> {

                                    Toast.makeText(CloudActivity.this,
                                            "Role updated successfully",
                                            Toast.LENGTH_SHORT).show();

                                })
                                .addOnFailureListener(e -> {

                                    Toast.makeText(CloudActivity.this,
                                            "Failed to update role",
                                            Toast.LENGTH_SHORT).show();

                                    Log.w("CloudLab",
                                            "Update failed", e);
                                });

                    } else {

                        Toast.makeText(CloudActivity.this,
                                "User does not exist",
                                Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {

                    Toast.makeText(CloudActivity.this,
                            "Failed to check user",
                            Toast.LENGTH_SHORT).show();

                    Log.w("CloudLab", "Read failed", e);
                });
    }

    private void deleteUser() {

        String userId = editUserId.getText().toString().trim();

        if (userId.isEmpty()) {

            Toast.makeText(this,
                    "Please enter User ID",
                    Toast.LENGTH_SHORT).show();

            return;
        }

        new AlertDialog.Builder(CloudActivity.this)
                .setTitle("Delete User")
                .setMessage("Are you sure you want to delete this user?")
                .setPositiveButton("Yes", (dialog, which) -> {

                    databaseReference.child("users")
                            .child(userId)
                            .removeValue()
                            .addOnSuccessListener(unused -> {

                                Toast.makeText(CloudActivity.this,
                                        "User deleted successfully",
                                        Toast.LENGTH_SHORT).show();

                            })
                            .addOnFailureListener(e -> {

                                Toast.makeText(CloudActivity.this,
                                        "Failed to delete user",
                                        Toast.LENGTH_SHORT).show();

                                Log.w("CloudLab",
                                        "Delete failed", e);
                            });
                })
                .setNegativeButton("No", null)
                .show();
    }


}
