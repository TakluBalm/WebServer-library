package server;
import java.nio.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Base64;

public class ImageResponse extends Response {
    public String imagePath = "";
    public String extension = "";
    public ImageResponse(String version, String imagePath) {
        super(version);
        this.imagePath = imagePath;
        String[] splitPath = imagePath.split("\\.");
        this.extension = splitPath[splitPath.length - 1];
        System.out.println(extension);
        this.setHeader("Content-Type", "image/" + extension + "; charset=UTF-8");
        this.setBody(this.imagePath);
    }

    public ImageResponse setBody(String imagePath) {

        byte[] imageData = null;
        try {
            Path path = Paths.get(imagePath);
//            long size = Files.size(path);
            imageData = Files.readAllBytes(path);
        } catch (IOException e) {
            System.out.println(e);
        }
        super.setBody(imageData);
        return this;
    }
}
