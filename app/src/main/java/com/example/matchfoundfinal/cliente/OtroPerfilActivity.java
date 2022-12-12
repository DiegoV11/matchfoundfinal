package com.example.matchfoundfinal.cliente;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.matchfoundfinal.R;
import com.example.matchfoundfinal.dto.UsuarioDTO;

public class OtroPerfilActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otro_perfil);
        Intent intent = getIntent();
        UsuarioDTO usuario = (UsuarioDTO) intent.getSerializableExtra("usuario");
        TextView nombre = findViewById(R.id.textViewNombrePerfil2);
        TextView descripcion = findViewById(R.id.textViewDescripcionPerfil2);
        TextView rol = findViewById(R.id.textViewAgentePerfil2);
        ImageView imageView = findViewById(R.id.imageViewPerfil2);

        nombre.setText(usuario.getUsername()+"#"+usuario.getTag());
        descripcion.setText(usuario.getDescripcion());
        rol.setText(usuario.getAgente());
        Glide.with(OtroPerfilActivity.this).load(usuario.getRankImage()).override(600, 200).into(imageView);






    }
}