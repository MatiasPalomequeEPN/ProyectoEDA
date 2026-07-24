package Colas;

/**
 * Contrato que debe implementar cada panel de animación REAL (el que
 * reemplaza a AnimacionPlaceholder) para poder sincronizarse con el
 * pseudocódigo/código.
 *
 * - saltarALinea: cuando el usuario clickea una línea, salta de una vez
 *   al primer Paso calculado para esa línea (sin animar/interpolar).
 *   esPseudocodigo indica si el click vino de la pestaña PSEUDOCÓDIGO
 *   o de la pestaña CÓDIGO, porque cada una tiene su propia numeración.
 * - lineaPseudoActual / lineaCodigoActual: LeccionPanel las consulta
 *   periódicamente mientras la animación corre sola, para resaltar en
 *   vivo la línea que se está ejecutando en cada pestaña.
 */
public interface AnimacionInteractiva {
    void saltarALinea(int numeroLinea, boolean esPseudocodigo);
    int lineaPseudoActual();
    int lineaCodigoActual();
}
