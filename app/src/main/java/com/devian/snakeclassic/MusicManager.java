package com.devian.snakeclassic;


import android.content.Context;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.util.Log;


public class MusicManager {
    private static MusicManager instance;
    private MediaPlayer mediaPlayer;
    private SoundPool soundPool;
    private int biteSoundId;
    private boolean isMusicEnabled = true;
    private boolean isSoundEffectEnabled = true;
    private Context context;

    private MusicManager(Context context) {
        this.context = context.getApplicationContext();

        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();

        soundPool = new SoundPool.Builder()
                .setAudioAttributes(audioAttributes)
                .setMaxStreams(5)
                .build();

        biteSoundId = soundPool.load(context, R.raw.bite_sound_effect, 1);
    }

    public static synchronized MusicManager getInstance(Context context) {
        if (instance == null) {
            instance = new MusicManager(context);
        }
        return instance;
    }

    public void playSoundEffect() {
        if (isSoundEffectEnabled && biteSoundId != 0) {
            soundPool.play(biteSoundId, 1f, 1f, 1, 0, 1f);
        }
    }

    public void release() {
        if (soundPool != null) {
            soundPool.release();
            soundPool = null;
        }
    }

    public void startBackgroundMusic() {
        if (!isMusicEnabled) return;

        try {
            if (mediaPlayer == null) {
                mediaPlayer = MediaPlayer.create(context, R.raw.background_music);
                mediaPlayer.setLooping(true); // Loop the music
                mediaPlayer.setVolume(0.5f, 0.5f); // Set volume (0.0 to 1.0)
            }

            if (!mediaPlayer.isPlaying()) {
                mediaPlayer.start();
                Log.d("MusicManager", "Background music started");
            }
        } catch (Exception e) {
            Log.e("MusicManager", "Error starting background music: " + e.getMessage());
        }
    }

    public void pauseBackgroundMusic() {
        try {
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
                Log.d("MusicManager", "Background music paused");
            }
        } catch (Exception e) {
            Log.e("MusicManager", "Error pausing background music: " + e.getMessage());
        }
    }

    public void resumeBackgroundMusic() {
        if (!isMusicEnabled) return;

        try {
            if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
                mediaPlayer.start();
                Log.d("MusicManager", "Background music resumed");
            }
        } catch (Exception e) {
            Log.e("MusicManager", "Error resuming background music: " + e.getMessage());
        }
    }

    public void stopBackgroundMusic() {
        try {
            if (mediaPlayer != null) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                }
                mediaPlayer.release();
                mediaPlayer = null;
                Log.d("MusicManager", "Background music stopped and released");
            }
        } catch (Exception e) {
            Log.e("MusicManager", "Error stopping background music: " + e.getMessage());
        }
    }

    public void setMusicEnabled(boolean enabled) {
        this.isMusicEnabled = enabled;
        if (!enabled) {
            pauseBackgroundMusic();
        } else {
            resumeBackgroundMusic();
        }
    }

    public boolean isMusicEnabled() {
        return isMusicEnabled;
    }

    public void setVolume(float volume) {
        if (mediaPlayer != null) {
            mediaPlayer.setVolume(volume, volume);
        }
    }
}