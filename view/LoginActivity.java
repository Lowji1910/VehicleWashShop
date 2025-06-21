package com.example.tiemchuixe.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.tiemchuixe.R;
import com.example.tiemchuixe.model.NhanVien;
import com.example.tiemchuixe.controller.NhanVienDAO;
import com.example.tiemchuixe.controller.DatabaseHelper;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextUsername;
    private EditText editTextPassword;
    private Button buttonLogin;
    private NhanVienDAO nhanVienDAO;

    private static final String PREF_NAME = "LoginPrefs";
    private static final String KEY_LOGGED_IN_MANV = "loggedInMaNV";
    private static final String KEY_LOGGED_IN_VAITRO = "loggedInVaiTro";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Log.d("LoginActivity", "Starting LoginActivity...");

        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);

        nhanVienDAO = new NhanVienDAO(this);
        Log.d("LoginActivity", "Created NhanVienDAO instance");

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

        // Luôn hiển thị màn hình đăng nhập, chỉ chuyển sang MainActivity sau khi đăng nhập thành công
        Log.d("LoginActivity", "Always show login screen first");
    }

    private void loginUser() {
        String username = editTextUsername.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        Log.d("LoginActivity", "Login attempt - Username: " + username);

        if (username.isEmpty() || password.isEmpty()) {
            Log.d("LoginActivity", "Login failed - Empty username or password");
            Toast.makeText(this, "Vui lòng nhập tên đăng nhập và mật khẩu", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            Log.d("LoginActivity", "Opening database connection...");
            nhanVienDAO.open();
            
            Log.d("LoginActivity", "Checking login credentials...");
            boolean loginSuccess = nhanVienDAO.checkLogin(username, password);
            Log.d("LoginActivity", "Login check result: " + loginSuccess);

            if (loginSuccess) {
                Log.d("LoginActivity", "Login successful, retrieving user info...");
                NhanVien loggedInUser = nhanVienDAO.getNhanVienByTenDangNhap(username);
                if (loggedInUser != null) {
                    Log.d("LoginActivity", "User info retrieved - MaNV: " + loggedInUser.getMaNV() + ", VaiTro: " + loggedInUser.getVaiTro());
                    
                    // Store login info
                    SharedPreferences sharedPref = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putInt(KEY_LOGGED_IN_MANV, loggedInUser.getMaNV());
                    editor.putString(KEY_LOGGED_IN_VAITRO, loggedInUser.getVaiTro());
                    editor.apply();
                    Log.d("LoginActivity", "Login info saved to SharedPreferences");

                    Toast.makeText(this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                    redirectToMainActivity();
                } else {
                    Log.e("LoginActivity", "Login successful but could not retrieve user info");
                    Toast.makeText(this, "Lỗi hệ thống: Không tìm thấy thông tin người dùng.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Log.d("LoginActivity", "Login failed - Invalid credentials");
                Toast.makeText(this, "Sai tên đăng nhập hoặc mật khẩu", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.e("LoginActivity", "Error during login: " + e.getMessage(), e);
            Toast.makeText(this, "Lỗi hệ thống: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            Log.d("LoginActivity", "Closing database connection");
            nhanVienDAO.close();
        }
    }

    private boolean isLoggedIn() {
        SharedPreferences sharedPref = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        int loggedInMaNV = sharedPref.getInt(KEY_LOGGED_IN_MANV, -1);
        return loggedInMaNV != -1;
    }

    private void redirectToMainActivity() {
        // Replace MainActivity.class with your actual main activity class
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish(); // Close LoginActivity so user can't go back
    }

    // Optional: Add a method to clear login info on logout
    public static void logout(Context context) {
         SharedPreferences sharedPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
         SharedPreferences.Editor editor = sharedPref.edit();
         editor.remove(KEY_LOGGED_IN_MANV);
         editor.remove(KEY_LOGGED_IN_VAITRO);
         editor.apply();

         // Redirect to LoginActivity after logout
         Intent intent = new Intent(context, LoginActivity.class);
         intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
         context.startActivity(intent);
    }

    // Helper method to get logged in user's MaNV
    public static int getLoggedInMaNV(Context context) {
         SharedPreferences sharedPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
         return sharedPref.getInt(KEY_LOGGED_IN_MANV, -1);
    }

     // Helper method to get logged in user's VaiTro
    public static String getLoggedInVaiTro(Context context) {
         SharedPreferences sharedPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
         return sharedPref.getString(KEY_LOGGED_IN_VAITRO, null);
    }
} 