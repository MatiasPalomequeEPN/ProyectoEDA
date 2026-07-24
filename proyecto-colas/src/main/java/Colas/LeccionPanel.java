package Colas;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Layout compartido por todas las pantallas:
 *   [ titulo arriba ]
 *   [ visualizacion centro ] [ JTabbedPane Pseudocodigo/Codigo a la derecha ]
 *
 * Cada linea de pseudocodigo/codigo es clickeable (salta la animacion a
 * ese punto). Ademas, si el panel de la leccion implementa
 * AnimacionInteractiva, un Timer va consultando la linea que se esta
 * ejecutando en cada momento y la resalta automaticamente mientras la
 * animacion corre sola, sin que el usuario tenga que clickear nada.
 */
public class LeccionPanel extends JPanel {

    private static final Color RESALTADO = new Color(255, 225, 130);
    private static final Color HOVER = new Color(230, 240, 255);

    public LeccionPanel(Leccion leccion) {
        setLayout(new BorderLayout());

        // --- titulo ---
        JLabel titulo = new JLabel(leccion.titulo, SwingConstants.CENTER);
        titulo.setOpaque(true);
        titulo.setBackground(new Color(20, 40, 80));
        titulo.setForeground(Color.WHITE);
        titulo.setFont(titulo.getFont().deriveFont(Font.BOLD, 20f));
        titulo.setBorder(BorderFactory.createEmptyBorder(12, 0, 12, 0));
        add(titulo, BorderLayout.NORTH);

        // --- centro: visualizacion ---
        add(leccion.panelVisualizacion, BorderLayout.CENTER);

        // --- derecha: tabs pseudocodigo / codigo ---
        List<JLabel> lineasPseudoUI = new ArrayList<>();
        List<JLabel> lineasCodigoUI = new ArrayList<>();

        JTabbedPane tabs = new JTabbedPane();
        tabs.setUI(new TabbedPaneUIPlano());
        tabs.setFont(tabs.getFont().deriveFont(Font.BOLD, 13f));
        tabs.addTab("PSEUDOCÓDIGO", construirPanelLineas(leccion.lineasPseudocodigo, leccion.panelVisualizacion, true, lineasPseudoUI));
        tabs.addTab("CÓDIGO", construirPanelLineas(leccion.lineasCodigo, leccion.panelVisualizacion, false, lineasCodigoUI));
        tabs.setPreferredSize(new Dimension(380, 0));
        add(tabs, BorderLayout.EAST);

        // --- resaltado en vivo mientras la animacion corre sola ---
        if (leccion.panelVisualizacion instanceof AnimacionInteractiva) {
            AnimacionInteractiva animacion = (AnimacionInteractiva) leccion.panelVisualizacion;
            Timer resaltadoTimer = new Timer(150, e -> {
                resaltarLinea(lineasPseudoUI, animacion.lineaPseudoActual());
                resaltarLinea(lineasCodigoUI, animacion.lineaCodigoActual());
            });
            resaltadoTimer.start();
        }
    }

    private void resaltarLinea(List<JLabel> lineas, int numeroLinea) {
        for (int i = 0; i < lineas.size(); i++) {
            JLabel linea = lineas.get(i);
            boolean activa = (i + 1) == numeroLinea;
            linea.setBackground(activa ? RESALTADO : Color.WHITE);
            linea.setFont(linea.getFont().deriveFont(activa ? Font.BOLD : Font.PLAIN));
        }
    }

    private JScrollPane construirPanelLineas(String[] lineas, JPanel panelVisualizacion,
                                              boolean esPseudocodigo, List<JLabel> registroLineas) {
        JPanel contenedor = new JPanel();
        contenedor.setLayout(new BoxLayout(contenedor, BoxLayout.Y_AXIS));
        contenedor.setBackground(Color.WHITE);

        for (int i = 0; i < lineas.length; i++) {
            final int numeroLinea = i + 1;
            JLabel linea = new JLabel(" " + lineas[i]);
            // Fuente monoespaciada mas chica para que la linea mas larga
            // entre completa sin necesitar scroll horizontal.
            linea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 11));
            linea.setOpaque(true);
            linea.setBackground(Color.WHITE);
            linea.setAlignmentX(Component.LEFT_ALIGNMENT);
            linea.setMaximumSize(new Dimension(Integer.MAX_VALUE, 18));
            linea.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            linea.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    if (panelVisualizacion instanceof AnimacionInteractiva) {
                        ((AnimacionInteractiva) panelVisualizacion).saltarALinea(numeroLinea, esPseudocodigo);
                    }
                }

                @Override
                public void mouseEntered(java.awt.event.MouseEvent e) {
                    if (linea.getBackground() != RESALTADO) {
                        linea.setBackground(HOVER);
                    }
                }

                @Override
                public void mouseExited(java.awt.event.MouseEvent e) {
                    if (linea.getBackground() != RESALTADO) {
                        linea.setBackground(Color.WHITE);
                    }
                }
            });

            registroLineas.add(linea);
            contenedor.add(linea);
        }

        JScrollPane scroll = new JScrollPane(contenedor);
        scroll.setBorder(null);
        // Sin scrollbar horizontal: preferimos que la fuente sea chica
        // y todo entre en el ancho, en vez de tener que desplazarse.
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getVerticalScrollBar().setUI(new ScrollBarUIPlano());
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        return scroll;
    }
}
