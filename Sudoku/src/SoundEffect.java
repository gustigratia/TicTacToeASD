/**
 * ES234317-Algorithm and Data Structures
 * Semester Ganjil, 2024/2025
 * Group Capstone Project
 * Group #8
 * 1 - 5026231083 - Arya Wiraguna Dwiputra
 * 2 - 5026231097 - Gusti Gratia Delpiera
 * 3 - 5026231152 - M. Naufal Erwin Effendi
 */

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.net.URL;

public enum SoundEffect {
    SONG("game.wav"),
    CORRECT("correct.wav"),
    WRONG("wrong.wav"),
    CLICK("click.wav"),
    WIN("win.wav");

    public static enum Volume {
        MUTE, LOW, MEDIUM, HIGH
    }

    public static Volume volume = Volume.HIGH; // Default volume HIGH

    private Clip clip;

    private SoundEffect(String soundFileName) {
        try {
            URL url = this.getClass().getClassLoader().getResource(soundFileName);
            if (url == null) {
                throw new IllegalArgumentException("File audio tidak ditemukan: " + soundFileName);
            }
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(url);
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
        } catch (Exception e) {
            System.err.println("Gagal memuat file audio: " + soundFileName);
            e.printStackTrace();
        }
    }

    public void play() {
        if (clip != null && volume != Volume.MUTE) {
            if (clip.isRunning()) {
                clip.stop();
            }
            clip.setFramePosition(0); // Kembali ke awal
            clip.start();
        }
    }

    public void loop() {
        if (clip != null && volume != Volume.MUTE) {
            if (clip.isRunning()) {
                clip.stop();
            }
            clip.setFramePosition(0); // Kembali ke awal
            clip.loop(Clip.LOOP_CONTINUOUSLY); // Loop tanpa batas
        }
    }

    public void stop() {
        if (clip != null) {
            clip.stop();
        }
    }

    static void initGame() {
        values(); // Memuat semua elemen enum
    }
}
