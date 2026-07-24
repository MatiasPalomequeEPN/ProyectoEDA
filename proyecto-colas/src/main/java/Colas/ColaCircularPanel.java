package Colas;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Animación interactiva de Cola Circular adaptada a la lógica de la Dra. Mayra Carrión.
 * Muestra el paso a paso de las operaciones y es compatible con AnimacionInteractiva.
 */
public class ColaCircularPanel extends JPanel implements AnimacionInteractiva {

    private static final int CAPACIDAD = 6;

    /** Pseudocódigo combinado (Encolar + Desencolar), numeración global 1..N. */
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
        "Fin ALGORITMO Desencolar()",
        " ",
        "ALGORITMO ColaVacia(COLA, frente, B)",
        "  Si (frente == -1) entonces Hacer B <- Verdadero",
        "  sino Hacer B <- Falso",
        "Fin ALGORITMO",
        " ",
        "ALGORITMO ColaLlena(COLA, frente, FIN, MAX, B)",
        "  Si ((frente == 0 Y FIN + 1 == MAX) O (FIN + 1 == frente)) entonces Hacer B <- Verdadero",
        "  sino Hacer B <- Falso",
        "Fin ALGORITMO"
    };

    /** Código Java combinado, numeración global 1..N. */
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
        "}",
        " ",
        "public boolean estaVacia() {",
        "  return frente == -1;",
        "}",
        " ",
        "public boolean estaLlena() {",
        "  return (frente == 0 && FIN + 1 == capacidad) || (FIN + 1 == frente);",
        "}"
    };

    private final Integer[] arreglo = new Integer[CAPACIDAD];
    private int frente = -1;
    private int fin = -1;

    private final List<Paso> pasos = new ArrayList<>();
    private int pasoActual = -1;

    private final Color colorTema;
    private final Timer timer;

    private final JLabel mensajeLabel = new JLabel("Escribe un dato y elige una operación", SwingConstants.CENTER);
    private final Lienzo lienzo = new Lienzo();

    private final JTextField campoDato = new JTextField(4);
    private final JButton btnEncolar = new JButton("Encolar");
    private final JButton btnDesencolar = new JButton("Desencolar");
    private final JButton btnVacia = new JButton("¿Vacía?");
    private final JButton btnLlena = new JButton("¿Llena?");

    public ColaCircularPanel(Color colorTema) {
        this.colorTema = colorTema;

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        add(lienzo, BorderLayout.CENTER);

        mensajeLabel.setFont(mensajeLabel.getFont().deriveFont(Font.ITALIC, 14f));
        mensajeLabel.setBorder(BorderFactory.createEmptyBorder(4, 0, 6, 0));

        JPanel controles = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        controles.setBackground(Color.WHITE);
        controles.add(new JLabel("Dato:"));
        controles.add(campoDato);
        controles.add(btnEncolar);
        controles.add(btnDesencolar);
        
        controles.add(new JSeparator(SwingConstants.VERTICAL));
        controles.add(btnVacia);
        controles.add(btnLlena);

        JPanel sur = new JPanel(new BorderLayout());
        sur.setBackground(Color.WHITE);
        sur.add(mensajeLabel, BorderLayout.NORTH);
        sur.add(controles, BorderLayout.SOUTH);
        add(sur, BorderLayout.SOUTH);

        timer = new Timer(1500, e -> avanzarUnPaso());

        // Eventos
        btnEncolar.addActionListener(e -> intentarEncolar());
        btnDesencolar.addActionListener(e -> intentarDesencolar());
        campoDato.addActionListener(e -> intentarEncolar());
        btnVacia.addActionListener(e -> verificarVacia());
        btnLlena.addActionListener(e -> verificarLlena());

        actualizarVista();
        actualizarBotones();
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

    // ---------- Generación de pasos por operación ----------

    private List<Paso> generarPasosEncolar(int valor) {
        List<Paso> r = new ArrayList<>();
        Integer[] copia = arreglo.clone();
        int f = frente, fi = fin;
        String op = "ENCOLAR(" + valor + ")";

        r.add(new Paso(copia.clone(), f, fi, 1, 1, "Iniciando Encolar(" + valor + ")", op, null));
        boolean estaLlena = (f == 0 && fi + 1 == CAPACIDAD) || (fi + 1 == f);
        r.add(new Paso(copia.clone(), f, fi, 2, 2, "¿Cola llena? -> " + estaLlena, op, null));

        if (estaLlena) {
            r.add(new Paso(copia.clone(), f, fi, 3, 3, "Desbordamiento: la cola circular está llena", op, null));
            return r;
        }

        r.add(new Paso(copia.clone(), f, fi, 5, 5, "¿Está vacía? (frente == -1 -> " + (f == -1) + ")", op, null));

        if (f == -1) {
            f = 0;
            fi = 0;
            copia[fi] = valor;
            r.add(new Paso(copia.clone(), f, fi, 6, 6, "Hacer frente <- 0, FIN <- 0", op, fi));
            r.add(new Paso(copia.clone(), f, fi, 8, 7, "Hacer COLA[" + fi + "] <- " + valor, op, fi));
        } else {
            r.add(new Paso(copia.clone(), f, fi, 10, 9, "¿FIN + 1 == MAX? (" + (fi + 1) + " == " + CAPACIDAD + ")", op, null));
            if (fi + 1 == CAPACIDAD) {
                fi = 0;
                copia[fi] = valor;
                r.add(new Paso(copia.clone(), f, fi, 11, 10, "Vuelta circular: Hacer FIN <- 0", op, fi));
                r.add(new Paso(copia.clone(), f, fi, 12, 11, "Hacer COLA[0] <- " + valor, op, fi));
            } else {
                fi = fi + 1;
                copia[fi] = valor;
                r.add(new Paso(copia.clone(), f, fi, 14, 13, "Hacer FIN <- FIN + 1 -> FIN = " + fi, op, fi));
                r.add(new Paso(copia.clone(), f, fi, 15, 14, "Hacer COLA[" + fi + "] <- " + valor, op, fi));
            }
        }

        r.add(new Paso(copia.clone(), f, fi, 19, 17, "Fin Algoritmo Encolar()", op, fi));

        frente = f;
        fin = fi;
        System.arraycopy(copia, 0, arreglo, 0, CAPACIDAD);

        return r;
    }

    private List<Paso> generarPasosDesencolar() {
        List<Paso> r = new ArrayList<>();
        Integer[] copia = arreglo.clone();
        int f = frente, fi = fin;
        String op = "DESENCOLAR()";

        r.add(new Paso(copia.clone(), f, fi, 21, 19, "Iniciando Desencolar()", op, null));
        r.add(new Paso(copia.clone(), f, fi, 22, 20, "¿Está vacía? (frente == -1 -> " + (f == -1) + ")", op, null));

        if (f == -1) {
            r.add(new Paso(copia.clone(), f, fi, 23, 21, "Subdesbordamiento: la cola está vacía", op, null));
            return r;
        }

        r.add(new Paso(copia.clone(), f, fi, 25, 23, "¿frente == FIN? (" + f + " == " + fi + " -> " + (f == fi) + ")", op, null));

        if (f == fi) {
            r.add(new Paso(copia.clone(), f, fi, 26, 24, "Limpiando COLA[" + f + "] <- Nulo", op, f));
            copia[f] = null;
            f = -1;
            fi = -1;
            r.add(new Paso(copia.clone(), f, fi, 27, 25, "Hacer frente <- -1, FIN <- -1 (cola vacía)", op, null));
        } else {
            r.add(new Paso(copia.clone(), f, fi, 30, 27, "¿frente + 1 == MAX? (" + (f + 1) + " == " + CAPACIDAD + ")", op, null));
            if (f + 1 == CAPACIDAD) {
                r.add(new Paso(copia.clone(), f, fi, 31, 28, "Limpiando COLA[" + f + "] <- Nulo", op, f));
                copia[f] = null;
                f = 0;
                r.add(new Paso(copia.clone(), f, fi, 32, 29, "Vuelta circular: Hacer frente <- 0", op, null));
            } else {
                r.add(new Paso(copia.clone(), f, fi, 34, 31, "Limpiando COLA[" + f + "] <- Nulo", op, f));
                copia[f] = null;
                f = f + 1;
                r.add(new Paso(copia.clone(), f, fi, 35, 32, "Hacer frente <- frente + 1 -> frente = " + f, op, null));
            }
        }

        r.add(new Paso(copia.clone(), f, fi, 39, 36, "Fin Algoritmo Desencolar()", op, null));

        frente = f;
        fin = fi;
        System.arraycopy(copia, 0, arreglo, 0, CAPACIDAD);

        return r;
    }

    private void verificarVacia() {
        if (timer.isRunning()) return;
        List<Paso> r = new ArrayList<>();
        Integer[] copia = arreglo.clone();
        String op = "VERIFICAR VACIA()";
        boolean vacia = (frente == -1);

        r.add(new Paso(copia.clone(), frente, fin, 41, 38, "Llamando al algoritmo ColaVacia()", op, null));
        r.add(new Paso(copia.clone(), frente, fin, 42, 39, "¿frente == -1? (" + frente + " == -1) -> " + vacia, op, null));

        String msjFinal = vacia ? "RESULTADO: La cola SÍ está vacía." : "RESULTADO: La cola NO está vacía.";
        r.add(new Paso(copia.clone(), frente, fin, 42, 39, msjFinal, op, null));

        ejecutarOperacion(r);
    }

    private void verificarLlena() {
        if (timer.isRunning()) return;
        List<Paso> r = new ArrayList<>();
        Integer[] copia = arreglo.clone();
        String op = "VERIFICAR LLENA()";
        boolean llena = (frente == 0 && fin + 1 == CAPACIDAD) || (fin + 1 == frente);

        r.add(new Paso(copia.clone(), frente, fin, 46, 42, "Llamando al algoritmo ColaLlena()", op, null));
        r.add(new Paso(copia.clone(), frente, fin, 47, 43, "Verificando condición de lleno -> " + llena, op, null));

        String msjFinal = llena ? "RESULTADO: La cola SÍ está llena." : "RESULTADO: La cola NO está llena.";
        r.add(new Paso(copia.clone(), frente, fin, 47, 43, msjFinal, op, null));

        ejecutarOperacion(r);
    }

    // ---------- Disparo de operaciones desde la UI ----------

    private void intentarEncolar() {
        if (timer.isRunning()) return;
        String texto = campoDato.getText().trim();
        int valor;
        try {
            valor = Integer.parseInt(texto);
        } catch (NumberFormatException ex) {
            mensajeLabel.setText("Ingresa un número entero válido para encolar");
            return;
        }
        campoDato.setText("");
        ejecutarOperacion(generarPasosEncolar(valor));
    }

    private void intentarDesencolar() {
        if (timer.isRunning()) return;
        ejecutarOperacion(generarPasosDesencolar());
    }

    private void ejecutarOperacion(List<Paso> nuevaSecuencia) {
        pasos.clear();
        pasos.addAll(nuevaSecuencia);
        pasoActual = 0;
        actualizarVista();
        iniciarReproduccion();
        actualizarBotones();
    }

    private void avanzarUnPaso() {
        if (pasoActual < pasos.size() - 1) {
            pasoActual++;
            actualizarVista();
        } else {
            detenerReproduccion();
            actualizarBotones();
        }
    }

    private void iniciarReproduccion() {
        if (pasos.size() > 1) {
            timer.start();
        }
    }

    private void detenerReproduccion() {
        timer.stop();
    }

    private void actualizarBotones() {
        boolean animando = timer.isRunning();
        btnEncolar.setEnabled(!animando);
        btnDesencolar.setEnabled(!animando);
        btnVacia.setEnabled(!animando);
        btnLlena.setEnabled(!animando);
        campoDato.setEnabled(!animando);
    }

    private void actualizarVista() {
        Paso p = pasoActualObj();
        if (p != null) {
            if (pasoActual == pasos.size() - 1 && p.operacion.startsWith("VERIFICAR")) {
                mensajeLabel.setText("<html><b>[" + p.operacion + "] &nbsp;&nbsp; <font color='blue'>" + p.mensaje + "</font></b></html>");
            } else {
                mensajeLabel.setText("[" + p.operacion + "]  " + p.mensaje);
            }
        }
        lienzo.repaint();
    }

    private Paso pasoActualObj() {
        if (pasoActual >= 0 && pasoActual < pasos.size()) return pasos.get(pasoActual);
        return null;
    }

    @Override
    public void saltarALinea(int numeroLinea, boolean esPseudocodigo) {
        detenerReproduccion();
        for (int i = 0; i < pasos.size(); i++) {
            Paso p = pasos.get(i);
            int linea = esPseudocodigo ? p.lineaPseudo : p.lineaCodigo;
            if (linea == numeroLinea) {
                pasoActual = i;
                actualizarVista();
                iniciarReproduccion();
                actualizarBotones();
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
            g2.drawString("Cola Circular (arreglo lineal, capacidad " + CAPACIDAD + ")", 15, 22);

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
                if (i == fi) etiqueta(g2, x, y + cajaAlto + 30, cajaAncho, "FIN", new Color(30, 110, 190));
            }

            g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 12f));
            g2.setColor(Color.DARK_GRAY);
            String estado = "frente = " + f + "    |    FIN = " + fi;
            g2.drawString(estado, startX, y + cajaAlto + 55);

            if (!pasos.isEmpty()) {
                g2.setColor(colorTema.darker());
                g2.setFont(g2.getFont().deriveFont(Font.BOLD, 12f));
                String etiquetaOp = p != null ? p.operacion : "";
                g2.drawString(etiquetaOp + "    -    Paso " + (pasoActual + 1) + " / " + pasos.size(), startX, y + cajaAlto + 80);
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