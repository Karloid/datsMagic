import java.awt.Color
import java.awt.Graphics
import javax.swing.*


val CANVAS_SIZE = 1024

class Ui {

    val frame = JFrame("DatsMagic")
    val infoLabel = JTextArea("Info")

    val canvasPanel: JPanel = object : JPanel() {
        override fun paintComponent(g: Graphics) {
            super.paintComponent(g)
            g.color = Color.WHITE
            g.fillRect(0, 0, this.width, this.height) // Draw white background

            val w = currentWorldState
            val a = lastAction

            if (w == null) {
                g.color = Color.RED
                g.fillOval(50, 50, (200 * Math.random()).toInt(), 200) // Draw red circle
                return
            }

            val k = 1024 / w.mapSize.max()


            w.anomalies.forEach {
                val baseColor = if (it.strength > 0) Color.GREEN else Color.PINK
                g.color = baseColor.withAlpha(0.3)
                val radius = it.radius * k
                val effectiveRadius = it.effectiveRadius * k

                g.fillOval(((it.pos.x * k).toInt() - radius / 2).toInt(), ((it.pos.y * k).toInt() - radius / 2).toInt(), radius.toInt(), radius.toInt())

                g.color = baseColor.withAlpha(0.3)
                g.fillOval(((it.pos.x * k).toInt() - effectiveRadius / 2).toInt(), ((it.pos.y * k).toInt() - effectiveRadius / 2).toInt(), effectiveRadius.toInt(), effectiveRadius.toInt())
            }

            w.enemies.forEach {
                g.color = Color.RED
                g.fillOval((it.pos.x * k).toInt() - 5, (it.pos.y * k).toInt() - 5, 10, 10)
            }



            w.bounties.forEach {
                g.color = Color.YELLOW
                val radius = 6
                g.fillOval((it.pos.x * k).toInt() - radius / 2, (it.pos.y * k).toInt() - radius / 2, radius, radius)
            }

            val sims = stats.sims

            sims?.forEach { sims ->

                sims.allVariants.forEach { variant ->
                    // draw acc
                    g.color = Color.MAGENTA
                    /*              variant.acc.copy().mul(40.0).let { acc ->
                                      g.drawLine(
                                          (variant.variantPoses.first().x * k).toInt(),
                                          (variant.variantPoses.first().y * k).toInt(),
                                          ((variant.variantPoses.first().x + acc.x ) * k).toInt(),
                                          ((variant.variantPoses.first().y + acc.y ) * k).toInt()
                                      )
                                  }*/

                    g.color = if (variant == sims.best) Color.PINK else Color.BLUE
                    // draw line all poses

                    variant.variantPoses.forEachIndexed { index, pos ->
                        if (index < variant.variantPoses.size - 1) {
                            val nextPos = variant.variantPoses[index + 1]
                            g.drawLine(
                                (pos.x * k).toInt(),
                                (pos.y * k).toInt(),
                                (nextPos.x * k).toInt(),
                                (nextPos.y * k).toInt()
                            )
                        }
                    }

                    if (variant == sims.best) {
                        g.color = Color.cyan
                        // draw all bounties
                        variant.bountiesPicked.forEach { (pos, bounty) ->
                            g.fillOval((pos.x * k).toInt() - 3, (pos.y * k).toInt() - 3, 6, 6)
                        }
                    }
                }
                g.color = Color.RED
                sims.best.variantPoses.forEach { pos ->
                    g.fillOval((pos.x * k).toInt() - 2, (pos.y * k).toInt() - 2, 4, 4)
                }
            }

            w.transports.forEach { myTr ->
                g.color = Color.BLUE
                g.fillOval((myTr.pos.x * k).toInt() - 5, (myTr.pos.y * k).toInt() - 5, 10, 10)


                a?.transports?.firstOrNull { action ->
                    action.id == myTr.id
                }?.let { action ->
                    g.color = Color.BLACK
                    val acceleration = action.acceleration.copy().mul(20.0)
                    g.drawLine(
                        (myTr.pos.x * k).toInt(),
                        (myTr.pos.y * k).toInt(),
                        ((myTr.pos.x + acceleration.x) * k).toInt(),
                        ((myTr.pos.y + acceleration.y) * k).toInt()
                    )

                    action.attack?.let { attack ->
                        g.color = Color.RED
                        g.drawLine(
                            (myTr.pos.x * k).toInt(),
                            (myTr.pos.y * k).toInt(),
                            (attack.x * k).toInt(),
                            (attack.y * k).toInt()
                        )
                    }
                }
            }


            //   println("Repaint canvas")
        }
    }

