package com.example.matchfoundfinal.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.SearchView;

import com.example.matchfoundfinal.R;
import com.example.matchfoundfinal.adapters.ListaReportadosAdapter;
import com.example.matchfoundfinal.adapters.ListaUsuariosAdapter;
import com.example.matchfoundfinal.cliente.ListaUsuariosActivity;
import com.example.matchfoundfinal.dto.UsuarioDTO;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class AdminListaUsuariosActivity extends AppCompatActivity implements SearchView.OnQueryTextListener{

    ListaReportadosAdapter adapter = new ListaReportadosAdapter();
    ArrayList<UsuarioDTO> listaUsuarios = new ArrayList<>();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseDatabase firebaseDatabase =  FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference();

    @Override
    protected void onResume() {
        super.onResume();
        databaseReference.child("users").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {

            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                listaUsuarios.clear();
                for(DataSnapshot children : dataSnapshot.getChildren()){
                    if(children.child("rol").getValue(String.class).equals("ROL_USER")){
                        UsuarioDTO usuarioDTO = children.getValue(UsuarioDTO.class);
                        listaUsuarios.add(usuarioDTO);
                    }
                }
                adapter.setList(listaUsuarios);
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_lista_usuarios);
        SearchView searchView = findViewById(R.id.searchViewUsersAdmin);

        searchView.setOnQueryTextListener(AdminListaUsuariosActivity.this);

        adapter.setContext(AdminListaUsuariosActivity.this);
        RecyclerView recyclerView = findViewById(R.id.recyclerViewAdmin);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(AdminListaUsuariosActivity.this));


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