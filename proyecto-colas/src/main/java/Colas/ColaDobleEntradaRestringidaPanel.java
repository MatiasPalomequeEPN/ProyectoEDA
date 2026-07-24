package Colas;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Componente gráfico interactivo utilizado para visualizar y animar las operaciones 
 * de una Cola Doble con Entrada Restringida (se inserta por un solo extremo, pero se elimina por ambos).
 * Implementa la interfaz AnimacionInteractiva para sincronizarse con visualizadores de código.
 * Muestra paso a paso el cambio de estado del arreglo, los punteros frente/fin y el código ejecutado.
 */
public class ColaDobleEntradaRestringidaPanel extends JPanel implements AnimacionInteractiva {

    private static final int CAPACIDAD = 6; // Capacidad máxima del arreglo que simula la cola

    /** Pseudocódigo combinado (ColaDVacia + ColaDLlena + Insertar + EliminarIzquierda + EliminarDerecha), numeración global 1..N. */
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

    /** Código Java combinado (colaDVacia + colaDLlena + insertar + eliminarIzquierda + eliminarDerecha), numeración global 1..N. */
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

    private final Integer[] arreglo = new Integer[CAPACIDAD]; // Arreglo lógico que almacena los elementos
    private int frente = -1; // Puntero al primer elemento (para extracción por la izquierda)
    private int fin = -1;    // Puntero al último elemento (para inserción, y extracción por la derecha)

    private final List<Paso> pasos = new ArrayList<>(); // Almacena la secuencia de estados para animar una operación
    private int pasoActual = -1; // Índice del fotograma o paso actualmente en pantalla

    private final Color colorTema; // Color principal utilizado para estilizar el componente
    private final Timer timer;     // Temporizador para controlar el avance automático de los pasos

    private final JLabel mensajeLabel = new JLabel("Escribe un dato y elige una operacion", SwingConstants.CENTER); // Muestra el estado actual
    private final Lienzo lienzo = new Lienzo(); // Panel personalizado donde se dibujan las estructuras

    private final JTextField campoDato = new JTextField(6); // Campo de texto para ingresar el dato a encolar
    private final JButton btnInsertar = new JButton("Insertar"); // Botón para disparar la inserción
    private final JButton btnEliminarIzq = new JButton("Eliminar Izquierda"); // Botón para eliminar por el inicio
    private final JButton btnEliminarDer = new JButton("Eliminar Derecha");   // Botón para eliminar por el final

    /**
     * Constructor que inicializa el panel, configura su diseño y los controles de usuario.
     * 
     * @param colorTema Color base para la representación visual de esta estructura.
     */
    public ColaDobleEntradaRestringidaPanel(Color colorTema) {
        this.colorTema = colorTema; // Asigna el color temático

        setLayout(new BorderLayout()); // Usa BorderLayout para organizar el lienzo y los controles
        setBackground(Color.WHITE); // Fondo blanco por defecto
        add(lienzo, BorderLayout.CENTER); // Agrega el área de dibujo en el centro

        mensajeLabel.setFont(mensajeLabel.getFont().deriveFont(Font.ITALIC, 13f)); // Estiliza el texto de mensajes
        mensajeLabel.setBorder(BorderFactory.createEmptyBorder(4, 0, 6, 0)); // Agrega márgenes al mensaje

        JPanel controles = new JPanel(); // Panel contenedor para los controles interactivos
        controles.setBackground(Color.WHITE);
        controles.add(new JLabel("Dato:"));
        controles.add(campoDato);
        controles.add(btnInsertar);
        controles.add(btnEliminarIzq);
        controles.add(btnEliminarDer);

        JPanel sur = new JPanel(new BorderLayout()); // Panel inferior para agrupar mensajes y controles
        sur.setBackground(Color.WHITE);
        sur.add(mensajeLabel, BorderLayout.NORTH);
        sur.add(controles, BorderLayout.SOUTH);
        add(sur, BorderLayout.SOUTH); // Añade el panel compuesto a la parte inferior

        timer = new Timer(1500, e -> avanzarUnPaso()); // Configura el timer para avanzar de paso cada 1.5 segundos

        // Asigna los eventos de clic a los botones
        btnInsertar.addActionListener(e -> intentarInsertar());
        btnEliminarIzq.addActionListener(e -> intentarEliminar(true));
        btnEliminarDer.addActionListener(e -> intentarEliminar(false));

        actualizarVista(); // Renderiza el estado inicial
        actualizarBotones(); // Configura qué botones están activos
    }

