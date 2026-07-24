package Colas;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ColaDobleEntradaRestringidaPanel extends JPanel implements AnimacionInteractiva {

    private static final int CAPACIDAD = 6;

    /** Pseudocodigo combinado (ColaDVacia + ColaDLlena + Insertar + EliminarIzquierda + EliminarDerecha), numeracion global 1..N. */
    public static final String[] PSEUDOCODIGO = {
        "ALGORITMO ColaDVacia(COLA, frente, fin, B)",
        "  COLA = Arreglo[0..5] de enteros",
        "  Si (frente == -1) entonces",
        "    Hacer B <- Verdadero",
        "  sino",
        "    Hacer B <- Falso",
        "  Fin Si",
        "Fin ALGORITMO ColaDVacia()",
        " ",
        "ALGORITMO ColaDLlena(COLA, MAX, fin, B)",
        "  COLA = Arreglo[0..5] de enteros",
        "  Si (fin == MAX - 1) entonces",
        "    Hacer B <- Verdadero",
        "  sino",
        "    Hacer B <- Falso",
        "  Fin Si",
        "Fin ALGORITMO ColaDLlena()",
        " ",
        "ALGORITMO Insertar(COLA, frente, fin, MAX, B, D)",
        "  COLA = Arreglo[0..5] de enteros",
        "  Llamar ColaDLlena()",
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
        "Fin Algoritmo Insertar()",
        " ",
        "ALGORITMO EliminarIzquierda(COLA, frente, fin, MAX, B, Dato)",
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
        "Fin Algoritmo EliminarIzquierda()",
        " ",
        "ALGORITMO EliminarDerecha(COLA, frente, fin, MAX, B, Dato)",
        "  COLA = ARREGLO[0..5] de enteros",
        "  Si (frente != -1) entonces // No esta vacia",
        "    Hacer Dato <- COLA[fin]",
        "    Si (frente == fin) entonces",
        "      Hacer frente <- -1",
        "      Hacer fin <- -1",
        "    sino",
        "      Hacer fin <- fin - 1",
        "    Fin Si",
        "  sino",
        "    Escribir \"Subdesbordamiento\"",
        "  Fin Si",
        "Fin Algoritmo EliminarDerecha()"
    };

    /** Codigo Java combinado (colaDVacia + colaDLlena + insertar + eliminarIzquierda + eliminarDerecha), numeracion global 1..N. */
    public static final String[] CODIGO = {
        "public boolean colaDVacia() {",
        "  return frente == -1;",
        "}",
        " ",
        "public boolean colaDLlena() {",
        "  return fin == capacidad - 1;",
        "}",
        " ",
        "public boolean insertar(int valor) {",
        "  if (colaDLlena()) return false;",
        "  if (fin < capacidad - 1) {",
        "    fin++;",
        "    arreglo[fin] = valor;",
        "    if (frente == -1) frente = 0;",
        "  }",
        "  return true;",
        "}",
        " ",
        "public Integer eliminarIzquierda() {",
        "  if (!colaDVacia()) {",
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
        "}",
        " ",
        "public Integer eliminarDerecha() {",
        "  if (!colaDVacia()) {",
        "    int valor = arreglo[fin];",
        "    arreglo[fin] = null;",
        "    if (frente == fin) {",
        "      frente = -1; fin = -1;",
        "    } else {",
        "      fin--;",
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

    private final JLabel mensajeLabel = new JLabel("Escribe un dato y elige una operacion", SwingConstants.CENTER);
    private final Lienzo lienzo = new Lienzo();

    private final JTextField campoDato = new JTextField(6);
    private final JButton btnInsertar = new JButton("Insertar");
    private final JButton btnEliminarIzq = new JButton("Eliminar Izquierda");
    private final JButton btnEliminarDer = new JButton("Eliminar Derecha");

    public ColaDobleEntradaRestringidaPanel(Color colorTema) {
        this.colorTema = colorTema;

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        add(lienzo, BorderLayout.CENTER);

        mensajeLabel.setFont(mensajeLabel.getFont().deriveFont(Font.ITALIC, 13f));
        mensajeLabel.setBorder(BorderFactory.createEmptyBorder(4, 0, 6, 0));

        JPanel controles = new JPanel();
        controles.setBackground(Color.WHITE);
        controles.add(new JLabel("Dato:"));
        controles.add(campoDato);
        controles.add(btnInsertar);
        controles.add(btnEliminarIzq);
        controles.add(btnEliminarDer);

        JPanel sur = new JPanel(new BorderLayout());
        sur.setBackground(Color.WHITE);
        sur.add(mensajeLabel, BorderLayout.NORTH);
        sur.add(controles, BorderLayout.SOUTH);
        add(sur, BorderLayout.SOUTH);

        timer = new Timer(1500, e -> avanzarUnPaso());

        btnInsertar.addActionListener(e -> intentarInsertar());
        btnEliminarIzq.addActionListener(e -> intentarEliminar(true));
        btnEliminarDer.addActionListener(e -> intentarEliminar(false));

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

    // ---------- generacion de pasos por operacion ----------

    private List<Paso> generarPasosInsertar(int valor) {
        List<Paso> r = new ArrayList<>();
        Integer[] copia = arreglo.clone();
        int f = frente, fi = fin;
        String op = "INSERTAR(" + valor + ")";

        r.add(new Paso(copia.clone(), f, fi, 19, 9, "Iniciando Insertar(" + valor + ")", op, null));
        r.add(new Paso(copia.clone(), f, fi, 21, 10, "Llamar ColaDLlena() para verificar...", op, null));
        r.add(new Paso(copia.clone(), f, fi, 12, 6, "\u00bfEsta llena? (fin == " + (CAPACIDAD - 1) + " -> " + (fi == CAPACIDAD - 1) + ")", op, null));

        if (fi == CAPACIDAD - 1) { // revisamos si ya no hay espacio antes de seguir
            r.add(new Paso(copia.clone(), f, fi, 23, 10, "Desbordamiento: no se puede insertar " + valor, op, null));
            return r;
        }

        r.add(new Paso(copia.clone(), f, fi, 25, 11, "fin < MAX-1 es verdadero", op, null));

        fi = fi + 1;
        r.add(new Paso(copia.clone(), f, fi, 26, 12, "Hacer fin <- fin + 1  ->  fin = " + fi, op, null));

        copia[fi] = valor;
        r.add(new Paso(copia.clone(), f, fi, 27, 13, "Hacer COLA[" + fi + "] <- " + valor, op, fi));

        r.add(new Paso(copia.clone(), f, fi, 28, 14, "Verificando si frente == -1...", op, fi));

        if (f == -1) { // si es el primer dato tambien fijamos el frente
            f = 0;
            r.add(new Paso(copia.clone(), f, fi, 29, 14, "Hacer frente <- 0 (primer elemento)", op, fi));
        }

        r.add(new Paso(copia.clone(), f, fi, 33, 16, "Fin Algoritmo Insertar()", op, fi));

        frente = f;
        fin = fi;
        arreglo[fi] = valor;

        return r;
    }

    private List<Paso> generarPasosEliminarIzquierda() {
        List<Paso> r = new ArrayList<>();
        Integer[] copia = arreglo.clone();
        int f = frente, fi = fin;
        String op = "ELIMINAR IZQUIERDA()";

        r.add(new Paso(copia.clone(), f, fi, 35, 19, "Iniciando EliminarIzquierda()", op, null));
        r.add(new Paso(copia.clone(), f, fi, 37, 20, "\u00bfEsta vacia? (frente != -1 -> " + (f != -1) + ")", op, null));

        if (f == -1) { // no hay nada que sacar por la izquierda
            r.add(new Paso(copia.clone(), f, fi, 46, 30, "Subdesbordamiento: la cola esta vacia", op, null));
            return r;
        }

        r.add(new Paso(copia.clone(), f, fi, 38, 21, "Hacer Dato <- COLA[" + f + "] (" + copia[f] + ")", op, f));
        copia[f] = null;

        r.add(new Paso(copia.clone(), f, fi, 39, 23, "\u00bffrente == fin? (" + f + " == " + fi + " -> " + (f == fi) + ")", op, f));

        if (f == fi) { // era el ultimo elemento, la cola queda vacia
            f = -1;
            fi = -1;
            r.add(new Paso(copia.clone(), f, fi, 40, 24, "Hacer frente <- -1, fin <- -1 (cola quedo vacia)", op, null));
        } else {
            f = f + 1;
            r.add(new Paso(copia.clone(), f, fi, 43, 26, "Hacer frente <- frente + 1  ->  frente = " + f, op, null));
        }

        r.add(new Paso(copia.clone(), f, fi, 48, 28, "Fin Algoritmo EliminarIzquierda()", op, null));

        frente = f;
        fin = fi;
        System.arraycopy(copia, 0, arreglo, 0, CAPACIDAD);

        return r;
    }

    private List<Paso> generarPasosEliminarDerecha() {
        List<Paso> r = new ArrayList<>();
        Integer[] copia = arreglo.clone();
        int f = frente, fi = fin;
        String op = "ELIMINAR DERECHA()";

        r.add(new Paso(copia.clone(), f, fi, 50, 33, "Iniciando EliminarDerecha()", op, null));
        r.add(new Paso(copia.clone(), f, fi, 52, 34, "\u00bfEsta vacia? (frente != -1 -> " + (f != -1) + ")", op, null));

        if (f == -1) { // no hay nada que sacar por la derecha
            r.add(new Paso(copia.clone(), f, fi, 61, 44, "Subdesbordamiento: la cola esta vacia", op, null));
            return r;
        }

        r.add(new Paso(copia.clone(), f, fi, 53, 35, "Hacer Dato <- COLA[" + fi + "] (" + copia[fi] + ")", op, fi));
        copia[fi] = null;

        r.add(new Paso(copia.clone(), f, fi, 54, 37, "\u00bffrente == fin? (" + f + " == " + fi + " -> " + (f == fi) + ")", op, fi));

        if (f == fi) { // era el ultimo elemento, la cola queda vacia
            f = -1;
            fi = -1;
            r.add(new Paso(copia.clone(), f, fi, 55, 38, "Hacer frente <- -1, fin <- -1 (cola quedo vacia)", op, null));
        } else {
            fi = fi - 1;
            r.add(new Paso(copia.clone(), f, fi, 58, 40, "Hacer fin <- fin - 1  ->  fin = " + fi, op, null));
        }

        r.add(new Paso(copia.clone(), f, fi, 63, 42, "Fin Algoritmo EliminarDerecha()", op, null));

        frente = f;
        fin = fi;
        System.arraycopy(copia, 0, arreglo, 0, CAPACIDAD);

        return r;
    }

    // ---------- disparo de operaciones desde la UI ----------

    private void intentarInsertar() {
        if (timer.isRunning()) return;
        String texto = campoDato.getText().trim();
        int valor;
        try {
            valor = Integer.parseInt(texto);
        } catch (NumberFormatException ex) { // el texto no es un entero valido
            mensajeLabel.setText("Ingresa un numero entero valido para insertar");
            return;
        }
        campoDato.setText("");
        ejecutarOperacion(generarPasosInsertar(valor));
    }

    private void intentarEliminar(boolean izquierda) {
        if (timer.isRunning()) return;
        ejecutarOperacion(izquierda ? generarPasosEliminarIzquierda() : generarPasosEliminarDerecha());
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
        btnInsertar.setEnabled(!animando);
        btnEliminarIzq.setEnabled(!animando);
        btnEliminarDer.setEnabled(!animando);
        campoDato.setEnabled(!animando);
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
        detenerReproduccion();
        for (int i = 0; i < pasos.size(); i++) { // recorremos los pasos guardados buscando la linea pedida
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
            g2.drawString("Cola Doble - Entrada Restringida (capacidad " + CAPACIDAD + ")", 15, 22);

            Color relleno = clarear(colorTema, 0.72f);

            for (int i = 0; i < CAPACIDAD; i++) { // dibujamos cada casilla del arreglo
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

                if (i == f) etiqueta(g2, x, y - 12, cajaAncho, "frente (solo sale)", new Color(200, 60, 60));
                if (i == fi) etiqueta(g2, x, y + cajaAlto + 30, cajaAncho, "fin (entra/sale)", new Color(30, 110, 190));
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