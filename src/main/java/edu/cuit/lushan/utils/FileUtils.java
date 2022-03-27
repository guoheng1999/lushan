package edu.cuit.lushan.utils;

import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class FileUtils {
    final List list = Arrays.asList(new String[]{"jpg", "png", "gif", "jpeg"});

    public boolean isPicture(String ext) {
        return list.contains(ext.toLowerCase());
    }
}
