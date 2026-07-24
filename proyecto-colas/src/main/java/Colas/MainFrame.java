package Colas;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel panelCards = new JPanel(cardLayout);

    public MainFrame() {
        super("Proyecto Algoritmos - Colas");

        // Nimbus para que no se vea tan "Java 2003"
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ignored) {}

        // Tema: Colas. Subtemas: un tipo de cola por pantalla.
        String[] nombresSubtemas = {
            "Cola Simple", "Cola Circular", "Cola DOBLE - Entrada Restringida", "Cola DOBLE - Salida Restringida"
        };
        String[] keysSubtemas = {
            "cola-simple", "cola-circular", "cola-doble-entrada-restringida", "cola-doble-salida-restringida"
        };
        Color[] coloresPorSubtema = {
            new Color(74, 144, 217), new Color(232, 163, 61),
            new Color(120, 180, 90), new Color(190, 90, 160)
        };

        // --- Cola Simple ---
        Leccion leccionColaSimple = new Leccion(
            "Cola Simple",
            new ColaSimplePanel(coloresPorSubtema[0]),
            ColaSimplePanel.PSEUDOCODIGO,
            ColaSimplePanel.CODIGO
        );
        panelCards.add(new LeccionPanel(leccionColaSimple), keysSubtemas[0]);

        // --- Cola Circular ---
        Leccion leccionColaCircular = new Leccion(
            "Cola Circular",
            new ColaCircularPanel(coloresPorSubtema[1]),
            ColaCircularPanel.PSEUDOCODIGO,
            ColaCircularPanel.CODIGO
        );
        panelCards.add(new LeccionPanel(leccionColaCircular), keysSubtemas[1]);
         
        // --- Cola Doble - Entrada Restringida ---
        Leccion leccionColaDobleEntradaRestringida = new Leccion(
            "Cola Doble - Entrada Restringida",
            new ColaDobleEntradaRestringidaPanel(coloresPorSubtema[2]),
            ColaDobleEntradaRestringidaPanel.PSEUDOCODIGO,
            ColaDobleEntradaRestringidaPanel.CODIGO
        );
        panelCards.add(new LeccionPanel(leccionColaDobleEntradaRestringida), keysSubtemas[2]);

        // --- Cola Doble - Salida Restringida ---
        Leccion leccionColaDobleSalidaRestringida = new Leccion(
            "Cola Doble - Salida Restringida",
            new ColaDobleSalidaRestringidaPanel(coloresPorSubtema[3]),
            ColaDobleSalidaRestringidaPanel.PSEUDOCODIGO,
            ColaDobleSalidaRestringidaPanel.CODIGO
        );
        panelCards.add(new LeccionPanel(leccionColaDobleSalidaRestringida), keysSubtemas[3]);

        panelCards.add(new CreditosPanel(), "creditos");

        Sidebar sidebar = new Sidebar(nombresSubtemas, keysSubtemas, key -> cardLayout.show(panelCards, key));

        setLayout(new BorderLayout());
        add(sidebar, BorderLayout.WEST);
        add(panelCards, BorderLayout.CENTER);

        JLabel footer = new JLabel("ESCUELA POLITÉCNICA NACIONAL", SwingConstants.CENTER);
        footer.setOpaque(true);
        footer.setBackground(new Color(20, 20, 30));
        footer.setForeground(Color.WHITE);
        footer.setBorder(BorderFactory.createEmptyBorder(6, 0, 6, 0));
        add(footer, BorderLayout.SOUTH);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1150, 650);
        setLocationRelativeTo(null);

        cardLayout.show(panelCards, "cola-simple");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
    }
}