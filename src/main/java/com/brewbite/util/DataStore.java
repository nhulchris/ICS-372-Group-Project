package com.brewbite.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * Simple JSON-based persistence helper.
 * Saves data to ~/brewbite-data/ so application state survives
 * across runs even when launched from a JAR.
 */
public class DataStore {

    private static final String DATA_FOLDER =
            System.getProperty("user.home") + "/brewbite-data";

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    // ---- Object ----
    public static <T> void saveObject(String fileName, T object) {
        saveText(fileName, gson.toJson(object));
    }

    public static <T> T loadObject(String fileName, Class<T> clazz) {
        String json = loadText(fileName);
        if (json.isBlank()) return null;
        try {
            return gson.fromJson(json, clazz);
        } catch (Exception e) {
            System.err.println("Failed to parse " + fileName + ": " + e.getMessage());
            return null;
        }
    }

    /**
     * Type-based overload for deserializing generic types like
     * Map&lt;String, Ingredient&gt; that can't be expressed with a Class.
     */
    public static <T> T loadObject(String fileName, Type type) {
        String json = loadText(fileName);
        if (json.isBlank()) return null;
        try {
            return gson.fromJson(json, type);
        } catch (Exception e) {
            System.err.println("Failed to parse " + fileName + ": " + e.getMessage());
            return null;
        }
    }

    // ---- List ----
    public static <T> List<T> loadList(String fileName, Type type) {
        String json = loadText(fileName);
        if (json.isBlank()) return null;
        try {
            return gson.fromJson(json, type);
        } catch (Exception e) {
            System.err.println("Failed to parse " + fileName + ": " + e.getMessage());
            return null;
        }
    }

    // ---- Raw text ----
    public static void ensureDataFolderExists() {
        try {
            Files.createDirectories(Paths.get(DATA_FOLDER));
        } catch (IOException e) {
            System.err.println("Could not create data folder: " + e.getMessage());
        }
    }

    public static void saveText(String fileName, String content) {
        ensureDataFolderExists();
        try {
            Files.writeString(Paths.get(DATA_FOLDER, fileName), content);
        } catch (IOException e) {
            System.err.println("Error saving file " + fileName + ": " + e.getMessage());
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
