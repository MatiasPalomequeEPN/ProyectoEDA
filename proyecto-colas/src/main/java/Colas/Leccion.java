package Colas;
import javax.swing.*;

/**
 * Contiene todo lo que necesita una pantalla de "Sub Tema":
 * título, panel de visualización, líneas de pseudocódigo y de código.
 * Cada tipo de cola solo necesita construir una lista de estas.
 */
public class Leccion {
    public final String titulo;
    public final JPanel panelVisualizacion;
    public final String[] lineasPseudocodigo;
    public final String[] lineasCodigo;

    public Leccion(String titulo, JPanel panelVisualizacion,
                    String[] lineasPseudocodigo, String[] lineasCodigo) {
        this.titulo = titulo;
        this.panelVisualizacion = panelVisualizacion;
        this.lineasPseudocodigo = lineasPseudocodigo;
        this.lineasCodigo = lineasCodigo;
    }
}
