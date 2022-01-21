package uob.cs.teamproject.sabrewulf.audio;

import javafx.scene.media.AudioClip;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import uob.cs.teamproject.sabrewulf.GameSettings;
import uob.cs.teamproject.sabrewulf.ResourceManager;

/** Audio system:
 *  - provides methods for playing background music
 *  - provides methods for playing FX clips
 *  - provides methods for setting music volume
 *  - provides methods for setting FX volume
 */
public class Audio {

    private MediaPlayer mediaPlayer;

    /** Constructor - creates an instance of the audio system and sets the media player up to play the
     *  default background music.
     */
    public Audio() {
        playBackgroundMusic();
    }

    /** Set volume of the background music
     * @param vol - Double value of volume for background music
     */
    public void setVolumeMusic (double vol){
        GameSettings.setMusicVolume(vol);
        try {
            mediaPlayer.setVolume(vol);
        }
        catch (Exception e){
            System.out.println("Error setting volume to " + vol);
        }
    }

    /** Set volume of all FX clips
     * @param vol - Double value of volume for FX clips
     */
    public void setVolumeFX (double vol){
        GameSettings.setFxVolume(vol);
    }

    /** Play 'button clicked' audio
     */
    public void playButtonAudio() {
        this.playClip("audio/button_selected.wav");
    }

    /** Play 'heart' audio when a life is picked up
     */
    public void playHeartAudio() {
        this.playClip("audio/heart.mp3");
    }

    /** Play 'power up' audio when a power up is picked up
     */
    public void playPowerupAudio() {
        this.playClip("audio/powerup.mp3");
    }

    /** Play 'key' audio when a key is picked up
     */
    public void playKeyAudio() {
        this.playClip("audio/key.wav");
    }

    /** Play 'coin' audio when a coin is collected
     */
    public void playCoinAudio() {
        this.playClip("audio/coin.wav");
    }

    /** Play 'hit' audio when a player collides with an AI
     */
    public void playHitAudio() {
        this.playClip("audio/hit.wav");
    }

    /** Play 'detected' audio when a player is detected by an AI
     */
    public void playDetectedAudio() {
        this.playClip("audio/detected.wav");
    }

    /** Play 'invalid' audio clip
     */
    public void playInvalidAudio() {
        this.playClip("audio/invalid.wav");
    }

    /** Play 'speed boost' audio clip when a player gets a speed boost
     */
    public void playGetSpeedBoostAudio() {
        this.playClip("audio/get_speed_boost.wav");
    }

    /** Play 'going invisible' audio clip when a player goes invisible
     */
    public void playGoingInvisibleAudio() {
        this.playClip("audio/going_invisible.mp3");
    }

    /** Play 'going through door' audio
     */
    public void playThroughDoorAudio() {
        this.playClip("audio/open_door.wav");
    }

    /** Play 'game completed successfully' audio
     */
    public void playSuccessAudio() {
        this.playClip("audio/success.wav");
    }

    /** Create an Audio Clip for an FX sound
     * @param s - String containing path for FX audio clip
     */
    private void playClip(String s) {
        try {
            AudioClip clip = ResourceManager.getAudioClip(s);
            clip.play(GameSettings.getFxVolume());
        }
        catch (Exception e){
            e.printStackTrace();
            System.out.println("Error playing clip:  " + s);
        }
    }

    /**
     * Play an audio file as music.
     * @param music: The file location of the music to be played.
     */
    private void playMusic(String music) {

        try{
            mediaPlayer = new MediaPlayer(ResourceManager.getMedia(music));
            mediaPlayer.play();
            mediaPlayer.setVolume(GameSettings.getMusicVolume());
            mediaPlayer.setOnEndOfMedia(() -> {
                mediaPlayer.seek(Duration.ZERO);
                mediaPlayer.play();
            });
        }
        catch (Exception e) {
            System.out.println("Error playing background music");
        }
    }

    /**
     * Play background music.
     */
    private void playBackgroundMusic() {
        this.playMusic("audio/background_music.wav");
    }
}
