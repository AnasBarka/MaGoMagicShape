package com.example;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import java.io.IOException;
import java.nio.file.*;
import java.util.List;
//code fais a l'aide de chatgpt
public class MessageParserTest {

    private Path tempFile;         // Fichier temporaire pour les tests
    private MessageParser parser;  // Parser réutilisé dans chaque test

    // Méthode exécutée avant chaque test
    @BeforeEach
    public void setUp() throws IOException {
        // Création d’un fichier temporaire avec du contenu de test
        tempFile = Files.createTempFile("messages", ".txt");
        Files.write(tempFile, List.of(
            "Hello",               // valide
            "http://example.com",  // invalide (contient un lien)
            "",                    // invalide (vide)
            "Bonjour"              // valide
        ));

        // Initialisation du parser avec le fichier temporaire
        parser = new MessageParser(tempFile.toString());
    }

    // Méthode exécutée après chaque test (nettoyage)
    @AfterEach
    public void tearDown() throws IOException {
        Files.deleteIfExists(tempFile);
    }

    // Test : vérifier que seuls les messages valides sont chargés
    @Test
    public void shouldLoadOnlyValidMessages() {
        List<String> validMessages = parser.getAllValidMessages();
        assertEquals(2, validMessages.size());
        assertTrue(validMessages.contains("Hello"));
        assertTrue(validMessages.contains("Bonjour"));
    }

    // Test : vérifier le fonctionnement de hasNextMessage et getNextMessage
    @Test
    public void hasNextAndGetNextShouldWorkCorrectly() {
        assertTrue(parser.hasNextMessage());
        assertEquals("Hello", parser.getNextMessage());
        assertEquals("Bonjour", parser.getNextMessage());
        assertFalse(parser.hasNextMessage());
    }

    // Test : vérifier que getNextMessage lève une exception si plus de messages
    @Test
    public void getNextMessageShouldThrowIfNoMoreMessages() {
        parser.getNextMessage();
        parser.getNextMessage();
    // Vérifie que l'appel de la méthode getNextMessage() sur parser lance bien 
    //une exception de type IndexOutOfBoundsException
    /*
    *
    * assertThrows prend en premier argument la classe de l'exception attendue (ici IndexOutOfBoundsException.class)
    * Le deuxième argument est une lambda (ici () -> parser.getNextMessage()) qui exécute la méthode à tester.
    * 
    * Ce test vérifie que lorsqu'on appelle getNextMessage() alors qu'il n'y a plus de messages valides,
    * une IndexOutOfBoundsException est bien lancée.
    * 
    * Si l'exception est levée, le test passe.
    * Si aucune exception ou une autre exception est levée, le test échoue.
    */
    assertThrows(IndexOutOfBoundsException.class, () -> parser.getNextMessage());
    }

    // Test : vérifier que removeMessage recharge les messages si liste vide
    @Test
    public void removeMessageShouldReloadIfEmpty() {
        parser.removeMessage("Hello");
        parser.removeMessage("Bonjour");
        assertEquals(2, parser.getAllValidMessages().size()); // recharge automatique
    }

    @Test
    public void currentIndexShouldResetAfterReload() {
    parser.getNextMessage(); // avance l'index
    parser.removeMessage("Hello");
    parser.removeMessage("Bonjour"); // vide -> reload
    assertEquals("Hello", parser.getNextMessage()); // devrait recommencer depuis début
}

}
