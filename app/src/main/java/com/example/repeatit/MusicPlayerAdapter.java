package com.example.repeatit;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.airbnb.lottie.LottieAnimationView;

public class MusicPlayerAdapter extends RecyclerView.Adapter<MusicPlayerAdapter.ViewHolder> {
    private Song currentSong;
    private Context context;
    private MediaPlayer mediaPlayer;
    private boolean isSongPlaying = false;

    public MusicPlayerAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflating item layout for the song item (can be customized as per your layout)
        View view = LayoutInflater.from(context).inflate(R.layout.item_song, parent, false);
        return new ViewHolder(view);
    }

    // Method to update song being played
    public void updateSong(Song song) {
        // Check if the song has changed
        if (currentSong == null || !currentSong.getFilePath().equals(song.getFilePath())) {
            this.currentSong = song;
            notifyDataSetChanged();  // Refresh the RecyclerView to reflect new song
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (currentSong != null) {
            holder.title.setText(currentSong.getTitle());
            holder.artist.setText(currentSong.getArtist());

            // Load album art using Glide
            Glide.with(context)
                    .load("http://10.0.2.2:3000/uploads/" + currentSong.getAlbumImagePath())
                    .into(holder.albumArt);
        }
    }

    @Override
    public int getItemCount() {
        return currentSong != null ? 1 : 0; // Display only one item for the currently playing song
    }

    // ViewHolder class for the song layout
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, artist;
        ImageView albumArt;
        LottieAnimationView lottiePlayPause;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.songTitle);
            artist = itemView.findViewById(R.id.songArtist);
            albumArt = itemView.findViewById(R.id.albumArt);
            lottiePlayPause = itemView.findViewById(R.id.lottiePlayPause);
        }
    }
}
