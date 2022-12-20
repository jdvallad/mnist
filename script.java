import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;
import java.awt.Graphics2D;

public class script {
    public static void main(String[] args) throws IOException {
        System.out.println("Starting serialization...");
        String filePath = "../";
        String[] types = new String[] { "testing/", "training/", "validation/" };
        int[] labels = new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
        File[] files;
        double[][][] dataPairs;
        List<double[][]> data = new ArrayList<>();
        for (String t : types) {
            for (int l : labels) {
                files = new File(filePath + t + l + "/").listFiles();
                for (int i = 0; i < files.length; i++) {
                    BufferedImage image = pathToImage(filePath + t + l + "/" + files[i].getName());
                    double[] imageList = scale(flatten(imageToList(image)), 1. / 255.);
                    double[] imageLabel = new double[10];
                    for (int r = 0; r < 10; r++) {
                        imageLabel[r] = 0.;
                    }
                    imageLabel[l] = 1.;
                    double[][] temp = new double[][] { imageList, imageLabel };
                    data.add(temp);
                }
            }
            Collections.shuffle(data);
            dataPairs = new double[2][][];
            dataPairs[0] = new double[data.size()][];
            dataPairs[1] = new double[data.size()][];
            for (int i = 0; i < data.size(); i++) {
                dataPairs[0][i] = data.get(i)[0].clone();
                dataPairs[1][i] = data.get(i)[1].clone();
            }
            data.clear();
            FileOutputStream fileOut = new FileOutputStream(filePath + t + "data.ser");
            System.out.println("Created file at " + filePath + t + "data.ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(dataPairs);
            out.close();
            fileOut.close();
        }
        return;
    }

    static double[] flatten(int[][][] input) {
        double[] output = new double[input.length * input[0].length * input[0][0].length];
        for (int r = 0, index = 0; r < input.length; r++)
            for (int c = 0; c < input[0].length; c++)
                for (int k = 0; k < input[0][0].length; k++)
                    output[index++] = input[r][c][k];
        return output;
    }

    static double[] scale(double[] input, double scale) {
        double[] res = new double[input.length];
        for (int i = 0; i < res.length; i++) {
            res[i] = scale * input[i];
        }
        return res;
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
            for (int i = 0, row = 0, col = 0; i < pixels.length; i++) {
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

    static BufferedImage pathToImage(String filePath) throws IOException {
        return ImageIO.read(new File(filePath));
    }

    static boolean isColor(BufferedImage image) {
        // Test the type
        if (image.getType() == BufferedImage.TYPE_BYTE_GRAY)
            return false;
        if (image.getType() == BufferedImage.TYPE_USHORT_GRAY)
            return false;
        // Test the number of channels / bands
        if (image.getRaster().getNumBands() == 1)
            return false; // Single channel => gray scale

        // Multi-channels image; then you have to test the color for each pixel.
        for (int y = 0; y < image.getHeight(); y++)
            for (int x = 0; x < image.getWidth(); x++)
                for (int c = 1; c < image.getRaster().getNumBands(); c++)
                    if (image.getRaster().getSample(x, y, c - 1) != image.getRaster().getSample(x, y, c))
                        return true;

        return false;

    }
}