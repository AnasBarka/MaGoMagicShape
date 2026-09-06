package com.example;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.Node;

public abstract class MaGo {
    protected int emotionalState = 0;
    protected int valueOfMaxIntervalle = 100;
    private List<MaGoObserver> observers = new ArrayList<>();

    // Permet à la vue de s'abonner
    public void addObserver(MaGoObserver observer) {
        observers.add(observer);
    }

    // Notification des observateurs avec le MaGo lui-même et son état
    protected void notifyMaGoObservers() {
        for (MaGoObserver observer : observers) {
            observer.update(this, emotionalState);
        }
    }
    public int getValueOfMaxIntervalle() {
        return valueOfMaxIntervalle;
    }

    public int getEmotionState() {
        return emotionalState;
    }
    public void setEmotionalState(int emotionalState) {
        this.emotionalState = emotionalState;
    }
    

    public Node getShape(double size) {
        int min = -valueOfMaxIntervalle;
        int max = valueOfMaxIntervalle;
        ColorUtils.setEmotionRange(min, max);
        return createShape(size);
    }

      /**
     * Méthode finale qui applique l'analyse du message en utilisant
     * la méthode calculMessageImpact implémentée dans les sous-classes.
     */
    public final void analyzeMessage(String message) {
        int delta = calculMessageImpact(message);
        emotionalState += delta;

        // Clamp entre -max et +max
        emotionalState = Math.max(-valueOfMaxIntervalle, Math.min(emotionalState, valueOfMaxIntervalle));

        notifyMaGoObservers();
    }

    /**
     * Méthode à surcharger par les classes filles pour définir
     * la logique de calcul de l'impact d'un message.
     * @param message Le message à analyser
     * @return la variation d'état émotionnel
     */
    protected abstract int calculMessageImpact(String message);

    /**
     * À surcharger : chaque type de MaGo retourne sa forme (Circle, Rectangle, etc.)
     */
    protected abstract Node createShape(double size);
}

