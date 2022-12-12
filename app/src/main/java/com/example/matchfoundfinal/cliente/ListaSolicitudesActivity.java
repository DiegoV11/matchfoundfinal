package com.example.matchfoundfinal.cliente;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.matchfoundfinal.R;
import com.example.matchfoundfinal.adapters.ListaSolicitudesAdapter;
import com.example.matchfoundfinal.dto.SolicitudDTO;
import com.example.matchfoundfinal.dto.UsuarioDTO;
import com.example.matchfoundfinal.login.LoginActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ListaSolicitudesActivity extends AppCompatActivity  implements SearchView.OnQueryTextListener{

    ListaSolicitudesAdapter adapter = new ListaSolicitudesAdapter();
    ArrayList<SolicitudDTO> listaSolicitudes = new ArrayList<>();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseDatabase firebaseDatabase =  FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference();

    @Override
    protected void onResume() {
        super.onResume();

        FirebaseDatabase.getInstance().getReference().child("users").child(firebaseAuth.getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                UsuarioDTO usuarioDTO =  dataSnapshot.getValue(UsuarioDTO.class);
                databaseReference.child("solicitudes").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                    @Override
                    public void onSuccess(DataSnapshot dataSnapshot) {
                        listaSolicitudes.clear();
                        for(DataSnapshot children : dataSnapshot.getChildren()){
                            if(children.child("receptor").getValue(String.class).equals(usuarioDTO.getUsername()) ||children.child("solicitante").getValue(String.class).equals(usuarioDTO.getUsername()) ){
                                SolicitudDTO solicitudDTO = children.getValue(SolicitudDTO.class);
                                listaSolicitudes.add(solicitudDTO);
                            }
                        }
                        adapter.setList(listaSolicitudes);
                        adapter.notifyDataSetChanged();
                    }
                });
            }

        });


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_solicitudes);

        SearchView searchView = findViewById(R.id.searchViewSolicitudes);

        searchView.setOnQueryTextListener(ListaSolicitudesActivity.this);

        adapter.setContext(ListaSolicitudesActivity.this);
        RecyclerView recyclerView = findViewById(R.id.recyclerViewSolicitudes);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(ListaSolicitudesActivity.this));




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
                Intent intent = new Intent(ListaSolicitudesActivity.this, PerfilActivity.class);
                startActivity(intent);
                finish();
                return true;
            case R.id.btnCerrarSesion:
                firebaseAuth.signOut();
                Intent intent2 =  new Intent(ListaSolicitudesActivity.this, LoginActivity.class);
                startActivity(intent2);
                finish();
                return true;
            case R.id.btnUsuarios:
                Intent intent3 = new Intent(ListaSolicitudesActivity.this,ListaUsuariosActivity.class);
                startActivity(intent3);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }


    @Override
    public boolean onQueryTextChange(String s) {
        adapter.filtrado(s);
        return false;
    }

}