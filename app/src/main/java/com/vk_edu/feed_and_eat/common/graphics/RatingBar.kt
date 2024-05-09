package com.vk_edu.feed_and_eat.common.graphics

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.vk_edu.feed_and_eat.R
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun RatingBar(
    rating: Float,
    modifier: Modifier = Modifier,
    stars: Int = 5,
    ratingColor: Color = colorResource(R.color.yellow),
    backgroundColor: Color = colorResource(R.color.gray),
    onRatingChanged: ((Float) -> Unit)? = null
) {
    val realRating = rating * stars / 5
    Row(modifier = modifier.wrapContentSize()) {
        (1..stars).forEach { step ->
            val stepRating = when {
                realRating > step -> 1f
                step.rem(realRating) < 1 -> realRating - (step - 1f)
                else -> 0f
            }
            RatingStar(stepRating, ratingColor, backgroundColor, step, onRatingChanged)
        }
    }
}

@Composable
private fun RatingStar(
    rating: Float,
    ratingColor: Color,
    backgroundColor: Color,
    starNumber: Int = 1,
    onRatingChanged: ((Float) -> Unit)? = null
) {
    var boxModifier = Modifier
        .fillMaxHeight()
        .aspectRatio(1f)
        .heightIn(0.dp, 25.dp)
        .clip(starShape)
    if (onRatingChanged != null) {
        boxModifier = boxModifier.clickable {
            onRatingChanged(starNumber.toFloat())
        }
    }
    BoxWithConstraints(
        modifier = boxModifier
    ) {
        Canvas(modifier = Modifier.size(maxHeight)) {
            drawRect(
                brush = SolidColor(backgroundColor),
                size = Size(
                    height = size.height * 1.4f,
                    width = size.width * 1.4f
                ),
                topLeft = Offset(
                    x = -(size.width * 0.1f),
                    y = -(size.height * 0.1f)
                )
            )
            if (rating > 0) {
                drawRect(
                    brush = SolidColor(ratingColor),
                    size = Size(
                        height = size.height * 1.1f,
                        width = size.width * rating
                    )
                )
            }
        }
    }
}

private val starShape = GenericShape { size, _ ->
    addPath(starPath(size.height))
}

private val starPath = { size: Float ->
    Path().apply {
        val outerRadius: Float = size / 1.8f
        val innerRadius: Double = outerRadius / 2.5
        var rot: Double = Math.PI / 2 * 3
        val cx: Float = size / 2
        val cy: Float = size / 20 * 11
        var x: Float
        var y: Float
        val step = Math.PI / 5

        moveTo(cx, cy - outerRadius)
        repeat(5) {
            x = (cx + cos(rot) * outerRadius).toFloat()
            y = (cy + sin(rot) * outerRadius).toFloat()
            lineTo(x, y)
            rot += step

            x = (cx + cos(rot) * innerRadius).toFloat()
            y = (cy + sin(rot) * innerRadius).toFloat()
            lineTo(x, y)
            rot += step
        }
        close()
    }
}

@Composable
fun RatingBarPres(rating : Double) {
    Column {
        RatingBar(
            rating = rating.toFloat(),
            modifier = Modifier.height(25.dp)
        )
    }
}