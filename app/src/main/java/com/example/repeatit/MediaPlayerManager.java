    package com.example.repeatit;
    import android.media.MediaPlayer;
    import android.util.Log;

    public class MediaPlayerManager {
        private static MediaPlayer mediaPlayer;
        private static boolean isPlaying = false;

        private static void initializeMediaPlayer() {
            if (mediaPlayer == null) {
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setOnCompletionListener(mp -> {
                    isPlaying = false;
                    Log.d("MediaPlayerManager", "Song completed.");
                });
                mediaPlayer.setOnErrorListener((mp, what, extra) -> {
                    Log.e("MediaPlayerManager", "Error during playback. What: " + what + ", Extra: " + extra);
                    stop();
                    return true;
                });
            }
        }

        public static void play(String audioUrl, Runnable onSuccess, Runnable onError) {
            stop(); // Reset sebelum memutar lagu baru

            initializeMediaPlayer();
            try {
                mediaPlayer.setDataSource(audioUrl);
                mediaPlayer.setOnPreparedListener(mp -> {
                    mp.start();
                    isPlaying = true;
                    if (onSuccess != null) {
                        onSuccess.run();
                    }
                });
                mediaPlayer.prepareAsync();
            } catch (Exception e) {
                Log.e("MediaPlayerManager", "Error preparing MediaPlayer: " + e.getMessage());
                if (onError != null) {
                    onError.run();
                }
            }
        }

        public static void stop() {
            if (mediaPlayer != null) {
                mediaPlayer.reset();
                isPlaying = false;
            }
        }

        public static void release() {
            if (mediaPlayer != null) {
                mediaPlayer.release();
                mediaPlayer = null;
                isPlaying = false;
            }
        }

        public static boolean isPlaying() {
            return isPlaying;
        }

        public static void pause() {
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
                isPlaying = false;
            }
        }
    }


