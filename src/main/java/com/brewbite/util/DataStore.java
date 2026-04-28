package com.brewbite.util;

import java.io.*;
import java.nio.file.*;

public class DataStore {

    private static final String DATA_FOLDER = "data";

    public static void ensureDataFolderExists() {
        try {
            Files.createDirectories(Paths.get(DATA_FOLDER));
        } catch (IOException e) {
            System.out.println("Could not create data folder.");
        }
    }

    public static void saveText(String fileName, String content) {
        ensureDataFolderExists();

        try {
            Files.writeString(Paths.get(DATA_FOLDER, fileName), content);
        } catch (IOException e) {
            System.out.println("Error saving file: " + fileName);
        }
    }

    public static String loadText(String fileName) {
        ensureDataFolderExists();

        Path path = Paths.get(DATA_FOLDER, fileName);

        if (!Files.exists(path)) {
            return "";
        }

        try {
            return Files.readString(path);
        } catch (IOException e) {
            System.out.println("Error loading file: " + fileName);
            return "";
        }
    }
}
