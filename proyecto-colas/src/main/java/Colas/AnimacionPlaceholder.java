package Colas;

import javax.swing.*;
import java.awt.*;

/**
 * Placeholder de animación: cada equipo reemplaza el contenido de
 * paintComponent()/la lógica de Timer con su propia estructura,
 * pero la clase ya viene lista para conectarse al patrón de Paso
 * que se explicó (pasoActual, saltarAPaso(n), etc.).
 */
public class AnimacionPlaceholder extends JPanel {

    private final String etiqueta;
    private final Color color;
    private double x = -60;
    private Timer timer;

    public AnimacionPlaceholder(String etiqueta, Color color) {
        this.etiqueta = etiqueta;
        this.color = color;
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        iniciarLoopDemo();
    }

    // Solo para que el shell se vea "vivo" al correrlo; cada equipo
    // cambia esto por su propia lógica de pasos.
    private void iniciarLoopDemo() {
        timer = new Timer(20, e -> {
            x += 2;
            if (x > getWidth() + 60) x = -60;
            repaint();
        });
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(Color.GRAY);
        g2.drawString("Panel de animación — " + etiqueta, 15, 25);

        int y = getHeight() / 2 - 25;
        g2.setColor(color);
        g2.fillRoundRect((int) x, y, 50, 50, 10, 10);
        g2.setColor(Color.BLACK);
        g2.drawRoundRect((int) x, y, 50, 50, 10, 10);
    }
}
