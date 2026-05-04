package com.brewbite.util;

import java.io.IOException;
import java.nio.file.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public class DataStore {

    private static final String DATA_FOLDER =
            System.getProperty("user.home") + "/brewbite-data";


    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static <T> void saveObject(String fileName, T object) {
        saveText(fileName, gson.toJson(object));
    }

    public static <T> T loadObject(String fileName, Class<T> clazz) {
        String json = loadText(fileName);
        if (json.isBlank()) return null;
        return gson.fromJson(json, clazz);
    }

    public static <T> List<T> loadList(String fileName, Type type) {
        String json = loadText(fileName);
        if (json.isBlank()) return List.of();
        return gson.fromJson(json, type);
    }
    
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

        if (!Files.exists(path)) return "";

        try {
            return Files.readString(path);
        } catch (IOException e) {
            return "";
        }
    }
}