package Colas;

import javax.swing.*;
import javax.swing.plaf.basic.BasicTabbedPaneUI;
import java.awt.*;

/**
 * UI plana para JTabbedPane: pestañas rectangulares (sin los bordes
 * redondeados que pinta Nimbus por defecto), texto centrado, y
 * resaltado en el mismo azul que usa el ítem activo del Sidebar.
 *
 * Se aplica solo al JTabbedPane de PSEUDOCÓDIGO/CÓDIGO con
 * tabs.setUI(new TabbedPaneUIPlano()) — no afecta al resto de la UI
 * (Nimbus sigue aplicándose normalmente en los demás componentes).
 */
public class TabbedPaneUIPlano extends BasicTabbedPaneUI {

    private static final Color AZUL_SELECCIONADO = new Color(20, 40, 80);
    private static final Color FONDO_NORMAL = Color.WHITE;
    private static final Color TEXTO_SELECCIONADO = Color.WHITE;
    private static final Color TEXTO_NORMAL = new Color(50, 50, 50);
    private static final Color BORDE = new Color(210, 210, 210);

    @Override
    protected void installDefaults() {
        super.installDefaults();
        tabPane.setOpaque(true);
        shadow = BORDE;
        darkShadow = BORDE;
        lightHighlight = BORDE;
        highlight = BORDE;
        focus = AZUL_SELECCIONADO;
        tabInsets = new Insets(10, 4, 10, 4);
        contentBorderInsets = new Insets(1, 1, 1, 1);
        
    }

    @Override
    protected int calculateTabHeight(int tabPlacement, int tabIndex, int fontHeight) {
        return fontHeight + 22;
    }

    @Override
    protected void paintTabBackground(Graphics g, int tabPlacement, int tabIndex,
                                       int x, int y, int w, int h, boolean isSelected) {
        g.setColor(isSelected ? AZUL_SELECCIONADO : FONDO_NORMAL);
        g.fillRect(x, y, w, h);
    }

    @Override
    protected void paintTabBorder(Graphics g, int tabPlacement, int tabIndex,
                                   int x, int y, int w, int h, boolean isSelected) {
        g.setColor(BORDE);
        g.drawRect(x, y, w - 1, h - 1);
    }

    @Override
    protected void paintFocusIndicator(Graphics g, int tabPlacement, Rectangle[] rects,
                                        int tabIndex, Rectangle iconRect, Rectangle textRect,
                                        boolean isSelected) {
        // Sin rectángulo punteado de foco.
    }

    @Override
    protected void paintText(Graphics g, int tabPlacement, Font font, FontMetrics metrics,
                              int tabIndex, String title, Rectangle textRect, boolean isSelected) {
        // layoutLabel ya centra textRect horizontal y verticalmente por
        // defecto en BasicTabbedPaneUI, solo controlamos el color aquí.
        g.setFont(font);
        g.setColor(isSelected ? TEXTO_SELECCIONADO : TEXTO_NORMAL);
        g.drawString(title, textRect.x, textRect.y + metrics.getAscent());
    }
    @Override
    protected void paintContentBorder(Graphics g, int tabPlacement, int selectedIndex) {
        // Sin borde de contenido: nuestras pestañas ya se pintan solas
        // (fondo + borde), no necesitamos que Basic "conecte" la pestaña
        // seleccionada con el panel de abajo.
    }
}
