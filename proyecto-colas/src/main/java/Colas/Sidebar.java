package Colas;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Sidebar: encabezado general "Algoritmos", y debajo el tema "Colas"
 * con un item por cada subtema (tipo de cola). Al hacer click en un
 * item, notifica el "key" de la card a mostrar (ej. "cola-simple",
 * "creditos").
 *
 * Nota: esta pantalla es parte de un proyecto mas grande de Algoritmos;
 * "Colas" es, por ahora, el unico tema, pero la estructura ya queda
 * lista para sumar mas temas al lado de este en el futuro.
 */
public class Sidebar extends JPanel {

    private static final Color FONDO = new Color(15, 30, 60);
    private static final Color FONDO_ACTIVO = new Color(30, 90, 200);
    private static final Color TEXTO = Color.WHITE;

    private final List<JLabel> itemsClickeables = new ArrayList<>();
    private JLabel itemActivo;

    public Sidebar(String[] nombresSubtemas, String[] keysSubtemas, Consumer<String> onSeleccion) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(FONDO);
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setPreferredSize(new Dimension(220, 0));

        JLabel encabezado = new JLabel("Algoritmos");
        encabezado.setForeground(TEXTO);
        encabezado.setFont(encabezado.getFont().deriveFont(Font.BOLD, 20f));
        encabezado.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(encabezado);
        add(Box.createVerticalStrut(15));

        JLabel tema = new JLabel("Colas");
        tema.setForeground(TEXTO);
        tema.setFont(tema.getFont().deriveFont(Font.BOLD, 15f));
        tema.setAlignmentX(Component.LEFT_ALIGNMENT);
        tema.setBorder(BorderFactory.createEmptyBorder(6, 0, 4, 0));
        add(tema);

        for (int i = 0; i < nombresSubtemas.length; i++) {
            agregarItem("   " + nombresSubtemas[i], keysSubtemas[i], onSeleccion);
        }

        add(Box.createVerticalStrut(8));
        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(60, 80, 120));
        sep.setAlignmentX(Component.LEFT_ALIGNMENT);
        sep.setMaximumSize(new Dimension(190, 1));
        add(sep);
        add(Box.createVerticalStrut(8));

        agregarItem("   Creditos", "creditos", onSeleccion);
    }

    private void agregarItem(String texto, String key, Consumer<String> onSeleccion) {
        JLabel item = new JLabel(texto);
        item.setOpaque(true);
        item.setBackground(FONDO);
        item.setForeground(TEXTO);
        item.setFont(item.getFont().deriveFont(14f));
        item.setAlignmentX(Component.LEFT_ALIGNMENT);
        item.setMaximumSize(new Dimension(500, 26));
        item.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        item.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        item.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                marcarActivo(item);
                onSeleccion.accept(key);
            }
        });

        itemsClickeables.add(item);
        add(item);

        // El primero que se agrega (Cola Simple) arranca marcado como activo.
        if (itemActivo == null) {
            marcarActivo(item);
        }
    }

    private void marcarActivo(JLabel item) {
        if (itemActivo != null) itemActivo.setBackground(FONDO);
        item.setBackground(FONDO_ACTIVO);
        itemActivo = item;
    }
}
