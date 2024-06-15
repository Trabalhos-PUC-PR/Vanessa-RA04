import kotlin.random.Random
import org.jetbrains.letsPlot.export.ggsave
import org.jetbrains.letsPlot.geom.geomBar
import org.jetbrains.letsPlot.geom.geomQQ
import org.jetbrains.letsPlot.geom.geomQQLine
import org.jetbrains.letsPlot.ggplot
import org.jetbrains.letsPlot.label.ggtitle
import org.jetbrains.letsPlot.letsPlot

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

    val pesoTemposRotaDireta = temposRotaDireta.sum()*0.15
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
    plotQQ(consumosRotaDireta, "consumoRotaDireta")
    plotQQ(temposRotaDireta, "temposRotaDireta")
    println()
    normalizado = pesoTemposRotaDireta+pesoTemposRotaAlternativa+pesoTemposRotaSegura
    println("Valor consumo combustível rota direta: %.2f".format(pesoTemposRotaDireta/normalizado))
    println("Valor consumo combustível rota alternativa: %.2f".format(pesoTemposRotaAlternativa/normalizado))
    println("Valor consumo combustível rota segura: %.2f".format(pesoTemposRotaSegura/normalizado))
    plotQQ(consumosRotaAlternativa, "consumosRotaAlternativa")
    plotQQ(temposRotaAlternativa, "temposRotaAlternativa")
    println()
    normalizado = pesoConsumoRotaDireta+pesoConsumoRotaAlternativa+pesoConsumoRotaSegura
    println("Valor tempo rota direta: %.2f".format(pesoConsumoRotaDireta/normalizado))
    println("Valor tempo rota alternativa: %.2f".format(pesoConsumoRotaAlternativa/normalizado))
    println("Valor tempo rota segura: %.2f".format(pesoConsumoRotaSegura/normalizado))
    plotQQ(consumosRotaSegura, "consumosRotaSegura")
    plotQQ(temposRotaSegura, "temposRotaSegura")

}

fun plotQQ(ys: List<Number>, outputName: String, title: String = "", subTitle: String = "") {
    val data = mapOf<String, Any>("x" to List(ys.size){ it+1 }, "y" to ys)
    val fig = letsPlot(data) {sample = ys} + geomQQ(size = 3, alpha = .3) + geomQQLine(size = 1) + ggtitle(title, subTitle)
    ggsave(fig, "$outputName.png")
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