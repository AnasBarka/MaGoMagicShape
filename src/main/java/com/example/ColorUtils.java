package com.example;

import javafx.scene.paint.Color;
//base du code faites avec chatgpt + amelioration de ma part
public class ColorUtils {

    private static final int DEFAULT_MIN_EMOTION = -100;
    private static final int DEFAULT_MAX_EMOTION = 100;
    //	Seuil pour décider entre interpolation négative→neutre et neutre→positive
    private static final double INTERPOLATION_MIDPOINT = 0.5;

    private static int minEmotion = DEFAULT_MIN_EMOTION;
    private static int maxEmotion = DEFAULT_MAX_EMOTION;
    
    
    // Couleurs dynamique configurables
    private static Color negativeColor = Color.BLUE; // bleu par défaut
    private static Color positiveColor = Color.RED;  // rouge par défaut
    private static Color neutralColor = Color.WHITE; // couleur centrale

    public static Color getNegativeColor() {
        return negativeColor;
    }
    public static Color getPositiveColor() {
        return positiveColor;
    }
    // Setters pour configurer les couleurs externes
    public static void setNegativeColor(Color color) {
        negativeColor = color;
    }

    public static void setPositiveColor(Color color) {
        positiveColor = color;
    }

    public static void setNeutralColor(Color color) {
        neutralColor = color;
    }
    // Appelée une fois pour fixer l'intervalle
    public static void setEmotionRange(int min, int max) {
        minEmotion = min;
        maxEmotion = max;
    }

    public static Color getColorForEmotion(int emotion) {
        // Clamp de la valeur entre minEmotion et maxEmotion
        emotion = Math.max(minEmotion, Math.min(maxEmotion, emotion));

        // Calcul du ratio (0 = minEmotion, 1 = maxEmotion)
        double t = (double)(emotion - minEmotion) / (maxEmotion - minEmotion);

        if (t < INTERPOLATION_MIDPOINT) {
            // Si dans la première moitié, on interpole entre négatif et neutre
            double factor = t * 2; // Normalisation entre 0 et 1
            return interpolateColor(negativeColor, neutralColor, factor);
        } else {
            // Sinon, interpolation entre neutre et positif
            double factor = (t - INTERPOLATION_MIDPOINT) * 2; // Normalisation entre 0 et 1
            return interpolateColor(neutralColor, positiveColor, factor);
        }
    }

    /**
     * Interpole linéairement entre deux couleurs.
     * Chaque composante RGBA est interpolée séparément selon le facteur.
     * 
     * @param c1 couleur de départ
     * @param c2 couleur d'arrivée
     * @param factor facteur d'interpolation entre 0 et 1
     * @return couleur interpolée
     */
    private static Color interpolateColor(Color c1, Color c2, double factor) {
        double r = c1.getRed() + factor * (c2.getRed() - c1.getRed());
        double g = c1.getGreen() + factor * (c2.getGreen() - c1.getGreen());
        double b = c1.getBlue() + factor * (c2.getBlue() - c1.getBlue());
        double a = c1.getOpacity() + factor * (c2.getOpacity() - c1.getOpacity());
        return new Color(r, g, b, a);
    }

    /**
     * Méthode privée utilitaire pour limiter une valeur entre 0 et 255
     * (inutile ici, remplacée par JavaFX Color)
     */
    // private static int clamp(int value) {
    //     return Math.max(0, Math.min(255, value));
    // }
}
