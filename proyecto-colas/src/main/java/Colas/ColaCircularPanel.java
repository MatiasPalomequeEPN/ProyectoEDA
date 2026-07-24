package Colas;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Animación de Cola Circular con representación lineal de arreglo,
 * adaptada a la lógica de la Dra. Mayra Carrión (Encolar y Desencolar con 'dato').
 */
public class ColaCircularPanel extends JPanel implements AnimacionInteractiva {

    private static final int CAPACIDAD = 6;

    /** Pseudocódigo genérico basado en la lógica de la Dra. Mayra Carrión */
    public static final String[] PSEUDOCODIGO = {
        "ALGORITMO Encolar(COLA, frente, FIN, MAX, dato)",
        "  Si ((frente == 0 Y FIN + 1 == MAX) O (FIN + 1 == frente))",
        "    Escribir \"Cola llena / Desbordamiento\"",
        "  sino",
        "    Si (frente == -1) entonces",
        "      Hacer FIN <- 0",
        "      Hacer frente <- 0",
        "      Hacer COLA[FIN] <- dato",
        "    sino",
        "      Si (FIN + 1 == MAX) entonces",
        "        Hacer FIN <- 0",
        "        Hacer COLA[FIN] <- dato",
        "      sino",
        "        Hacer FIN <- FIN + 1",
        "        Hacer COLA[FIN] <- dato",
        "      Fin Si",
        "    Fin Si",
        "  Fin Si",
        "Fin ALGORITMO Encolar()",
        " ",
        "ALGORITMO Desencolar(COLA, frente, FIN, MAX)",
        "  Si (frente == -1) entonces",
        "    Escribir \"Cola vacia / Subdesbordamiento\"",
        "  sino",
        "    Si (frente == FIN) entonces",
        "      Hacer COLA[frente] <- Nulo",
        "      Hacer frente <- -1",
        "      Hacer FIN <- -1",
        "    sino",
        "      Si (frente + 1 == MAX) entonces",
        "        Hacer COLA[frente] <- Nulo",
        "        Hacer frente <- 0",
        "      sino",
        "        Hacer COLA[frente] <- Nulo",
        "        Hacer frente <- frente + 1",
        "      Fin Si",
        "    Fin Si",
        "  Fin Si",
        "Fin ALGORITMO Desencolar()"
    };

