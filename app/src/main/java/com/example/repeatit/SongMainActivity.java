package com.example.repeatit;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.repeatit.Api.ApiService;
import com.example.repeatit.Api.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SongMainActivity extends AppCompatActivity {

    private RecyclerView recyclerViewMusic;
    private MusicPlayerAdapter musicPlayerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.song_main_activity);

        // Inisialisasi RecyclerView untuk tampilan lagu yang sedang diputar
        recyclerViewMusic = findViewById(R.id.recyclerViewMusic);  // Pastikan ID sesuai dengan layout XML Anda
        musicPlayerAdapter = new MusicPlayerAdapter(this);
        recyclerViewMusic.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewMusic.setAdapter(musicPlayerAdapter);

        // Ambil data lagu dari API (misalnya, lagu yang sedang diputar)
        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        fetchSong(apiService);
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
}