    /**
     * Clase interna que representa un "fotograma" o estado de la cola en un momento específico.
     * Guarda una copia de todas las variables relevantes para poder reconstruir ese instante visualmente.
     */
    private static class Paso {
        final Integer[] arreglo;     // Copia de los datos de la cola en este instante
        final int frente;            // Valor del puntero frente en este instante
        final int fin;               // Valor del puntero fin en este instante
        final int lineaPseudo;       // Línea de pseudocódigo resaltada
        final int lineaCodigo;       // Línea de código Java resaltada
        final String mensaje;        // Texto descriptivo de lo que ocurre en este paso
        final String operacion;      // Nombre de la operación en curso
        final Integer indiceResaltado; // Índice de la casilla que debe destacarse visualmente, si aplica

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

    /**
     * Simula la operación de inserción y genera los pasos intermedios para la animación.
     * 
     * @param valor Dato entero que se desea insertar en la cola.
     * @return Lista de objetos Paso con cada estado de la simulación.
     */
    private List<Paso> generarPasosInsertar(int valor) {
        List<Paso> r = new ArrayList<>();
        Integer[] copia = arreglo.clone(); // Crea una copia del estado actual para simular sin afectar el real
        int f = frente, fi = fin;
        String op = "INSERTAR(" + valor + ")";

        r.add(new Paso(copia.clone(), f, fi, 19, 9, "Iniciando Insertar(" + valor + ")", op, null));
        r.add(new Paso(copia.clone(), f, fi, 21, 10, "Llamar ColaDLlena() para verificar...", op, null));
        r.add(new Paso(copia.clone(), f, fi, 12, 6, "¿Esta llena? (fin == " + (CAPACIDAD - 1) + " -> " + (fi == CAPACIDAD - 1) + ")", op, null));

        if (fi == CAPACIDAD - 1) { // Verifica la condición de desbordamiento antes de proceder
            r.add(new Paso(copia.clone(), f, fi, 23, 10, "Desbordamiento: no se puede insertar " + valor, op, null));
            return r; // Retorna prematuramente pues la cola está llena
        }

        r.add(new Paso(copia.clone(), f, fi, 25, 11, "fin < MAX-1 es verdadero", op, null));

        fi = fi + 1; // Mueve el puntero fin a la siguiente posición libre
        r.add(new Paso(copia.clone(), f, fi, 26, 12, "Hacer fin <- fin + 1  ->  fin = " + fi, op, null));

        copia[fi] = valor; // Inserta el dato en el arreglo
        r.add(new Paso(copia.clone(), f, fi, 27, 13, "Hacer COLA[" + fi + "] <- " + valor, op, fi));

        r.add(new Paso(copia.clone(), f, fi, 28, 14, "Verificando si frente == -1...", op, fi));

        if (f == -1) { // Si es el primer elemento insertado, se debe inicializar el frente
            f = 0;
            r.add(new Paso(copia.clone(), f, fi, 29, 14, "Hacer frente <- 0 (primer elemento)", op, fi));
        }

        r.add(new Paso(copia.clone(), f, fi, 33, 16, "Fin Algoritmo Insertar()", op, fi));

        // Aplica los cambios definitivos al estado real de la clase
        frente = f;
        fin = fi;
        arreglo[fi] = valor;

        return r;
    }

    /**
     * Simula la extracción de un elemento por el extremo izquierdo (frente) de la cola.
     * 
     * @return Lista de pasos generados para animar esta acción.
     */
    private List<Paso> generarPasosEliminarIzquierda() {
        List<Paso> r = new ArrayList<>();
        Integer[] copia = arreglo.clone();
        int f = frente, fi = fin;
        String op = "ELIMINAR IZQUIERDA()";

        r.add(new Paso(copia.clone(), f, fi, 35, 19, "Iniciando EliminarIzquierda()", op, null));
        r.add(new Paso(copia.clone(), f, fi, 37, 20, "¿Esta vacia? (frente != -1 -> " + (f != -1) + ")", op, null));

        if (f == -1) { // Si el frente es -1, no hay elementos para sacar
            r.add(new Paso(copia.clone(), f, fi, 46, 30, "Subdesbordamiento: la cola esta vacia", op, null));
            return r;
        }

        r.add(new Paso(copia.clone(), f, fi, 38, 21, "Hacer Dato <- COLA[" + f + "] (" + copia[f] + ")", op, f));
        copia[f] = null; // Borra visualmente el dato extraído

        r.add(new Paso(copia.clone(), f, fi, 39, 23, "¿frente == fin? (" + f + " == " + fi + " -> " + (f == fi) + ")", op, f));

        if (f == fi) { // Si frente y fin apuntan al mismo índice, era el último elemento
            f = -1;
            fi = -1;
            r.add(new Paso(copia.clone(), f, fi, 40, 24, "Hacer frente <- -1, fin <- -1 (cola quedo vacia)", op, null));
        } else {
            f = f + 1; // Mueve el puntero frente hacia la derecha
            r.add(new Paso(copia.clone(), f, fi, 43, 26, "Hacer frente <- frente + 1  ->  frente = " + f, op, null));
        }

        r.add(new Paso(copia.clone(), f, fi, 48, 28, "Fin Algoritmo EliminarIzquierda()", op, null));

        // Actualiza el estado real de la estructura con los resultados
        frente = f;
        fin = fi;
        System.arraycopy(copia, 0, arreglo, 0, CAPACIDAD);

        return r;
    }

