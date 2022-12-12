package com.example.matchfoundfinal.cliente;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.matchfoundfinal.R;
import com.example.matchfoundfinal.dto.UsuarioDTO;
import com.example.matchfoundfinal.login.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;

public class OtroPerfilActivity extends AppCompatActivity {
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_cliente,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){

        switch (item.getItemId()) {
            case R.id.btnPerfil:
                Intent intent = new Intent(OtroPerfilActivity.this, PerfilActivity.class);
                startActivity(intent);
                finish();
                return true;
            case R.id.btnCerrarSesion:
                firebaseAuth.signOut();
                Intent intent2 =  new Intent(OtroPerfilActivity.this, LoginActivity.class);
                startActivity(intent2);
                finish();
                return true;
            case R.id.btnUsuarios:
                Intent intent3 = new Intent(OtroPerfilActivity.this,ListaUsuariosActivity.class);
                startActivity(intent3);
                finish();
                return true;
            case R.id.btnSolicitudes:
            Intent intent4 = new Intent(OtroPerfilActivity.this,ListaSolicitudesActivity.class);
            startActivity(intent4);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}