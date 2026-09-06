package com.example;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.geometry.Pos;

// Classe représentant la vue principale de l'application
// Elle implémente l'interface MaGoObserver pour réagir aux changements de MaGo
public class MaGoView implements MaGoObserver {

    private Stage stage; // Fenêtre principale
    private MaGoController controller;
    private Label averageEmotionLabel; // Affiche la moyenne des émotions
    public TextArea messageArea; // Zone de texte pour écrire les messages
    public Button enterButton;   // Bouton pour envoyer
    private Label countdownLabel; // Affiche le compte à rebours
    
    public RadioButton radioRandom;
    public RadioButton radioGoCircle;
    public RadioButton radioGoSquare;
    public RadioButton radioBestRandomType;
    
    private HBox emotionContainer; // Conteneur visible pour les labels MaGo
    private VBox root; // Conteneur principal de la scène

    // Associe chaque MaGo à un label qui affichera son état émotionnel
    private Map<MaGo, Label> maGoLabels = new HashMap<>();
    private List<MaGo> maGos; // liste des MaGo fournie à l'avance

    // Constructeur de la vue
    public MaGoView(Stage stage, List<MaGo> maGos) {
        this.stage = stage;
        this.maGos = maGos;
        initializeUI(); // Initialise l'interface
    }

    // Méthode privée pour construire l'interface graphique
    private void initializeUI() {
        // ---------------------- HEADER ----------------------
      // Création de la barre du haut avec une moyenne à gauche et couleurs à droite
        BorderPane header = new BorderPane();
        header.setPadding(new Insets(10));

        // Moyenne à gauche
        // Label technique juste pour contenir le cercle dynamique
        averageEmotionLabel = new Label();
        averageEmotionLabel.setGraphic(createAverageCircle(60, 0)); // Taille 60, valeur initiale 0

        VBox avgBox = new VBox(averageEmotionLabel);
        avgBox.setPadding(new Insets(5));
        avgBox.setStyle("-fx-border-color: black; -fx-border-radius: 10; -fx-background-radius: 10;");
        avgBox.setAlignment(Pos.CENTER_LEFT);
        header.setLeft(avgBox);


        // Couleurs à droite dans un HBox
        VBox negativeBox = createColorBox(ColorUtils.getNegativeColor(), "Négatif Color");
        VBox positiveBox = createColorBox(ColorUtils.getPositiveColor(), "Positif Color");

        HBox colorBoxes = new HBox(10, negativeBox, positiveBox);
        colorBoxes.setAlignment(Pos.CENTER_RIGHT);
        header.setRight(colorBoxes);

        // ---------------------- ZONE MESSAGE / COMPTEUR / RADIO ----------------------

        // Zone de saisie avec bordures arrondies
        messageArea = new TextArea();
        messageArea.setWrapText(true);
        messageArea.setPrefRowCount(3);
        messageArea.setStyle("-fx-border-color: black; -fx-border-radius: 10; -fx-background-radius: 10;");
        messageArea.setPrefWidth(400);
        messageArea.setMaxWidth(400);

        // Compteur (compte à rebours)
        countdownLabel = new Label(MaGoController.getInitialCountdownSeconds());
        VBox countdownBox = new VBox(countdownLabel);
        countdownBox.setPadding(new Insets(5));
        countdownBox.setStyle("-fx-border-color: black; -fx-border-radius: 10; -fx-background-radius: 10;");
        countdownBox.setMinWidth(100);
        countdownBox.setMinHeight(50);
        countdownBox.setAlignment(Pos.CENTER);

        // Bouton Enter (encadré)
        enterButton = new Button("→");
        VBox enterBox = new VBox(enterButton);
        enterBox.setPadding(new Insets(5));
        enterBox.setStyle("-fx-border-color: black; -fx-border-radius: 10; -fx-background-radius: 10;");
        enterBox.setMinWidth(100);
        enterBox.setMinHeight(50);
        enterBox.setAlignment(Pos.CENTER);

        // Radio boutons
        radioRandom = new RadioButton("Random");
        radioGoCircle = new RadioButton("GoCircle");
        radioGoSquare = new RadioButton("GoSquare");
        radioBestRandomType = new RadioButton("GoAny");
       
        ToggleGroup radioGroup = new ToggleGroup(); // Pour sélection unique
        radioRandom.setToggleGroup(radioGroup);
        radioGoCircle.setToggleGroup(radioGroup);
        radioGoSquare.setToggleGroup(radioGroup);
        radioBestRandomType.setToggleGroup(radioGroup);

        // Sélection par défaut :
        radioRandom.setSelected(true);

        VBox radioBox = new VBox(10, radioRandom, radioGoCircle, radioGoSquare, radioBestRandomType);
        radioBox.setPadding(new Insets(10));
        radioBox.setStyle("-fx-border-color: black; -fx-border-radius: 10; -fx-background-radius: 10;");
        radioBox.setMinWidth(100);
        radioBox.setMinHeight(120);

        // Conteneur vertical du compteur + bouton
        VBox btnCmptBox = new VBox(30);
        btnCmptBox.getChildren().addAll(countdownBox, enterBox);

        // Conteneur principal centré horizontalement
        HBox centerBox = new HBox(10);
        centerBox.setPadding(new Insets(10));
        centerBox.setAlignment(Pos.CENTER);
        centerBox.getChildren().addAll(messageArea, btnCmptBox, radioBox);

        // ---------------------- FOOTER ----------------------

        // Ligne où seront ajoutés les labels des MaGo observés
        emotionContainer = new HBox(20); // au lieu de: HBox emotionContainer = new HBox(20);
        emotionContainer.setAlignment(Pos.CENTER);
        emotionContainer.setPadding(new Insets(10));
        emotionContainer.setStyle("-fx-border-color: black; -fx-border-radius: 10;");
        
        for (MaGo maGo : maGos) {
            // Création du label SANS Platform.runLater
            Label label = new Label();
            label.setStyle("-fx-border-color: black; -fx-border-radius: 10; -fx-background-radius: 10;");
            label.setAlignment(Pos.CENTER);
            label.prefHeightProperty().bind(label.widthProperty()); // carré
            label.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            HBox.setHgrow(label, Priority.ALWAYS);

            // Lier le MaGo au clic
            label.setCursor(Cursor.HAND);
            label.setOnMouseClicked(event -> {
                controller.onMaGoClicked(maGo); // appelle une méthode du contrôleur
            });
            
            emotionContainer.getChildren().add(label);
            maGoLabels.put(maGo, label);

            

        }

        // Initialisation des labels des MaGo fournis
        for (MaGo maGo : maGos) {
            update(maGo, maGo.getEmotionState());
        }


        // Conteneur global vertical (header + centre + footer)
        VBox topContainer = new VBox(10, header, centerBox, emotionContainer);
        topContainer.setPadding(new Insets(10));
        topContainer.setAlignment(Pos.CENTER);

        // Affecte la racine
        root = new VBox(topContainer);

        // Création de la scène
        Scene scene = new Scene(root, 700, 470);
        stage.setScene(scene);
        stage.setResizable(false);// empêche le redimensionnement
        stage.centerOnScreen();// centre à l’écran
        stage.setTitle("MaGo System");
        stage.show();
    }