    fun setup() {
        frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        frame.setSize(CANVAS_SIZE + 200, CANVAS_SIZE)
        frame.layout = null

        canvasPanel.setBounds(0, 0, CANVAS_SIZE, CANVAS_SIZE)

        val button1 = JButton("Button 1")
        val button2 = JButton("Button 2")

        button1.setBounds(CANVAS_SIZE, 20, 100, 30) // Align buttons to the right
        button2.setBounds(CANVAS_SIZE, 100, 100, 30)

        frame.add(canvasPanel)
        frame.add(button1)
        frame.add(button2)

        // add label under button 2 with with 200 and 200 height

        infoLabel.setBounds(CANVAS_SIZE + 3, button2.y + button2.height, 200, 1024 - (button2.y + button2.height))
        infoLabel.setEditable(false); // Make it non-editable
        infoLabel.setOpaque(false);   // Make background transparent
        infoLabel.setFocusable(false); // Do not focus
        infoLabel.setWrapStyleWord(true); // Wrap words
        infoLabel.setLineWrap(true); // Enable line wrap
        infoLabel.setBorder(BorderFactory.createEmptyBorder()); // Remove border
        frame.add(infoLabel)


        frame.isVisible = true

        redraw()
    }

    fun redraw() {
        //  println("redraw called")
        canvasPanel.repaint()

        infoLabel.text = "info\n" +
                "points: ${currentWorldState?.points}\n" +
                "name: ${currentWorldState?.name}\n" +
                "successCount: ${stats.successMoves}\n" +
                "maxSpeed: ${currentWorldState?.maxSpeed} " +
                "maxAccel: ${currentWorldState?.maxAccel} " +
                "shieldTimeMs: ${currentWorldState?.shieldTimeMs} " +
                "shieldCooldownMs: ${currentWorldState?.shieldCooldownMs} " +
                "reviveTimeoutSec: ${currentWorldState?.reviveTimeoutSec} " +
                "enemies: ${currentWorldState?.enemies?.size} " +
                "bounties: ${currentWorldState?.bounties?.size} " +
                "transports: ${currentWorldState?.transports?.size}\n" +
                "anomalies: ${currentWorldState?.anomalies?.size}\n" +
                "mapSize: ${currentWorldState?.mapSize}\n" +
                "lastAction.transport.count: ${lastAction?.transports?.size}\n" +
                "logicTookMs: ${stats.logicTookMs}\n" +
                "requestTook: ${stats.requestTook}\n" +
                "transpInfo: ${printTransportInfo(currentWorldState)}"

    }

    private fun printTransportInfo(currentWorldState: WorldStateDto?): String {
        currentWorldState ?: return "null"

        stats.sims
        var index = -1
        return currentWorldState.transports.joinToString("\n\n") {
            index++
            val simInfo = stats.sims?.getOrNull(index)
            "id: ${it.id.take(10)}\n" +
                    "pos: ${it.pos}\n" +
                    "vel: ${it.velocity} " +
                    "selfAcc: ${it.selfAcceleration}\n" +
                    "health: ${it.health} " +
                    "${it.status} " +
                    "deaths: ${it.deathCount}\n" +
                    "bestScore ${simInfo?.best?.score}\n worstScore ${simInfo?.allVariants?.minOf { it.score }}\n"
        }
    }
}

private fun Color.withAlpha(alpha: Double): Color {
    return Color(this.red, this.green, this.blue, (alpha * 255).toInt())
}
