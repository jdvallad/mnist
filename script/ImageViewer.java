import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;
import java.io.File;
import java.io.IOException;

public class ImageViewer {
    private final JFrame frame;
    private JLabel label;

    public ImageViewer(String s) {
        frame = new JFrame(s);
        label = new JLabel();
        frame.add(label);
        frame.setResizable(false);
    }

    public void show(ImageIcon image, int width, int height) {
        frame.remove(label);
        label = new JLabel(new ImageIcon(image.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH)));
        frame.add(label);
        frame.pack();
        frame.validate();
        frame.repaint();
    }

    public void show(String filePath, int width, int height) {
        show(new ImageIcon(filePath), width, height);
    }

    public void show(String filePath) throws IOException {
        show(pathToImage(filePath));
    }

    public void show(BufferedImage image) {
        ImageIcon icon = new ImageIcon(image);
        show(icon, icon.getIconWidth(), icon.getIconHeight());
    }

    public void show(String filePath, double scale) throws IOException {
        show(pathToImage(filePath), scale);
    }

    public void show(BufferedImage image, double scale) {
        ImageIcon icon = new ImageIcon(image);
        show(icon, (int) (icon.getIconWidth() * scale), (int) (icon.getIconHeight() * scale));
    }

    public void setVisible(boolean visible) {
        frame.setVisible(visible);
        frame.repaint();
    }

    static BufferedImage standardize(BufferedImage image) {
        boolean isColor = isColor(image);
        BufferedImage newImage;
        if (isColor) {
            newImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        } else {
            newImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
        }
        Graphics2D g = newImage.createGraphics();
        g.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), null);
        g.dispose();
        return newImage;
    }

    static int[][][] imageToList(BufferedImage img) {
        boolean isColor = isColor(img);
        BufferedImage image = standardize(img);
        final int width = image.getWidth();
        final int height = image.getHeight();
        int[][][] result = new int[width][height][isColor ? 3 : 1];
        if (isColor) {
            final int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
            for (int i = 0, row = 0, col = 0; i < pixels.length; i ++ ) {
                int red = (pixels[i] >> 16) & 0xff;
                int green = (pixels[i] >> 8) & 0xff;
                int blue = pixels[i] & 0xff;
                result[row][col] = new int[] { red, green, blue };
                col++;
                if (col == height) {
                    row++;
                    col = 0;
                }
            }
        } else {
            final byte[] pixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
            for (int i = 0, row = 0, col = 0; i < pixels.length; i++) {
                result[row][col][0] = pixels[i] & 0xff;
                col++;
                if (col == height) {
                    row++;
                    col = 0;
                }
            }
        }
        return result;
    }

    static BufferedImage listToImage(int[][][] dubs) {
        boolean isColor = dubs[0][0].length == 3;
        BufferedImage out;
        if (isColor) {
            out = new BufferedImage(dubs.length, dubs[0].length, BufferedImage.TYPE_INT_RGB);
        } else {
            out = new BufferedImage(dubs.length, dubs[0].length, BufferedImage.TYPE_BYTE_GRAY);
        }
        int[] outPixels = ((DataBufferInt) out.getRaster().getDataBuffer()).getData();
        int red, green, blue;
        for (int r = 0; r < dubs.length; r++) {
            for (int c = 0; c < dubs[0].length; c++) {
                if (isColor) {
                    red = dubs[r][c][0] << 16;
                    green = dubs[r][c][1] << 8;
                    blue = dubs[r][c][2];
                } else {
                    red = dubs[r][c][0] << 16;
                    green = dubs[r][c][0] << 8;
                    blue = dubs[r][c][0];
                }
                outPixels[r * dubs[0].length + c] = red | green | blue;
            }
        }
        return out;
    }

    static BufferedImage listToImage(double[][][] dubs) {
        boolean isColor = dubs[0][0].length == 3;
        BufferedImage out;
        if (isColor) {
            out = new BufferedImage(dubs.length, dubs[0].length, BufferedImage.TYPE_INT_RGB);
        } else {
            out = new BufferedImage(dubs.length, dubs[0].length, BufferedImage.TYPE_BYTE_GRAY);
        }
        int red, green, blue;
        if (isColor) {
            int[] outPixels = ((DataBufferInt) out.getRaster().getDataBuffer()).getData();
            for (int r = 0; r < dubs.length; r++) {
                for (int c = 0; c < dubs[0].length; c++) {
                    red = ((int) dubs[r][c][0]) << 16;
                    green = ((int) dubs[r][c][1]) << 8;
                    blue = ((int) dubs[r][c][2]);
                    outPixels[r * dubs[0].length + c] = red | green | blue;
                }
            }
        } else {
            byte[] outPixels = ((DataBufferByte) out.getRaster().getDataBuffer()).getData();
            for (int r = 0; r < dubs.length; r++) {
                for (int c = 0; c < dubs[0].length; c++) {
                    outPixels[r * dubs[0].length + c] = 
                    (byte)dubs[r][c][0];
                }
            } 
        }
        return out;
    }

    static BufferedImage pathToImage(String filePath) throws IOException {
        return ImageIO.read(new File(filePath));
    }

    static void imageToPath(BufferedImage image, String filePath) throws IOException {
        ImageIO.write(image, "jpg", new File(filePath));
    }

    static boolean isColor(BufferedImage image)
    {
    // Test the type
    if ( image.getType() == BufferedImage.TYPE_BYTE_GRAY ) return false ;
    if ( image.getType() == BufferedImage.TYPE_USHORT_GRAY ) return false ;
    // Test the number of channels / bands
    if ( image.getRaster().getNumBands() == 1 ) return false ; // Single channel => gray scale

    // Multi-channels image; then you have to test the color for each pixel.
    for (int y=0 ; y < image.getHeight() ; y++)
    for (int x=0 ; x < image.getWidth() ; x++)
        for (int c=1 ; c < image.getRaster().getNumBands() ; c++)
            if ( image.getRaster().getSample(x, y, c-1) != image.getRaster().getSample(x, y, c) ) return true ;

    return false ;
    }
}