    /**
     * Simula la extracción de un elemento por el extremo derecho (fin) de la cola.
     * 
     * @return Lista de pasos generados para animar esta acción.
     */
    private List<Paso> generarPasosEliminarDerecha() {
        List<Paso> r = new ArrayList<>();
        Integer[] copia = arreglo.clone();
        int f = frente, fi = fin;
        String op = "ELIMINAR DERECHA()";

        r.add(new Paso(copia.clone(), f, fi, 50, 33, "Iniciando EliminarDerecha()", op, null));
        r.add(new Paso(copia.clone(), f, fi, 52, 34, "¿Esta vacia? (frente != -1 -> " + (f != -1) + ")", op, null));

        if (f == -1) { // Verifica si hay elementos para extraer
            r.add(new Paso(copia.clone(), f, fi, 61, 44, "Subdesbordamiento: la cola esta vacia", op, null));
            return r;
        }

        r.add(new Paso(copia.clone(), f, fi, 53, 35, "Hacer Dato <- COLA[" + fi + "] (" + copia[fi] + ")", op, fi));
        copia[fi] = null; // Borra visualmente el elemento del extremo derecho

        r.add(new Paso(copia.clone(), f, fi, 54, 37, "¿frente == fin? (" + f + " == " + fi + " -> " + (f == fi) + ")", op, fi));

        if (f == fi) { // Caso de extraer el último elemento disponible
            f = -1;
            fi = -1;
            r.add(new Paso(copia.clone(), f, fi, 55, 38, "Hacer frente <- -1, fin <- -1 (cola quedo vacia)", op, null));
        } else {
            fi = fi - 1; // Retrocede el puntero fin (hacia la izquierda)
            r.add(new Paso(copia.clone(), f, fi, 58, 40, "Hacer fin <- fin - 1  ->  fin = " + fi, op, null));
        }

        r.add(new Paso(copia.clone(), f, fi, 63, 42, "Fin Algoritmo EliminarDerecha()", op, null));

        // Consolida los datos modificados en el arreglo de la clase
        frente = f;
        fin = fi;
        System.arraycopy(copia, 0, arreglo, 0, CAPACIDAD);

        return r;
    }

    // ---------- Disparo de operaciones desde la UI ----------

    /**
     * Valida el texto introducido y, si es correcto, dispara el inicio de la animación de inserción.
     */
    private void intentarInsertar() {
        if (timer.isRunning()) return; // Ignora clics adicionales si ya se está reproduciendo algo
        String texto = campoDato.getText().trim();
        int valor;
        try {
            valor = Integer.parseInt(texto); // Intenta convertir el texto a un número entero
        } catch (NumberFormatException ex) { // Captura el error si el texto no es un número válido
            mensajeLabel.setText("Ingresa un numero entero valido para insertar");
            return;
        }
        campoDato.setText(""); // Limpia el campo tras una entrada exitosa
        ejecutarOperacion(generarPasosInsertar(valor)); // Inicia la secuencia calculada
    }

    /**
     * Inicia la animación de eliminación basada en el extremo seleccionado.
     * @param izquierda true si se elimina por el frente, false si es por el final.
     */
    private void intentarEliminar(boolean izquierda) {
        if (timer.isRunning()) return; // Evita interrumpir una animación en curso
        ejecutarOperacion(izquierda ? generarPasosEliminarIzquierda() : generarPasosEliminarDerecha());
    }

    /**
     * Prepara el motor de animación reemplazando los pasos antiguos por la nueva secuencia y la arranca.
     * @param nuevaSecuencia Lista de pasos generada por alguna operación.
     */
    private void ejecutarOperacion(List<Paso> nuevaSecuencia) {
        pasos.clear(); // Limpia la lista de la animación anterior
        pasos.addAll(nuevaSecuencia); // Almacena la nueva secuencia de fotogramas
        pasoActual = 0; // Reinicia el contador al primer cuadro
        actualizarVista();
        iniciarReproduccion();
        actualizarBotones(); // Bloquea los botones para que no haya interferencias
    }

