package com.example.matchfoundfinal.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.matchfoundfinal.R;
import com.example.matchfoundfinal.dto.UsuarioDTO;

import java.util.ArrayList;

public class ListaUsuariosAdapter extends RecyclerView.Adapter<ListaUsuariosAdapter.UsuarioDTOViewHolder> {

    private ArrayList<UsuarioDTO> usuarioDTOS = new ArrayList<>();
    private ArrayList<UsuarioDTO> listaOriginal;
    private Context context;
    private OnItemClickListener detalles;
    private OnItemClickListener solicitud;

    public OnItemClickListener getDetalles() {
        return detalles;
    }

    public void setDetalles(OnItemClickListener detalles) {
        this.detalles = detalles;
    }

    public void setSolicitud(OnItemClickListener solicitud) {
        this.solicitud = solicitud;
    }

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
                if(d.getUsername().toLowerCase().contains(txtBuscar)){
                    usuarioDTOS.add(d);
                }
            }
        }
        notifyDataSetChanged();
    }

    public class UsuarioDTOViewHolder extends RecyclerView.ViewHolder{
        UsuarioDTO usuarioDTO;
        public UsuarioDTOViewHolder(@NonNull View itemView, OnItemClickListener detalles){
            super(itemView);
            Button btnSolicitud = itemView.findViewById(R.id.btnSolicitud);
            btnSolicitud.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    solicitud.OnItemClick(getAdapterPosition());
                }
            });
            TextView details = itemView.findViewById(R.id.textViewIrPerfil);
            details.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    detalles.OnItemClick(getAdapterPosition());
                }
            });
        }
    }

    @NonNull
    @Override
    public UsuarioDTOViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_rv_usuarios,parent,false);
        return new UsuarioDTOViewHolder(itemView, detalles);
    }

    @Override
    public void onBindViewHolder(@NonNull UsuarioDTOViewHolder holder, int position) {
        UsuarioDTO usuarioDTO = usuarioDTOS.get(position);
        holder.usuarioDTO=usuarioDTO;
        TextView username = holder.itemView.findViewById(R.id.textViewUserrv);
        TextView rango = holder.itemView.findViewById(R.id.textViewRango);
        username.setText(usuarioDTO.getUsername()+"#"+usuarioDTO.getTag());
        rango.setText("Rango: "+usuarioDTO.getRango());
        Button button = holder.itemView.findViewById(R.id.btnSolicitud);
        ImageView image = holder.itemView.findViewById(R.id.imageView);
        Glide.with(context).load(usuarioDTO.getRankImage()).into(image);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("msg","Solicitud");
            }
        });
        TextView details = holder.itemView.findViewById(R.id.textViewIrPerfil);
        details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("msg","Detalles");
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