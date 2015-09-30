package net.catchpole.B9.devices.imaging;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class Photo {
    private File file;
    private BufferedImage bufferedImage;

    public Photo(File file) {
        this.file = file;
    }

    public Photo(BufferedImage bufferedImage) {
        this.bufferedImage = bufferedImage;
    }

    public BufferedImage getImage() throws IOException {
        return bufferedImage != null ? bufferedImage : ImageIO.read(file);
    }

    public File getFile() {
        return file;
    }

    public byte[] load() throws IOException {
        byte[] data = new byte[(int)file.length()];
        DataInputStream dataInputStream = new DataInputStream(new FileInputStream(file));
        try {
            dataInputStream.readFully(data);
        } finally {
            dataInputStream.close();
        }
        return data;
    }
}