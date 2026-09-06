package com.example;

import java.util.List;
import java.util.Random;

public class MessageStrategySelector {

    // Interface stratégie
    public interface MessageSelectionStrategy {
        String selectMessage(List<String> messages, List<MaGo> magos);
    }

    // Stratégie random
    public static class RandomStrategy implements MessageSelectionStrategy {
        private Random random = new Random();
        @Override
        public String selectMessage(List<String> messages, List<MaGo> magos) {
            if (messages.isEmpty()) return null;
            return messages.get(random.nextInt(messages.size()));
        }
    }

    // Stratégie meilleur pour un type donné
    public static class BestForTypeStrategy implements MessageSelectionStrategy {
        private Class<? extends MaGo> targetType;
        public BestForTypeStrategy(Class<? extends MaGo> targetType) {
            this.targetType = targetType;
        }

        @Override
        public String selectMessage(List<String> messages, List<MaGo> magos) {
            if (messages.isEmpty()) return null;

            String bestMessage = null;
            double bestScore = Double.NEGATIVE_INFINITY;

            for (String msg : messages) {
                double score = averageImpactOnType(magos, msg, targetType);
                if (score > bestScore) {
                    bestScore = score;
                    bestMessage = msg;
                }
            }
            return bestMessage;
        }
    }

    // Stratégie meilleur pour type aléatoire (parmi cercle et carré)
    public static class BestForRandomTypeStrategy implements MessageSelectionStrategy {
        private Random random = new Random();
        private List<Class<? extends MaGo>> types = List.of(CircleMaGo.class, SquareMaGo.class);

        @Override
        public String selectMessage(List<String> messages, List<MaGo> magos) {
            if (messages.isEmpty()) return null;
            Class<? extends MaGo> randomType = types.get(random.nextInt(types.size()));

            String bestMessage = null;
            double bestScore = Double.NEGATIVE_INFINITY;

            for (String msg : messages) {
                double score = averageImpactOnType(magos, msg, randomType);
                if (score > bestScore) {
                    bestScore = score;
                    bestMessage = msg;
                }
            }
            return bestMessage;
        }
    }

    // Méthode commune utilisée par les stratégies
    private static double averageImpactOnType(List<MaGo> magos, String msg, Class<? extends MaGo> type) {
        double total = 0;
        int count = 0;
        for (MaGo m : magos) {
            if (type.isInstance(m)) {
                total += m.calculMessageImpact(msg);
                count++;
            }
        }
        return count == 0 ? Double.NEGATIVE_INFINITY : total / count;
    }
}
