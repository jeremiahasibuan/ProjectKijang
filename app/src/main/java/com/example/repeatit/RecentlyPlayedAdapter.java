package com.example.repeatit;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class RecentlyPlayedAdapter extends RecyclerView.Adapter<RecentlyPlayedAdapter.ViewHolder> {
    private List<Song> songs;
    private final Context context;
    private final PlayingNowAdapter playingNowAdapter;

    // Constructor to initialize the adapter and playingNowAdapter
    public RecentlyPlayedAdapter(Context context, List<Song> songs, PlayingNowAdapter playingNowAdapter) {
        this.context = context;
        this.songs = songs;
        this.playingNowAdapter = playingNowAdapter;  // Make sure this is not null
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for the item view
        View view = LayoutInflater.from(context).inflate(R.layout.item_recently_played, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Song song = songs.get(position);

        if (song == null) {
            Log.e("RecentlyPlayedAdapter", "Song object is null at position: " + position);
            return; // Skip binding if the song is null
        }

        // Set the song title and artist
        holder.title.setText(song.getTitle());
        holder.artist.setText(song.getArtist());

        // Load album art using Glide
        String albumImageName = song.getAlbumImagePath();
        String imageUrl = "http://10.0.2.2:3000/uploads/" + albumImageName;

        Glide.with(context)
                .load(imageUrl)
                .apply(new RequestOptions().timeout(10000))
                .into(holder.albumArt);

        holder.cardView.setOnClickListener(v -> {
            String filePath = song.getFilePath();
            if (filePath != null && !filePath.isEmpty()) {
                String audioUrl = "http://10.0.2.2:3000/uploads/" + filePath;
                MediaPlayerManager.play(audioUrl,
                        () -> {
                            playingNowAdapter.updateSong(song); // Perbarui UI saat lagu mulai diputar

                            Intent intent = new Intent(v.getContext(), SongMainActivity.class);
                            v.getContext().startActivity(intent);
                            Log.d("RecentlyPlayedAdapter", "Playing: " + song.getTitle());
                        },
                        () -> Log.e("AudioError", "Failed to play song: " + song.getTitle())
                );
            } else {
                Log.e("AudioError", "File path is null or empty for song: " + song.getTitle());
            }
        });
    }

    @Override
    public int getItemCount() {
        return songs != null ? songs.size() : 0;
    }

    // Method to update the songs in the adapter
    public void updateSongs(List<Song> newSongs) {
        this.songs = newSongs != null ? newSongs : List.of();
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, artist;
        ImageView albumArt;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.songTitle);
            artist = itemView.findViewById(R.id.songArtist);
            albumArt = itemView.findViewById(R.id.albumArt);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }
}
