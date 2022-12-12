package com.example.matchfoundfinal.cliente;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.matchfoundfinal.R;
import com.example.matchfoundfinal.adapters.EditarPerfilActivity;
import com.example.matchfoundfinal.dto.UsuarioDTO;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class PerfilActivity extends AppCompatActivity {
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;

    Uri uri;


    ActivityResultLauncher<Intent> openDocumentLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    uri = result.getData().getData();
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        firebaseDatabase = FirebaseDatabase.getInstance();

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();




        FirebaseDatabase.getInstance().getReference().child("users").child(firebaseAuth.getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                UsuarioDTO usuarioDTO =  dataSnapshot.getValue(UsuarioDTO.class);
                TextView nombre = findViewById(R.id.textViewNombrePerfil);
                nombre.setText(usuarioDTO.getUsername()+"#"+usuarioDTO.getTag());
                TextView descripcion = findViewById(R.id.textViewDescripcionPerfil);
                descripcion.setText(usuarioDTO.getDescripcion());
                TextView rolPreferido = findViewById(R.id.textViewAgentePerfil);
                rolPreferido.setText(usuarioDTO.getAgente());
                ImageView imageView = findViewById(R.id.imageViewPerfil);
                Glide.with(PerfilActivity.this).load(usuarioDTO.getRankImage()).override(600, 200).into(imageView);
            }

        });

        ((Button) findViewById(R.id.buttonSelectFoto)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.setType("image/jpeg");
                openDocumentLauncher.launch(intent);
            }
        });

        ((Button) findViewById(R.id.buttonSubirFoto)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(uri != null){
                    TextView nombre = findViewById(R.id.textViewNombrePerfil2);
                    Log.d("msg","Reference: "+storageReference);
                    storageReference.child("users").child(nombre.getText().toString().trim() + "/photo.jpg").putFile(uri)
                           .addOnSuccessListener(taskSnapshot -> Log.d("msg", "archivo subido exitosamente"))
                           .addOnFailureListener(e -> Log.d("msg", "error", e.getCause()));


                }else{
                    Toast.makeText(PerfilActivity.this, "No hay imagen adjunta", Toast.LENGTH_SHORT).show();
                }

            }
        });

        ((Button) findViewById(R.id.btnEditarPerfil)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PerfilActivity.this, EditarPerfilActivity.class);
                startActivity(intent);
            }
        });


    }
}