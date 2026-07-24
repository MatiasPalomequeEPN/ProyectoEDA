package Colas;

import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;

/**
 * Scrollbar vertical plana: sin botones de flecha arriba/abajo y sin
 * el relieve 3D "estilo Windows XP" que trae Nimbus por defecto. Solo
 * una barrita redondeada gris que se resalta un poco al pasar el mouse.
 */
public class ScrollBarUIPlano extends BasicScrollBarUI {

    private static final Color PISTA = Color.WHITE;
    private static final Color BARRA = new Color(195, 195, 195);
    private static final Color BARRA_HOVER = new Color(150, 150, 150);

    @Override
    protected void configureScrollBarColors() {
        this.thumbColor = BARRA;
        this.trackColor = PISTA;
    }

    @Override
    protected JButton createDecreaseButton(int orientation) {
        return botonInvisible();
    }

    @Override
    protected JButton createIncreaseButton(int orientation) {
        return botonInvisible();
    }

    private JButton botonInvisible() {
        JButton boton = new JButton();
        boton.setPreferredSize(new Dimension(0, 0));
        boton.setMinimumSize(new Dimension(0, 0));
        boton.setMaximumSize(new Dimension(0, 0));
        boton.setOpaque(false);
        boton.setBorderPainted(false);
        return boton;
    }

    @Override
    protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
        g.setColor(PISTA);
        g.fillRect(trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height);
    }

    @Override
    protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
        if (thumbBounds.isEmpty() || !c.isEnabled()) return;
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(isThumbRollover() ? BARRA_HOVER : BARRA);
        int arco = 8;
        g2.fillRoundRect(thumbBounds.x + 3, thumbBounds.y + 2,
                thumbBounds.width - 6, thumbBounds.height - 4, arco, arco);
        g2.dispose();
    }

    @Override
    protected Dimension getMinimumThumbSize() {
        return new Dimension(8, 30);
    }
}
