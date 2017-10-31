
package k;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class K {

    //calculate the distance
    public double EulDistance(Color pixelmean, Color pixels) {

        double r_distance = pixelmean.getRed() - pixels.getRed();
        double g_distance = pixelmean.getGreen() - pixels.getGreen();
        double b_distance = pixelmean.getBlue() - pixels.getBlue();

        double sum = (r_distance * r_distance) + (g_distance * g_distance) + (b_distance * b_distance);
        double sqrt = Math.sqrt(sum);

        return sqrt;
    }

    //calculate the updated centers
    public Color getUpdatedPoint(ArrayList<Color> list) {
        if (list.isEmpty()) {
            return new Color(0, 0, 0);
        }
        int r = 0;
        int g = 0;
        int b = 0;
        for (int i = 0; i < list.size(); i++) {
            r = r + list.get(i).getRed();
            g = g + list.get(i).getGreen();
            b = b + list.get(i).getBlue();
        }

        int rNorm = r / list.size();
        int gNorm = g / list.size();
        int bNorm = b / list.size();

        Color c = new Color(rNorm, gNorm, bNorm);
        return c;
    }

    public void kmeans(BufferedImage image, int width, int height) {
        boolean status = true;
        System.out.println("Enter Region to be segmented");
        Scanner sc = new Scanner(System.in);

        int regionTobeSeg = sc.nextInt();

        ArrayList<Color> randomRegion = new ArrayList<Color>();

        for (int i = 0; i < regionTobeSeg; ++i) {
            Random rand = new Random();

            int widthRand = rand.nextInt(width) + 1;
            int heightRand = rand.nextInt(height) + 1;

            randomRegion.add(new Color(image.getRGB(widthRand, heightRand)));
        }

        //set output file
        File f_output = new File("D://oa.jpg");

        ArrayList<ArrayList> points = new ArrayList<ArrayList>();
        ArrayList<ArrayList> pointlist_location_i = new ArrayList<ArrayList>();
        ArrayList<ArrayList> pointlist_location_j = new ArrayList<ArrayList>();

        while (status) {
            for (int x = 0; x < regionTobeSeg; ++x) {
                ArrayList point1list = new ArrayList();
                points.add(point1list);

                ArrayList pointclist_location_i = new ArrayList();
                pointlist_location_i.add(pointclist_location_i);

                ArrayList pointclist_location_j = new ArrayList();
                pointlist_location_j.add(pointclist_location_j);

            }
            //starting from 1 raw end to height-1 raws
            for (int i = 0; i < width - 1; ++i) {

                //starting from 1 column end to width-1 column
                for (int j = 0; j < height - 1; ++j) {
                    Color pixels = new Color(image.getRGB(i, j));

                    double[] EuclDistance = new double[regionTobeSeg];
                    for (int x = 0; x < regionTobeSeg; x++) {
                        double EuclDistancePoint = EulDistance((Color) randomRegion.get(x), pixels);
                        EuclDistance[x] = EuclDistancePoint;
                    }

                    double[] sortEuclDistance = new double[regionTobeSeg];
                    for (int x = 0; x < regionTobeSeg; ++x) {
                        sortEuclDistance[x] = EuclDistance[x];
                    }

                    Arrays.sort(EuclDistance);
                    for (int x = 0; x < regionTobeSeg; ++x) {
                        if (sortEuclDistance[x] == EuclDistance[0]) {
                            points.get(x).add(pixels);
                            pointlist_location_i.get(x).add(i);
                            pointlist_location_j.get(x).add(j);
                            System.out.println(pixels);
                            break;
                        }
                    }
                }
            }
            ArrayList<Color> updated_randomRegion = new ArrayList<Color>();
            for (int x = 0; x < regionTobeSeg; ++x) {
                Color newrandom_1 = getUpdatedPoint(points.get(x));
                updated_randomRegion.add(newrandom_1);
            }

            //find the stabilized centers
            int checker = 0;
            int x = 0;
            for (x = 0; x < regionTobeSeg; ++x) {
                if (updated_randomRegion.get(x).getBlue() == randomRegion.get(x).getBlue()
                        && updated_randomRegion.get(x).getRed() == randomRegion.get(x).getRed()
                        && updated_randomRegion.get(x).getGreen() == randomRegion.get(x).getGreen()) {
                    checker = checker + 1;
                }
            }

            //color the region after finding the stabuliezed center
            if (checker == regionTobeSeg) {
                status = false;

                for (int x1 = 0; x1 < regionTobeSeg; ++x1) {
                    Random rand = new Random();
                    int r = 0;
                    int b = 0;
                    int g = 0;
                    switch (x1 % 10) {
                        case 0:
                            r = 255;
                            break;
                        case 1:
                            g = 255;
                            break;
                        case 2:
                            b = 255;
                            break;
                        case 3:
                            g = 255;
                            r = 100;
                            break;
                        case 4:
                            g = 255;
                            r = 255;
                            break;
                        case 5:
                            g = 100;
                            r = 210;
                            b = 50;
                            break;
                        case 6:
                            r = 100;
                            g = 210;
                            b = 50;
                            break;
                        case 7:
                            r = 10;
                            g = 210;
                            b = 100;
                            break;
                        case 8:
                            r = 100;
                            g = 110;
                            b = 100;
                            break;
                        case 9:
                            r = 220;
                            g = 10;
                            b = 220;
                            break;
                        default:
                            r = rand.nextInt(255) + 0;
                            g = rand.nextInt(255) + 0;
                            b = rand.nextInt(255) + 0;
                    }

                    for (int i = 0; i < pointlist_location_i.get(x1).size(); ++i) {
                        image.setRGB((int) pointlist_location_i.get(x1).get(i),
                                (int) pointlist_location_j.get(x1).get(i), new Color(r, g, b).getRGB());
                    }

                }

                try {
                    ImageIO.write(image, "jpg", f_output);
                } catch (IOException ex) {
                    Logger.getLogger(K.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            randomRegion.clear();
            randomRegion = updated_randomRegion;

            points.removeAll(points);
            pointlist_location_i.removeAll(pointlist_location_i);
            pointlist_location_j.removeAll(pointlist_location_j);
        }
    }

    public void readImage() {
        BufferedImage image = null;
        int width = 0;
        int height = 0;
        int i = 0, j = 0;

        try {
            //read image file
            File f_input = new File("D://baboon.png");
            image = ImageIO.read(f_input);
            width = image.getWidth();
            height = image.getHeight();
            kmeans(image, width, height);
        } catch (Exception e) {
            System.out.println(e);
            System.out.println(width - 1);
            System.out.println(height);
        }
        System.out.println("done");
    }

    public static void main(String[] args) {
        K k = new K();
        k.readImage();
    }
}
