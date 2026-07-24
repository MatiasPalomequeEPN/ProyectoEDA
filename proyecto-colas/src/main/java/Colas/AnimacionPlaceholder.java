package Colas;

import javax.swing.*;
import java.awt.*;

/**
 * Componente gráfico temporal (placeholder) utilizado para mostrar una animación 
 * visual mientras se implementan los paneles definitivos para los subtemas avanzados de colas.
 * Simula un bucle de animación básico moviendo una figura redondeada horizontalmente 
 * a lo largo del panel con el color y la etiqueta distintivos del tema correspondiente.
 */
public class AnimacionPlaceholder extends JPanel {

    private final String etiqueta; // Nombre descriptivo del subtema asociado
    private final Color color;     // Color visual representativo del tema
    private double x = -60;        // Coordenada horizontal actual del bloque móvil
    private Timer timer;           // Temporizador encargado de refrescar la animación

    /**
     * Constructor que inicializa el panel con la etiqueta y el color específicos del subtema.
     * Configura el fondo, el borde decorativo y arranca el bucle de animación.
     * 
     * @param etiqueta Nombre del tipo de cola o subtema a mostrar en el encabezado.
     * @param color    Color temático asignado al panel para la representación gráfica.
     */
    public AnimacionPlaceholder(String etiqueta, Color color) {
        this.etiqueta = etiqueta; // Asigna la etiqueta descriptiva
        this.color = color; // Asigna el color temático
        setBackground(Color.WHITE); // Establece el color de fondo predeterminado del panel
        setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220))); // Agrega un borde sutil y estético
        iniciarLoopDemo(); // Inicia la animación visual de prueba
    }

    /**
     * Inicializa y ejecuta un temporizador (Timer) que actualiza la posición 
     * de la figura en bucle continuo para dar la sensación de movimiento.
     */
    private void iniciarLoopDemo() {
        timer = new Timer(20, e -> { //Crea un timer con un intervalo de 20 milisegundos
            x += 2; //Incrementa la posición en el eje X para mover el bloque hacia la derecha
            if (x > getWidth() + 60) x = -60; //Reinicia la posición al extremo izquierdo si sale del panel
            repaint(); //Solicita redibujar el componente para actualizar el movimiento
        });
        timer.start(); //Dispara el inicio del temporizador
    }

    /**
     * Método encargado de renderizar los elementos gráficos del panel.
     * Dibuja un texto informativo con la etiqueta del subtema y un bloque animado 
     * que se desplaza horizontalmente usando antialiasing para mayor fluidez.
     * 
     * @.param g Objeto gráfico base utilizado para la renderización.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); //Limpia y pinta el contenedor base
        Graphics2D g2 = (Graphics2D) g; //Convierte a Graphics2D para mejores propiedades de renderizado
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); //Activa el suavizado de bordes

        g2.setColor(Color.GRAY); //Establece el color gris para el texto descriptivo
        g2.drawString("Panel de animación — " + etiqueta, 15, 25); //Dibuja la etiqueta superior identificando el subtema

        int y = getHeight() / 2 - 25; //Calcula la posición vertical centrada para el bloque animado
        g2.setColor(color); //Asigna el color temático al bloque móvil
        g2.fillRoundRect((int) x, y, 50, 50, 10, 10); //Dibuja el rectángulo con esquinas redondeadas relleno
        g2.setColor(Color.BLACK); //Asigna el color negro para el contorno de la figura
        g2.drawRoundRect((int) x, y, 50, 50, 10, 10); //Dibuja el borde del rectángulo redondeado
    }
}