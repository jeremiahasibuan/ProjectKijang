package com.example.repeatit;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.repeatit.Api.ApiService;
import com.example.repeatit.Api.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SongMainActivity extends AppCompatActivity {

    private RecyclerView recyclerViewMusic;
    private MusicPlayerAdapter musicPlayerAdapter;
    private LottieAnimationView lottiePause;
    private ImageView nextButton, previousButton;
    private boolean isPaused = true;
    private List<Song> songList;
    private int currentSongIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.song_main_activity);

        recyclerViewMusic = findViewById(R.id.recyclerViewMusic);
        lottiePause = findViewById(R.id.lottiePause);
        nextButton = findViewById(R.id.nextButton);
        previousButton = findViewById(R.id.previousButton);

        ImageView like = findViewById(R.id.like);
        like.setOnClickListener(new View.OnClickListener(){
            private boolean isLiked = false;
            @Override
            public void onClick(View v){
                if(isLiked){
                    like.setImageResource(R.drawable.like_putih);
                }else{
                    like.setImageResource(R.drawable.like_merah);
                }
                isLiked = !isLiked;
            }
        });

        ImageView arrow = findViewById(R.id.arrow);
        arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



        // Inisialisasi RecyclerView untuk tampilan lagu yang sedang diputar
        recyclerViewMusic = findViewById(R.id.recyclerViewMusic);  // Pastikan ID sesuai dengan layout XML Anda
        musicPlayerAdapter = new MusicPlayerAdapter(this);
        recyclerViewMusic.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewMusic.setAdapter(musicPlayerAdapter);

        // Ambil data lagu dari API (misalnya, lagu yang sedang diputar)
        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        fetchSong(apiService);

        lottiePause.setOnClickListener(v -> togglePlayPause());

        nextButton.setOnClickListener(v -> playNextSong());
        previousButton.setOnClickListener(v -> playPreviousSong());
    }

    // Mengambil lagu dari API untuk menampilkan lagu yang sedang diputar
    private void fetchSong(ApiService apiService) {
        apiService.getAllSongs().enqueue(new Callback<List<Song>>() {
            @Override
            public void onResponse(Call<List<Song>> call, Response<List<Song>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    Song currentSong = response.body().get(0);  // Misalnya, lagu pertama yang sedang diputar
                    updateMusicPlayer(currentSong);
                } else {
                    Toast.makeText(SongMainActivity.this, "Error fetching song", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Song>> call, Throwable t) {
                Toast.makeText(SongMainActivity.this, "Failed to load song", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Update tampilan dengan lagu yang sedang diputar
    private void updateMusicPlayer(Song currentSong) {
        if (musicPlayerAdapter != null) {
            musicPlayerAdapter.updateSong(currentSong); // Mengupdate RecyclerView untuk menampilkan lagu yang sedang diputar
        }
    }

    private void togglePlayPause() {
        if (MediaPlayerManager.isPlaying()) {
            MediaPlayerManager.pause();
            lottiePause.setAnimation(R.raw.start_animation);
            lottiePause.playAnimation();
            Toast.makeText(this, "Paused", Toast.LENGTH_SHORT).show();
        } else {
            Song currentSong = songList.get(currentSongIndex);
            playSong(currentSong);
        }
        isPaused = !isPaused;
    }

    private void playSong(Song song) {
        String filePath = song.getFilePath();
        if (filePath != null && !filePath.isEmpty()) {
            String audioUrl = "http://10.0.2.2:3000/uploads/" + filePath;
            MediaPlayerManager.play(audioUrl, () -> {
                lottiePause.setAnimation(R.raw.pause_animation);
                lottiePause.playAnimation();
                Toast.makeText(this, "Playing: " + song.getTitle(), Toast.LENGTH_SHORT).show();
            }, () -> Toast.makeText(this, "Failed to play song", Toast.LENGTH_SHORT).show());
        } else {
            Toast.makeText(this, "Invalid file path", Toast.LENGTH_SHORT).show();
        }
    }


    private void playNextSong() {
        if (songList != null && !songList.isEmpty()) {
            currentSongIndex = (currentSongIndex + 1) % songList.size();
            updateMusicPlayer(songList.get(currentSongIndex));
        }
    }

    private void playPreviousSong() {
        if (songList != null && !songList.isEmpty()) {
            currentSongIndex = (currentSongIndex - 1 + songList.size()) % songList.size();
            updateMusicPlayer(songList.get(currentSongIndex));
        }
    }
}




