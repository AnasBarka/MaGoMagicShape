package com.example;

import java.util.List;
import java.util.Random;

import javafx.application.Application;
import javafx.stage.Stage;

// Classe principale qui lance l'application JavaFX
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        List<MaGo> maGoList = MaGoFactory.createMaGos(); // création
        MaGoView view = new MaGoView(primaryStage,maGoList); // création de la vue
        
        MaGoController controller = new MaGoController(view, maGoList); // contrôle tout
        
        // 4. Associer le contrôleur à la vue
        view.setController(controller);

        // Chaque MaGo est observé par la vue
        for (MaGo maGo : maGoList) {
            maGo.addObserver(view);
        }
    }


    // Méthode main, point d'entrée du programme Java
    public static void main(String[] args) {
        launch(args); // Démarre l'application JavaFX
    }
}
