import java.awt.Color
import java.awt.Graphics
import javax.swing.JButton
import javax.swing.JFrame
import javax.swing.JPanel

class Ui {

    val frame = JFrame("DatsMagic")

    val canvasPanel: JPanel = object : JPanel() {
        override fun paintComponent(g: Graphics) {
            super.paintComponent(g)
            g.color = Color.WHITE
            g.fillRect(0, 0, this.width, this.height) // Draw white background
            g.color = Color.RED
            g.fillOval(50, 50, (200 * Math.random()).toInt(), 200) // Draw red circle

         //   println("Repaint canvas")
        }
    }

    fun setup() {
        frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        frame.setSize(1024+200, 1024)
        frame.layout = null

        canvasPanel.setBounds(0, 0, 1024, 1024)

        val button1 = JButton("Button 1")
        val button2 = JButton("Button 2")

        button1.setBounds(1024, 20, 100, 30) // Align buttons to the right
        button2.setBounds(1024, 100, 100, 30)

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