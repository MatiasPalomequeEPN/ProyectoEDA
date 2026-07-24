package Colas;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Panel grafico e interactivo que simula el funcionamiento de una Cola Circular 
 * implementada sobre un arreglo lineal de tamano fijo.
 * 
 * Permite visualizar paso a paso las operaciones principales de la estructura de datos:
 * - Encolar: Insercion controlada de un elemento al final de la cola.
 * - Desencolar: Eliminacion del elemento situado en el frente de la cola.
 * - ColaVacia: Verificacion del estado de subdesbordamiento.
 * - ColaLlena: Verificacion del estado de desbordamiento circular.
 * 
 * Incluye un sistema de sincronizacion visual que resalta la ejecucion tanto en el codigo 
 * fuente como en el panel grafico de nodos.
 */
public class ColaCircularPanel extends JPanel implements AnimacionInteractiva {

    private static final int CAPACIDAD = 6; // Capacidad maxima estatica de la cola circular

    /** Pseudocodigo combinado (Encolar + Desencolar), numeracion global 1..N. */
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

    /** Codigo Java combinado, numeracion global 1..N. */
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

    private final Integer[] arreglo = new Integer[CAPACIDAD]; // Arreglo estatico contenedor de los datos
    private int frente = -1; // Indice del primer elemento de la cola (-1 indica vacio)
    private int fin = -1;    // Indice del ultimo elemento de la cola (-1 indica vacio)

    private final List<Paso> pasos = new ArrayList<>(); // Lista secuencial de estados para la animacion de pasos
    private int pasoActual = -1;                        // Indice del paso actualmente renderizado

    private final Color colorTema; // Color base para la identidad visual del panel
    private final Timer timer;     // Temporizador para automatizar la reproduccion de la animacion

    private final JLabel mensajeLabel = new JLabel("Escribe un dato y elige una operacion", SwingConstants.CENTER); // Etiqueta informativa de estado
    private final Lienzo lienzo = new Lienzo(); // Componente grafico personalizado para dibujar el arreglo

    private final JTextField campoDato = new JTextField(4); // Campo de entrada de texto para el valor a encolar
    private final JButton btnEncolar = new JButton("Encolar");       // Boton para disparar la insercion
    private final JButton btnDesencolar = new JButton("Desencolar"); // Boton para disparar la extraccion
    private final JButton btnVacia = new JButton("¿Vacia?");         // Boton para consultar si esta vacia
    private final JButton btnLlena = new JButton("¿Llena?");         // Boton para consultar si esta llena

