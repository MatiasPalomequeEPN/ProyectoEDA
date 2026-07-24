# Proyecto EDA — Visualizador de Colas

Visualizador interactivo de 4 tipos de cola (Simple, Circular, Prioridad, Doble/Deque)
para la materia de Estructuras de Datos y Algoritmos. Cada operación se anima
paso a paso y está sincronizada con el pseudocódigo: al hacer click en una línea,
la animación salta a ese momento exacto.

## Estado actual

Lo que existe ahora mismo es el **shell de navegación** — la plantilla visual
completa (sidebar, tabs de pseudocódigo/código, panel de créditos) ya funcional,
pero con animaciones **placeholder**. Cada quien reemplaza su placeholder por la
lógica real de su tipo de cola.

## Cómo correrlo

```bash
javac *.java
java MainFrame
```

O desde NetBeans: abrir el proyecto y `Run` normal.

---

## Estructura de archivos

```
MainFrame.java              ← ensambla todo, aquí se registran las 8 lecciones
Sidebar.java                ← navegación izquierda (4 temas x 3 items c/u)
Leccion.java                ← "molde" de datos de una pantalla
LeccionPanel.java           ← layout fijo: título + visualización + tabs
AnimacionPlaceholder.java   ← REEMPLAZAR por la animación real de cada cola
CreditosPanel.java          ← pantalla de créditos, estática
```

### `MainFrame.java`
Punto de entrada (`main()`). Arma la ventana completa:
- Crea el `CardLayout` que controla qué "pantalla" se muestra en el centro.
- Registra una `Leccion` por cada combinación tema/sub-tema, con la key
  `"tema{N}-sub{M}"` (ej. `"tema0-sub0"` = Cola Simple, Sub Tema 1).
- Conecta el `Sidebar` para que al hacer click, cambie de card
  (`cardLayout.show(panelCards, key)`).

**Método a tener en cuenta:** el `for` donde se arman las 8 lecciones (busca
`for (int t = 0; t < nombresTemas.length; t++)`). Ahí es donde cada quien
reemplaza el `AnimacionPlaceholder` de su tema por su panel real, y mete su
pseudocódigo/código de verdad en vez del `pseudoDemo`/`codigoDemo` genérico.

### `Sidebar.java`
Solo navegación, no tiene lógica de negocio. Recibe un arreglo de nombres de
tema y un callback (`Consumer<String>`) que se dispara con la key
correspondiente cuando haces click en un ítem. No debería necesitar cambios.

### `Leccion.java`
Clase de datos simple (record-like): agrupa título, el `JPanel` de
visualización, y los arrays de líneas de pseudocódigo/código. No tiene lógica,
solo transporta información hacia `LeccionPanel`.

### `LeccionPanel.java`
El layout compartido por las 8 pantallas: título arriba, visualización al
centro, `JTabbedPane` (Pseudocódigo/Código) a la derecha. **No debería
modificarse** salvo que cambien el diseño general — si cada quien lo edita
por separado van a tener conflictos de merge en el archivo que todos comparten.

**Método a tener en cuenta:** `construirPanelLineas(String[] lineas)` — genera
un `JLabel` clickeable por cada línea de pseudocódigo/código. Ahí está el
`MouseListener` con el comentario:
```java
// AQUÍ es donde luego llaman:
// pasoActual = primerPasoConLinea(numeroLinea);
// animacion.saltarAPaso(pasoActual);
```
Ese es el gancho para conectar el click del pseudocódigo con la animación real.

### `AnimacionPlaceholder.java`
**Este es el archivo que cada quien reemplaza por su propio tipo de cola.**
Ahora mismo solo mueve una cajita de un lado a otro con un `Timer` para
demostrar que la animación "vive". El patrón a seguir para la versión real:

1. Definir una clase `Paso` con el snapshot del estado de la cola en ese
   momento + qué línea de pseudocódigo le corresponde.
2. Generar una `List<Paso>` completa cuando se ejecuta una operación
   (`enqueue`, `dequeue`, etc.) — **no se anima "en vivo" mientras se calcula**,
   primero se calculan todos los pasos, luego se reproducen.
3. Un índice `pasoActual` que:
   - avanza solo con un `javax.swing.Timer` (para el modo "reproducir automático")
   - salta directo (sin animar) cuando el usuario clickea una línea de
     pseudocódigo — ahí no interpolas, pones el estado final de una vez.
4. `paintComponent(Graphics g)` siempre dibuja el estado de `pasoActual`,
   nunca "el estado real de la cola" directamente — así garantizas que la
   animación y el click al pseudocódigo siempre muestren lo mismo.

### `CreditosPanel.java`
Estático. Solo hay que reemplazar los nombres placeholder por los reales.

---

## Convención de Git

Cada tipo de cola vive en su propia rama y, cuando sea posible, en sus propios
archivos (ej. `ColaSimplePanel.java`, `ColaCircularPanel.java`) para minimizar
conflictos de merge:

```bash
git checkout -b cola-simple      # persona 1
git checkout -b cola-circular    # persona 2
git checkout -b cola-prioridad   # persona 3
git checkout -b cola-deque       # persona 4
```

El único archivo compartido de verdad es `MainFrame.java` (donde se registran
las 8 lecciones). Al editarlo, marcar el bloque propio con un comentario claro
(`// --- Cola Simple ---`) para que un conflicto de merge sea fácil de resolver
a simple vista.

## Pendiente

- [ ] Reemplazar `AnimacionPlaceholder` por la lógica real en cada tema (4x)
- [ ] Pseudocódigo y código reales por operación (actualmente solo hay un
      ejemplo de `enqueue` genérico repetido en las 8 lecciones)
- [ ] Conectar el click de pseudocódigo con `pasoActual` (ver gancho en
      `LeccionPanel.construirPanelLineas`)
- [ ] Nombres reales en `CreditosPanel.java`
