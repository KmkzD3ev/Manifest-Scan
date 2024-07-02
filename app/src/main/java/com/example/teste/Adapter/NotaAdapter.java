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

import com.example.teste.Bd.DatabaseHelper;

import com.example.teste.R;
import com.example.teste.Validation.Model.Nota;
import com.example.teste.ui.gallery.GalleryViewModel;

import java.util.List;

/**
 * Adapter para gerenciar a exibição de uma lista de notas em um RecyclerView.
 * Cada nota é representada por um item que inclui seu código de barras, peso, valor e chave de contingência.
 */

public class NotaAdapter extends RecyclerView.Adapter<NotaAdapter.NotaViewHolder> {

    private List<Nota> notas;
    private Context context;
    private DatabaseHelper databaseHelper; //Intancia do sqlite local
    private GalleryViewModel galleryViewModel;


    /**
     * Construtor do adapter.
     *
     * @param context Contexto da aplicação.
     * @param notas Lista inicial de notas a serem exibidas.
     * @param galleryViewModel ViewModel associado para operações de UI relacionadas.

     */

    public NotaAdapter(Context context, List<Nota> notas, GalleryViewModel galleryViewModel) {
        this.context = context;
        this.notas = notas;
        this.galleryViewModel = galleryViewModel;
        this.databaseHelper = new DatabaseHelper(context);
    }

    //Configuração do Visualizador

    /**
     * Infla o layout do item de nota e retorna um novo ViewHolder.
     */
    @NonNull
    @Override
    public NotaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_nota, parent, false);
        return new NotaViewHolder(view);
    }

    //Configuração dos dados a serem exibidos e botoes
    /**
     * Vincula dados da nota ao ViewHolder.
     *
     * @param holder ViewHolder que deve ser atualizado.
     * @param position Posição do item na lista.
     */
    @Override
    public void onBindViewHolder(@NonNull NotaViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Nota nota = notas.get(position);
        holder.tvBarcode.setText(nota.getChave());
        holder.tvWeight.setText(nota.getPeso());
        holder.tvValue.setText(nota.getValor());
        holder.tvChaveContingencia.setText(nota.getChave_Contingencia());

        //Defina açoes para os botoes
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
                                // Remove nota do banco de dados
                                databaseHelper.deleteNota(nota.getChave());

                                // Remove item from the list
                                notas.remove(position);
                                notifyItemRemoved(position);
                                notifyItemRangeChanged(position, notas.size());

                                // Remove nota do ViewModel
                                galleryViewModel.removeNota(nota);
                            }
                        })
                        .setNegativeButton("Não", null)
                        .show();
            }
        });
    }

    /**
     * Retorna o número total de itens na lista.
     */
    @Override
    public int getItemCount() {
        return notas.size();
    }

    //Metodo para atualizar a lista de notas
    public void setNotas(List<Nota> notas) {
        this.notas = notas;
        notifyDataSetChanged();
    }

    //Adiciona uma nova nota na lista
    /**
     *
     * @param nota Nota a ser adicionada.
     */
    public void addNota(Nota nota) {
        notas.add(nota);
        notifyItemInserted(notas.size() - 1);
    }

    //Classe interna para o ViewHolder,configuraçao final da exibiçao
    /**
     * ViewHolder que armazena as referências para os componentes de UI que serão atualizados.
     */
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
