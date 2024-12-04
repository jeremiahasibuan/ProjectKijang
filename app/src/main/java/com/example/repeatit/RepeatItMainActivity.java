package com.example.repeatit;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.repeatit.Api.ApiService;
import com.example.repeatit.Api.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RepeatItMainActivity extends AppCompatActivity {

    private RecyclerView recyclerViewRecentlyPlayed, recyclerViewTrendingSongs;
    private RecentlyPlayedAdapter recentlyPlayedAdapter, trendingSongsAdapter;
    private List<Song> recentlyPlayedList, trendingSongList;
    private PlayingNowAdapter playingNowAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.repeatit_main_activity);

        // Initialize PlayingNow RecyclerView
        RecyclerView recyclerPlayingNow = findViewById(R.id.recyclerPlayingNow);
        playingNowAdapter = new PlayingNowAdapter(this);
        recyclerPlayingNow.setLayoutManager(new LinearLayoutManager(this));
        recyclerPlayingNow.setAdapter(playingNowAdapter);

        // Initialize Recently Played RecyclerView
        recyclerViewRecentlyPlayed = findViewById(R.id.recyclerRecentlyPlayed);
        recentlyPlayedList = new ArrayList<>();
        recentlyPlayedAdapter = new RecentlyPlayedAdapter(this, recentlyPlayedList, playingNowAdapter);
        setupRecyclerView(recyclerViewRecentlyPlayed, recentlyPlayedAdapter);

        // Initialize Trending Songs RecyclerView
        recyclerViewTrendingSongs = findViewById(R.id.recyclerTrendingSong);
        trendingSongList = new ArrayList<>();
        trendingSongsAdapter = new RecentlyPlayedAdapter(this, trendingSongList, playingNowAdapter);
        setupRecyclerView(recyclerViewTrendingSongs, trendingSongsAdapter);

        // Fetch data from API
        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        fetchSongs(apiService, "recently_played");
        fetchSongs(apiService, "trending");
    }

    private void setupRecyclerView(RecyclerView recyclerView, RecentlyPlayedAdapter adapter) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void fetchSongs(ApiService apiService, String type) {
        Call<List<Song>> call;

        if (type.equals("recently_played")) {
            call = apiService.getAllSongs(); // API call for recently played songs
        } else {
            call = apiService.getAllSongs(); // API call for trending songs
        }

        call.enqueue(new Callback<List<Song>>() {
            @Override
            public void onResponse(Call<List<Song>> call, Response<List<Song>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (type.equals("recently_played")) {
                        updateSongList(recentlyPlayedList, recentlyPlayedAdapter, response.body());
                    } else {
                        updateSongList(trendingSongList, trendingSongsAdapter, response.body());
                    }
                } else {
                    Toast.makeText(RepeatItMainActivity.this, "Error fetching " + type + " songs", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Song>> call, Throwable t) {
                Toast.makeText(RepeatItMainActivity.this, "Failed to load " + type + " songs", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateSongList(List<Song> songList, RecentlyPlayedAdapter adapter, List<Song> newSongs) {
        songList.clear();
        songList.addAll(newSongs);
        adapter.notifyDataSetChanged();
    }
}
