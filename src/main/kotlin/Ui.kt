import com.krld.currentWorldState
import com.krld.lastAction
import java.awt.Color
import java.awt.Graphics
import javax.swing.JButton
import javax.swing.JFrame
import javax.swing.JPanel


val CANVAS_SIZE = 1024

class Ui {

    val frame = JFrame("DatsMagic")

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

                g.fillOval(((it.pos.x * k).toInt() - radius/2).toInt(), ((it.pos.y * k).toInt()- radius/2).toInt(), radius.toInt(), radius.toInt())

                g.color = baseColor.withAlpha(0.3)
                g.fillOval(((it.pos.x * k).toInt() - effectiveRadius/2).toInt(), ((it.pos.y * k).toInt()- effectiveRadius/2).toInt(), effectiveRadius.toInt(), effectiveRadius.toInt())
            }

            w.enemies.forEach {
                g.color = Color.RED
                g.fillOval((it.pos.x * k).toInt()-5, (it.pos.y * k).toInt() -5, 10, 10)
            }



            w.bounties.forEach {
                g.color = Color.YELLOW
                val radius = 6
                g.fillOval((it.pos.x * k).toInt() - radius/2, (it.pos.y * k).toInt() - radius/2, radius, radius)
            }

            w.transports.forEach { myTr ->
                g.color = Color.BLUE
                g.fillOval((myTr.pos.x * k).toInt() -5, (myTr.pos.y * k).toInt() -5, 10, 10)


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

        frame.isVisible = true
    }

    fun redraw() {
        //  println("redraw called")
        canvasPanel.repaint()
    }
}

private fun Color.withAlpha(alpha: Double): Color {
    return Color(this.red, this.green, this.blue, (alpha * 255).toInt())
}
