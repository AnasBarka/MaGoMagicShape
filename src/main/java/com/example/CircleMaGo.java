package com.example;

import java.util.Set;

import javafx.scene.Node;
import javafx.scene.shape.Circle;

public class CircleMaGo extends MaGo {

    private static final double BONUS_PER_PUNCTUATION = 0.05; // +5% par "!" ou "?"
    private static final double PENALTY_NO_PUNCTUATION = 0.10; // -10% s’il n’y en a aucun

    // Liste des symboles positifs (peut être étendue facilement)
    private static final Set<Character> POSITIVE_PUNCTUATIONS = Set.of('!', '?');

    /**
     * Ici on implémente uniquement la logique de calcul de l'impact du message,
     * la méthode analyzeMessage() est gérée par la classe mère.
     */
    @Override
    protected int calculMessageImpact(String message) {
        int count = 0;
        for (char c : message.toCharArray()) {
            if (POSITIVE_PUNCTUATIONS.contains(c)) {
                count++;
            }
        }

        if (count > 0) {
            // Augmente de 5% * nombre d’occurrences
            return (int) (BONUS_PER_PUNCTUATION * valueOfMaxIntervalle * count);
        } else {
            // Diminue de 10%
            return (int) (-PENALTY_NO_PUNCTUATION * valueOfMaxIntervalle);
        }
    }

    @Override
    protected Node createShape(double size) {
        Circle circle = new Circle(size / 2);
        circle.setFill(ColorUtils.getColorForEmotion(emotionalState));
        circle.setStroke(javafx.scene.paint.Color.BLACK); // Bordure noire
        circle.setStrokeWidth(2);
        return circle;
    }
}
