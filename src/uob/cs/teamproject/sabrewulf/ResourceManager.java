package uob.cs.teamproject.sabrewulf;

import javafx.scene.image.Image;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.text.Font;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * The ResourceManager ensures that when the same resource (image, audio clip, etc.) is used multiple times, only one
 * object (Image, AudioClip, etc.) is created to refer to it. That object is re-used for each re-use of that resource.
 */
public class ResourceManager {

    private static final Map<String, Image> loadedImages;
    private static final Map<String, AudioClip> loadedAudioClips;
    private static final Map<String, Media> loadedMedia;
    private static final Map<String, Font> loadedFonts;

    static {
        loadedImages = new HashMap<>();
        loadedAudioClips = new HashMap<>();
        loadedMedia = new HashMap<>();
        loadedFonts = new HashMap<>();
    }

    /**
     * Get an {@link Image} instance referring to the image at the specified path.
     * @param path the path of the file containing the image to load
     */
    public static Image getImage(String path) {
        if (loadedImages.containsKey(path)) {
            return loadedImages.get(path);
        } else {
            Image newImage = new Image(Paths.get(path).toUri().toString());
            loadedImages.putIfAbsent(path, newImage);
            return newImage;
        }
    }

    /**
     * Get an {@link Image} instance referring to the image at the specified path.
     * This also allows you to provide additional parameters to pass to {@link Image}'s constructor, which are only
     * applied if a new {@link Image} needs to be created.
     * @param path the path of the file containing the image to load
     * @param requestedWidth the 'requestedWidth' parameter of the {@link Image}'s constructor
     * @param requestedHeight the 'requestedHeight' parameter of the {@link Image}'s constructor
     * @param preserveRatio the 'preserveRatio' parameter of the {@link Image}'s constructor
     * @param smooth the 'smooth' parameter of the {@link Image}'s constructor
     */
    public static Image getImage(String path,double requestedWidth, double requestedHeight,
                                 boolean preserveRatio, boolean smooth) {
        if (loadedImages.containsKey(path)) {
            return loadedImages.get(path);
        } else {
            Image newImage = new Image(Paths.get(path).toUri().toString(),
                    requestedWidth, requestedHeight, preserveRatio, smooth);
            loadedImages.putIfAbsent(path, newImage);
            return newImage;
        }
    }

    /**
     * Get an {@link AudioClip} instance referring to the audio clip at the specified path.
     * @param path the path of the file containing the audio clip to load
     */
    public static AudioClip getAudioClip(String path) {
        if (loadedAudioClips.containsKey(path)) {
            return loadedAudioClips.get(path);
        } else {
            AudioClip newAudioClip = new AudioClip(Paths.get(path).toUri().toString());
            loadedAudioClips.putIfAbsent(path, newAudioClip);
            return newAudioClip;
        }
    }

    /**
     * Get an {@link Media} instance referring to the media at the specified path.
     * @param path the path of the file containing the media to load
     */
    public static Media getMedia(String path) {
        if (loadedMedia.containsKey(path)) {
            return loadedMedia.get(path);
        } else {
            Media newMedia = new Media(Paths.get(path).toUri().toString());
            loadedMedia.putIfAbsent(path, newMedia);
            return newMedia;
        }
    }

    /**
     * Get an {@link Font} instance referring to the font at the specified path.
     * @param path the path of the file containing the font to load
     * @param size the size of the font to be displayed
     */
    public static Font getFont(String path, int size) {
        if (loadedFonts.containsKey(path + size)) {
            return loadedFonts.get(path + size);
        } else {
            Font newFont;
            try {
                newFont = Font.loadFont(new FileInputStream(path), size);
            } catch (FileNotFoundException e) {
                newFont = Font.font("Verdana", size);
            }
            loadedFonts.putIfAbsent(path + size, newFont);
            return newFont;
        }
    }
}
