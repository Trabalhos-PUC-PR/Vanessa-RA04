import kotlin.random.Random
import org.jetbrains.letsPlot.export.ggsave
import org.jetbrains.letsPlot.geom.geomQQ
import org.jetbrains.letsPlot.geom.geomQQLine
import org.jetbrains.letsPlot.label.ggtitle
import org.jetbrains.letsPlot.letsPlot

// variaveis para definir a distancia das rotas
val rota_direta_distancia = 6.24
val rota_alternativa_distancia = 7.3
val rota_segura_distancia = 7.8

// variaveis para definir o consumo de combustivel do carro, e o consumo parado no trânsito
val kmPorLitro = 12.0
val modificadorPorMinutoParado = 0.1

var temposRotaDireta = ArrayList<Int>()
var consumosRotaDireta = ArrayList<Double>()

var temposRotaAlternativa = ArrayList<Int>()
var consumosRotaAlternativa = ArrayList<Double>()

var temposRotaSegura = ArrayList<Int>()
var consumosRotaSegura = ArrayList<Double>()

fun main() {

    for (i in 1..3) {

        temposRotaDireta = ArrayList()
        consumosRotaDireta = ArrayList()

        temposRotaAlternativa = ArrayList()
        consumosRotaAlternativa = ArrayList()

        temposRotaSegura = ArrayList()
        consumosRotaSegura = ArrayList()

        var nomeCenario = "PRIMEIRO"
        when (i) {
            2 -> {nomeCenario = "SEGUNDO"}
            3 -> {nomeCenario = "TERCEIRO"}
        }

        val numeroSimulacoes = 100

        // repete os comandos dentro das {chaves} n vezes
        repeat(numeroSimulacoes) {
            simulaRotaDireta(i)
            simulaRotaAlternativa(i)
            simulaRotaSegura()
        }

        // soma todos os tempos e multiplica pelo peso, e então, salva em sua variavel respectiva
        val pesoTemposRotaDireta = temposRotaDireta.sum() * 0.15
        // faz a mesma coisa para o consumo
        val pesoConsumoRotaDireta = consumosRotaDireta.sum() * 0.85
        val valorRotaDireta = pesoTemposRotaDireta + pesoConsumoRotaDireta

        val pesoTemposRotaAlternativa = temposRotaAlternativa.sum() * 0.15
        val pesoConsumoRotaAlternativa = consumosRotaAlternativa.sum() * 0.85
        val valorRotaAlternativa = pesoTemposRotaAlternativa + pesoConsumoRotaAlternativa

        val pesoTemposRotaSegura = temposRotaSegura.sum() * 0.15
        val pesoConsumoRotaSegura = consumosRotaSegura.sum() * 0.85
        val valorRotaSegura = pesoTemposRotaSegura + pesoConsumoRotaSegura

        println("\n$nomeCenario CENARIO")
        var normalizado = valorRotaDireta + valorRotaAlternativa + valorRotaSegura
        println("Valor geral rota direta: %.2f".format(valorRotaDireta / normalizado))
        println("Valor geral rota alternativa: %.2f".format(valorRotaAlternativa / normalizado))
        println("Valor geral rota segura: %.2f".format(valorRotaSegura / normalizado))
        println()
        normalizado = pesoTemposRotaDireta + pesoTemposRotaAlternativa + pesoTemposRotaSegura
        println("Valor tempo rota direta: %.2f".format(pesoTemposRotaDireta / normalizado))
        println("Valor tempo rota alternativa: %.2f".format(pesoTemposRotaAlternativa / normalizado))
        println("Valor tempo rota segura: %.2f".format(pesoTemposRotaSegura / normalizado))
        println()
        normalizado = pesoConsumoRotaDireta + pesoConsumoRotaAlternativa + pesoConsumoRotaSegura
        println("Valor consumo combustível rota direta: %.2f".format(pesoConsumoRotaDireta / normalizado))
        println("Valor consumo combustível rota alternativa: %.2f".format(pesoConsumoRotaAlternativa / normalizado))
        println("Valor consumo combustível rota segura: %.2f".format(pesoConsumoRotaSegura / normalizado))

        plotQQ(consumosRotaDireta, "$nomeCenario-consumoRotaDireta")
        plotQQ(temposRotaDireta, "$nomeCenario-temposRotaDireta")
        plotQQ(consumosRotaAlternativa, "$nomeCenario-consumosRotaAlternativa")
        plotQQ(temposRotaAlternativa, "$nomeCenario-temposRotaAlternativa")
        plotQQ(consumosRotaSegura, "$nomeCenario-consumosRotaSegura")
        plotQQ(temposRotaSegura, "$nomeCenario-temposRotaSegura")
    }

}

// função que gera um grafico QQ, dada entrada, com saída utilizando o nome epecificado
fun plotQQ(ys: List<Number>, outputName: String, title: String = "", subTitle: String = "") {
    val data = mapOf<String, Any>("x" to List(ys.size){ it+1 }, "y" to ys)
    val fig = letsPlot(data) {sample = ys} + geomQQ(size = 3, alpha = .3) + geomQQLine(size = 1) + ggtitle(title, subTitle)
    ggsave(fig, "$outputName.png")
}

// gera um valor aleatório entre 0(inclusive) e 1(inclusive)
fun getRandomBetween0And1(): Double {
    return Random.nextDouble(0.0, 101.0) / 100.0
}

// simula a rota direta, calculando o tempo total gasto, o tempo parado, e o total de consumo de combustivel
fun simulaRotaDireta(indexCenario: Int){
    var tempoMinimo = 12;
    if(indexCenario == 2) {
        tempoMinimo = 14
    } else if(indexCenario == 3){

    }
    val tempo = getTempoRotaDireta(indexCenario)
    val tempoParado = (tempo-tempoMinimo)
    val consumoFinal = (rota_direta_distancia/kmPorLitro)+modificadorPorMinutoParado*tempoParado
    temposRotaDireta.add(tempo)
    consumosRotaDireta.add(consumoFinal)
}

// basicamente o mesmo calculo da função de cima
fun simulaRotaAlternativa(indexCenario: Int){
    var tempoMinimo = 17;
    if(indexCenario == 2) {
        tempoMinimo = 20
    } else if(indexCenario == 3){

    }
    val tempo = getTempoRotaAlternativa(indexCenario)
    val tempoParado = (tempo-tempoMinimo)
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


fun getTempoRotaDireta(indexCenario: Int): Int{
    val prob = getRandomBetween0And1()
    when(indexCenario) {
        1-> {
            return if (prob <= 0.1) {
                24;
            } else if (prob <= 0.3) {
                17;
            } else {
                12;
            }
        }

        2-> {
            return if (prob <= 0.2) {
                26;
            } else if (prob <= 0.5) {
                19;
            } else {
                14;
            }
        }

        else -> {
            return if (prob <= 0.05) {
                40;
            } else if (prob <= 0.1) {
                35;
            } else {
                12;
            }
        }
    }
}

fun getTempoRotaAlternativa(indexCenario: Int): Int{
    val prob = getRandomBetween0And1()

    when(indexCenario) {
        1-> {
            return if (prob <= 0.03) {
                20;
            } else {
                17;
            }
        }

        2-> {
            return if (prob <= 0.1) {
                25;
            } else {
                20;
            }
        }
        else -> {
            return if (prob <= 0.05) {
                60;
            } else {
                17;
            }
        }
    }
}
fun getTempoRotaSegura(): Int{
    return 20
}