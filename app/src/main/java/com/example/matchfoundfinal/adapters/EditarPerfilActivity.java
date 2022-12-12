package com.example.matchfoundfinal.adapters;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.matchfoundfinal.R;
import com.example.matchfoundfinal.cliente.ListaUsuariosActivity;
import com.example.matchfoundfinal.cliente.PerfilActivity;
import com.example.matchfoundfinal.dto.UsuarioDTO;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class EditarPerfilActivity extends AppCompatActivity {
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    DatabaseReference databaseReference = firebaseDatabase.getReference();
    Uri uri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil);

        firebaseDatabase = FirebaseDatabase.getInstance();

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        String[] lista = {"Controlador","Duelista","Centinela","Iniciador"};
        ArrayAdapter<String> strAdapter = new ArrayAdapter<String>(this, R.layout.color_spinner_layout,lista);
        strAdapter.setDropDownViewResource(R.layout.dropdown_color_spinner_layout);
        Spinner spinner = findViewById(R.id.spinner);
        spinner.setAdapter(strAdapter);
        FirebaseDatabase.getInstance().getReference().child("users").child(firebaseAuth.getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                UsuarioDTO usuarioDTO =  dataSnapshot.getValue(UsuarioDTO.class);
                TextView nombre = findViewById(R.id.textViewNombrePerfilEditar);
                nombre.setText(usuarioDTO.getUsername()+"#"+usuarioDTO.getTag());
                TextView descripcion = findViewById(R.id.textViewDescripcionPerfilEditar);
                descripcion.setText(usuarioDTO.getDescripcion());
                ImageView imageView = findViewById(R.id.imageViewPerfilEditar);
                Glide.with(EditarPerfilActivity.this).load(usuarioDTO.getRankImage()).override(600, 200).into(imageView);
                Button btn = findViewById(R.id.btnSubmitEditarPerfil);

                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        usuarioDTO.setAgente(spinner.getSelectedItem().toString());
                        databaseReference.child("users").child(firebaseAuth.getCurrentUser().getUid()).setValue(usuarioDTO)
                                .addOnSuccessListener(aVoid->{
                                    Toast.makeText(EditarPerfilActivity.this,"Rol de agente actualizado",Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(EditarPerfilActivity.this, ListaUsuariosActivity.class);
                                    startActivity(intent);
                                    finish();
                                })
                                .addOnFailureListener(e->{
                                    Log.d("msg",e.getMessage());
                                });
                        Log.d("msg",spinner.getSelectedItem().toString());
                    }
                });
            }

        });



    }
}