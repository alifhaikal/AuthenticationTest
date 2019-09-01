package my.advisoryapps.logintest.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import my.advisoryapps.logintest.R;
import my.advisoryapps.logintest.api.RetrofitClient;
import my.advisoryapps.logintest.model.LoginResponse;
import my.advisoryapps.logintest.model.Status;
import my.advisoryapps.logintest.storage.SharedPrefManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            Log.d("TAG", "Redirecting...");
            Intent intent = new Intent(this, ListingActivity.class);
            startActivity(intent);
        }

        Button btnLogin = (Button) findViewById(R.id.btnLogin);
        final EditText etEmail = (EditText) findViewById(R.id.etEmail);
        final EditText etPassword = (EditText) findViewById(R.id.etPassword);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Call<LoginResponse> call = RetrofitClient.getInstance().getApi().login(etEmail.getText().toString(), etPassword.getText().toString());

                call.enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                        try {
                            LoginResponse loginResponse = response.body();
                            Log.d("TAG", "Email: " + etEmail.getText().toString() + "\nPassword: " + etPassword.getText().toString()
                                    + "\ntoken: " + loginResponse.getToken() + "\nmessage: " + loginResponse.getStatus().getMessage());

                            Status status = loginResponse.getStatus();
                            int code = status.getCode();

                            if (code == 200) {
                                SharedPrefManager.getInstance(MainActivity.this)
                                        .saveUser(loginResponse.getId(), loginResponse.getToken());
                                etEmail.setText("");
                                etPassword.setText("");

                                Intent intent = new Intent(MainActivity.this, ListingActivity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(MainActivity.this, "" + status.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {
                        Log.d("TAG", "Error");
                        t.printStackTrace();
                    }
                });
            }
        });
    }
}