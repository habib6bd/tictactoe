package com.example.kidstictactoe;

import android.content.Context;
import android.media.SoundPool;
import android.os.Build;

public class SoundManager {
    private SoundPool soundPool;
    private int tapSoundId = -1;
    private int gameOverSoundId = -1;

    public SoundManager(Context context) {
        // Create SoundPool for playing sounds
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            android.media.AudioAttributes attributes = new android.media.AudioAttributes.Builder()
                    .setUsage(android.media.AudioAttributes.USAGE_GAME)
                    .setContentType(android.media.AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();
            soundPool = new SoundPool.Builder()
                    .setMaxStreams(2)
                    .setAudioAttributes(attributes)
                    .build();
        } else {
            soundPool = new SoundPool(2, android.media.AudioManager.STREAM_MUSIC, 0);
        }

        // Load sounds - we'll generate them programmatically since we don't have audio files
        createSounds();
    }

    private void createSounds() {
        // For simplicity, we'll use ToneGenerator to create beep sounds
        // This is handled in playTapSound() and playGameOverSound()
    }

    public void playTapSound() {
        // Play a short beep sound when tapping a square
        try {
            android.media.ToneGenerator toneGenerator = new android.media.ToneGenerator(
                    android.media.AudioManager.STREAM_MUSIC, 100);
            toneGenerator.startTone(android.media.ToneGenerator.TONE_CDMA_PIP, 100);
            toneGenerator.release();
        } catch (Exception ignored) {
        }
    }

    public void playGameOverSound() {
        // Play a success/celebration sound when game ends
        try {
            android.media.ToneGenerator toneGenerator = new android.media.ToneGenerator(
                    android.media.AudioManager.STREAM_MUSIC, 100);
            // Play a sequence of tones for celebration
            toneGenerator.startTone(android.media.ToneGenerator.TONE_CDMA_CONFIRM, 200);
            toneGenerator.release();
        } catch (Exception ignored) {
        }
    }

    public void release() {
        if (soundPool != null) {
            soundPool.release();
            soundPool = null;
        }
    }
}
