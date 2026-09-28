package co.edu.cineapp.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import co.edu.cineapp.R;
import co.edu.cineapp.data.entities.Pelicula;

public class PeliculaAdapter
        extends RecyclerView.Adapter<PeliculaAdapter.PeliculaViewHolder> {

    // =========================================================
    // INTERFAZ PARA DETECTAR CUANDO SE TOCA UNA PELÍCULA
    // =========================================================

    public interface OnPeliculaClick {

        void alTocar(Pelicula pelicula);
    }


    // =========================================================
    // VARIABLES
    // =========================================================

    private List<Pelicula> lista;

    private final OnPeliculaClick listener;


    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public PeliculaAdapter(
            List<Pelicula> lista,
            OnPeliculaClick listener
    ) {

        this.lista = lista != null
                ? lista
                : new ArrayList<>();

        this.listener = listener;
    }


    // =========================================================
    // ACTUALIZAR LISTA
    // =========================================================

    public void actualizarLista(List<Pelicula> nuevaLista) {

        if (nuevaLista == null) {

            this.lista = new ArrayList<>();

        } else {

            this.lista = nuevaLista;
        }

        notifyDataSetChanged();
    }


    // =========================================================
    // CREAR TARJETA
    // =========================================================

    @NonNull
    @Override
    public PeliculaViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {

        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(
                        R.layout.activity_item_pelicula,
                        parent,
                        false
                );

        return new PeliculaViewHolder(view);
    }


    // =========================================================
    // CARGAR DATOS EN LA TARJETA
    // =========================================================

    @Override
    public void onBindViewHolder(
            @NonNull PeliculaViewHolder holder,
            int position
    ) {

        Pelicula pelicula = lista.get(position);

        // Título
        if (pelicula.getTitulo() != null) {

            holder.tvItemTitulo.setText(
                    pelicula.getTitulo()
            );

        } else {

            holder.tvItemTitulo.setText(
                    "Sin título"
            );
        }


        // Click sobre la película
        holder.itemView.setOnClickListener(
                view -> {

                    if (listener != null) {

                        listener.alTocar(pelicula);
                    }
                }
        );
    }


    // =========================================================
    // CANTIDAD DE PELÍCULAS
    // =========================================================

    @Override
    public int getItemCount() {

        return lista != null
                ? lista.size()
                : 0;
    }


    // =========================================================
    // VIEW HOLDER
    // =========================================================

    static class PeliculaViewHolder
            extends RecyclerView.ViewHolder {

        private final TextView tvItemTitulo;


        PeliculaViewHolder(
                @NonNull View itemView
        ) {

            super(itemView);

            tvItemTitulo =
                    itemView.findViewById(
                            R.id.tvItemTitulo
                    );
        }
    }
}