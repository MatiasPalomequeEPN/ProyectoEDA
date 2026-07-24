package Colas;
import javax.swing.*;
import java.awt.*;

public class CreditosPanel extends JPanel {
    public CreditosPanel() {
        setLayout(new GridBagLayout());
        setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.insets = new Insets(8, 0, 8, 0);
        gbc.gridy = 0;

        JLabel titulo = new JLabel("Crédito", SwingConstants.CENTER);
        titulo.setFont(titulo.getFont().deriveFont(Font.PLAIN, 22f));
        add(titulo, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(30, 0, 8, 0);
        add(new JLabel("Integrantes:", SwingConstants.CENTER), gbc);

        String[] integrantes = {
            "Auz Montezuma Juan Diego",
            "Mathias Alejandro Mogrovejo Villavicencio",
            "Palomeque Burgos Matías Aldair",
            "Quillupangui Lozano Ana María"
        };
        for (String nombre : integrantes) {
            gbc.gridy++;
            gbc.insets = new Insets(2, 0, 2, 0);
            add(new JLabel(nombre, SwingConstants.CENTER), gbc);
        }

        gbc.gridy++;
        gbc.insets = new Insets(30, 0, 8, 0);
        add(new JLabel("Ingeniera: Dra. Mayra Carrión", SwingConstants.CENTER), gbc);
    }
}