    /** Código Java adaptado a nombres universales */
    public static final String[] CODIGO = {
        "public void encolar(int dato) {",
        "  if ((frente == 0 && FIN + 1 == capacidad) || (FIN + 1 == frente)) {",
        "    System.out.println(\"Cola llena - Desbordamiento\");",
        "  } else {",
        "    if (frente == -1) {",
        "      FIN = 0; frente = 0;",
        "      cola[FIN] = dato;",
        "    } else {",
        "      if (FIN + 1 == capacidad) {",
        "        FIN = 0;",
        "        cola[FIN] = dato;",
        "      } else {",
        "        FIN = FIN + 1;",
        "        cola[FIN] = dato;",
        "      }",
        "    }",
        "  }",
        "}",
        " ",
        "public void desencolar() {",
        "  if (frente == -1) {",
        "    System.out.println(\"Cola vacia - Subdesbordamiento\");",
        "  } else {",
        "    if (frente == FIN) {",
        "      cola[frente] = null;",
        "      frente = -1; FIN = -1;",
        "    } else {",
        "      if (frente + 1 == capacidad) {",
        "        cola[frente] = null;",
        "        frente = 0;",
        "      } else {",
        "        cola[frente] = null;",
        "        frente = frente + 1;",
        "      }",
        "    }",
        "  }",
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

    public ColaCircularPanel(Color colorTema) {
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

    /**
     * Secuencia de demostración:
     * 1. Encola 4 elementos.
     * 2. Desencola 2 elementos (liberando índices 0 y 1).
     * 3. Encola hasta llenar la cola (FIN da la vuelta circular a 0 y luego a 1).
     * 4. Intenta encolar un elemento EXTRA para demostrar el DESBORDAMIENTO.
     */
    private void prepararSecuenciaCompleta() {
        pasos.addAll(generarPasosEncolar(10));
        pasos.addAll(generarPasosEncolar(20));
        pasos.addAll(generarPasosEncolar(30));
        pasos.addAll(generarPasosEncolar(40));
        
        pasos.addAll(generarPasosDesencolar()); // libera [0]
        pasos.addAll(generarPasosDesencolar()); // libera [1]
        
        pasos.addAll(generarPasosEncolar(50)); 
        pasos.addAll(generarPasosEncolar(60)); // FIN llega al final (MAX - 1)
        pasos.addAll(generarPasosEncolar(70)); // FIN da la vuelta circular a [0]
        pasos.addAll(generarPasosEncolar(80)); // FIN se mueve a [1] -> COLA COMPLETA!
        
        // Intento de encolar con cola llena -> Provoca Desbordamiento
        pasos.addAll(generarPasosEncolar(99)); 
    }

    private List<Paso> generarPasosEncolar(int dato) {
        List<Paso> r = new ArrayList<>();
        Integer[] copia = arreglo.clone();
        int f = frente, fi = fin;
        String op = "ENCOLAR(" + dato + ")";

        r.add(new Paso(copia.clone(), f, fi, 1, 1, "Iniciando Encolar(" + dato + ")", op, null));

        boolean condicionLlena = (f == 0 && fi + 1 == CAPACIDAD) || (fi + 1 == f);
        r.add(new Paso(copia.clone(), f, fi, 2, 2, "Verificando si esta llena... (" + condicionLlena + ")", op, null));

        if (condicionLlena) {
            r.add(new Paso(copia.clone(), f, fi, 3, 3, "¡DESBORDAMIENTO! La cola circular esta llena", op, null));
            return r;
        }

        if (f == -1) {
            fi = 0;
            f = 0;
            r.add(new Paso(copia.clone(), f, fi, 6, 6, "Cola vacia inicial: frente <- 0, FIN <- 0", op, null));
            copia[fi] = dato;
            r.add(new Paso(copia.clone(), f, fi, 8, 7, "Hacer COLA[0] <- " + dato, op, fi));
        } else {
            if (fi + 1 == CAPACIDAD) {
                fi = 0;
                r.add(new Paso(copia.clone(), f, fi, 11, 10, "FIN + 1 == MAX -> VUELTA CIRCULAR: FIN <- 0", op, null));
            } else {
                fi = fi + 1;
                r.add(new Paso(copia.clone(), f, fi, 14, 13, "Hacer FIN <- FIN + 1 -> FIN = " + fi, op, null));
            }
            copia[fi] = dato;
            r.add(new Paso(copia.clone(), f, fi, 15, 14, "Hacer COLA[" + fi + "] <- " + dato, op, fi));
        }

        r.add(new Paso(copia.clone(), f, fi, 19, 18, "Fin ALGORITMO Encolar()", op, fi));

        frente = f;
        fin = fi;
        arreglo[fi] = dato;

        return r;
    }

    private List<Paso> generarPasosDesencolar() {
        List<Paso> r = new ArrayList<>();
        Integer[] copia = arreglo.clone();
        int f = frente, fi = fin;
        String op = "DESENCOLAR()";

        r.add(new Paso(copia.clone(), f, fi, 21, 21, "Iniciando Desencolar()", op, null));
        r.add(new Paso(copia.clone(), f, fi, 22, 22, "¿Esta vacia? (frente == -1 -> " + (f == -1) + ")", op, null));

        if (f == -1) {
            r.add(new Paso(copia.clone(), f, fi, 23, 23, "¡SUBDESBORDAMIENTO! La cola esta vacia", op, null));
            return r;
        }

        r.add(new Paso(copia.clone(), f, fi, 24, 24, "¿frente == FIN? (" + f + " == " + fi + ")", op, f));

        if (f == fi) {
            copia[f] = null;
            f = -1;
            fi = -1;
            r.add(new Paso(copia.clone(), f, fi, 26, 26, "Ultimo elemento eliminado: frente <- -1, FIN <- -1", op, null));
        } else {
            if (f + 1 == CAPACIDAD) {
                copia[f] = null;
                f = 0;
                r.add(new Paso(copia.clone(), f, fi, 31, 29, "frente + 1 == MAX -> VUELTA CIRCULAR: frente <- 0", op, null));
            } else {
                copia[f] = null;
                f = f + 1;
                r.add(new Paso(copia.clone(), f, fi, 34, 31, "Hacer frente <- frente + 1 -> frente = " + f, op, null));
            }
        }

        r.add(new Paso(copia.clone(), f, fi, 38, 35, "Fin ALGORITMO Desencolar()", op, null));

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
        timerReinicio = new Timer(3000, e -> {
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
            g2.drawString("Cola Circular (representacion lineal de arreglo, capacidad " + CAPACIDAD + ")", 15, 22);

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
                    g2.setFont(g2.getFont().deriveFont(Font.BOLD, 15f));
                    FontMetrics fm = g2.getFontMetrics();
                    String texto = String.valueOf(datos[i]);
                    g2.setColor(Color.BLACK);
                    g2.drawString(texto, x + (cajaAncho - fm.stringWidth(texto)) / 2, y + cajaAlto / 2 + 6);
                }

                g2.setColor(Color.GRAY);
                g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 11f));
                g2.drawString("[" + i + "]", x + cajaAncho / 2 - 8, y + cajaAlto + 16);

                if (i == f) etiqueta(g2, x, y - 12, cajaAncho, "frente", new Color(200, 60, 60));
                if (i == fi) etiqueta(g2, x, y + cajaAlto + 30, cajaAncho, "FIN", new Color(30, 110, 190));
            }

            g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 12f));
            g2.setColor(Color.DARK_GRAY);
            String estado = "frente = " + f + "   |   FIN = " + fi;
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