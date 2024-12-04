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

import java.io.IOException;

public class PlayingNowAdapter extends RecyclerView.Adapter<PlayingNowAdapter.ViewHolder> {
    private Song currentSong;
    private Context context;
    private MediaPlayer mediaPlayer;
    private boolean isSongPlaying = false;

    public PlayingNowAdapter(Context context) {
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_playing_now, parent, false);
        return new ViewHolder(view);
    }

    public void updateSong(Song song) {
        // Periksa apakah lagu baru berbeda dari currentSong
        if (currentSong == null || !currentSong.getFilePath().equals(song.getFilePath())) {
            this.currentSong = song;
            notifyDataSetChanged(); // Memperbarui RecyclerView
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (currentSong != null) {
            holder.title.setText(currentSong.getTitle());
            holder.artist.setText(currentSong.getArtist());
            Glide.with(context)
                    .load("http://10.0.2.2:3000/uploads/" + currentSong.getAlbumImagePath())
                    .into(holder.albumArt);

            holder.lottiePlayPause.setOnClickListener(v -> {
                if (MediaPlayerManager.isPlaying()) {
                    MediaPlayerManager.stop();
                    holder.lottiePlayPause.setAnimation("start_animation.json");
                    holder.lottiePlayPause.playAnimation();
                } else {
                    String filePath = currentSong.getFilePath();
                    if (filePath != null && !filePath.isEmpty()) {
                        String audioUrl = "http://10.0.2.2:3000/uploads/" + filePath;
                        MediaPlayerManager.play(audioUrl,
                                () -> {
                                    holder.lottiePlayPause.setAnimation("pause_animation.json");
                                    holder.lottiePlayPause.playAnimation();
                                },
                                () -> Log.e("AudioError", "Failed to play song: " + currentSong.getTitle())
                        );
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return currentSong != null ? 1 : 0; // Tampilkan hanya jika ada lagu yang aktif
    }

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

    private void playSong() {
        if (currentSong != null && !isSongPlaying) {
            String filePath = currentSong.getFilePath();
            if (filePath != null && !filePath.isEmpty()) {
                String audioUrl = "http://10.0.2.2:3000/uploads/" + filePath;

                if (mediaPlayer == null) {
                    try {
                        mediaPlayer = new MediaPlayer();
                        mediaPlayer.setDataSource(audioUrl);
                        mediaPlayer.setOnPreparedListener(mp -> {
                            mp.start(); // Mulai pemutaran setelah siap
                            isSongPlaying = true;
                        });
                        mediaPlayer.setOnErrorListener((mp, what, extra) -> {
                            Log.e("MediaPlayer Error", "What: " + what + ", Extra: " + extra);
                            stopSong();
                            return true;
                        });
                        mediaPlayer.prepareAsync();
                    } catch (IOException e) {
                        Log.e("MediaPlayer Error", "IOException: " + e.getMessage());
                    }
                } else {
                    if (mediaPlayer.isPlaying()) {
                        mediaPlayer.stop();
                        mediaPlayer.reset();
                    }
                    try {
                        mediaPlayer.setDataSource(audioUrl);
                        mediaPlayer.prepareAsync();
                    } catch (IOException e) {
                        Log.e("MediaPlayer Error", "IOException: " + e.getMessage());
                    }
                }
            }
        }
    }

    private void stopSong() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
            }
            mediaPlayer.reset();
            isSongPlaying = false;
        }
    }
}

