package Colas;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Animacion real de Cola Simple. A diferencia de la version anterior
 * (una pantalla para Encolar y otra para Desencolar, cada una esperando
 * que el usuario haga click para arrancar), ahora todo vive en una sola
 * pantalla: al construirse arranca solo, encola 3 valores, desencola 2,
 * y cuando termina espera un momento y vuelve a empezar - sin que el
 * usuario tenga que interactuar para nada.
 */
public class ColaSimplePanel extends JPanel implements AnimacionInteractiva {

    private static final int CAPACIDAD = 6;

    /** Pseudocodigo combinado (ColaVacia + ColaLlena + Encolar + Desencolar), numeracion global 1..N. */
    public static final String[] PSEUDOCODIGO = {
        "ALGORITMO ColaVacia(COLA, frente, fin, B)",
        "  COLA = Arreglo[0..5] de enteros",
        "  Si (frente == -1) entonces",
        "    Hacer B <- Verdadero",
        "  sino",
        "    Hacer B <- Falso",
        "  Fin Si",
        "Fin ALGORITMO ColaVacia()",
        " ",
        "ALGORITMO ColaLlena(COLA, MAX, fin, B)",
        "  COLA = Arreglo[0..5] de enteros",
        "  Si (fin == MAX - 1) entonces",
        "    Hacer B <- Verdadero",
        "  sino",
        "    Hacer B <- Falso",
        "  Fin Si",
        "Fin ALGORITMO ColaLlena()",
        " ",
        "ALGORITMO Encolar(COLA, frente, fin, MAX, B, D)",
        "  COLA = Arreglo[0..5] de enteros",
        "  Llamar ColaLlena()",
        "  Si (B == Verdadero) entonces",
        "    Escribir \"Desbordamiento\"",
        "  sino",
        "    Si (fin < MAX - 1) Entonces",
        "      Hacer fin <- fin + 1",
        "      Hacer COLA[fin] <- D",
        "      Si (frente == -1) entonces",
        "        Hacer frente <- 0",
        "      Fin Si",
        "    Fin Si",
        "  Fin Si",
        "Fin Algoritmo Encolar()",
        " ",
        "ALGORITMO Desencolar(COLA, frente, fin, MAX, B, Dato)",
        "  COLA = ARREGLO[0..5] de enteros",
        "  Si (frente != -1) entonces // No esta vacia",
        "    Hacer Dato <- COLA[frente]",
        "    Si (frente == fin) entonces",
        "      Hacer frente <- -1",
        "      Hacer fin <- -1",
        "    sino",
        "      Hacer frente <- frente + 1",
        "    Fin Si",
        "  sino",
        "    Escribir \"Subdesbordamiento\"",
        "  Fin Si",
        "Fin Algoritmo Desencolar()"
    };

    /** Codigo Java combinado (estaVacia + estaLlena + encolar + desencolar), numeracion global 1..N. */
    public static final String[] CODIGO = {
        "public boolean estaVacia() {",
        "  return frente == -1;",
        "}",
        " ",
        "public boolean estaLlena() {",
        "  return fin == capacidad - 1;",
        "}",
        " ",
        "public boolean encolar(int valor) {",
        "  if (estaLlena()) return false;",
        "  if (fin < capacidad - 1) {",
        "    fin++;",
        "    arreglo[fin] = valor;",
        "    if (frente == -1) frente = 0;",
        "  }",
        "  return true;",
        "}",
        " ",
        "public Integer desencolar() {",
        "  if (!estaVacia()) {",
        "    int valor = arreglo[frente];",
        "    arreglo[frente] = null;",
        "    if (frente == fin) {",
        "      frente = -1; fin = -1;",
        "    } else {",
        "      frente++;",
        "    }",
        "    return valor;",
        "  }",
        "  return null;",
        "}"
    };

    private final Integer[] arreglo = new Integer[CAPACIDAD];
    private int frente = -1;
    private int fin = -1;

    private final List<Paso> pasos = new ArrayList<>();
    private int pasoActual = -1;

    private final Color colorTema;
    private final Timer timer;
    private Timer timerReinicio;

    private final JLabel mensajeLabel = new JLabel(" ", SwingConstants.CENTER);
    private final Lienzo lienzo = new Lienzo();

    public ColaSimplePanel(Color colorTema) {
        this.colorTema = colorTema;

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        add(lienzo, BorderLayout.CENTER);

        mensajeLabel.setFont(mensajeLabel.getFont().deriveFont(Font.ITALIC, 13f));
        mensajeLabel.setBorder(BorderFactory.createEmptyBorder(4, 0, 10, 0));
        add(mensajeLabel, BorderLayout.SOUTH);

        timer = new Timer(1500, e -> avanzarUnPaso());

        prepararSecuenciaCompleta();

        pasoActual = 0;
        actualizarVista();
        iniciarReproduccion();
    }

    private static class Paso {
        final Integer[] arreglo;
        final int frente;
        final int fin;
        final int lineaPseudo;
        final int lineaCodigo;
        final String mensaje;
        final String operacion;
        final Integer indiceResaltado;

