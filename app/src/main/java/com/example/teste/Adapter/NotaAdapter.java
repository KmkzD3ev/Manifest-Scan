package com.example.teste.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.teste.R;
import com.example.teste.Validation.Model.Nota;

import java.util.List;

public class NotaAdapter extends RecyclerView.Adapter<NotaAdapter.NotaViewHolder> {


    private List<Nota> notas;
    private Context context;

    public NotaAdapter(Context context, List<Nota> notas) {
        this.context = context;
        this.notas = notas;
    }

    @NonNull
    @Override
    public NotaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_nota, parent, false);
        return new NotaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotaViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Nota nota = notas.get(position);
        holder.tvBarcode.setText(nota.getBarcode());
        holder.tvWeight.setText(nota.getWeight());
        holder.tvValue.setText(nota.getValue());
        holder.tvChaveContingencia.setText(nota.getChaveContingencia());

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Mostrar alerta de confirmação
                new AlertDialog.Builder(context)
                        .setTitle("Excluir Nota")
                        .setMessage("Esta nota será excluída. Tem certeza que deseja concluir a operação?")
                        .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Remove item from the list
                                notas.remove(position);
                                notifyItemRemoved(position);
                                notifyItemRangeChanged(position, notas.size());
                            }
                        })
                        .setNegativeButton("Não", null)
                        .show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return notas.size();
    }

    // Método para definir uma nova lista de notas e atualizar o adapter
    public void setNotas(List<Nota> notas) {
        this.notas = notas;
        notifyDataSetChanged();
    }

    // Método para adicionar uma nota
    public void addNota(Nota nota) {
        notas.add(nota);
        notifyItemInserted(notas.size() - 1);
    }

    static class NotaViewHolder extends RecyclerView.ViewHolder {
        TextView tvBarcode, tvWeight, tvValue, tvChaveContingencia;
        Button btnDelete;

        public NotaViewHolder(@NonNull View itemView) {
            super(itemView);
            tvBarcode = itemView.findViewById(R.id.tvBarcode);
            tvWeight = itemView.findViewById(R.id.tvWeight);
            tvValue = itemView.findViewById(R.id.tvValue);
            tvChaveContingencia = itemView.findViewById(R.id.tvChaveContingencia);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}