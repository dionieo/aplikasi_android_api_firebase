package com.example.belajarapi;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private EditText editName, editEmail;
    private Button btnSubmit, btnRefresh;
    private TableLayout tableLayout;
    FirebaseDatabase database = FirebaseDatabase.getInstance("https://projectapi-fc6ca-default-rtdb.asia-southeast1.firebasedatabase.app/");
    DatabaseReference dbRef = database.getReference("users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Inisialisasi view
        tableLayout = findViewById(R.id.tableLayout);
        editName = findViewById(R.id.editName);
        editEmail = findViewById(R.id.editEmail);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnRefresh = findViewById(R.id.btnRefresh);

        // Load data pertama kali
        loadData();

        // Tombol Submit (Simpan ke Firebase)
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editName.getText().toString().trim();
                String email = editEmail.getText().toString().trim();

                if (!name.isEmpty() && !email.isEmpty()) {
                    // Generate key unik
                    String userId = dbRef.push().getKey();
                    User user = new User(name, email);
                    dbRef.child(userId).setValue(user);

                    Toast.makeText(MainActivity.this, "Data disimpan!", Toast.LENGTH_SHORT).show();

                    // Kosongkan inputan
                    editName.setText("");
                    editEmail.setText("");
                } else {
                    Toast.makeText(MainActivity.this, "Isi nama dan email!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Tombol Refresh data
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadData();
            }
        });
    }

    // Ambil data dari Firebase & tampilkan di TableLayout
    // Ambil data dari Firebase & tampilkan realtime
    private void loadData() {
        dbRef.addValueEventListener(new ValueEventListener() { // GANTI addListenerForSingleValueEvent KE addValueEventListener
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                // Hapus semua baris lama (kecuali header)
                tableLayout.removeViews(1, Math.max(0, tableLayout.getChildCount() - 1));

                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    User user = userSnapshot.getValue(User.class);

                    if (user != null) {
                        TableRow row = new TableRow(MainActivity.this);

                        TextView tvName = new TextView(MainActivity.this);
                        tvName.setText(user.getName());
                        tvName.setPadding(16, 16, 16, 16);
                        row.addView(tvName);

                        TextView tvEmail = new TextView(MainActivity.this);
                        tvEmail.setText(user.getEmail());
                        tvEmail.setPadding(16, 16, 16, 16);
                        row.addView(tvEmail);

                        tableLayout.addView(row);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(MainActivity.this, "Gagal ambil data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("Firebase", "onCancelled: " + error.getMessage());
            }
        });
    }

}
