package com.example.repeatit.Api;

import com.example.repeatit.Song;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiService {
    @Multipart
    @POST("/add-song")
    Call<Void> addSong(
            @Part("id") RequestBody id,
            @Part("album") RequestBody album,
            @Part MultipartBody.Part gambarAlbum,
            @Part("title") RequestBody title,
            @Part("artist") RequestBody artist,
            @Part MultipartBody.Part audio
    );
    @GET("/songs")  // Endpoint API untuk mengambil data lagu
    Call<List<Song>> getAllSongs();
}
