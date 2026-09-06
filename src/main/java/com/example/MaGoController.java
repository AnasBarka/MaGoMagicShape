package com.example;

import java.util.List;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;

public class MaGoController {
    private List<MaGo> maGoList; // liste des MaGos
    private MaGoView view;
    private Timeline countdownTimeline;
    private static final int INITIAL_COUNTDOWN_SECONDS = 5;
    

    private int countdownSeconds = INITIAL_COUNTDOWN_SECONDS;
    
    private MessageParser parserMessage;
    private final String messageDataFilename =  "src/main/java/com/example/mess.msg";
    
    
    public MaGoController(MaGoView view, List<MaGo> maGoList) {
        this.view = view;
        this.maGoList = maGoList;

        this.parserMessage = new MessageParser(messageDataFilename);

        setActions();
        startCountdown();
        updateMessageAreaBasedOnStrategy(); 


    }

    // Lance un compte à rebours de 10s
    private void startCountdown() {
        countdownTimeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            countdownSeconds--;
            view.updateCountdownLabel(countdownSeconds);
            if (countdownSeconds <= 0) {
                handleMessage(); // si temps écoulé, traiter le message
            }
        }));
        countdownTimeline.setCycleCount(Timeline.INDEFINITE);
        countdownTimeline.play();
    }

    // Réinitialise le chrono
    private void resetCountdown() {
        countdownSeconds = INITIAL_COUNTDOWN_SECONDS;
        view.updateCountdownLabel(countdownSeconds);
    }

    // Associe le clic sur le bouton à la méthode de traitement
    private void setActions() {
        view.enterButton.setOnAction(e -> handleMessage());
    }

    // Envoie le message à tous les MaGos
    private void handleMessage() {
        String message = view.messageArea.getText();
        if (message != null && !message.isEmpty()) {
            for (MaGo maGo : maGoList) {
                maGo.analyzeMessage(message); // chaque MaGo traite
            }
            view.messageArea.clear(); // nettoyage zone de texte
            parserMessage.removeMessage(message); // on supprime après affichage
        }
        resetCountdown();
        // ← ici, pour rafraîchir la zone texte
        updateMessageAreaBasedOnStrategy(); 
    }

    private void updateMessageAreaBasedOnStrategy() {
        MessageStrategySelector.MessageSelectionStrategy strategy = new MessageStrategySelector.RandomStrategy();
        if (view.radioRandom.isSelected()) {
            strategy = new MessageStrategySelector.RandomStrategy();
        } else if (view.radioGoCircle.isSelected()) {
            strategy = new MessageStrategySelector.BestForTypeStrategy(CircleMaGo.class);
        } else if (view.radioGoSquare.isSelected()) {
            strategy = new MessageStrategySelector.BestForTypeStrategy(SquareMaGo.class);
        } else if (view.radioBestRandomType.isSelected()) {
            strategy = new MessageStrategySelector.BestForRandomTypeStrategy();
        } 
        
    
        List<String> messages = parserMessage.getAllValidMessages();
        String message = strategy.selectMessage(messages, maGoList);
        if (message != null) {
            view.messageArea.setText(message);
        }
        
    }

    public double calculateAverageEmotion() {
        double sum = 0;
        for (MaGo maGo : maGoList) {
            sum += maGo.getEmotionState();
        }
        return maGoList.isEmpty() ? 0 : sum / maGoList.size();
    }

    public void onMaGoClicked(MaGo maGo) {
        // ❄️ Met en pause le timer ou d'autres threads
        if (countdownTimeline != null) {
            countdownTimeline.pause();
        }
        System.out.println("MaGo cliqué : " + maGo + ", État émotionnel : " + maGo.getEmotionState());
    
        MaGoControlView.display(maGo, updatedValue -> {
            // Ceci est une *lambda*, donc un callback
            view.update(maGo, updatedValue);
        });

        // ▶️ Reprend le timer après fermeture de la fenêtre
        if (countdownTimeline != null) {
            countdownTimeline.play();
        }

    }
    //pour afficher le bon compteur dans le label de la
    public static String getInitialCountdownSeconds() {
        return INITIAL_COUNTDOWN_SECONDS + "";
    }
    
}

