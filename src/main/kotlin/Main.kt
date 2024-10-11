package com.krld

import EnemyDto
import State
import TransportCommandDto
import Ui
import WorldStateDto
import java.io.File
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import javax.swing.Timer


var lastAction: ActionPlanned? = null

class ActionPlanned(var transports: List<TransportCommandDto> = emptyList()) {

}

lateinit var state: State
val ui = Ui()

@Volatile
var apiToken = ""

val logicThread = Executors.newSingleThreadScheduledExecutor()


val TICKS_PER_SECOND = 20


@Volatile
var currentWorldState: WorldStateDto? = null

fun main(args: Array<String>) {
    logicThread.execute {
        apiToken = args.first()
        realMain()
    }

    logicThread.awaitTermination(Int.MAX_VALUE.toLong(), TimeUnit.DAYS)
}

private fun realMain() {
    println("Hello World!")
    drawUi()


    loadState()

    globalLoop()
}

private fun globalLoop() {
    while (true) {
        runCatching { doLoop() }
            .onFailure {
                logicThread.schedule({ globalLoop() }, 1, TimeUnit.SECONDS)
                System.err.println("doLoop failed " + it.stackTraceToString())
                return
            }
    }
}

fun doLoop() {
    val roundsInfo = Api.getRounds()

    val rounds = roundsInfo.rounds.sortedBy { it.getStartAsLong() }
    val activeRound = rounds.firstOrNull { it.status == "active" }


    actualStrategy()
}

fun actualStrategy() {
    val myAction = doSimpleGuy()
    lastAction = myAction
    currentWorldState = Api.move(myAction.transports)


    logicThread.schedule({ actualStrategy() }, 200, TimeUnit.MILLISECONDS)
}

private fun doSimpleGuy(): ActionPlanned {
    val w = currentWorldState

    if (w == null) {
        return ActionPlanned(emptyList<TransportCommandDto>())
    }

    val actionPlanned = ActionPlanned()

    val maxAttackRange = 200

    val result = mutableListOf<TransportCommandDto>()
    w.transports.forEach { myShip ->

        var attack: EnemyDto? = null
        if (myShip.attackCooldownMs == 0) {
            attack = w.enemies.filter { it.pos.distance(myShip.pos) < maxAttackRange }.minByOrNull { it.health }
        }

        val closestBounty = w.bounties.minByOrNull { it.pos.sqDistance(myShip.pos) }?.pos
        val target = closestBounty ?: w.mapSize.copy().mul(0.5)
        val acceleration = target.copy().minus(myShip.pos).normalize().mul(w.maxAccel)
        result.add(
            TransportCommandDto(
                acceleration = acceleration,
                activateShield = myShip.shieldCooldownMs == 0 && w.enemies.any { it.pos.distance(myShip.pos) < 200 },
                attack = attack?.pos?.toVec2(),
                id = myShip.id
            )
        )
    }

    actionPlanned.transports = result
    return actionPlanned

}

fun loadState() {
    File("state.json").takeIf { it.exists() }?.let {
        val parsedState = Api.gson.fromJson(it.readText(), State::class.java)
        state = parsedState
    } ?: run {
        state = State()
    }
}

fun drawUi() {
    ui.setup()
    loop()
}

fun loop() {
    val timer = Timer(32) { e ->
        // Code to execute after delay
        ui.redraw()
    }
    timer.isRepeats = true
    timer.start()
}


fun log(s: String?) {
    println(Date().toString() + " " + s)
}
