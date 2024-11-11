package com.wde.eventplanner;

import static com.wde.eventplanner.constants.RegexConstants.EMAIL_REGEX;
import static com.wde.eventplanner.constants.RegexConstants.HAS_DIGIT_REGEX;
import static com.wde.eventplanner.constants.RegexConstants.HAS_SPECIAL_CHAR_REGEX;
import static com.wde.eventplanner.constants.RegexConstants.MIN_PASSWORD_LENGTH;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class LoginScreen extends AppCompatActivity {


    private List<DemoUser> userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        userList = new ArrayList<>();
        userList.add(new DemoUser("email@email.com", "Faks1312!"));
        userList.add(new DemoUser("ftn@ftn.com", "Faks1312!"));
        userList.add(new DemoUser("a@a.com", "Faks1312!"));


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        TextInputEditText emailInput = findViewById(R.id.emailInput);
        TextInputEditText passwordInput = findViewById(R.id.passwordInput);
        MaterialButton loginButton = findViewById(R.id.loginButton);

        loginButton.setOnClickListener(v -> {
            String email = emailInput.getText().toString().trim();
            String password = passwordInput.getText().toString();
            if(email.isEmpty() || password.isEmpty()){
                showSnackbar("Please fill in the fields");
            }
            else if (!isValidEmail(email)) {
                showSnackbar("Invalid email format");
//            } else if (!isStrongPassword(password)) {
//                showSnackbar("Password is too weak. Use 8+ chars, upper & lowercase, number, and special char.")
            }
            else if(!isValidUser(email,password)){
                showSnackbar("Wrong email or password");
            }else{
                Toast.makeText(this, "Successful login", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginScreen.this, HomeScreen.class);
                intent.putExtra("email",email);
                startActivity(intent);
                finish(); //backing from homepage should not return to login page
            }
        });
        MaterialButton registerOrganizerButton = findViewById(R.id.registerOrganizerButton);

        registerOrganizerButton.setOnClickListener(v ->{
            Intent intent = new Intent(LoginScreen.this,RegisterScreen.class);
            startActivity(intent);
            //no finish for back stack
        });

        MaterialButton registerSellerButton = findViewById(R.id.registerSellerButton);

        registerSellerButton.setOnClickListener(v ->{
            Intent intent = new Intent(LoginScreen.this,SellerRegistrationScreen.class);
            startActivity(intent);
            //no finish for back stack
        });

    }

    private boolean isValidEmail(String email) {
        return Pattern.compile(EMAIL_REGEX).matcher(email).matches();
    }

    private boolean isStrongPassword(String password) {
        if (password.length() < MIN_PASSWORD_LENGTH) return false;
        boolean hasUppercase = !password.equals(password.toLowerCase());
        boolean hasLowercase = !password.equals(password.toUpperCase());
        boolean hasDigit = password.matches(HAS_DIGIT_REGEX);
        boolean hasSpecialChar = password.matches(HAS_SPECIAL_CHAR_REGEX);

        return hasUppercase && hasLowercase && hasDigit && hasSpecialChar;
    }

    private void showSnackbar(String message) {
        Snackbar.make(findViewById(R.id.main_layout), message, Snackbar.LENGTH_LONG).show();
    }

    private boolean isValidUser(String email, String password) {
        for (DemoUser user : userList) {
            if (user.getEmail().equals(email) && user.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }
    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}