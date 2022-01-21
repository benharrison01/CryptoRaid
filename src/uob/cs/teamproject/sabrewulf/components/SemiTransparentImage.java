package uob.cs.teamproject.sabrewulf.components;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

/**
 * This class converts a player's current sprite into a semi-transparent version (ghost effect)
 * in a form which can be suitably rendered to the game screen.
 */
public class SemiTransparentImage {

    private Image image;

    /**
     * The constructor for this class.
     * @param image: The image to be made semi-transparent.
     */
    public SemiTransparentImage(Image image) {
        this.image = image;
    }

    /**
     * Converts the image to a 2D array of Color pixels.
     * @return the 2D array of Color pixels.
     */
    private Color[][] getPixelData() {
        PixelReader pr = image.getPixelReader();
        int rows = (int) image.getHeight();
        int cols = (int) image.getWidth();

        Color[][] pixelData = new Color[cols][rows];

        for (int i = 0; i < cols; i++) {
            for (int j = 0; j < rows; j++) {
                try {
                    pixelData[i][j] = pr.getColor(i, j);
                }
                catch (Exception e) {
                    pixelData[i][j] = new Color(0,0,0,0);
                }
            }
        }
        return pixelData;
    }

    /**
     * Applies the transparency to the image.
     * @param x: The transparency coefficient where 0 is completely transparent and 1 is opaque.
     * @return the new image with the transparency filter applied.
     */
    public Image applyTransparency(double x) {
        Color[][] pixelArray = getPixelData();
        WritableImage wimg = new WritableImage((int)image.getWidth(), (int)image.getHeight());
        PixelWriter pw = wimg.getPixelWriter();

        for (int i = 0; i < (int)image.getWidth(); i++) {
            for (int j = 0; j < (int)image.getHeight(); j++) {
                try {

                    double red = pixelArray[i][j].getRed();
                    double green = pixelArray[i][j].getGreen();
                    double blue = pixelArray[i][j].getBlue();
                    double opacity = pixelArray[i][j].getOpacity();
                    Color transparentColor = new Color(red, green, blue, Math.min(opacity, x));
                    pw.setColor(i, j, transparentColor);
                }
                catch (Exception ignored) {
                }
            }
        }
        return wimg;
    }
}
