package com.notjuststudio.utils;

import net.sf.image4j.codec.bmp.BMPDecoder;
import net.sf.image4j.codec.ico.ICODecoder;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Created by George on 27.06.2017.
 */
public class ImageUtils {

    public static BufferedImage arrayToImage(final byte[] source, final int width, final int height) {
        BufferedImage bImageFromConvert = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int i = (x + (width * y)) * 4;
                int r = source[i] & 0xFF;
                int g = source[i + 1] & 0xFF;
                int b = source[i + 2] & 0xFF;
                int a = source[i + 3] & 0xFF;
                bImageFromConvert.setRGB(x, y, (a << 24) | (r << 16) | (g << 8) | b);
            }
        }

        return bImageFromConvert;
    }

    public static byte[] imageToArray(BufferedImage image) {

        final int[] pixels = new int[image.getWidth() * image.getHeight()];
        final byte[] bytes = new byte[image.getWidth() * image.getHeight() * 4];
        image.getRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());

        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                final int pixel = pixels[y * image.getWidth() + x];
                final int i = (x + (image.getWidth() * y)) * 4;
                bytes[i] = (byte) ((pixel >> 16) & 0xFF);     // Red component
                bytes[i + 1] = (byte) ((pixel >> 8) & 0xFF);      // Green component
                bytes[i + 2] = (byte) (pixel & 0xFF);               // Blue component
                bytes[i + 3] = (byte) ((pixel >> 24) & 0xFF);    // Alpha component. Only for RGBA
            }
        }

        return bytes;
    }

    public static BufferedImage bufferToImage(ByteBuffer buffer, int width, int height) {
        BufferedImage bImageFromConvert = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int i = (x + (width * y)) * 4;
                int r = buffer.get(i) & 0xFF;
                int g = buffer.get(i + 1) & 0xFF;
                int b = buffer.get(i + 2) & 0xFF;
                int a = buffer.get(i + 3) & 0xFF;
                bImageFromConvert.setRGB(x, y, (a << 24) | (r << 16) | (g << 8) | b);
            }
        }

        return bImageFromConvert;
    }

    public static ByteBuffer imageToBuffer(BufferedImage image) {

        int[] pixels = new int[image.getWidth() * image.getHeight()];
        image.getRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());

        ByteBuffer buffer = ByteBuffer.allocateDirect(image.getWidth() * image.getHeight() * 4).order(ByteOrder.nativeOrder()); //4 for RGBA, 3 for RGB

        for(int y = 0; y < image.getHeight(); y++){
            for(int x = 0; x < image.getWidth(); x++){
                int pixel = pixels[y * image.getWidth() + x];
                buffer.put((byte) ((pixel >> 16) & 0xFF));     // Red component
                buffer.put((byte) ((pixel >> 8) & 0xFF));      // Green component
                buffer.put((byte) (pixel & 0xFF));               // Blue component
                buffer.put((byte) ((pixel >> 24) & 0xFF));    // Alpha component. Only for RGBA
            }
        }

        buffer.flip();

        return buffer;
    }

    public static BufferedImage resize(BufferedImage img, int newW, int newH) {
        Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
        BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = dimg.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();

        return dimg;
    }

    public static BufferedImage flipImage(BufferedImage bi) {
        BufferedImage flipped = new BufferedImage(
                bi.getWidth(),
                bi.getHeight(),
                bi.getType());
        AffineTransform tran = AffineTransform.getTranslateInstance(0, bi.getHeight());
        AffineTransform flip = AffineTransform.getScaleInstance(1d, -1d);
        tran.concatenate(flip);

        Graphics2D g = flipped.createGraphics();
        g.setTransform(tran);
        g.drawImage(bi, 0, 0, null);
        g.dispose();

        return flipped;
    }

    public static BufferedImage loadImage(File file) {
        BufferedImage image = null;
        if (file.getName().endsWith("png")) {
            try {
                image = ImageIO.read(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (file.getName().endsWith("ico")) {
            try {
                image = ICODecoder.read(file).get(0);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (file.getName().endsWith("bmp")) {
            try {
                image = BMPDecoder.read(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return image;
    }

}