        Paso(Integer[] arreglo, int frente, int fin, int lineaPseudo, int lineaCodigo,
             String mensaje, String operacion, Integer indiceResaltado) {
            this.arreglo = arreglo;
            this.frente = frente;
            this.fin = fin;
            this.lineaPseudo = lineaPseudo;
            this.lineaCodigo = lineaCodigo;
            this.mensaje = mensaje;
            this.operacion = operacion;
            this.indiceResaltado = indiceResaltado;
        }
    }

    private void prepararSecuenciaCompleta() {
        pasos.addAll(generarPasosEncolar(12));
        pasos.addAll(generarPasosEncolar(45));
        pasos.addAll(generarPasosEncolar(89));
        pasos.addAll(generarPasosDesencolar());
        pasos.addAll(generarPasosDesencolar());
    }

    private List<Paso> generarPasosEncolar(int valor) {
        List<Paso> r = new ArrayList<>();
        Integer[] copia = arreglo.clone();
        int f = frente, fi = fin;
        String op = "ENCOLAR(" + valor + ")";

        r.add(new Paso(copia.clone(), f, fi, 19, 9, "Iniciando Encolar(" + valor + ")", op, null));
        r.add(new Paso(copia.clone(), f, fi, 21, 10, "Llamar ColaLlena() para verificar...", op, null));
        r.add(new Paso(copia.clone(), f, fi, 12, 6, "\u00bfEsta llena? (fin == " + (CAPACIDAD - 1) + " -> " + (fi == CAPACIDAD - 1) + ")", op, null));

        if (fi == CAPACIDAD - 1) {
            r.add(new Paso(copia.clone(), f, fi, 23, 10, "Desbordamiento: no se puede encolar " + valor, op, null));
            return r;
        }

        r.add(new Paso(copia.clone(), f, fi, 25, 11, "fin < MAX-1 es verdadero", op, null));

        fi = fi + 1;
        r.add(new Paso(copia.clone(), f, fi, 26, 12, "Hacer fin <- fin + 1  ->  fin = " + fi, op, null));

        copia[fi] = valor;
        r.add(new Paso(copia.clone(), f, fi, 27, 13, "Hacer COLA[" + fi + "] <- " + valor, op, fi));

        r.add(new Paso(copia.clone(), f, fi, 28, 14, "Verificando si frente == -1...", op, fi));

        if (f == -1) {
            f = 0;
            r.add(new Paso(copia.clone(), f, fi, 29, 14, "Hacer frente <- 0 (primer elemento)", op, fi));
        }

        r.add(new Paso(copia.clone(), f, fi, 33, 16, "Fin Algoritmo Encolar()", op, fi));

        frente = f;
        fin = fi;
        arreglo[fi] = valor;

        return r;
    }

    private List<Paso> generarPasosDesencolar() {
        List<Paso> r = new ArrayList<>();
        Integer[] copia = arreglo.clone();
        int f = frente, fi = fin;
        String op = "DESENCOLAR()";

        r.add(new Paso(copia.clone(), f, fi, 35, 19, "Iniciando Desencolar()", op, null));
        r.add(new Paso(copia.clone(), f, fi, 37, 20, "\u00bfEsta vacia? (frente != -1 -> " + (f != -1) + ")", op, null));

        if (f == -1 || f > fi) {
            r.add(new Paso(copia.clone(), f, fi, 46, 30, "Subdesbordamiento: la cola esta vacia", op, null));
            return r;
        }

        r.add(new Paso(copia.clone(), f, fi, 38, 21, "Hacer Dato <- COLA[" + f + "] (" + copia[f] + ")", op, f));
        copia[f] = null;

        r.add(new Paso(copia.clone(), f, fi, 39, 23, "\u00bffrente == fin? (" + f + " == " + fi + " -> " + (f == fi) + ")", op, f));

        if (f == fi) {
            f = -1;
            fi = -1;
            r.add(new Paso(copia.clone(), f, fi, 40, 24, "Hacer frente <- -1, fin <- -1 (cola quedo vacia)", op, null));
        } else {
            f = f + 1;
            r.add(new Paso(copia.clone(), f, fi, 43, 26, "Hacer frente <- frente + 1  ->  frente = " + f, op, null));
        }

        r.add(new Paso(copia.clone(), f, fi, 48, 28, "Fin Algoritmo Desencolar()", op, null));

        frente = f;
        fin = fi;
        System.arraycopy(copia, 0, arreglo, 0, CAPACIDAD);

        return r;
    }

    private void avanzarUnPaso() {
        if (pasos.isEmpty()) return;
        if (pasoActual < pasos.size() - 1) {
            pasoActual++;
            actualizarVista();
        } else {
            detenerReproduccion();
            reiniciarLuegoDePausa();
        }
    }

    private void reiniciarLuegoDePausa() {
        if (timerReinicio != null) timerReinicio.stop();
        timerReinicio = new Timer(2500, e -> {
            frente = -1;
            fin = -1;
            Arrays.fill(arreglo, null);
            pasos.clear();
            prepararSecuenciaCompleta();
            pasoActual = 0;
            actualizarVista();
            iniciarReproduccion();
        });
        timerReinicio.setRepeats(false);
        timerReinicio.start();
    }

