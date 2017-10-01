package cz.rbe.monitor.common;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import java.io.InputStream;

/**
 * Simple audio player.
 * @author Radek Beran
 */
public class AudioPlayer {

    /**
     * Play a WAV file.
     * @param is
     */
    public static void play(InputStream is) {
        try{
            AudioInputStream ais = AudioSystem.getAudioInputStream(is);
            Clip clip = AudioSystem.getClip();
            clip.open(ais);
            clip.start();
            clip.addLineListener(event -> {
                if (event.getType() == LineEvent.Type.STOP) {
                    clip.close();
                }
            });
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }

    private AudioPlayer() {
    }
}
