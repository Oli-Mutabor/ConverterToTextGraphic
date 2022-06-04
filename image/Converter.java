package ru.netology.graphics.image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.net.URL;

public class Converter implements TextGraphicsConverter {
    private int height;
    private int width;
    private double maxRatio;
    private TextColorSchema schema = new ColorSchema();

    @Override
    public String convert(String url) throws IOException, BadImageSizeException {
        BufferedImage img = ImageIO.read(new URL(url));
        int imageWidth = img.getWidth();
        int imageHeight = img.getHeight();
        double ratio = (double) imageWidth / (double) imageHeight;

        if (maxRatio != 0 && ratio > maxRatio) {
            throw new BadImageSizeException(ratio, maxRatio);
        }

        int newWidth = imageWidth;
        int newHeight = imageHeight;

        if (width != 0 && imageWidth > width) {
            double quotient = (double) imageWidth / (double) width;
            double doubleNewWidth = (double) imageWidth / quotient;
            double doubleNewHeight = (double) imageHeight / quotient;
            newWidth = (int) doubleNewWidth;
            newHeight = (int) doubleNewHeight;
        }

        if (height != 0 && imageHeight > height) {
            double quotient = (double) imageHeight / (double) height;
            double doubleNewWidth = (double) imageWidth / quotient;
            double doubleNewHeight = (double) imageHeight / quotient;
            newWidth = (int) doubleNewWidth;
            newHeight = (int) doubleNewHeight;
        }

        Image scaledImage = img.getScaledInstance(newWidth, newHeight, BufferedImage.SCALE_SMOOTH);
        BufferedImage bwImg = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_BYTE_GRAY);
        Graphics2D graphics = bwImg.createGraphics();
        graphics.drawImage(scaledImage, 0, 0, null);
        WritableRaster bwRaster = bwImg.getRaster();

        StringBuilder textImg = new StringBuilder();
        for (int h = 0; h < newHeight; h++) {
            for (int w = 0; w < newWidth; w++) {
                int color = bwRaster.getPixel(w, h, new int[3])[0];
                char c = schema.convert(color);
                textImg.append(c);
                textImg.append(c);
            }
            textImg.append("\n");
        }

        return textImg.toString();
    }

    @Override
    public void setMaxWidth(int width) {
        this.width = width;
    }

    @Override
    public void setMaxHeight(int height) {
        this.height = height;
    }

    @Override
    public void setMaxRatio(double maxRatio) {
        this.maxRatio = maxRatio;
    }

    @Override
    public void setTextColorSchema(TextColorSchema schema) {
        this.schema = schema;
    }
}
