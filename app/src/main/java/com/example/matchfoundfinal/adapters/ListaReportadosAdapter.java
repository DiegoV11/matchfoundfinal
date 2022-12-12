package com.example.matchfoundfinal.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.matchfoundfinal.R;
import com.example.matchfoundfinal.cliente.OtroPerfilActivity;
import com.example.matchfoundfinal.dto.SolicitudDTO;
import com.example.matchfoundfinal.dto.UsuarioDTO;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ListaReportadosAdapter extends RecyclerView.Adapter<ListaReportadosAdapter.UsuarioReportadoDTOViewHolder> {
    private ArrayList<UsuarioDTO> usuarioDTOS = new ArrayList<>();
    private ArrayList<UsuarioDTO> listaOriginal;
    private Context context;

    public OnItemClickListener getDetalles() {
        return detalles;
    }

    public void setDetalles(OnItemClickListener detalles) {
        this.detalles = detalles;
    }

    private ListaReportadosAdapter.OnItemClickListener detalles;


    FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();;
    DatabaseReference databaseReference = firebaseDatabase.getReference();






    public interface OnItemClickListener{
        void OnItemClick(int position);
    }

    public void filtrado(String txtBuscar){
        int size = txtBuscar.length();
        if(size == 0){
            usuarioDTOS.clear();
            usuarioDTOS.addAll(listaOriginal);
        }else{
            usuarioDTOS.clear();
            for(UsuarioDTO d : listaOriginal){
                if(d.getUsername().toLowerCase().contains(txtBuscar.toLowerCase())){
                    usuarioDTOS.add(d);
                }
            }
        }
        notifyDataSetChanged();
    }

    public class UsuarioReportadoDTOViewHolder extends RecyclerView.ViewHolder{
        UsuarioDTO usuarioDTO;
        public UsuarioReportadoDTOViewHolder(@NonNull View itemView){
            super(itemView);

        }
    }

    @NonNull
    @Override
    public ListaReportadosAdapter.UsuarioReportadoDTOViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_rv_usuariosreportados,parent,false);
        return new ListaReportadosAdapter.UsuarioReportadoDTOViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ListaReportadosAdapter.UsuarioReportadoDTOViewHolder holder, int position) {


        UsuarioDTO usuarioDTO = usuarioDTOS.get(position);
        holder.usuarioDTO=usuarioDTO;
        TextView username = holder.itemView.findViewById(R.id.textViewUserReportado);
        TextView cantidad = holder.itemView.findViewById(R.id.textViewCantidadReportes);
        username.setText(usuarioDTO.getUsername()+"#"+usuarioDTO.getTag());
        cantidad.setText("Cantidad de reportes: "+usuarioDTO.getCantReportes());
        Button button = holder.itemView.findViewById(R.id.btnSolicitudReporte);
        ImageView image = holder.itemView.findViewById(R.id.imageView);
        Glide.with(context).load(usuarioDTO.getRankImage()).override(600, 200).into(image);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context,"Se bloqueará al usuario",Toast.LENGTH_SHORT).show();
                Log.d("msg","presionaste el botn");
            }
        });







    }

    @Override
    public int getItemCount() {
        return usuarioDTOS.size();
    }

    public ArrayList<UsuarioDTO> getList() {
        return usuarioDTOS;
    }

    public void setList(ArrayList<UsuarioDTO> list) {
        this.usuarioDTOS = list;
        listaOriginal = new ArrayList<>();
        listaOriginal.addAll(list);
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