    private void iniciarReproduccion() {
        if (pasos.isEmpty() || pasos.size() <= 1) return;
        timer.start();
    }

    private void detenerReproduccion() {
        timer.stop();
    }

    private void actualizarVista() {
        Paso p = pasoActualObj();
        if (p != null) mensajeLabel.setText("[" + p.operacion + "]  " + p.mensaje);
        lienzo.repaint();
    }

    private Paso pasoActualObj() {
        if (pasoActual >= 0 && pasoActual < pasos.size()) return pasos.get(pasoActual);
        return null;
    }

    @Override
    public void saltarALinea(int numeroLinea, boolean esPseudocodigo) {
        if (timerReinicio != null) timerReinicio.stop();
        detenerReproduccion();
        for (int i = 0; i < pasos.size(); i++) {
            Paso p = pasos.get(i);
            int linea = esPseudocodigo ? p.lineaPseudo : p.lineaCodigo;
            if (linea == numeroLinea) {
                pasoActual = i;
                actualizarVista();
                iniciarReproduccion();
                return;
            }
        }
    }

    @Override
    public int lineaPseudoActual() {
        Paso p = pasoActualObj();
        return p != null ? p.lineaPseudo : -1;
    }

    @Override
    public int lineaCodigoActual() {
        Paso p = pasoActualObj();
        return p != null ? p.lineaCodigo : -1;
    }

    private class Lienzo extends JPanel {
        Lienzo() {
            setBackground(Color.WHITE);
            setPreferredSize(new Dimension(420, 260));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            Paso p = pasoActualObj();
            Integer[] datos = p != null ? p.arreglo : arreglo;
            int f = p != null ? p.frente : frente;
            int fi = p != null ? p.fin : fin;
            Integer resaltado = p != null ? p.indiceResaltado : null;

            int cajaAncho = 55, cajaAlto = 50, espacio = 10;
            int anchoTotal = CAPACIDAD * (cajaAncho + espacio) - espacio;
            int startX = Math.max(20, (getWidth() - anchoTotal) / 2);
            int y = 90;

            g2.setColor(Color.GRAY);
            g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 12f));
            g2.drawString("Cola Simple (arreglo lineal, capacidad " + CAPACIDAD + ")", 15, 22);

            Color relleno = clarear(colorTema, 0.72f);

            for (int i = 0; i < CAPACIDAD; i++) {
                int x = startX + i * (cajaAncho + espacio);

                if (resaltado != null && resaltado == i) {
                    g2.setColor(new Color(255, 225, 130));
                } else {
                    g2.setColor(datos[i] != null ? relleno : Color.WHITE);
                }
                g2.fillRoundRect(x, y, cajaAncho, cajaAlto, 10, 10);
                g2.setColor(Color.DARK_GRAY);
                g2.drawRoundRect(x, y, cajaAncho, cajaAlto, 10, 10);

                if (datos[i] != null) {
                    g2.setFont(g2.getFont().deriveFont(Font.BOLD, 16f));
                    FontMetrics fm = g2.getFontMetrics();
                    String texto = String.valueOf(datos[i]);
                    g2.setColor(Color.BLACK);
                    g2.drawString(texto, x + (cajaAncho - fm.stringWidth(texto)) / 2, y + cajaAlto / 2 + 6);
                }

                g2.setColor(Color.GRAY);
                g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 11f));
                g2.drawString("[" + i + "]", x + cajaAncho / 2 - 8, y + cajaAlto + 16);

                if (i == f) etiqueta(g2, x, y - 12, cajaAncho, "frente", new Color(200, 60, 60));
                if (i == fi) etiqueta(g2, x, y + cajaAlto + 30, cajaAncho, "fin", new Color(30, 110, 190));
            }

            g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 12f));
            g2.setColor(Color.DARK_GRAY);
            String estado = "frente = " + f + "   |   fin = " + fi;
            g2.drawString(estado, startX, y + cajaAlto + 55);

            if (!pasos.isEmpty()) {
                g2.setColor(colorTema.darker());
                g2.setFont(g2.getFont().deriveFont(Font.BOLD, 12f));
                String etiquetaOp = p != null ? p.operacion : "";
                g2.drawString(etiquetaOp + "   -   Paso " + (pasoActual + 1) + " / " + pasos.size(), startX, y + cajaAlto + 80);
            }
        }

        private void etiqueta(Graphics2D g2, int x, int y, int ancho, String texto, Color color) {
            g2.setColor(color);
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 11f));
            FontMetrics fm = g2.getFontMetrics();
            g2.drawString(texto, x + (ancho - fm.stringWidth(texto)) / 2, y);
        }
    }

    private static Color clarear(Color c, float factor) {
        int r = (int) (c.getRed() + (255 - c.getRed()) * factor);
        int g = (int) (c.getGreen() + (255 - c.getGreen()) * factor);
        int b = (int) (c.getBlue() + (255 - c.getBlue()) * factor);
        return new Color(r, g, b);
    }
}
