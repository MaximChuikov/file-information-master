package com.example.fileinformation.modules;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@Component
public class ImageModule extends Module {
    @Override
    public boolean isMyExtension(File file) {
        String extension = getExtension(file);
        extension = extension.toLowerCase();
        return extension.equals("jpeg") || extension.equals("jpg") || extension.equals("png");
    }
    
    @Override
    public String functionalDescription() {
        return "size - Вывод разрешения\n" + "exif - Вывод информации exif\n" + "";
    }
    
    @Override
    public void doCommand(String command, File file) {
        switch (command.toLowerCase()) {
            case "size":
                printSize(file);
                break;
            case "exif":
                printExif(file);
                break;
        }
    }
    
    private void printSize(File file) {
        try {
            BufferedImage image = ImageIO.read(file);
            System.out.println("Разрешение: " + image.getWidth() + "x" + image.getHeight() + "px");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    private void printExif(File file) {
        try {
            Metadata metadata = ImageMetadataReader.readMetadata(file);
            
            System.out.println("EXIF: ");
            for (Directory directory : metadata.getDirectories()) {
                for (Tag tag : directory.getTags()) {
                    System.out.println(tag);
                }
            }
        } catch (IOException | ImageProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
