package com.example.repeatit;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.repeatit.Api.ApiService;
import com.example.repeatit.Api.RetrofitClient;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminAddSongActivity extends AppCompatActivity {

    private static final int PICK_AUDIO_REQUEST = 1;
    private static final int PICK_IMAGE_REQUEST = 2;

    private Uri audioUri;
    private Uri albumImageUri;

    private EditText titleEditText, artistEditText, albumEditText;
    private ImageView albumImageView;
    private Button selectImageButton, selectAudioButton, uploadButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_activity_addsong);

        // Inisialisasi elemen UI
        titleEditText = findViewById(R.id.titleEditText);
        artistEditText = findViewById(R.id.artistEditText);
        albumEditText = findViewById(R.id.albumEditText);
        albumImageView = findViewById(R.id.albumImageView);
        selectImageButton = findViewById(R.id.selectImageButton);
        selectAudioButton = findViewById(R.id.selectAudioButton);
        uploadButton = findViewById(R.id.uploadButton);

        // Pilih file audio
        selectAudioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("audio/*");
                startActivityForResult(intent, PICK_AUDIO_REQUEST);
            }
        });

        // Pilih gambar album
        selectImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            }
        });

        // Upload file ke server
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadSong();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == PICK_AUDIO_REQUEST) {
                audioUri = data.getData();
                Toast.makeText(this, "Audio file selected", Toast.LENGTH_SHORT).show();
            } else if (requestCode == PICK_IMAGE_REQUEST) {
                albumImageUri = data.getData();
                albumImageView.setImageURI(albumImageUri);
                Toast.makeText(this, "Album image selected", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void uploadSong() {
        if (audioUri == null || albumImageUri == null) {
            Toast.makeText(this, "Please select both audio and image files", Toast.LENGTH_SHORT).show();
            return;
        }

        // Ambil data input dari pengguna
        String title = titleEditText.getText().toString().trim();
        String artist = artistEditText.getText().toString().trim();
        String album = albumEditText.getText().toString().trim();

        if (title.isEmpty() || artist.isEmpty() || album.isEmpty()) {
            Toast.makeText(this, "Please fill in all the fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Ambil ID yang mungkin diperlukan (bisa Anda ganti dengan nilai yang sesuai)
        String id = "some_unique_id";  // Gantilah ini sesuai dengan ID yang Anda inginkan

        // Konversi URI ke File
        File audioFile = FileUtils.getFileFromUri(this, audioUri);
        File imageFile = FileUtils.getFileFromUri(this, albumImageUri);

        // Cek apakah file audio dan gambar album valid
        if (audioFile == null || imageFile == null) {
            Toast.makeText(this, "Error with audio or image file", Toast.LENGTH_SHORT).show();
            return;
        }

        // Request body untuk data input
        RequestBody titlePart = RequestBody.create(MediaType.parse("text/plain"), title);
        RequestBody artistPart = RequestBody.create(MediaType.parse("text/plain"), artist);
        RequestBody albumPart = RequestBody.create(MediaType.parse("text/plain"), album);
        RequestBody idPart = RequestBody.create(MediaType.parse("text/plain"), id); // Menambahkan id

        // Multipart body untuk file
        MultipartBody.Part audioPart = MultipartBody.Part.createFormData(
                "audio", audioFile.getName(),
                RequestBody.create(MediaType.parse("audio/*"), audioFile)
        );

        MultipartBody.Part imagePart = MultipartBody.Part.createFormData(
                "gambarAlbum", imageFile.getName(),
                RequestBody.create(MediaType.parse("image/*"), imageFile)
        );

        // Buat API service
        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);

        // Lakukan panggilan API
        Call<Void> call = apiService.addSong(idPart, albumPart, imagePart, titlePart, artistPart, audioPart);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(AdminAddSongActivity.this, "Song uploaded successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(AdminAddSongActivity.this, "Upload failed: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(AdminAddSongActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("Upload Error", t.getMessage());
            }
        });
    }

}
