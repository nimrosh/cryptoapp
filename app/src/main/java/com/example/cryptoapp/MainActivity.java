package com.example.cryptoapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;


public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseDatabase db;
    private DatabaseReference dr;
    private GoogleSignInOptions gso;
    private GoogleSignInClient mGoogleSignInClient;
    Button signin;
    Button register;
    Button google;
    EditText em;
    EditText ps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        signin = (Button) findViewById(R.id.signin);
        register = (Button) findViewById(R.id.register);
        google = (Button) findViewById(R.id.google);
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("969040408662-bki5fri49vkouqfrudbc4jm7can6036a.apps.googleusercontent.com")
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        em = (EditText) findViewById(R.id.email);
        ps = (EditText) findViewById(R.id.password);
    }

    @Override
    public void onStart() {
        super.onStart();

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Object email = em.getText();
                Object password = ps.getText();
                if (email != null && password != null) {
                    signIn(email.toString(), password.toString());
                }
                else {
                    Toast.makeText(MainActivity.this, "Field is missing", Toast.LENGTH_SHORT).show();
                }
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Object email = em.getText();
                Object password = ps.getText();
                if (email != null && password != null) {
                    createAccount(email.toString(), password.toString());
                }
                else {
                    Toast.makeText(MainActivity.this, "Field is missing", Toast.LENGTH_SHORT).show();
                }
            }
        });

        google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                googleSignIn();
            }
        });

        db = FirebaseDatabase.getInstance();
        dr = db.getReference("users");
    }

    private void createAccount(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            System.out.println("createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            String uID = user.getUid();
                            dr.child(uID).child("email").setValue(user.getEmail());
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            System.out.println("createUserWithEmail:failure" + task.getException());
                            updateUI(null);
                        }
                    }
                });
    }

    private void signIn(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            System.out.println("signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            System.out.println("signInWithEmail:failure" + task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }

    private void googleSignIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                System.out.println("firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                System.out.println("Google sign in failed" + e);
                Toast.makeText(this,"Google Sign-in failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            System.out.println("signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            System.out.println("signInWithCredential:failure" + task.getException());
                            updateUI(null);
                        }
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            Toast.makeText(MainActivity.this, user.getEmail() + " signed in", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, CryptoDisplay.class);
            startActivity(intent);
        }
        else {
            Toast.makeText(MainActivity.this, "Create/Signin failed", Toast.LENGTH_SHORT).show();
        }
    }
}