    /**
     * Lógica que ejecuta el Timer en cada ciclo. Avanza el índice de pasoActual o detiene el temporizador.
     */
    private void avanzarUnPaso() {
        if (pasoActual < pasos.size() - 1) { // Verifica si aún quedan pasos por mostrar
            pasoActual++;
            actualizarVista();
        } else {
            detenerReproduccion(); // Termina la animación al llegar al final
            actualizarBotones();
        }
    }

    /**
     * Activa el Timer de animación si hay más de un paso en la secuencia.
     */
    private void iniciarReproduccion() {
        if (pasos.size() > 1) {
            timer.start();
        }
    }

    /**
     * Interrumpe la animación actual deteniendo el Timer.
     */
    private void detenerReproduccion() {
        timer.stop();
    }

    /**
     * Habilita o deshabilita los controles de la UI dependiendo de si hay una animación en curso.
     */
    private void actualizarBotones() {
        boolean animando = timer.isRunning();
        btnInsertar.setEnabled(!animando);
        btnEliminarIzq.setEnabled(!animando);
        btnEliminarDer.setEnabled(!animando);
        campoDato.setEnabled(!animando); // Impide escribir en el campo de texto durante la animación
    }

    /**
     * Actualiza las etiquetas de texto de la UI y solicita al lienzo que se redibuje con el paso actual.
     */
    private void actualizarVista() {
        Paso p = pasoActualObj();
        if (p != null) mensajeLabel.setText("[" + p.operacion + "]  " + p.mensaje);
        lienzo.repaint(); // Llama al método paintComponent del subpanel
    }

    /**
     * Método auxiliar para recuperar el objeto Paso que corresponde al instante en curso de la animación.
     * @return El objeto Paso, o null si la lista está vacía o el índice es inválido.
     */
    private Paso pasoActualObj() {
        if (pasoActual >= 0 && pasoActual < pasos.size()) return pasos.get(pasoActual);
        return null;
    }

    @Override
    public void saltarALinea(int numeroLinea, boolean esPseudocodigo) {
        detenerReproduccion(); // Pausa la animación automática
        for (int i = 0; i < pasos.size(); i++) { // Recorre los pasos guardados buscando la línea pedida
            Paso p = pasos.get(i);
            int linea = esPseudocodigo ? p.lineaPseudo : p.lineaCodigo;
            if (linea == numeroLinea) { // Si encuentra coincidencia con la línea del click
                pasoActual = i; // Salta al fotograma correspondiente
                actualizarVista();
                iniciarReproduccion(); // Reanuda la animación desde ese punto
                actualizarBotones();
                return;
            }
        }
    }

    @Override
    public int lineaPseudoActual() {
        Paso p = pasoActualObj();
        return p != null ? p.lineaPseudo : -1; // Devuelve la línea de pseudocódigo resaltada actualmente
    }

    @Override
    public int lineaCodigoActual() {
        Paso p = pasoActualObj();
        return p != null ? p.lineaCodigo : -1; // Devuelve la línea de código Java resaltada actualmente
    }

    /**
     * Clase interna que se encarga del renderizado gráfico de la estructura (las casillas del arreglo, 
     * punteros y textos) utilizando Java 2D.
     */
    private class Lienzo extends JPanel {
        Lienzo() {
            setBackground(Color.WHITE); // Fondo blanco limpio para el área de dibujo
            setPreferredSize(new Dimension(420, 260)); // Establece un tamaño base recomendado
        }

        /**
         * Método sobrescrito encargado de renderizar todos los componentes gráficos del diagrama.
         * 
         * @param g Objeto base usado para dibujar primitivas gráficas.
         */
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g); // Limpia y prepara el lienzo base
            Graphics2D g2 = (Graphics2D) g; // Cast a Graphics2D para métodos avanzados y mejor calidad
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); // Activa el suavizado de bordes

            // Obtiene los datos a dibujar (usa el paso en curso si existe, o el arreglo global si está en reposo)
            Paso p = pasoActualObj();
            Integer[] datos = p != null ? p.arreglo : arreglo;
            int f = p != null ? p.frente : frente;
            int fi = p != null ? p.fin : fin;
            Integer resaltado = p != null ? p.indiceResaltado : null;

