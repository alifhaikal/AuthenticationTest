package my.advisoryapps.logintest.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import my.advisoryapps.logintest.R;
import my.advisoryapps.logintest.storage.SharedPrefManager;

public class ListingActivity extends AppCompatActivity {
    private String token = "";
    private int id = -1;
    private Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listing);

        btnLogout = (Button) findViewById(R.id.btnLogout);

        if (SharedPrefManager.getInstance(this).getToken() != null) {
            token = SharedPrefManager.getInstance(this).getToken();
            id = SharedPrefManager.getInstance(this).getId();
        }

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPrefManager.getInstance(ListingActivity.this).clear();
//                Toast.makeText(ListingActivity.this, "Logout successful!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ListingActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
    }
}