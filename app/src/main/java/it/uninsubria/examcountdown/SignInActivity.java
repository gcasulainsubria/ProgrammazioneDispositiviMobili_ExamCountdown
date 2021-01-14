package it.uninsubria.examcountdown;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class SignInActivity extends AppCompatActivity {

    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText passwordConfirmEditText;
    private Button signInButton;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        this.emailEditText = findViewById(R.id.email);
        this.passwordEditText = findViewById(R.id.password);
        this.passwordConfirmEditText = findViewById(R.id.passwordconfirm);
        this.signInButton = findViewById(R.id.signin);
        this.mAuth = FirebaseAuth.getInstance();

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignIn(emailEditText.getText().toString(),
                        passwordEditText.getText().toString(),
                        passwordConfirmEditText.getText().toString());
            }

        });
    }

    private boolean CheckConnection() {
        Context context = this;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }

    private void SignIn(String username,String psw1, String psw2) {
        //returns if there currently isn't connection
        if (!CheckConnection()) {
            Toast.makeText(this, "Nessuna connessione", Toast.LENGTH_SHORT).show();
            return;
        }

        //checks if user has compiled every necessary field
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(psw1) || TextUtils.isEmpty(psw2)) {
            Toast.makeText(this, "Email e password devono essere compilati", Toast.LENGTH_SHORT).show();
            return;
        }
        //checks if user's confirmed the password
        if (!TextUtils.equals(psw1, psw2)) {
            Toast.makeText(this, "Le password non corrispondono", Toast.LENGTH_SHORT).show();
            return;
        }
        //invokes FirebaseAuth to create new profile
        mAuth.createUserWithEmailAndPassword(username, psw1).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                try {
                    //if everything goes right launches the Main Activity
                    if (task.isSuccessful()) {
                        Toast.makeText(SignInActivity.this, "Registrazione avvenuta con successo", Toast.LENGTH_SHORT).show();
                        finish();
                        Intent launchMain = new Intent(SignInActivity.this, MainActivity.class);
                        startActivity(launchMain);

                    }
                    //user has already an account with given email
                    else if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                        Toast.makeText(SignInActivity.this, "Utente già registrato ", Toast.LENGTH_SHORT).show();
                    }
                    //weak password
                    else if (task.getException() instanceof FirebaseAuthWeakPasswordException){
                        Toast.makeText(SignInActivity.this, "password minimo 6 caratteri", Toast.LENGTH_SHORT).show();
                    }

                    //registration goes wrong
                    else {
                        Toast.makeText(SignInActivity.this, "registrazione fallita", Toast.LENGTH_SHORT).show();

                    }


                } catch (Exception e) {
                    Toast.makeText(SignInActivity.this, "errore in registrazione", Toast.LENGTH_SHORT).show();

                }


            }

        });
    }
}