package com.example;

import java.util.*;

public class MaGoFactory {

    private static final int MIN_MAGO_COUNT = 2;
    private static final int MAX_MAGO_COUNT = 8;


    // Méthode pour générer dynamiquement des MaGo
    public static List<MaGo> createMaGos() {
        
        int numberOfMaGos = new Random().nextInt(MAX_MAGO_COUNT - MIN_MAGO_COUNT + 1) + MIN_MAGO_COUNT;// génère entre 2 et 8
        List<MaGo> maGos = new ArrayList<>();
        long timestamp = System.currentTimeMillis() / 1000; // timestamp Unix
        String digits = new StringBuilder(String.valueOf(timestamp)).reverse().toString(); // inversion des chiffres

        for (int i = 0; i < numberOfMaGos; i++) {
            int digit = Character.getNumericValue(digits.charAt(i));
            if (digit % 2 == 0) {
                maGos.add(new CircleMaGo());
            } else {
                maGos.add(new SquareMaGo());
            }
        }

        return maGos;
    }
}