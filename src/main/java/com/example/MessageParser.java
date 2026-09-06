package com.example;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Classe qui parse un fichier contenant des messages
 * et ne garde que les messages valides selon les critères définis.
 * 
 * Chaque instance a sa propre liste de messages valides.
 */
public class MessageParser {

    // Liste contenant tous les messages valides extraits du fichier
    private List<String> validMessages = new ArrayList<>();

    // Index courant pour parcourir les messages un par un
    private int INITIAL_CURRENT_INDEX = 0;
    private int currentIndex = INITIAL_CURRENT_INDEX;

    // pour se souvenir du fichier utilisé
    private String filename; 

    /**
     * Constructeur public qui charge et parse un fichier
     * au moment de la création de l'instance.
     *
     * @param filename Le chemin vers le fichier contenant les messages.
     */
    public MessageParser(String filename) {
        this.filename = filename;
        parse(filename);
    }

    /**
     * Indique s'il reste des messages valides non encore retournés.
     */
    public boolean hasNextMessage() {
        return currentIndex < validMessages.size();
    }

    /**
     * Renvoie le prochain message valide et avance l'index interne.
     */
    public String getNextMessage() {
        if (hasNextMessage()) {
            return validMessages.get(currentIndex++);
        } else {
            throw new IndexOutOfBoundsException("No more valid messages");
        }
    }

    /**
     * Renvoie la liste complète des messages valides.
     * Retourne une copie pour éviter toute modification externe.
     */
    public List<String> getAllValidMessages() {
        return new ArrayList<>(validMessages);
    }

    /**
     * Supprime un message de la liste.
     * Si la liste devient vide, recharge les messages depuis le fichier.
     */
    public void removeMessage(String message) {
        validMessages.remove(message);
        if (validMessages.isEmpty()) {
            reloadMessages();
        }
    }

    private void reloadMessages() {
        validMessages.clear();
        currentIndex = INITIAL_CURRENT_INDEX;
        parse(filename);
    }

    /**
     * Lit le fichier ligne par ligne et ajoute les messages valides à la liste.
     */
    private void parse(String filename) {
        try (Scanner scanner = new Scanner(new File(filename))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (isValid(line)) {
                    validMessages.add(line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }

    /**
     * Critères de validation d'un message :
     * - Doit contenir au moins une lettre
     * - Ne doit pas contenir de lien commençant par http:// ou https://
     */
    private boolean isValid(String message) {
        if (!message.matches(".*[a-zA-Z].*")) {
            return false;
        }
        if (message.contains("http://") || message.contains("https://")) {
            return false;
        }
        return true;
    }
}
