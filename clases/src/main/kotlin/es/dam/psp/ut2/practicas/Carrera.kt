package es.dam.psp.ut2.practicas

import kotlin.random.Random

/**
 * Práctica 1 · Carrera de hilos. Código de partida: completa los TODO (sus letras son las de
 * los apartados del enunciado).
 *
 * Apuntes UT-2, apartados 2 (crear, esperar e interrumpir), 3 (estados) y 6.1 (synchronized).
 * Ejecutar: ▶ junto a `main`. Mientras falten los TODO, lanza NotImplementedError.
 *
 * Cada corredor es un hilo que avanza entre 1 y 10 metros cada 100-300 ms hasta llegar a 100 m.
 * La Meta registra el orden de llegada. Cuando han llegado 3 corredores (podio), el juez
 * interrumpe a los demás, que deben retirarse indicando cuántos metros llevaban.
 */

const val DISTANCIA = 100
const val PODIO = 3

class Meta {
    private val llegadas = mutableListOf<String>()

    /**
     * TODO a: registra la llegada de [nombre] y devuelve su puesto (1, 2, 3...).
     * Varios hilos llaman a este método a la vez: protégelo.
     */
    fun llegar(nombre: String): Int {
        synchronized(this) {
            llegadas.add(nombre)
            return llegadas.size
        }
    }

    /** Número de corredores que han llegado. También debe estar protegido. */
    fun llegados(): Int {
        synchronized(this) {
            return llegadas.size
        }
    }

    /** Copia de la lista de llegadas, hecha dentro del cerrojo para que nadie la cambie a la vez. */
    fun clasificacion(): List<String> = synchronized(this) { llegadas.toList() }
}

class Corredor(nombre: String, private val meta: Meta) : Thread(nombre) {
    var metros = 0
        private set                 // solo el propio corredor cambia sus metros; el juez los lee

    override fun run() {
        try {
            // TODO b: bucle de carrera
            while (metros < DISTANCIA) {

                // Dormir entre 100 y 300 milisegundos
                Thread.sleep(Random.nextLong(100, 301))

                // Avanzar entre 1 y 10 metros
                val avance = Random.nextInt(1, 11)

                metros = (metros + avance).coerceAtMost(DISTANCIA)
            }

            // Registrar llegada
            val puesto = meta.llegar(name)

            println("$name llega en el puesto $puesto")

        } catch (e: InterruptedException) {

            // Si el juez interrumpe al corredor
            println("$name se retira en $metros m")
        }
    }
}




fun main() {
    val meta = Meta()
    val corredores = listOf("Ana", "Bruno", "Carla", "David", "Elena", "Fran").map { Corredor(it, meta) }

    corredores.forEach {
        it.start()
    }

    // El hilo principal hace de juez
    while (meta.llegados() < PODIO) {

        Thread.sleep(500)

        println("\n--- ESTADO DE LA CARRERA ---")

        corredores.forEach {
            println("${it.name}: ${it.state} - ${it.metros} metros")
        }
    }

    println("\n¡PODIO COMPLETO!")

    // Interrumpir a los corredores que siguen activos
    corredores.forEach {
        if (it.isAlive) {
            it.interrupt()
        }
    }

    // TODO d: esperar a que terminen todos los hilos
    corredores.forEach {
        it.join()
    }

    // Mostrar clasificación final
    println("\n========== CLASIFICACIÓN FINAL ==========")

    meta.clasificacion().forEachIndexed { index, nombre ->
        println("${index + 1}º puesto: $nombre")
    }

    println("=========================================")
}
