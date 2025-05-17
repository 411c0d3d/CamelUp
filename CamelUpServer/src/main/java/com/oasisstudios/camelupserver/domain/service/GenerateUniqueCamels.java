package com.oasisstudios.camelupserver.domain.service;

import com.oasisstudios.camelupserver.dataaccess.dto.Camel;

import java.util.*;

public class GenerateUniqueCamels {

    /**
     * Generiert eine Liste von Hex-Farbstrings mit fortlaufenden IDs.
     *
     * @param numberOfColors die Anzahl der zu generierenden Farben
     * @return ein JSON-ähnlicher String, der die Farben und deren IDs darstellt
     * @throws IllegalArgumentException wenn die Anzahl der Farben nicht positiv ist
     */
    public static String generateHexColorsWithSequentialIds(int numberOfColors) {
        if (numberOfColors <= 0) {
            throw new IllegalArgumentException("Number of colors must be positive.");
        }

        StringBuilder output = new StringBuilder();
        output.append("\"camels\": [\n");

        for (int i = 0; i < numberOfColors; i++) {
            // Hue berechnen
            float hue = (float) i / numberOfColors;

            // RGB aus HSL berechnen
            int[] rgb = hslToRgb(hue, 1.0f, 0.5f);

            // RGB in Hex-Farbcode umwandeln
            String hexColor = String.format("#%02x%02x%02x", rgb[0], rgb[1], rgb[2]);

            output.append("  {\n");
            output.append("    \"id\": ").append(i).append(",\n"); // Start ID von 0
            output.append("    \"color\": \"").append(hexColor).append("\"\n");
            output.append("  }");

            // Komma hinzufügen, wenn es nicht das letzte Element ist
            if (i < numberOfColors - 1) {
                output.append(",");
            }

            output.append("\n");
        }

        output.append("]");

        return output.toString();
    }

    /**
     * Konvertiert HSL in RGB.
     *
     * @param hue Farbton (0.0 - 1.0)
     * @param saturation Sättigung (0.0 - 1.0)
     * @param lightness Helligkeit (0.0 - 1.0)
     * @return ein Array mit RGB-Werten [r, g, b] (jeweils 0-255)
     */
    private static int[] hslToRgb(float hue, float saturation, float lightness) {
        float chroma = (1 - Math.abs(2 * lightness - 1)) * saturation;
        float x = chroma * (1 - Math.abs((hue * 6) % 2 - 1));
        float m = lightness - chroma / 2;

        float r = 0, g = 0, b = 0;

        if (hue < 1.0 / 6.0) {
            r = chroma;
            g = x;
            b = 0;
        } else if (hue < 2.0 / 6.0) {
            r = x;
            g = chroma;
            b = 0;
        } else if (hue < 3.0 / 6.0) {
            r = 0;
            g = chroma;
            b = x;
        } else if (hue < 4.0 / 6.0) {
            r = 0;
            g = x;
            b = chroma;
        } else if (hue < 5.0 / 6.0) {
            r = x;
            g = 0;
            b = chroma;
        } else {
            r = chroma;
            g = 0;
            b = x;
        }

        int red = Math.round((r + m) * 255);
        int green = Math.round((g + m) * 255);
        int blue = Math.round((b + m) * 255);

        return new int[]{red, green, blue};
    }

    /**
     * Erstellt eine Menge von CamelDTOs basierend auf der gewünschten Anzahl.
     *
     * @param count Anzahl der zu erzeugenden Camel-Objekte
     * @return eine Menge von CamelDTOs mit jeweils einer eindeutigen ID und Farbe
     */
    public static List<Camel> createCamels(int count) {
        List<Camel> camels = new ArrayList<>() {
        };
        String camelData = GenerateUniqueCamels.generateHexColorsWithSequentialIds(count-2);
        String[] lines = camelData.split("\n");
        int id = -1;
        String color = null;
//        camels.add(Camel.builder().withColor("#000000").withId(-1).build());
//        camels.add(Camel.builder().withColor("#FFFFFF").withId(-2).build());
        camels.add(new Camel(-1,"#000000"));
        camels.add(new Camel(-2,"#FFFFFF"));
        for (String line : lines) {
            line = line.trim();
            if (line.startsWith("\"id\":")) {
                id = Integer.parseInt(line.replaceAll("[^\\d]", ""));
            } else if (line.startsWith("\"color\":")) {
                color = line.split(":")[1].trim().replaceAll("\"", "");
            }

            if (id >= 0 && color != null) {
                camels.add(new Camel(id,color));
                // Reset id and color for the next Camel
                id = -1;
                color = null;
            }
        }

        return camels;
    }
}
