package co.edu.cineapp.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import co.edu.cineapp.R;
import co.edu.cineapp.data.entities.Pelicula;
import com.bumptech.glide.Glide;

public class PeliculaAdapter
        extends RecyclerView.Adapter<PeliculaAdapter.PeliculaViewHolder> {

    // =========================================================
    // INTERFAZ PARA DETECTAR CUANDO SE TOCA UNA PELÍCULA
    // =========================================================

    public interface OnPeliculaClick {

        void alTocar(Pelicula pelicula);
    }

    public interface OnFavoritoClick {

        void alCambiar(Pelicula pelicula, ImageButton boton);
    }


    // =========================================================
    // VARIABLES
    // =========================================================

    private List<Pelicula> lista;

    private final OnPeliculaClick listener;
    private final OnFavoritoClick favoritoListener;


    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public PeliculaAdapter(
            List<Pelicula> lista,
            OnPeliculaClick listener,
            OnFavoritoClick favoritoListener
    ) {

        this.lista = lista != null
                ? lista
                : new ArrayList<>();

        this.listener = listener;
        this.favoritoListener = favoritoListener;
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

        Glide.with(holder.imgPoster.getContext())
            .load(pelicula.getPosterUrl())
            .placeholder(R.drawable.images__1_)
            .error(obtenerPosterLocal(pelicula))
            .into(holder.imgPoster);

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

        String info = pelicula.getAnio();
        if (!pelicula.getNombreGenero().isEmpty()) {
            info += (info.isEmpty() ? "" : " · ") + pelicula.getNombreGenero();
        }
        if (!pelicula.getDuracionTexto().isEmpty()) {
            info += (info.isEmpty() ? "" : " · ") + pelicula.getDuracionTexto();
        }
        holder.tvItemInfo.setText(info);


        // Click sobre la película
        holder.itemView.setOnClickListener(
                view -> {

                    if (listener != null) {

                        listener.alTocar(pelicula);
                    }
                }
        );

        holder.btnFavorito.setOnClickListener(view -> {
            if (favoritoListener != null) {
                favoritoListener.alCambiar(pelicula, holder.btnFavorito);
            }
        });
    }

    private int obtenerPosterLocal(Pelicula pelicula) {
        String titulo = pelicula.getTitulo() != null
                ? pelicula.getTitulo().toLowerCase()
                : "";
        if (titulo.contains("diverg")) {
            return R.drawable.divergente;
        }
        if (titulo.contains("juego") || titulo.contains("hambre")) {
            return R.drawable.juegos_del_hambre;
        }
        return R.drawable.images__1_;
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
        private final TextView tvItemInfo;
        private final ImageView imgPoster;
        private final ImageButton btnFavorito;


        PeliculaViewHolder(
                @NonNull View itemView
        ) {

            super(itemView);

            tvItemTitulo =
                    itemView.findViewById(
                            R.id.tvItemTitulo
                    );
                        tvItemInfo = itemView.findViewById(R.id.tvItemInfo);
                        imgPoster = itemView.findViewById(R.id.imgItemPoster);
                        btnFavorito = itemView.findViewById(R.id.btnItemFavorito);
        }
    }
}