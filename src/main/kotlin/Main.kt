package com.krld

import Ui
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import javax.swing.Timer


val ui = Ui()

@Volatile
var apiToken = ""

val logicThread = Executors.newSingleThreadScheduledExecutor()

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
