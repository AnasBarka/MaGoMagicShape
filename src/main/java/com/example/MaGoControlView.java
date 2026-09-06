package com.example;

import java.util.function.Consumer;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Fenêtre de contrôle permettant de modifier le statut d'un MaGo.
 * La fenêtre est modale : elle bloque l'interface principale tant qu'elle est ouverte.
 */
public class MaGoControlView {

    /**
     * Affiche la fenêtre de contrôle pour le MaGo donné.
     * 
     * @param magoInstance L’instance du MaGo à modifier.
     */
    //Consumer<T> est une interface fonctionnelle fournie par Java dans le package java.util.function.
    public static void display(MaGo magoInstance, Consumer<Integer> callbackOnValueConfirmed) {
        // Crée une nouvelle fenêtre
        Stage window = new Stage();


        // Rend la fenêtre modale (bloque l’interaction avec la fenêtre principale)
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("MaGo Control Panel");

        // Crée un layout en grille
        GridPane gridLayout = new GridPane();
        gridLayout.setPadding(new Insets(20));
        gridLayout.setVgap(20); // espacement vertical
        gridLayout.setHgap(10); // espacement horizontal

        // ----------- Partie 1 : Affichage du statut actuel -----------

        Label currentStatusLabel = new Label("Actual Status:");
        Label currentStatusValue = new Label(String.valueOf(magoInstance.getEmotionState()));

        // ----------- Partie 2 : Saisie de la nouvelle valeur -----------

        Label inputPromptLabel = new Label("New Value in [-"+magoInstance.getValueOfMaxIntervalle()+" : "+magoInstance.getValueOfMaxIntervalle()+"]:");
        TextField newStatusInputField = new TextField();
        newStatusInputField.setPromptText("Enter a value"); // texte d’aide

        // ----------- Partie 3 : Bouton de confirmation -----------

        Button confirmButton = new Button("Confirm New Value");
        confirmButton.setDisable(true); // désactivé par défaut

        // Activation du bouton uniquement si la saisie est un entier valide dans [-100 ; 100]
        newStatusInputField.textProperty().addListener((observable, oldText, newText) -> {
            try {
                int enteredValue = Integer.parseInt(newText.trim());
                boolean isValid = (enteredValue >= -magoInstance.getValueOfMaxIntervalle() && enteredValue <= magoInstance.getValueOfMaxIntervalle());
                confirmButton.setDisable(!isValid);
            } catch (NumberFormatException e) {
                confirmButton.setDisable(true); // désactive si pas un nombre entier
            }
        });

        // Action lorsque l’utilisateur clique sur "Confirm New Value"
        confirmButton.setOnAction(event -> {
            int newStatus = Integer.parseInt(newStatusInputField.getText().trim());
            magoInstance.setEmotionalState(newStatus); // met à jour le statut du MaGo
            //le callback
            callbackOnValueConfirmed.accept(newStatus);
            window.close(); // ferme la fenêtre
        });

        // ----------- Placement des éléments dans la grille -----------

        gridLayout.add(currentStatusLabel, 0, 0);
        gridLayout.add(currentStatusValue, 1, 0);

        gridLayout.add(inputPromptLabel, 0, 1);
        gridLayout.add(newStatusInputField, 1, 1);

        gridLayout.add(confirmButton, 1, 2); // bouton en bas à droite

        // ----------- Création et affichage de la scène -----------

        Scene scene = new Scene(gridLayout, 400, 150);
        window.setScene(scene);
        window.showAndWait(); // bloquant (modale)
    }
}
