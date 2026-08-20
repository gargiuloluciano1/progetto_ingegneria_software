package it.unibo.skiscope;

import it.unibo.skiscope.ui.AppFrame;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Punto di ingresso del prototipo SkiScope. Avvia la finestra principale
 * dell'applicazione desktop (Swing), che simula il client descritto
 * nell'Architettura Logica del documento di progetto.
 */
public final class Main {

    private Main() {
    }

    public static void main(String[] args) {
        try {
            // Si usa deliberatamente il Look and Feel Metal (cross-platform),
            // invece di quello di sistema: quest'ultimo, su alcuni sistemi
            // con tema scuro attivo, applicherebbe colori di sfondo/testo
            // ai componenti (campi di testo, combo box, finestre di dialogo)
            // che entrano in conflitto con la palette definita in UiTheme,
            // rendendo alcuni testi illeggibili. Metal garantisce un aspetto
            // prevedibile e coerente con i colori scelti per l'applicazione,
            // qualunque sia il sistema operativo o il suo tema.
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
        } catch (Exception ex) {
            // Se anche Metal non fosse disponibile, si prosegue con il default della JVM.
        }

        SwingUtilities.invokeLater(() -> {
            AppFrame frame = new AppFrame();
            frame.setVisible(true);
        });
    }
}
