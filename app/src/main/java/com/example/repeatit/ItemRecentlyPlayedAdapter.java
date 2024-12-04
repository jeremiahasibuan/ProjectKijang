package com.example.repeatit;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class ItemRecentlyPlayedAdapter extends RecyclerView.Adapter<ItemRecentlyPlayedAdapter.SongViewHolder> {

    private List<Song> songList;
    private Context context;



    // Constructor
    public ItemRecentlyPlayedAdapter(List<Song> songList, Context context) {
        this.songList = songList;
        this.context = context;
    }

    @NonNull
    @Override
    public SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_recently_played, parent, false);
        return new SongViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SongViewHolder holder, int position) {
        // Ambil data lagu berdasarkan posisi
        Song song = songList.get(position);

        // Validasi data sebelum di-set
        if (song != null) {
            // Set judul lagu, validasi agar tidak null
            holder.songTitle.setText(song.getTitle() != null ? song.getTitle() : "Unknown Title");

            // Set nama artis, validasi agar tidak null
            holder.songArtist.setText(song.getArtist() != null ? song.getArtist() : "Unknown Artist");

            // Gunakan Glide untuk memuat gambar album
            Glide.with(context)
                    .load(song.getAlbumImageUri()) // URI dari album art
                    .placeholder(R.drawable.arrow) // Placeholder saat loading
                    .error(R.drawable.arrow) // Gambar default jika terjadi error
                    .into(holder.albumArt); // Target ImageView
        } else {
            // Jika data song null, atur nilai default
            holder.songTitle.setText("Unknown Title");
            holder.songArtist.setText("Unknown Artist");
            holder.albumArt.setImageResource(R.drawable.arrow);
        }
    }


    @Override
    public int getItemCount() {
        return songList.size();
    }

    public static class SongViewHolder extends RecyclerView.ViewHolder {
        ImageView albumArt;
        TextView songTitle, songArtist;

        public SongViewHolder(@NonNull View itemView) {
            super(itemView);
            albumArt = itemView.findViewById(R.id.albumArt);
            songTitle = itemView.findViewById(R.id.songTitle);
            songArtist = itemView.findViewById(R.id.songArtist);
        }
    }
}

