package main.kotlin.es.dam.psp.ut2.descargas

import java.lang.Thread.sleep
import kotlin.concurrent.thread

fun main(){
    val descargas = listOf(
        Descargas("fotos",100),
        Descargas("video",25),
        Descargas("musica",800),
    )


    descargas.forEach { println("${it.name}: ${it.state}")}

    val inicio = System.currentTimeMillis()




    descargas.forEach { it.start() }
    println("Descargas lanzadas")

    descargas.forEach { println("${it.name}: ${it.state}")}
    Thread.sleep(200)
    descargas.forEach { println("${it.name}: ${it.state}")}

    thread(isDaemon = true,name = "usuatio"){
       // Thread.sleep(3000)
       // descargas[2].interrupt()

        while (true) {
            val linea = readlnOrNull() ?: break
            val numero = linea.trim().toIntOrNull()
            if (numero != null && numero in 0 until descargas.size) descargas[numero].interrupt()
        }
    }


    while (descargas.any { it.isAlive}){
        Thread.sleep(500)
        println(descargas.joinToString(" | "){"${it.name} ${it.porcentaje}% ${it.state}"})
    }




    //descargas.forEach { it.join() }
    println("Descargas terminadas en ${System.currentTimeMillis() - inicio} ms")

}
