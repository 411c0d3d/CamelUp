package com.oasisstudios.camelupserver.dataaccess.jsonstorage;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * The Json storage handler.
 */
@Service
public class JsonStorageHandler {
    public static final ObjectMapper objectMapper = new ObjectMapper();
    public static final Logger logger = LogManager.getLogger(JsonStorageHandler.class);
    /**
     * Save json to file.
     *
     * @param <T>      the type parameter
     * @param object   the object
     * @param filePath the file path
     * @throws IOException the io exception
     */
// Generic method to save any object as JSON file
    public <T> void saveJsonToFile(T object, String filePath) throws IOException {
        File file = new File(filePath);
        file.getParentFile().mkdirs(); // Ensure directories exist
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, object);
    }

    /**
     * Load json file.
     *
     * @param <T>      the type parameter
     * @param basePath the base path
     * @param fileName the file name
     * @param clazz    the clazz
     * @return the t
     */
    public static <T> T load(String basePath, String fileName, Class<T> clazz) {
        File file = new File(basePath, fileName + ".json");
        if (!file.exists()) {
            return null;
        }

        try {
            return objectMapper.readValue(file, clazz);
        } catch (IOException e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    /**
     * Delete json file.
     *
     * @param basePath the base path
     * @param fileName the file name
     * @return the boolean
     */
    public static boolean delete(String basePath, String fileName) {
        File file = new File(basePath, fileName + ".json");
        return file.exists() && file.delete();
    }

    /**
     * Readable timestamp string.
     *
     * @return the string
     */
    public static String readableTimestamp() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
        return LocalDateTime.now().format(formatter);
    }
}