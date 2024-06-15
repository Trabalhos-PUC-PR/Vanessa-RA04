import kotlin.random.Random

val rota_direta_distancia = 6.24
val rota_alternativa_distancia = 7.3
val rota_segura_distancia = 7.8

val kmPorLitro = 12.0
val modificadorPorMinutoParado = 0.1

var temposRotaDireta = ArrayList<Int>()
var consumosRotaDireta = ArrayList<Double>()

var temposRotaAlternativa = ArrayList<Int>()
var consumosRotaAlternativa = ArrayList<Double>()

var temposRotaSegura = ArrayList<Int>()
var consumosRotaSegura = ArrayList<Double>()

fun main() {
    val numeroSimulacoes = 100

    repeat(numeroSimulacoes) {
        simulaRotaDireta()
        simulaRotaAlternativa()
        simulaRotaSegura()
    }

    val pesoTemposRotaDireta = temposRotaDireta.reduce{ acc, i -> acc+i }*0.15
    val pesoConsumoRotaDireta = consumosRotaDireta.reduce{ acc, i -> acc+i }*0.85
    val valorRotaDireta = pesoTemposRotaDireta+pesoConsumoRotaDireta

    val pesoTemposRotaAlternativa = temposRotaAlternativa.reduce{ acc, i -> acc+i }*0.15
    val pesoConsumoRotaAlternativa = consumosRotaAlternativa.reduce{ acc, i -> acc+i }*0.85
    val valorRotaAlternativa = pesoTemposRotaAlternativa+pesoConsumoRotaAlternativa

    val pesoTemposRotaSegura = temposRotaSegura.reduce{ acc, i -> acc+i }*0.15
    val pesoConsumoRotaSegura = consumosRotaSegura.reduce{ acc, i -> acc+i }*0.85
    val valorRotaSegura = pesoTemposRotaSegura+pesoConsumoRotaSegura

    var normalizado = valorRotaDireta+valorRotaAlternativa+valorRotaSegura
    println("Valor geral rota direta: %.2f".format(valorRotaDireta/normalizado))
    println("Valor geral rota alternativa: %.2f".format(valorRotaAlternativa/normalizado))
    println("Valor geral rota segura: %.2f".format(valorRotaSegura/normalizado))
    println()
    normalizado = pesoTemposRotaDireta+pesoTemposRotaAlternativa+pesoTemposRotaSegura
    println("Valor consumo combustível rota direta: %.2f".format(pesoTemposRotaDireta/normalizado))
    println("Valor consumo combustível rota alternativa: %.2f".format(pesoTemposRotaAlternativa/normalizado))
    println("Valor consumo combustível rota segura: %.2f".format(pesoTemposRotaSegura/normalizado))
    println()
    normalizado = pesoConsumoRotaDireta+pesoConsumoRotaAlternativa+pesoConsumoRotaSegura
    println("Valor tempo rota direta: %.2f".format(pesoConsumoRotaDireta/normalizado))
    println("Valor tempo rota alternativa: %.2f".format(pesoConsumoRotaAlternativa/normalizado))
    println("Valor tempo rota segura: %.2f".format(pesoConsumoRotaSegura/normalizado))

}

enum class Rota {
    DIRETA, ALTERNATIVA, SEGURA
}
fun getRotaEscolhida(): Rota{
    val rota = Random.nextInt(0, 3)
    return when (rota) {
        0 -> Rota.DIRETA
        1 -> Rota.ALTERNATIVA
        else -> Rota.SEGURA
    }
}

fun getRandomBetween0And1(): Double {
    return Random.nextDouble(0.0, 101.0) / 100.0
}

fun simulaRotaDireta(){
    val tempo = getTempoRotaDireta()
    val tempoParado = (tempo-12)
    val consumoFinal = (rota_direta_distancia/kmPorLitro)+modificadorPorMinutoParado*tempoParado
    temposRotaDireta.add(tempo)
    consumosRotaDireta.add(consumoFinal)
}

fun simulaRotaAlternativa(){
    val tempo = getTempoRotaAlternativa()
    val tempoParado = (tempo-17)
    val consumoFinal = (rota_alternativa_distancia/kmPorLitro)+modificadorPorMinutoParado*tempoParado
    temposRotaAlternativa.add(tempo)
    consumosRotaAlternativa.add(consumoFinal)
}

fun simulaRotaSegura(){
    val tempo = getTempoRotaSegura()
    val consumoFinal = (rota_segura_distancia/kmPorLitro)
    temposRotaSegura.add(tempo)
    consumosRotaSegura.add(consumoFinal)
}


fun getTempoRotaDireta(): Int{
    val prob = getRandomBetween0And1()
    return if( prob <= 0.1 ) {
        24;
    } else if( prob <= 0.3 ) {
        17;
    } else {
        12;
    }
}

fun getTempoRotaAlternativa(): Int{
    val prob = getRandomBetween0And1()
    return if( prob <= 0.03 ) {
        20;
    } else {
        17;
    }
}
fun getTempoRotaSegura(): Int{
    return 20;
}