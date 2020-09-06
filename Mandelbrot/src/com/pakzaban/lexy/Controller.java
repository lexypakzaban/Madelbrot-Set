package com.pakzaban.lexy;

import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;


public class Controller {
    public Pane graphPane;
    public AnchorPane anchorPane;
    public ImageView imageView;
    private double xLowerLimit = -2.0;
    private double xUpperLimit = 2.0;
    private double yLowerLimit = -2.0;
    private double yUpperLimit = 2.0;
    private MandelCoordinate[][] mandelCoordinates;

    public void initialize(){
        graphPane.getChildren().clear();
        graphPane.getChildren().add(imageView);
        int width = 600;
        int height = 600;
        int maxIterations = 500;

        mandelCoordinates = new MandelCoordinate[width][height];

        WritableImage wImage = new WritableImage(width,height);
        PixelWriter pixelWriter = wImage.getPixelWriter();

        String colorString = "RED,YELLOW,PURPLE,BLUE,GOLDENROD,PINK,violet,salmon,magenta,hotpink,turquoise,skyblue";
        String[] colorArray = colorString.split(",");



        for(int x = 0; x < width; x++){
            for(int y = 0; y < height; y++){

                double a = xLowerLimit + ((x * (xUpperLimit - xLowerLimit)) / width);
                double b = yLowerLimit + ((y * (yUpperLimit - yLowerLimit)) / height);
                double A = a;
                double B = b;
                MandelCoordinate m = new MandelCoordinate(a,b);
                mandelCoordinates[x][y] = m;
                int n = 0;
                while (n < maxIterations){
                    double newA = Math.pow(a,2) - Math.pow(b,2) + A;
                    double newB = (2 * a * b) + B;
                    a = newA;
                    b = newB;

                    if (Math.abs(a+b) > 4){
                        break;
                    }
                    n++;
                }
                if(n == maxIterations){
                    pixelWriter.setColor(x,y,Color.BLACK);
                }

                else if (n >= 8 && n < maxIterations){
                    int colorIndex = n % (colorArray.length - 1);
                    Color color = Color.valueOf(colorArray[colorIndex]);
                    pixelWriter.setColor(x,y,color);
                }

                else if (n < 8){
                    pixelWriter.setColor(x,y,Color.BLACK);
                }
            }
        }

        imageView.setImage(wImage);

    }


    public void mouseUsed(MouseEvent me){
        double xBorder = (anchorPane.getWidth() - graphPane.getWidth()) /2;
        double yBorder = (anchorPane.getHeight() - graphPane.getHeight()) /2;
        double x = me.getSceneX() - xBorder;
        double y = me.getSceneY() - yBorder;
        System.out.println(x + ", " + y);
        int xStart = (int) x - 10;
        int yStart = (int) y - 10;
        int xEnd = (int) x + 10;
        int yEnd = (int) y + 10;

        if(me.getEventType().equals(MouseEvent.MOUSE_PRESSED)) {
            Polygon p = new Polygon(xStart, yStart, xEnd, yStart, xEnd, yEnd, xStart, yEnd);
            p.setStyle("-fx-fill:none;-fx-stroke:white");
            graphPane.getChildren().add(p);
        }

        else if (me.getEventType().equals(MouseEvent.MOUSE_RELEASED)){
            xLowerLimit = mandelCoordinates[xStart][yStart].getA();
            xUpperLimit = mandelCoordinates[xEnd][yEnd].getA();
            yLowerLimit = mandelCoordinates[xStart][yStart].getB();
            yUpperLimit = mandelCoordinates[xEnd][yEnd].getB();
            initialize();
        }
    }

    public void resetButtonPressed(){
        xLowerLimit = -2.0;
        xUpperLimit = 2.0;
        yLowerLimit = -2.0;
        yUpperLimit = 2.0;
        initialize();
    }

}