    // Mise à jour du compte à rebours (depuis un autre thread si nécessaire)
    public void updateCountdownLabel(int seconds) {
        Platform.runLater(() -> countdownLabel.setText(String.valueOf(seconds)));
    }

    // Mise à jour graphique de l'état émotionnel d'un MaGo
    @Override
    public void update(MaGo source, int emotionalState) {
        Platform.runLater(() -> {
            // Récupère (ou crée) le label du MaGo
            Label label = maGoLabels.get(source);
            if (label == null) {
                label = new Label();
                
                //label.setPadding(new Insets(5));
                label.setStyle("-fx-border-color: black; -fx-border-radius: 10; -fx-background-radius: 10;");
                label.setAlignment(Pos.CENTER);
                //label.setPrefWidth(100); // largeur initiale
                label.prefHeightProperty().bind(label.widthProperty()); //  liaison : hauteur = largeur
                label.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE); //  permet l'agrandissement
                
                HBox.setHgrow(label, Priority.ALWAYS);
                VBox.setVgrow(label, Priority.ALWAYS);

                maGoLabels.put(source, label);
                emotionContainer.getChildren().add(label);
            }

            // Affiche la classe et l'état émotionnel
            // Remplace le contenu graphique du label par la forme
            // Utilisation de la fonction dédiée
            updateLabelGraphic(source, label);

            // Mise à jour de la moyenne via le contrôleur
            double average = controller.calculateAverageEmotion(); // <-- appel au contrôleur
            averageEmotionLabel.setGraphic(createAverageCircle(30, average));
        });
    }

    // Crée un cercle coloré avec le texte "Average Status" a droite
    private Node createAverageCircle(double size, double average) {
         // Cercle avec couleur selon la moyenne
        Circle circle = new Circle(size / 2);
        circle.setFill(ColorUtils.getColorForEmotion((int) average));
        circle.setStroke(Color.BLACK);
        circle.setStrokeWidth(2);

        // Texte à gauche
        Label text = new Label("Average Status");
        text.setStyle("-fx-text-fill: black; -fx-font-size: 14; -fx-font-weight: bold;");
        text.setAlignment(Pos.CENTER_LEFT);

        // Conteneur horizontal pour texte + cercle
        HBox hbox = new HBox(10); // espace entre texte et cercle
        hbox.setAlignment(Pos.CENTER_LEFT);
        hbox.getChildren().addAll(circle, text);

        return hbox;
    }
    // Crée un VBox contenant un carré coloré + texte, avec le style voulu
    // Pour les labeles positive et negatif color
    private VBox createColorBox(Color color, String labelText) {
        // Carré coloré
        Region square = new Region();
        square.setPrefSize(30, 30);
        square.setMinSize(30, 30);
        square.setMaxSize(30, 30);
        square.setBackground(new Background(new BackgroundFill(color, new CornerRadii(5), Insets.EMPTY))); // coins légèrement arrondis
        square.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, new CornerRadii(5), new BorderWidths(2))));

        // Texte à droite du carré (dans un HBox)
        Label label = new Label(labelText);
        label.setStyle("-fx-font-weight: bold; -fx-font-size: 14;");
        label.setAlignment(Pos.CENTER_LEFT);

        HBox hbox = new HBox(10, square, label);
        hbox.setAlignment(Pos.CENTER_LEFT);

        // VBox avec padding, bordure et arrondi (comme avant)
        VBox box = new VBox(hbox);
        box.setPadding(new Insets(5));
        box.setStyle("-fx-border-color: black; -fx-border-radius: 10; -fx-background-radius: 10;");
        box.setAlignment(Pos.CENTER);

        return box;
    }


    // Calcule et applique dynamiquement la taille du MaGo en fonction de la largeur du container
    private void updateLabelGraphic(MaGo maGo, Label label) {
        double containerWidth = emotionContainer.getWidth();
        int count = maGoLabels.size();
        double padding = 20; // correspond au spacing défini dans emotionContainer
        double size = (containerWidth - padding * (count - 1)) / count;

        if (size < 10){
            size = 10;
        }
        else{
            // Réduction visuelle à ~80% pour qu'ils ne remplissent pas toute la cellule
            size = size * 0.6;
        }

        label.setGraphic(maGo.getShape(size));
    }


    public void setController(MaGoController controller) {
        this.controller = controller;
    }
}
