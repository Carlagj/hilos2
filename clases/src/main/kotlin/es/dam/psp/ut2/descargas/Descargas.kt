package main.kotlin.es.dam.psp.ut2.descargas

import kotlin.random.Random

class Descargas(nombre:String, val tamanoMb: Int) :Thread(nombre) {

    @Volatile var descargados = 0
    private set

    val porcentaje : Int
    get() = descargados * 100 / tamanoMb

    override fun run() {
        try {


            while (descargados < tamanoMb) {

                sleep(Random.nextLong(100, 301))
                descargados++
            }
        }catch (e: InterruptedException){
            println("Descarga interrumpida (${name})${e.message} ")
        }

    }
}