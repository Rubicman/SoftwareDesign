package ru.itmo.ct.kubantsev.softwaredesign.drawing

import java.awt.Frame
import java.awt.Graphics
import java.awt.Toolkit
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import kotlin.system.exitProcess

class AWTDrawingApi : Frame(), DrawingApi {

    override var drawingAreaWidth: Int
        get() = width
        set(value) {
            setSize(value, height)
        }
    override var drawingAreaHeight: Int
        get() = height
        set(value) {
            setSize(width, value)
        }
    override val screenWidth: Int
    override val screenHeight: Int

    private val operations = mutableListOf<(Graphics) -> Unit>()

    init {
        addWindowListener(object : WindowAdapter() {
            override fun windowClosing(e: WindowEvent?) {
                exitProcess(0)
            }
        })
        Toolkit.getDefaultToolkit().screenSize.also { size ->
            screenWidth = size.width
            screenHeight = size.height
        }
    }

    override fun drawLine(start: Pair<Int, Int>, end: Pair<Int, Int>) {
        operations.add {
            graphics.drawLine(start.first, start.second, end.first, end.second)
        }
    }

    override fun drawCircle(center: Pair<Int, Int>, radius: Int) {
        operations.add { graphics ->
            graphics.drawOval(
                center.first - radius,
                center.second - radius,
                radius * 2,
                radius * 2
            )
        }
    }

    override fun drawString(position: Pair<Int, Int>, string: String) {
        operations.add {
            graphics.drawString(string, position.first, position.second)
        }
    }

    override fun showGraph() {
        isVisible = true
        operations
            .onEach { operation -> operation(graphics) }
        clear()
    }

    override fun clear() {
        operations.clear()
    }
}