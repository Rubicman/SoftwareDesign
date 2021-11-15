package ru.itmo.ct.kubantsev.softwaredesign.drawing

import kotlinx.html.*
import kotlinx.html.stream.appendHTML
import java.awt.Desktop
import kotlin.io.path.writer
import kotlin.math.PI
import kotlin.math.acos
import kotlin.math.sqrt

class HTMLDrawingApi : DrawingApi {
    override var drawingAreaWidth: Int = 1
    override var drawingAreaHeight: Int = 1
    override val screenWidth: Int = 1920
    override val screenHeight: Int = 1080

    private val elements = mutableListOf<DIV.() -> Unit>()

    override fun drawLine(start: Pair<Int, Int>, end: Pair<Int, Int>) {
        val (x1, y1) = start
        val (x2, y2) = end
        val length = sqrt(
            (x1 - x2) * (x1 - x2).toDouble() + (y1 - y2) * (y1 - y2).toDouble()
        ).toInt()
        var angle = acos((x2 - x1).toDouble() / length)
        if (y2 < y1) angle = 2 * PI - angle
        val id = "line${elements.size}"
        elements.add {
            classes = setOf("line")
            this.id = id
            styleRaw(
                """
                #$id {
                    width: $length;
                    left: ${(x1 + x2 - length) / 2};
                    top: ${(y1 + y2) / 2};
                    transform: rotate(${angle}rad)
                }
            """.trimIndent()
            )
        }
    }

    override fun drawCircle(center: Pair<Int, Int>, radius: Int) {
        val id = "circle${elements.size}"
        elements.add {
            classes = setOf("circle")
            this.id = id
            styleRaw(
                """
               #$id {
                   width: ${radius * 2}px;
                   height: ${radius * 2}px;
                   border-radius: ${radius}px;
                   left: ${center.first - radius}px;
                   top: ${center.second - radius}px;
               }
            """
            )
        }
    }

    override fun drawString(position: Pair<Int, Int>, string: String) {
        val id = "string${elements.size}"
        elements.add {
            this.id = id
            +string
            styleRaw(
                """
               #$id {
                   left: ${position.first}px;
                   top: ${position.second}px;
               }
            """
            )
        }
    }

    override fun showGraph() {
        val htmlFile = kotlin.io.path.createTempFile("graph", ".html")
        println(htmlFile)
        htmlFile.writer().use {
            it.appendHTML().html {
                head {
                    meta {
                        charset = "utf-8"
                    }
                }
                body {
                    elements.forEach { element ->
                        div("", element)
                    }
                    styleRaw(
                        """
                        body {
                            width: ${drawingAreaWidth}px;
                            height: ${drawingAreaHeight}px;
                        }
                        
                        .circle {
                            border: 1px solid black;
                        }
                        
                        .line {
                            border-top: 1px solid black
                        }
                        
                        div {
                            position: absolute;
                        }
                    """
                    )
                }
            }
        }
        Desktop.getDesktop().browse(htmlFile.toUri())
        clear()
    }

    private fun FlowOrMetaDataContent.styleRaw(style: String) = style {
        unsafe {
            raw(style.trimIndent())
        }
    }

    override fun clear() {
        elements.clear()
    }
}