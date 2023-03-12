package com.example.guide.chart

import android.graphics.PointF
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.asAndroidPath
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.core.graphics.flatten
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout.*
import java.math.BigDecimal
import java.time.LocalDate

@OptIn(ExperimentalTextApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Chart() {
    Box(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.primary)
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        val progress = remember { Animatable(0f) }
        LaunchedEffect(key1 = graphData, block = {
            progress.animateTo(1f, infiniteRepeatable(tween(3000)))
        })
        val textMeasurer = rememberTextMeasurer()

        val gridColor = MaterialTheme.colorScheme.onPrimary
        Spacer(modifier = Modifier
            .padding(8.dp)
            .aspectRatio(3 / 2f)
            .fillMaxSize()
            .drawBehind {
                val barWithPx = 1.dp.toPx()
                drawRoundRect(
                    gridColor, cornerRadius = CornerRadius(5f), style = Stroke(barWithPx)
                )
                val verticalLines = 4
                val verticalSize = size.width / (verticalLines + 1)
                repeat(verticalLines) { i ->
                    val startX = verticalSize * (i + 1)
                    drawLine(
                        gridColor,
                        start = Offset(startX, 0f),
                        end = Offset(startX, size.height),
                        strokeWidth = barWithPx
                    )
                }
                val horizontalLines = 4
                val horizontalSize = size.height / (horizontalLines + 1)
                repeat(horizontalLines) { i ->
                    val startY = horizontalSize * (i + 1)
                    drawLine(
                        gridColor,
                        start = Offset(0f, startY),
                        end = Offset(size.width, startY),
                        strokeWidth = barWithPx
                    )
                }
            }
            .drawWithCache {
                val right = size.width * progress.value
                val path = generatePath(graphData, size)
                val points = getPoints(graphData, size)

                val lines = path
                    .asAndroidPath()
                    .flatten(0.5f)

                val lastPoint = lines.findLast { it.end.x <= right }?.end ?: PointF(0f, size.height)
                val filledPath = Path()
                filledPath.addPath(path)
                filledPath.relativeLineTo(0f, size.height)
                filledPath.lineTo(0f, size.height)
                filledPath.close()

                onDrawBehind {
                    clipRect(right = size.width * progress.value) {
                        drawPath(
                            path, Color.Green, style = Stroke(2.dp.toPx())
                        )
                        drawPath(
                            filledPath, brush = Brush.verticalGradient(
                                listOf(
                                    Color.LightGray, Color.Transparent
                                )
                            ), style = Fill
                        )
                    }
                    points.forEach {
                        if (it.offset.x <= right) {
                            drawIndicator(it.offset)

                            val measuredText = textMeasurer.measure(
                                AnnotatedString(" ${it.value} "),
                                style = TextStyle(fontSize = 6.sp)
                            )
                            val x = it.offset.x - (measuredText.size.width / 2f)
                            val y = it.offset.y - (measuredText.size.height + 5.dp.toPx())
                            val p = Path()
                            p.moveTo(it.offset.x, it.offset.y - 3.dp.toPx())
                            p.lineTo(it.offset.x + 3.dp.toPx(), y + measuredText.size.height)
                            p.lineTo(it.offset.x - 3.dp.toPx(), y + measuredText.size.height)
                            p.lineTo(it.offset.x, it.offset.y - 3.dp.toPx())
                            p.close()
                            drawPath(p, Color.Green.copy(alpha = 0.8f), style = Fill)

                            drawRoundRect(
                                color = Color.Green.copy(alpha = 0.8f),
                                size = measuredText.size.toSize(),
                                cornerRadius = CornerRadius(2.dp.toPx()),
                                topLeft = Offset(x, y)
                            )
                            drawText(measuredText, topLeft = Offset(x, y))
                        }
                    }
                    drawIndicator(lastPoint)
                }
            })
    }

}

