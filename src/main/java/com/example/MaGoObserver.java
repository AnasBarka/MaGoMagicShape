package com.example;

// Interface que doit implémenter toute classe voulant observer un MaGo
public interface MaGoObserver {
    // Méthode appelée pour informer l'observateur de la nouvelle valeur émotionnelle
    void update(MaGo source, int emotionalState);
}
