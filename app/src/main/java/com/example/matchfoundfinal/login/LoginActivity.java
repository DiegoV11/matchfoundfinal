package com.example.matchfoundfinal.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.matchfoundfinal.R;
import com.example.matchfoundfinal.cliente.ListaUsuariosActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        String url = "https://media.valorant-api.com/competitivetiers/03621f52-342b-cf4e-4f86-9350a49c6d04/27/largeicon.png";
        ImageView imageView = findViewById(R.id.imageViewRadiant);
        Glide.with(LoginActivity.this).load(url).fitCenter().override(400, 400).into(imageView);

        if(firebaseAuth.getCurrentUser() != null){
            databaseReference.child("users").child(firebaseAuth.getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                @Override
                public void onSuccess(DataSnapshot dataSnapshot) {
                    Intent intent = null;
                    if(dataSnapshot.child("rol").getValue(String.class).equals("ROL_TI")){
                        //intent = new Intent(LoginActivity.this, ListaDispositivosActivity.class);
                        Toast.makeText(LoginActivity.this,"TI",Toast.LENGTH_SHORT).show();
                    }
                    if(dataSnapshot.child("rol").getValue(String.class).equals("ROL_USER")){
                        intent = new Intent(LoginActivity.this, ListaUsuariosActivity.class);
                        Toast.makeText(LoginActivity.this,"USUARIO",Toast.LENGTH_SHORT).show();
                    }
                    if(dataSnapshot.child("rol").getValue(String.class).equals("ROL_ADMIN")){
                        //intent = new Intent(LoginActivity.this,AdminMainActivity.class);
                        Toast.makeText(LoginActivity.this,"ADMIN",Toast.LENGTH_SHORT).show();
                    }
                    if(intent != null){
                        startActivity(intent);
                        finish();
                    }else{
                        Toast.makeText(LoginActivity.this,"ERROR AL REDIRECCIONAR POR USUARIO",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else{
            Button registerButton = findViewById(R.id.btnRegister);
            registerButton.setOnClickListener(view -> {
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            });
            ((Button) findViewById(R.id.btnLogin)).setOnClickListener(view -> {
                EditText email = findViewById(R.id.editEmailLogin);
                EditText contrasena = findViewById(R.id.editPasswordLogin);
                //
                boolean bool=true;
                if(email.getText().toString().isEmpty()){
                    email.setError("Ingrese un email");
                    bool =false;
                }
                if(contrasena.getText().toString().isEmpty()){
                    contrasena.setError("Ingrese una contrasena");
                    bool =false;
                }
                if(bool){
                    firebaseAuth.signInWithEmailAndPassword(email.getText().toString(),contrasena.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                if(firebaseAuth.getCurrentUser().isEmailVerified()){
                                    databaseReference.child("users").child(firebaseAuth.getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                                        @Override
                                        public void onSuccess(DataSnapshot dataSnapshot) {
                                            Intent intent = null;
                                            if(dataSnapshot.child("rol").getValue(String.class).equals("ROL_TI")){
                                                //intent = new Intent(LoginActivity.this, ListaDispositivosActivity.class);
                                                Toast.makeText(LoginActivity.this,"TI",Toast.LENGTH_SHORT).show();
                                            }
                                            if(dataSnapshot.child("rol").getValue(String.class).equals("ROL_USER")){
                                                intent = new Intent(LoginActivity.this, ListaUsuariosActivity.class);
                                                Toast.makeText(LoginActivity.this,"USUARIO",Toast.LENGTH_SHORT).show();

                                            }
                                            if(dataSnapshot.child("rol").getValue(String.class).equals("ROL_ADMIN")){
                                                //intent = new Intent(LoginActivity.this,AdminMainActivity.class);
                                                Toast.makeText(LoginActivity.this,"ADMIN",Toast.LENGTH_SHORT).show();
                                            }
                                            if(intent != null){
                                                startActivity(intent);
                                                finish();
                                            }else{
                                                Toast.makeText(LoginActivity.this,"ERROR AL REDIRECCIONAR POR USUARIO",Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                                }else{
                                    Toast.makeText(LoginActivity.this, "Confirme su correo electronico", Toast.LENGTH_SHORT).show();
                                }
                            }else{
                                Toast.makeText(LoginActivity.this, "Verifique sus credenciales", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            });
        }

    }
}