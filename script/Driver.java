import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Driver {

    public static void main(String[] args) throws IOException {
        String filePath = "../mnist/";
        String[] types = new String[] { "testing/", "training/", "validation/" };
        int[] labels = new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
        File[] files;
        double[][][] dataPairs;
        List<DataPair> data = new ArrayList<>();
        for (String t : types) {
            for (int l : labels) {
                files = new File(filePath + t + l + "/").listFiles();
                for (int i = 0; i < files.length; i++) {
                    BufferedImage image = ImageViewer.pathToImage(filePath + t + l + "/" + files[i].getName());
                    double[] imageList = Functions.scale(Functions.flatten(ImageViewer.imageToList(image)), 1. / 255.);
                    double[] imageLabel = new double[10];
                    for (int r = 0; r < 10; r++) {
                        imageLabel[r] = 0.;
                    }
                    imageLabel[l] = 1.;
                    DataPair temp = new DataPair(imageList, imageLabel);
                    data.add(temp);
                }
            }
            Collections.shuffle(data);
            dataPairs = new double[2][][];
            dataPairs[0] = new double[data.size()][];
            dataPairs[1] = new double[data.size()][];
            for (int i = 0; i < data.size(); i++) {
                dataPairs[0][i] = data.get(i).data.clone();
                dataPairs[1][i] = data.get(i).label.clone();
            }
            data.clear();
            FileOutputStream fileOut = new FileOutputStream(filePath + t + "data.ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(dataPairs);
            out.close();
            fileOut.close();

        }
    }
}