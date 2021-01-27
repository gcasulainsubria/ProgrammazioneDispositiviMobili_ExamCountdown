package it.uninsubria.examcountdown;
import androidx.annotation.NonNull;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

public class LoginActivity extends AppCompatActivity {

    private EditText emailEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private Button signInButton;
    private FirebaseAuth mAuth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        this.emailEditText = findViewById(R.id.email);
        this.passwordEditText = findViewById(R.id.password);
        this.loginButton = findViewById(R.id.login);
        this.signInButton = findViewById(R.id.signin);
        this.mAuth = FirebaseAuth.getInstance();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Login(emailEditText.getText().toString(),
                        passwordEditText.getText().toString());
                }
            });

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignIn();
                }
            });
        }

    private void SignIn() {
        Intent launchSignIn = new Intent(LoginActivity.this, SignInActivity.class);
        finish();
        startActivity(launchSignIn);
        }

    private boolean CheckConnection() {
        Context context = this;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        return isConnected;
        }

    private void Login(String username, String password) {
        if (!CheckConnection()) {
            Toast.makeText(LoginActivity.this, "Nessuna Connessione al Database", Toast.LENGTH_SHORT).show();
            }
        else {
            if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
                Toast.makeText(this, "Email o Password non compilate", Toast.LENGTH_SHORT).show();
                return;
                }
            else {
                //FirebasAuth signs in user
                this.mAuth.signInWithEmailAndPassword(username, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            //if everything goes right launches Main Activity
                            if (task.isSuccessful()) {
                                finish();
                                startActivity(new Intent(getApplicationContext(), ItemListActivity.class));
                                }
                            //if some credentials are wrong
                            else if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(LoginActivity.this, "Email o Password Errata/e", Toast.LENGTH_SHORT).show();
                                }
                            //if the given email is associated with a disabled or unregistered account
                            else if (task.getException() instanceof FirebaseAuthInvalidUserException) {
                                Toast.makeText(LoginActivity.this, "Utente non Registrato o Disabilitato", Toast.LENGTH_SHORT).show();
                                }
                            else {
                                Toast.makeText(LoginActivity.this, "Login Fallito", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                }
            }
        }
    }