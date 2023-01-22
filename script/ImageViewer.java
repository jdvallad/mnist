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

    public ImageViewer(String name) {
        frame = new JFrame(name);
        label = new JLabel();
        frame.setResizable(false);
    }

    public void draw(BufferedImage image, int width, int height) {
        ImageIcon icon = new ImageIcon(image);
        frame.remove(label);
        label = new JLabel(new ImageIcon(icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH)));
        frame.add(label);
        frame.pack();
        frame.validate();
        frame.repaint();
        return;
    }

    public void draw(BufferedImage image, double scale) {
        draw(image, (int) (image.getWidth() * scale), (int) (image.getHeight() * scale));
    }

    public void draw(BufferedImage image) {
        this.draw(image, 1.);
    }

    public void draw(Matrix m, int width, int height) throws Exception {
        draw(ImageViewer.matrixToImage(m), width, height);
        return;
    }

    public void draw(Matrix m, double scale) throws Exception {
        draw(ImageViewer.matrixToImage(m), scale);
    }

    public void draw(Matrix m) throws Exception {
        this.draw(ImageViewer.matrixToImage(m));
    }

    public void show() {
        frame.setVisible(true);
        frame.repaint();
    }

    public void hide() {
        frame.setVisible(false);
        frame.repaint();
    }

    static Matrix getRedMatrix(BufferedImage image) throws Exception {
        Matrix output = Matrix.create(image.getHeight(), image.getWidth());
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                Color color = new Color(image.getRGB(i, j));
                output.set(j, i, color.getRed());
            }
        }
        return output;
    }

    static Matrix getRedMatrix(Matrix combinedMatrix) throws Exception {
        Matrix output = Matrix.create(combinedMatrix.getRows(), combinedMatrix.getColumns() / 3);
        for (int i = 0; i < output.getRows(); i++) {
            for (int j = 0; j < output.getColumns(); j++) {
                output.set(i, j, combinedMatrix.get(i, j));
            }
        }
        return output;
    }

    static Matrix getGreenMatrix(BufferedImage image) throws Exception {
        Matrix output = Matrix.create(image.getHeight(), image.getWidth());
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                Color color = new Color(image.getRGB(i, j));
                output.set(j, i, color.getGreen());
            }
        }
        return output;
    }

    static Matrix getGreenMatrix(Matrix combinedMatrix) throws Exception {
        Matrix output = Matrix.create(combinedMatrix.getRows(), combinedMatrix.getColumns() / 3);
        for (int i = 0; i < output.getRows(); i++) {
            for (int j = 0; j < output.getColumns(); j++) {
                output.set(i, j, combinedMatrix.get(i, output.getColumns() + j));
            }
        }
        return output;
    }

    static Matrix getBlueMatrix(BufferedImage image) throws Exception {
        Matrix output = Matrix.create(image.getHeight(), image.getWidth());
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                Color color = new Color(image.getRGB(i, j));
                output.set(j, i, color.getBlue());
            }
        }
        return output;
    }

    static Matrix getBlueMatrix(Matrix combinedMatrix) throws Exception {
        Matrix output = Matrix.create(combinedMatrix.getRows(), combinedMatrix.getColumns() / 3);
        for (int i = 0; i < output.getRows(); i++) {
            for (int j = 0; j < output.getColumns(); j++) {
                output.set(i, j, combinedMatrix.get(i, (2 * output.getColumns()) + j));
            }
        }
        return output;
    }

    static Matrix getCombinedMatrix(BufferedImage image) throws Exception {
        Matrix red = getRedMatrix(image);
        Matrix blue = getBlueMatrix(image);
        Matrix green = getGreenMatrix(image);
        return combineMatrices(red, green, blue);
    }

    static BufferedImage matrixToImage(Matrix input) throws Exception {
        return matrixToImage(input, input, input);
    }

    static BufferedImage matrixToImage(Matrix redMatrix, Matrix greenMatrix, Matrix blueMatrix) throws Exception {
        BufferedImage output = new BufferedImage(redMatrix.getColumns(), redMatrix.getRows(),
                BufferedImage.TYPE_INT_RGB);
        for (int r = 0; r < redMatrix.getRows(); r++) {
            for (int c = 0; c < redMatrix.getColumns(); c++) {
                int red, green, blue;
                red = (int) redMatrix.get(r, c);
                green = (int) greenMatrix.get(r, c);
                blue = (int) blueMatrix.get(r, c);
                Color color = new Color(red, green, blue);
                output.setRGB(c, r, color.getRGB());
            }
        }
        return output;
    }

    static BufferedImage combinedMatrixToImage(Matrix combinedMatrix) throws Exception {
        Matrix red = getRedMatrix(combinedMatrix);
        Matrix blue = getBlueMatrix(combinedMatrix);
        Matrix green = getGreenMatrix(combinedMatrix);
        return matrixToImage(red, green, blue);
    }

    static Matrix combineMatrices(Matrix redMatrix, Matrix greenMatrix, Matrix blueMatrix) throws Exception {
        Matrix output = Matrix.create(redMatrix.getRows(), 3 * redMatrix.getColumns());
        for (int r = 0; r < redMatrix.getRows(); r++) {
            for (int c = 0; c < redMatrix.getColumns(); c++) {
                output.set(r, c, redMatrix.get(r, c));
                output.set(r, redMatrix.getColumns() + c, greenMatrix.get(r, c));
                output.set(r, (2 * redMatrix.getColumns()) + c, blueMatrix.get(r, c));
            }
        }
        return output;
    }

    static Matrix greyMatrix(Matrix combinedMatrix) throws Exception {
        Matrix red = getRedMatrix(combinedMatrix);
        Matrix blue = getBlueMatrix(combinedMatrix);
        Matrix green = getGreenMatrix(combinedMatrix);
        red.add(blue).add(green);
        red.product(1. / 3.);
        return red;
    }

    static Matrix greyMatrix(BufferedImage image) throws Exception {
        return greyMatrix(getCombinedMatrix(image));
    }

    static BufferedImage pathToImage(String filePath) throws IOException {
        return ImageIO.read(new File(filePath));
    }

    static void imageToPath(BufferedImage image, String filePath) throws IOException {
        ImageIO.write(image, "jpg", new File(filePath));
    }

}
