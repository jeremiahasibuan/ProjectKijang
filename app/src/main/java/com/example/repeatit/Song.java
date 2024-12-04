package com.example.repeatit;

import android.net.Uri;
import android.util.Log;

import java.io.File;

public class Song {
    private String title;
    private String artist;
    private String album;
    private String filePath;
    private String albumImagePath;  // Ini untuk menyimpan path gambar album
    private String playedAt;

    // Constructor
    public Song(String title, String artist, String album, String filePath, String albumImagePath, String playedAt) {
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.filePath = filePath;
        this.albumImagePath = albumImagePath;
        this.playedAt = playedAt;
    }

    // Getter dan Setter
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getFilePath() {
        Log.d("AudioFormat", "Playing file: " + filePath);

        if (filePath != null && !filePath.isEmpty()) {
            // Mencari index separator terakhir, baik '/' atau '\\' (untuk path di berbagai platform)
            int separatorIndex = Math.max(filePath.lastIndexOf('/'), filePath.lastIndexOf('\\'));

            // Mengambil bagian setelah separator terakhir
            if (separatorIndex != -1) {
                return filePath.substring(separatorIndex + 1); // Ambil bagian setelah separator
            } else {
                return filePath; // Jika tidak ada separator, return seluruh filePath (misal hanya nama file)
            }
        }
        return null; // Jika filePath null atau kosong
    }



    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getAlbumImagePath() {
        if (albumImagePath != null) {
            // Mengambil nama file dari path
            return albumImagePath.substring(albumImagePath.lastIndexOf("\\") + 1);
        }
        return null;
    }

    public void setAlbumImagePath(String albumImagePath) {
        this.albumImagePath = albumImagePath;
    }

    public String getPlayedAt() {
        return playedAt;
    }

    public void setPlayedAt(String playedAt) {
        this.playedAt = playedAt;
    }

    // Menambahkan method untuk mengonversi albumImagePath ke Uri
    public Uri getAlbumImageUri() {
        if (albumImagePath != null && !albumImagePath.isEmpty()) {
            return Uri.fromFile(new File(albumImagePath));  // Mengonversi albumImagePath menjadi Uri
        }
        return null;  // Jika albumImagePath kosong atau null
    }
}


