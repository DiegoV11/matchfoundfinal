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
import android.widget.SearchView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.matchfoundfinal.R;
import com.example.matchfoundfinal.adapters.ListaUsuariosAdapter;
import com.example.matchfoundfinal.dto.ApiDTO;
import com.example.matchfoundfinal.dto.DataApiDTO;
import com.example.matchfoundfinal.dto.UsuarioDTO;
import com.example.matchfoundfinal.login.LoginActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.util.ArrayList;

public class ListaUsuariosActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    ListaUsuariosAdapter adapter = new ListaUsuariosAdapter();
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
                    Log.d("msg","Los ids de cada uno son "+children.getKey());
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
        setContentView(R.layout.activity_lista_usuarios);

        SearchView searchView = findViewById(R.id.searchViewUsers);

        searchView.setOnQueryTextListener(ListaUsuariosActivity.this);
        FirebaseDatabase.getInstance().getReference().child("users").child(firebaseAuth.getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                UsuarioDTO userDto =  dataSnapshot.getValue(UsuarioDTO.class);
                RequestQueue queue = Volley.newRequestQueue(ListaUsuariosActivity.this);
                String usernameParsed="";
                if(userDto.getUsername().contains(" ")){
                    String[] list = userDto.getUsername().split(" ");
                    int i = 0;
                    for(String s : list){
                        if(i==0){
                            usernameParsed=s;
                        }else{
                            usernameParsed=usernameParsed+"%20"+s;
                        }
                        i++;
                    }
                }
                String url = "https://api.henrikdev.xyz/valorant/v1/mmr/"+"na/"+usernameParsed+"/"+userDto.getTag();

                StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();
                        ApiDTO d = gson.fromJson(response,ApiDTO.class);
                        DataApiDTO data = d.getData();
                        userDto.setRango(data.getCurrenttierpatched());
                        userDto.setRankImage(data.getImages().getLarge());
                        String desc;
                        if(Integer.parseInt( data.getMmr_change_to_last_game())>0){
                            desc= "El usuario tiene "+data.getElo()+" puntos de elo, en su rango tiene "+data.getRanking_in_tier()+" puntos y la ultima partida ha ganado "+data.getMmr_change_to_last_game()+" puntos";
                        }else{
                            desc= "El usuario tiene "+data.getElo()+" puntos de elo, en su rango tiene "+data.getRanking_in_tier()+" puntos y la ultima partida ha perdido "+data.getMmr_change_to_last_game()+" puntos";
                        }
                        userDto.setDescripcion(desc);
                        databaseReference.child("users").child(firebaseAuth.getCurrentUser().getUid()).setValue(userDto);
                        adapter.notifyDataSetChanged();
                    }

                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ListaUsuariosActivity.this,"Error al obtener tu información",Toast.LENGTH_SHORT);
                    }
                });
                queue.add(stringRequest);

            }
        });



        adapter.setContext(ListaUsuariosActivity.this);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(ListaUsuariosActivity.this));

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
                Intent intent = new Intent(ListaUsuariosActivity.this, PerfilActivity.class);
                startActivity(intent);
                finish();
                return true;
            case R.id.btnCerrarSesion:
                firebaseAuth.signOut();
                Intent intent2 =  new Intent(ListaUsuariosActivity.this, LoginActivity.class);
                startActivity(intent2);
                finish();
                return true;
            case R.id.btnSolicitudes:
                Intent intent3 = new Intent(ListaUsuariosActivity.this,ListaSolicitudesActivity.class);
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