private fun DrawScope.drawIndicator(lastPoint: PointF) {
    drawCircle(
        brush = Brush.radialGradient(
            listOf(Color.Green, Color.Transparent),
            center = Offset(lastPoint.x, lastPoint.y),
            radius = 6.dp.toPx()
        ),
        radius = 6.dp.toPx(),
        center = Offset(lastPoint.x, lastPoint.y),
    )
    drawCircle(
        color = Color.White,
        radius = 3.dp.toPx(),
        center = Offset(lastPoint.x, lastPoint.y),
        style = Fill
    )
}


@RequiresApi(Build.VERSION_CODES.O)
val graphData = listOf(
    Balance(LocalDate.now(), BigDecimal(65631)),
    Balance(LocalDate.now().plusWeeks(1), BigDecimal(65931)),
    Balance(LocalDate.now().plusWeeks(2), BigDecimal(65851)),
    Balance(LocalDate.now().plusWeeks(3), BigDecimal(65931)),
    Balance(LocalDate.now().plusWeeks(4), BigDecimal(66484)),
    Balance(LocalDate.now().plusWeeks(5), BigDecimal(67684)),
    Balance(LocalDate.now().plusWeeks(6), BigDecimal(66684)),
    Balance(LocalDate.now().plusWeeks(7), BigDecimal(66984)),
    Balance(LocalDate.now().plusWeeks(8), BigDecimal(70600)),
    Balance(LocalDate.now().plusWeeks(9), BigDecimal(71600)),
    Balance(LocalDate.now().plusWeeks(10), BigDecimal(72600)),
    Balance(LocalDate.now().plusWeeks(11), BigDecimal(72526)),
    Balance(LocalDate.now().plusWeeks(12), BigDecimal(72976)),
    Balance(LocalDate.now().plusWeeks(13), BigDecimal(73589)),
)

data class Balance(val date: LocalDate, val amount: BigDecimal)
data class DataPoint(val offset: PointF, val value: BigDecimal)

fun generatePath(data: List<Balance>, size: Size): Path {
    val path = Path()
    val numberEntries = data.size - 1
    val weekWidth = size.width / numberEntries

    val max = data.maxBy { it.amount }
    val min = data.minBy { it.amount } // will map to x= 0, y = height
    val range = max.amount - min.amount
    val heightPxPerAmount = size.height / range.toFloat()

    var previousBalanceX = 0f
    var previousBalanceY = size.height
    data.forEachIndexed { i, balance ->
        if (i == 0) {
            path.moveTo(
                0f, size.height - (balance.amount - min.amount).toFloat() * heightPxPerAmount
            )

        }

        val balanceX = i * weekWidth
        val balanceY = size.height - (balance.amount - min.amount).toFloat() * heightPxPerAmount
        // to do smooth curve graph - we use cubicTo, uncomment section below for non-curve
        val controlPoint1 = PointF((balanceX + previousBalanceX) / 2f, previousBalanceY)
        val controlPoint2 = PointF((balanceX + previousBalanceX) / 2f, balanceY)
        path.cubicTo(
            controlPoint1.x, controlPoint1.y, controlPoint2.x, controlPoint2.y, balanceX, balanceY
        )

        previousBalanceX = balanceX
        previousBalanceY = balanceY
    }
    return path
}

fun getPoints(data: List<Balance>, size: Size): List<DataPoint> {
    val numberEntries = data.size - 1
    val weekWidth = size.width / numberEntries
    val max = data.maxBy { it.amount }
    val min = data.minBy { it.amount } // will map to x= 0, y = height
    val range = max.amount - min.amount
    val heightPxPerAmount = size.height / range.toFloat()

    return data.mapIndexed { i, balance ->
        val balanceX = i * weekWidth
        val balanceY = size.height - (balance.amount - min.amount).toFloat() * heightPxPerAmount
        DataPoint(PointF(balanceX, balanceY), balance.amount)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
@Preview(showSystemUi = true)
fun ChartPreview() {
    Chart()
}

