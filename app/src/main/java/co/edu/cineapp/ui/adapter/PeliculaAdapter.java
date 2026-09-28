package co.edu.cineapp.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import co.edu.cineapp.R;
import co.edu.cineapp.data.entities.Pelicula;

public class PeliculaAdapter extends RecyclerView.Adapter<PeliculaAdapter.PeliculaViewHolder> { //adaptador del RecyclerView
    public interface OnPeliculaClick { //interfaz para avisar a la pantalla que pelicula se toco
        void alTocar(Pelicula pelicula); //Metodo para implementar el catalogo
    }

    private List<Pelicula> lista; //Peliculas que se muestran
    private final OnPeliculaClick listener; //escucha los clicks

    public PeliculaAdapter(List<Pelicula> lista, OnPeliculaClick listener) { //constructor
        this.lista = lista; //Guarda la lista
        this.listener = listener; //Guarda el listener
    }

    public void actualizarLista(List<Pelicula> nuevaLista) { //Cambia la lista para filtrar
        this.lista = nuevaLista; // Reemplaza los datos
        notifyDataSetChanged(); // Le indica al RecyclerView que se redibuje
    }

    @NonNull
    @Override
    public PeliculaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) { //Crea la vista de una tarjeta
        View view = LayoutInflater.from(parent.getContext()) // Toma el inflater de layouts
                .inflate(R.layout.activity_item_pelicula, parent, false); //Infla item_pelicula.xml
        return new PeliculaViewHolder(view); //Devuelve el contenedor de esa tarjeta
    }

    @Override
    public void onBindViewHolder(@NonNull PeliculaViewHolder holder, int position) { // Llena una tarjeta con datos
        Pelicula pelicula = lista.get(position); //Obtener la pelicula de esa posicion
        holder.tvItemTitulo.setText(pelicula.getTitulo()); // Muestra el titulo
        holder.itemView.setOnClickListener(view -> listener.alTocar(pelicula)); // al tocar avisa al catalogo
    }

    @Override
    public int getItemCount() { //cantidad de tarjetas
        return lista.size(); // Es el tamaño de la lista
    }

    static class PeliculaViewHolder extends RecyclerView.ViewHolder { //guardar las vistas de una tarjeta
        private TextView tvItemTitulo; //Titulo dentro de la tarjeta

        PeliculaViewHolder(@NonNull View itemView){ //Constructor
            super(itemView); //llama al padre
            tvItemTitulo = itemView.findViewById(R.id.tvItemTitulo); //busca el titulo de la tarjeta
        }

    }
}
