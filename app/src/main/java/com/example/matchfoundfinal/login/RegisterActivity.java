package com.example.matchfoundfinal.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.matchfoundfinal.R;
import com.example.matchfoundfinal.dto.UsuarioDTO;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //Iniciamos las instancias
        firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference();
        firebaseAuth = FirebaseAuth.getInstance();

        //Confirmamos que están los datos llenos
        EditText email = findViewById(R.id.editEmailRegister);
        EditText username = findViewById(R.id.editUsername);
        EditText riotTag = findViewById(R.id.editTag);
        EditText pass = findViewById(R.id.editTextTextPassword);
        EditText pass2 = findViewById(R.id.editTextTextPassword2);




        //Inicializamos un booleano
        ((Button) findViewById(R.id.btnRegistrarse)).setOnClickListener(view -> {
            boolean bool = true;
            if(email.getText().toString().isEmpty()){
                email.setError("Ingrese su correo");
                bool = false;
            }
            if(username.getText().toString().isEmpty()){
                username.setError("Ingrese su codigo DNI");
                bool = false;
            }
            if(riotTag.getText().toString().isEmpty()){
                riotTag.setError("Ingrese su tag");
                bool = false;
            }


            if(pass.getText().toString().isEmpty()){
                pass.setError("Ingrese su contraseña");
                bool = false;
            }
            if(pass2.getText().toString().isEmpty()){
                pass2.setError("Ingrese nuevamente su contraseña");
                bool = false;
            }
            if(!pass.getText().toString().equals(pass2.getText().toString())){
                pass.setError("Las contraseñas no coinciden");
                bool = false;
            }
            if(bool){
                firebaseAuth.createUserWithEmailAndPassword(email.getText().toString(),pass.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            UsuarioDTO usuarioDTO = new UsuarioDTO(username.getText().toString(),email.getText().toString(),riotTag.getText().toString(),"","Duelista","ROL_USER","hierro","https://media.valorant-api.com/competitivetiers/03621f52-342b-cf4e-4f86-9350a49c6d04/3/largeicon.png",0,firebaseAuth.getCurrentUser().getUid());
                            firebaseDatabase.getReference().child("users").child(firebaseAuth.getCurrentUser().getUid()).setValue(usuarioDTO).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(RegisterActivity.this,"Cuenta creada exitosamente, inicie sesión",Toast.LENGTH_SHORT).show();
                                        firebaseAuth.getCurrentUser().sendEmailVerification();
                                        Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                                        firebaseAuth.signOut();
                                        startActivity(intent);
                                        finish();
                                    }else{
                                        Toast.makeText(RegisterActivity.this,"Error al guardar datos de usuario",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }else{
                            Toast.makeText(RegisterActivity.this,"Error al crear Usuario",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }else{
                Toast.makeText(RegisterActivity.this, "Verifique sus datos", Toast.LENGTH_SHORT).show();
            }
        });
    }
}