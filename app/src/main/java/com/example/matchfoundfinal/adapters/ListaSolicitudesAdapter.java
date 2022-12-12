package com.example.matchfoundfinal.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.matchfoundfinal.R;
import com.example.matchfoundfinal.dto.SolicitudDTO;
import com.example.matchfoundfinal.dto.UsuarioDTO;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ListaSolicitudesAdapter extends RecyclerView.Adapter<ListaSolicitudesAdapter.SolicitudesDTOViewHolder>  {

    private ArrayList<SolicitudDTO> solicitudDTOS = new ArrayList<>();
    private  ArrayList<SolicitudDTO> listaOriginal;
    private Context context;

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public OnItemClickListener getSolicitudes() {
        return solicitudes;
    }

    public void setSolicitudes(OnItemClickListener solicitudes) {
        this.solicitudes = solicitudes;
    }

    private OnItemClickListener solicitudes;



    public class SolicitudesDTOViewHolder extends RecyclerView.ViewHolder{
        SolicitudDTO solicitudDTO;

        public SolicitudesDTOViewHolder(@NonNull View itemView, OnItemClickListener solicitudes) {
            super(itemView);
            Button btnSolicitud = itemView.findViewById(R.id.buttonAceptarSolicitud);
            btnSolicitud.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    solicitudes.OnItemClick(getAdapterPosition());
                }
            });
        }
    }

    @NonNull
    @Override
    public SolicitudesDTOViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_rv_solicitud,parent,false);

        return new SolicitudesDTOViewHolder(itemView,solicitudes);
    }

    @Override
    public void onBindViewHolder(@NonNull SolicitudesDTOViewHolder holder, int position) {
        SolicitudDTO solicitudDTO = solicitudDTOS.get(position);
        holder.solicitudDTO=solicitudDTO;
        TextView solicitante = holder.itemView.findViewById(R.id.textViewSolicitante);
        TextView receptor = holder.itemView.findViewById(R.id.textViewReceptor);
        solicitante.setText("De: "+solicitudDTO.getSolicitante());
        receptor.setText("Para: "+solicitudDTO.getReceptor());
        TextView mensaje = holder.itemView.findViewById(R.id.textViewMensaje);
        TextView estado = holder.itemView.findViewById(R.id.textViewEstado);
        estado.setText(solicitudDTO.getEstado().toUpperCase());
        mensaje.setText(solicitudDTO.getMensaje());

        FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                UsuarioDTO usuarioPersonal =  dataSnapshot.getValue(UsuarioDTO.class);
                Button btn = holder.itemView.findViewById(R.id.buttonAceptarSolicitud);
                Button btn2 = holder.itemView.findViewById(R.id.buttonRechazarSolicitud);
                if(solicitudDTO.getReceptor().equalsIgnoreCase(usuarioPersonal.getUsername())){
                    if(solicitudDTO.getEstado().equalsIgnoreCase("pendiente")){
                        btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                SolicitudDTO solicitud = solicitudDTO;
                                Log.d("msg","Estado de la solicitud "+ solicitud.getEstado());
                                solicitud.setEstado("aceptado");
                                Log.d("msg","El key de esta solicitud es "+solicitud.getId());
                                databaseReference.child("solicitudes").child(solicitud.getId()).setValue(solicitudDTO)
                                        .addOnSuccessListener(aVoid->{
                                            Toast.makeText(context,"Solicitud enviada correctamente",Toast.LENGTH_SHORT).show();
                                        })
                                        .addOnFailureListener(e->{
                                            Log.d("msg",e.getMessage());
                                        });

                            }
                        });
                        btn2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                SolicitudDTO solicitud = solicitudDTO;
                                Log.d("msg","Estado de la solicitud "+ solicitud.getEstado());
                                solicitud.setEstado("rechazado");
                                databaseReference.child("solicitudes").child(solicitud.getId()).setValue(solicitudDTO)
                                        .addOnSuccessListener(aVoid->{
                                            Toast.makeText(context,"Solicitud rechazada correctamente",Toast.LENGTH_SHORT).show();
                                        })
                                        .addOnFailureListener(e->{
                                            Log.d("msg",e.getMessage());
                                        });

                            }
                        });
                    }else{
                        btn.setEnabled(false);
                        btn2.setEnabled(false);
                    }

                }else{
                   btn.setEnabled(false);
                   btn.setVisibility(View.INVISIBLE);
                   btn2.setEnabled(false);
                   btn2.setVisibility(View.INVISIBLE);
                }
            }

        });




    }

    @Override
    public int getItemCount() {
        return solicitudDTOS.size();
    }

    public interface OnItemClickListener{
        void OnItemClick(int position);
    }

    FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();;
    DatabaseReference databaseReference = firebaseDatabase.getReference();

    public void filtrado(String txtBuscar){
        int size = txtBuscar.length();
        if(size == 0){
            solicitudDTOS.clear();
            solicitudDTOS.addAll(listaOriginal);
        }else{
            solicitudDTOS.clear();
            for(SolicitudDTO d : listaOriginal){
                if(d.getReceptor().toLowerCase().contains(txtBuscar.toLowerCase())){
                    solicitudDTOS.add(d);
                }
            }
        }
        notifyDataSetChanged();
    }


    public void setList(ArrayList<SolicitudDTO> list){
        this.solicitudDTOS=list;
        listaOriginal = new ArrayList<>();
        listaOriginal.addAll(list);
    }





}