            int cajaAncho = 55, cajaAlto = 50, espacio = 10; // Configuraciones de dimensiones para los recuadros
            int anchoTotal = CAPACIDAD * (cajaAncho + espacio) - espacio; // Calculo total de la cuadrícula
            int startX = Math.max(20, (getWidth() - anchoTotal) / 2); // Centrado horizontal
            int y = 90; // Posición fija vertical de la estructura

            g2.setColor(Color.GRAY);
            g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 12f));
            g2.drawString("Cola Doble - Entrada Restringida (capacidad " + CAPACIDAD + ")", 15, 22); // Título en el panel

            Color relleno = clarear(colorTema, 0.72f); // Genera una versión más suave del color para las celdas ocupadas

            for (int i = 0; i < CAPACIDAD; i++) { // Bucle para dibujar cada casilla del arreglo
                int x = startX + i * (cajaAncho + espacio);

                // Determina el color de fondo de la celda (resaltada, ocupada o vacía)
                if (resaltado != null && resaltado == i) {
                    g2.setColor(new Color(255, 225, 130)); // Tono amarillo para indicar la casilla en foco de operación
                } else {
                    g2.setColor(datos[i] != null ? relleno : Color.WHITE);
                }
                
                g2.fillRoundRect(x, y, cajaAncho, cajaAlto, 10, 10); // Rellena el fondo con esquinas redondeadas
                g2.setColor(Color.DARK_GRAY);
                g2.drawRoundRect(x, y, cajaAncho, cajaAlto, 10, 10); // Dibuja el borde exterior

                if (datos[i] != null) { // Si hay un dato guardado, lo dibuja en el centro
                    g2.setFont(g2.getFont().deriveFont(Font.BOLD, 16f));
                    FontMetrics fm = g2.getFontMetrics(); // Objeto para calcular tamaños de texto
                    String texto = String.valueOf(datos[i]);
                    g2.setColor(Color.BLACK);
                    g2.drawString(texto, x + (cajaAncho - fm.stringWidth(texto)) / 2, y + cajaAlto / 2 + 6); // Dibuja el número centrado
                }

                g2.setColor(Color.GRAY);
                g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 11f));
                g2.drawString("[" + i + "]", x + cajaAncho / 2 - 8, y + cajaAlto + 16); // Dibuja el índice debajo del cuadro

                // Dibuja las etiquetas de los punteros si coinciden con esta posición
                if (i == f) etiqueta(g2, x, y - 12, cajaAncho, "frente (solo sale)", new Color(200, 60, 60)); // Rojo para el frente
                if (i == fi) etiqueta(g2, x, y + cajaAlto + 30, cajaAncho, "fin (entra/sale)", new Color(30, 110, 190)); // Azul para el fin
            }

            g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 12f));
            g2.setColor(Color.DARK_GRAY);
            String estado = "frente = " + f + "   |   fin = " + fi;
            g2.drawString(estado, startX, y + cajaAlto + 55); // Muestra los valores lógicos de los punteros abajo

            if (!pasos.isEmpty()) { // Si hay una animación activa, muestra en qué paso se encuentra
                g2.setColor(colorTema.darker());
                g2.setFont(g2.getFont().deriveFont(Font.BOLD, 12f));
                String etiquetaOp = p != null ? p.operacion : "";
                g2.drawString(etiquetaOp + "   -   Paso " + (pasoActual + 1) + " / " + pasos.size(), startX, y + cajaAlto + 80);
            }
        }

        /**
         * Método auxiliar para centralizar y dibujar un pequeño texto con color (usado para los punteros).
         */
        private void etiqueta(Graphics2D g2, int x, int y, int ancho, String texto, Color color) {
            g2.setColor(color);
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 11f));
            FontMetrics fm = g2.getFontMetrics();
            g2.drawString(texto, x + (ancho - fm.stringWidth(texto)) / 2, y); // Calcula el desplazamiento para centrar el texto respecto a su caja
        }
    }

    /**
     * Calcula una versión aclarada de un color para usarlo como relleno tenue.
     * 
     * @param c Color base original.
     * @param factor Porcentaje decimal (0.0 a 1.0) para acercar el color hacia blanco.
     * @return El nuevo color interpolado.
     */
    private static Color clarear(Color c, float factor) {
        int r = (int) (c.getRed() + (255 - c.getRed()) * factor); // Interpola el canal rojo hacia 255 (blanco)
        int g = (int) (c.getGreen() + (255 - c.getGreen()) * factor); // Interpola el canal verde hacia 255 (blanco)
        int b = (int) (c.getBlue() + (255 - c.getBlue()) * factor); // Interpola el canal azul hacia 255 (blanco)
        return new Color(r, g, b); // Retorna la nueva mezcla RGB
    }
}