    /**
     * Constructor que inicializa los componentes de la interfaz de usuario, 
     * define el diseño visual y enlaza los eventos de control de la cola circular.
     * 
     * @param colorTema Color tematico asignado al modulo de animacion.
     */
    public ColaCircularPanel(Color colorTema) {
        this.colorTema = colorTema; // Almacena el color de estilo del tema

        setLayout(new BorderLayout()); // Establece la distribucion general del panel principal
        setBackground(Color.WHITE);    // Define el fondo blanco predeterminado
        add(lienzo, BorderLayout.CENTER); // Agrega el lienzo grafico en la zona central

        mensajeLabel.setFont(mensajeLabel.getFont().deriveFont(Font.ITALIC, 14f)); // Configura la tipografia cursiva para mensajes
        mensajeLabel.setBorder(BorderFactory.createEmptyBorder(4, 0, 6, 0));       // Anade un margen interno de separacion

        JPanel controles = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5)); // Panel contenedor para los controles interactivos
        controles.setBackground(Color.WHITE); // Fondo blanco para los controles
        controles.add(new JLabel("Dato:"));   // Etiqueta descriptiva del campo de texto
        controles.add(campoDato); // Campo de texto para el numero
        controles.add(btnEncolar);    // Boton de encolamiento
        controles.add(btnDesencolar); // Boton de desencolamiento
        
        controles.add(new JSeparator(SwingConstants.VERTICAL)); // Linea divisoria estetica
        controles.add(btnVacia); // Boton de verificacion de vacio
        controles.add(btnLlena); // Boton de verificacion de lleno

        JPanel sur = new JPanel(new BorderLayout()); // Panel inferior que agrupa mensajes y controles
        sur.setBackground(Color.WHITE);
        sur.add(mensajeLabel, BorderLayout.NORTH);   // Ubica el mensaje de estado arriba de los botones
        sur.add(controles, BorderLayout.SOUTH);      // Ubica los botones en la parte inferior
        add(sur, BorderLayout.SOUTH);                // Integra el panel sur en la interfaz principal

        timer = new Timer(1500, e -> avanzarUnPaso()); // Configura el temporizador con un intervalo de 1.5 segundos por paso

        // Enlace de eventos de accion para los componentes de la interfaz
        btnEncolar.addActionListener(e -> intentarEncolar());
        btnDesencolar.addActionListener(e -> intentarDesencolar());
        campoDato.addActionListener(e -> intentarEncolar());
        btnVacia.addActionListener(e -> verificarVacia());
        btnLlena.addActionListener(e -> verificarLlena());

        actualizarVista();   // Refresca la interfaz grafica inicial
        actualizarBotones(); // Controla la habilitacion inicial de los botones
    }

    /**
     * Clase interna que encapsula el estado completo de la estructura y de la interfaz 
     * en un momento determinado (paso), permitiendo pausar y recorrer la animacion.
     */
    private static class Paso {
        final Integer[] arreglo;
        final int frente;
        final int fin;
        final int lineaPseudo;
        final int lineaCodigo;
        final String mensaje;
        final String operacion;
        final Integer indiceResaltado;

        /**
         * Constructor que registra los parametros de estado correspondientes a un paso de la animacion.
         * 
         * @param arreglo         Copia del estado del arreglo en este instante.
         * @param frente          Indice del frente en este paso.
         * @param fin             Indice del fin en este paso.
         * @param lineaPseudo     Numero de linea correspondiente en el pseudocodigo.
         * @param lineaCodigo     Numero de linea correspondiente en el codigo Java.
         * @param mensaje         Descripcion textual del evento en curso.
         * @param operacion       Nombre de la operacion principal que se esta ejecutando.
         * @param indiceResaltado Indice del arreglo que requiere un realce visual especial, si aplica.
         */
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
     * Genera la secuencia detallada de pasos logicos y graficos para la operacion Encolar.
     * 
     * @param valor Valor entero que se desea insertar en la cola circular.
     * @return Una lista de objetos Paso que representan la ejecucion paso a paso.
     */
    private List<Paso> generarPasosEncolar(int valor) {
        List<Paso> r = new ArrayList<>(); // Lista contenedora de los pasos de la operacion
        Integer[] copia = arreglo.clone(); // Clona el estado actual del arreglo
        int f = frente, fi = fin;
        String op = "ENCOLAR(" + valor + ")";

        r.add(new Paso(copia.clone(), f, fi, 1, 1, "Iniciando Encolar(" + valor + ")", op, null));
        boolean estaLlena = (f == 0 && fi + 1 == CAPACIDAD) || (fi + 1 == f); // Evalua la condicion de desbordamiento circular
        r.add(new Paso(copia.clone(), f, fi, 2, 2, "¿Cola llena? -> " + estaLlena, op, null));

        if (estaLlena) {
            r.add(new Paso(copia.clone(), f, fi, 3, 3, "Desbordamiento: la cola circular esta llena", op, null));
            return r; // Retorna la secuencia de pasos de error si esta llena
        }

        r.add(new Paso(copia.clone(), f, fi, 5, 5, "¿Esta vacia? (frente == -1 -> " + (f == -1) + ")", op, null));

        if (f == -1) { // Caso base: la cola esta vacia
            f = 0;
            fi = 0;
            copia[fi] = valor;
            r.add(new Paso(copia.clone(), f, fi, 6, 6, "Hacer frente <- 0, FIN <- 0", op, fi));
            r.add(new Paso(copia.clone(), f, fi, 8, 7, "Hacer COLA[" + fi + "] <- " + valor, op, fi));
        } else {
            r.add(new Paso(copia.clone(), f, fi, 10, 9, "¿FIN + 1 == MAX? (" + (fi + 1) + " == " + CAPACIDAD + ")", op, null));
            if (fi + 1 == CAPACIDAD) { // Caso de desbordamiento circular: el fin llega al limite y vuelve al inicio
                fi = 0;
                copia[fi] = valor;
                r.add(new Paso(copia.clone(), f, fi, 11, 10, "Vuelta circular: Hacer FIN <- 0", op, fi));
                r.add(new Paso(copia.clone(), f, fi, 12, 11, "Hacer COLA[0] <- " + valor, op, fi));
            } else { // Caso general: incremento lineal del indice fin
                fi = fi + 1;
                copia[fi] = valor;
                r.add(new Paso(copia.clone(), f, fi, 14, 13, "Hacer FIN <- FIN + 1 -> FIN = " + fi, op, fi));
                r.add(new Paso(copia.clone(), f, fi, 15, 14, "Hacer COLA[" + fi + "] <- " + valor, op, fi));
            }
        }

        r.add(new Paso(copia.clone(), f, fi, 19, 17, "Fin Algoritmo Encolar()", op, fi));

        frente = f;
        fin = fi;
        System.arraycopy(copia, 0, arreglo, 0, CAPACIDAD); // Actualiza el arreglo oficial

        return r;
    }

    /**
     * Genera la secuencia detallada de pasos logicos y graficos para la operacion Desencolar.
     * 
     * @return Una lista de objetos Paso que representan el proceso de extraccion paso a paso.
     */
    private List<Paso> generarPasosDesencolar() {
        List<Paso> r = new ArrayList<>();
        Integer[] copia = arreglo.clone();
        int f = frente, fi = fin;
        String op = "DESENCOLAR()";

        r.add(new Paso(copia.clone(), f, fi, 21, 19, "Iniciando Desencolar()", op, null));
        r.add(new Paso(copia.clone(), f, fi, 22, 20, "¿Esta vacia? (frente == -1 -> " + (f == -1) + ")", op, null));

        if (f == -1) { // Control de subdesbordamiento
            r.add(new Paso(copia.clone(), f, fi, 23, 21, "Subdesbordamiento: la cola esta vacia", op, null));
            return r;
        }

        r.add(new Paso(copia.clone(), f, fi, 25, 23, "¿frente == FIN? (" + f + " == " + fi + " -> " + (f == fi) + ")", op, null));

        if (f == fi) { // Caso en el que solo quedaba un elemento en la cola
            r.add(new Paso(copia.clone(), f, fi, 26, 24, "Limpiando COLA[" + f + "] <- Nulo", op, f));
            copia[f] = null;
            f = -1;
            fi = -1;
            r.add(new Paso(copia.clone(), f, fi, 27, 25, "Hacer frente <- -1, FIN <- -1 (cola vacia)", op, null));
        } else {
            r.add(new Paso(copia.clone(), f, fi, 30, 27, "¿frente + 1 == MAX? (" + (f + 1) + " == " + CAPACIDAD + ")", op, null));
            if (f + 1 == CAPACIDAD) { // Caso de vuelta circular del frente
                r.add(new Paso(copia.clone(), f, fi, 31, 28, "Limpiando COLA[" + f + "] <- Nulo", op, f));
                copia[f] = null;
                f = 0;
                r.add(new Paso(copia.clone(), f, fi, 32, 29, "Vuelta circular: Hacer frente <- 0", op, null));
            } else { // Caso general de avance lineal del frente
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

    /**
     * Genera y ejecuta los pasos correspondientes a la verificacion del estado de vacio de la cola.
     */
    private void verificarVacia() {
        if (timer.isRunning()) return; // Evita interrumpir animaciones activas
        List<Paso> r = new ArrayList<>();
        Integer[] copia = arreglo.clone();
        String op = "VERIFICAR VACIA()";
        boolean vacia = (frente == -1);

        r.add(new Paso(copia.clone(), frente, fin, 41, 38, "Llamando al algoritmo ColaVacia()", op, null));
        r.add(new Paso(copia.clone(), frente, fin, 42, 39, "¿frente == -1? (" + frente + " == -1) -> " + vacia, op, null));

        String msjFinal = vacia ? "RESULTADO: La cola SI esta vacia." : "RESULTADO: La cola NO esta vacia.";
        r.add(new Paso(copia.clone(), frente, fin, 42, 39, msjFinal, op, null));

        ejecutarOperacion(r);
    }

    /**
     * Genera y ejecuta los pasos correspondientes a la verificacion del estado de llenado de la cola.
     */
    private void verificarLlena() {
        if (timer.isRunning()) return;
        List<Paso> r = new ArrayList<>();
        Integer[] copia = arreglo.clone();
        String op = "VERIFICAR LLENA()";
        boolean llena = (frente == 0 && fin + 1 == CAPACIDAD) || (fin + 1 == frente);

        r.add(new Paso(copia.clone(), frente, fin, 46, 42, "Llamando al algoritmo ColaLlena()", op, null));
        r.add(new Paso(copia.clone(), frente, fin, 47, 43, "Verificando condicion de lleno -> " + llena, op, null));

        String msjFinal = llena ? "RESULTADO: La cola SI esta llena." : "RESULTADO: La cola NO esta llena.";
        r.add(new Paso(copia.clone(), frente, fin, 47, 43, msjFinal, op, null));

        ejecutarOperacion(r);
    }

    /**
     * Valida el campo de entrada e inicia el proceso de encolamiento si el valor es correcto.
     */
    private void intentarEncolar() {
        if (timer.isRunning()) return;
        String texto = campoDato.getText().trim();
        int valor;
        try {
            valor = Integer.parseInt(texto); // Intenta convertir el texto a entero
        } catch (NumberFormatException ex) {
            mensajeLabel.setText("Ingresa un numero entero valido para encolar");
            return;
        }
        campoDato.setText(""); // Limpia el campo de texto tras leer el dato
        ejecutarOperacion(generarPasosEncolar(valor));
    }

    /**
     * Inicia la ejecucion de la operacion de desencolado desde la interfaz.
     */
    private void intentarDesencolar() {
        if (timer.isRunning()) return;
        ejecutarOperacion(generarPasosDesencolar());
    }

    /**
     * Carga una nueva secuencia de pasos, actualiza los botones y arranca la reproduccion automatica.
     * 
     * @param nuevaSecuencia Lista de pasos generada para la accion requerida.
     */
    private void ejecutarOperacion(List<Paso> nuevaSecuencia) {
        pasos.clear();
        pasos.addAll(nuevaSecuencia);
        pasoActual = 0;
        actualizarVista();
        iniciarReproduccion();
        actualizarBotones();
    }

    /**
     * Avanza un paso en la animacion y detiene el temporizador si se llega al final.
     */
    private void avanzarUnPaso() {
        if (pasoActual < pasos.size() - 1) {
            pasoActual++;
            actualizarVista();
        } else {
            detenerReproduccion();
            actualizarBotones();
        }
    }

    /**
     * Inicia el temporizador de reproduccion si hay mas de un paso disponible.
     */
    private void iniciarReproduccion() {
        if (pasos.size() > 1) {
            timer.start();
        }
    }

    /**
     * Detiene la ejecucion del temporizador de animacion.
     */
    private void detenerReproduccion() {
        timer.stop();
    }

    /**
     * Habilita o desgrana los controles interactivos segun el estado de la animacion.
     */
    private void actualizarBotones() {
        boolean animando = timer.isRunning();
        btnEncolar.setEnabled(!animando);
        btnDesencolar.setEnabled(!animando);
        btnVacia.setEnabled(!animando);
        btnLlena.setEnabled(!animando);
        campoDato.setEnabled(!animando);
    }

    /**
     * Actualiza el texto de los mensajes informativos y solicita redibujar el lienzo.
     */
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

    /**
     * Retorna el objeto Paso correspondiente a la posicion actual de la animacion.
     * 
     * @return El paso actual o null si el indice no es valido.
     */
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

    /**
     * Componente grafico interno encargado de renderizar visualmente las celdas 
     * del arreglo, los punteros de frente y fin, y los estados de la cola circular.
     */
    private class Lienzo extends JPanel {
        
        /**
         * Constructor del lienzo grafico con dimensiones y color de fondo predefinidos.
         */
        Lienzo() {
            setBackground(Color.WHITE);
            setPreferredSize(new Dimension(420, 260));
        }

        /**
         * Metodo responsable de dibujar los elementos visuales de la cola circular 
         * (celdas, valores almacenados, indices y punteros).
         * 
         * @param g Objeto grafico para renderizar componentes 2D.
         */
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); // Activa el suavizado

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
                    g2.setColor(new Color(255, 225, 130)); // Resalta la celda activa con tono amarillo
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

                if (i == f) etiqueta(g2, x, y - 12, cajaAncho, "frente", new Color(200, 60, 60)); // Dibuja la etiqueta del frente
                if (i == fi) etiqueta(g2, x, y + cajaAlto + 30, cajaAncho, "FIN", new Color(30, 110, 190));    // Dibuja la etiqueta de fin
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

        /**
         * Metodo auxiliar para renderizar etiquetas indicadoras para los punteros de control.
         * 
         * @param g2     Objeto Graphics2D para dibujo.
         * @param x      Coordenada X base de la celda.
         * @param y      Coordenada Y de la etiqueta.
         * @param ancho  Ancho de la celda.
         * @param texto  Texto descriptivo del puntero ("frente" o "FIN").
         * @param color  Color asignado al texto indicador.
         */
        private void etiqueta(Graphics2D g2, int x, int y, int ancho, String texto, Color color) {
            g2.setColor(color);
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 11f));
            FontMetrics fm = g2.getFontMetrics();
            g2.drawString(texto, x + (ancho - fm.stringWidth(texto)) / 2, y);
        }
    }

    /**
     * Metodo utilitario para aclarar un color base segun un factor determinado.
     * 
     * @param c      Color base a modificar.
     * @param factor Factor de claridad (entre 0 y 1).
     * @return Un nuevo objeto Color mas claro.
     */
    private static Color clarear(Color c, float factor) {
        int r = (int) (c.getRed() + (255 - c.getRed()) * factor);
        int g = (int) (c.getGreen() + (255 - c.getGreen()) * factor);
        int b = (int) (c.getBlue() + (255 - c.getBlue()) * factor);
        return new Color(r, g, b);
    }
}