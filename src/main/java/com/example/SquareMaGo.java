package com.example;

import javafx.scene.Node;
import javafx.scene.shape.Rectangle;

public class SquareMaGo extends MaGo {

    // Pourcentage d’augmentation ou diminution de l’état émotionnel
    private static final double DELTA_PERCENTAGEVARIATION = 0.10;
    // Seuil du ratio voyelles / consonnes pour juger "positivement"
    private static final double RATIO_PIVOT = 0.89;
    // Liste des voyelles reconnues (y compris 'y')
    private static final String VOWELS = "aeiouy";

    /**
     * Calcule l'impact du message sur l'état émotionnel.
     */
    @Override
    protected int calculMessageImpact(String message) {
        int vowels = 0;
        int consonants = 0;

        // Parcours chaque caractère du message (en minuscules)
        for (char c : message.toLowerCase().toCharArray()) {
            if (Character.isLetter(c)) {
                // Si c’est une lettre, on regarde si c’est une voyelle ou une consonne
                if (VOWELS.indexOf(c) >= 0) vowels++;
                else consonants++;
            }
        }

        // Calcul du ratio voyelles / consonnes (évite division par zéro)
        double ratio = consonants == 0 ? 0 : (double) vowels / consonants;
        int delta = (int) (DELTA_PERCENTAGEVARIATION * valueOfMaxIntervalle);

        // Si le ratio dépasse le seuil, on augmente, sinon on diminue
        return ratio > RATIO_PIVOT ? delta : -delta;
    }

    @Override
    protected Node createShape(double size) {
        Rectangle rect = new Rectangle(size, size);
        rect.setFill(ColorUtils.getColorForEmotion(emotionalState));
        rect.setStroke(javafx.scene.paint.Color.BLACK); // Bordure noire
        rect.setStrokeWidth(2);
        return rect;
    }
}
