package com.app.socialapp.ui.nav

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import com.app.socialapp.R
import kotlin.math.*

class SemiCircularNavView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {

    enum class Item(val icon: Int, val label: String) {
        MAP(R.drawable.ic_map, "MAP"),
        CAMERA(R.drawable.ic_camera, "CAMERA"),
        FEED(R.drawable.ic_feed, "FEED"),
        EXPLORE(R.drawable.ic_explore, "EXPLORE"),
        PROFILE(R.drawable.ic_profile, "PROFILE")
    }

    private val items = Item.values()
    private val itemCount = items.size

    private var selectedIndex = items.indexOf(Item.FEED)

    private var angleOffset = 0f
    private var lastTouchX = 0f

    private var radius = 0f
    private var centerX = 0f
    private var centerY = 0f


    private val arcPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        style = Paint.Style.FILL
    }

    private val centerPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#2F80ED")
        setShadowLayer(16f, 0f, 6f, Color.parseColor("#33000000"))
    }

    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textAlign = Paint.Align.CENTER
        typeface = Typeface.DEFAULT_BOLD
        textSize = dp(12f)
        color = Color.parseColor("#2F80ED")
    }

    var onItemSelected: ((Item) -> Unit)? = null

    // Fixed height so it fits all screens
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = dp(220f).toInt()
        setMeasuredDimension(width, height)
    }

    fun setSelectedItem(item: Item) {
        val index = items.indexOf(item)
        if (index != -1 && index != selectedIndex) {
            selectedIndex = index
            // Reset angleOffset so the selected item is centered
            val sweep = 180f / (itemCount - 1)
            angleOffset = 0f
            invalidate()
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        centerX = w / 2f
        centerY = h + dp(28f)      // 🔑 pushes arc DOWN (fix cut)
        radius = h * 0.88f         // 🔑 tighter arc so all icons fit
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val rect = RectF(
            centerX - radius,
            centerY - radius,
            centerX + radius,
            centerY + radius
        )
        canvas.drawArc(rect, 180f, 180f, true, arcPaint)

        val sweep = 180f / (itemCount - 1)

        // Draw extra copies so wrapping looks continuous
        for (i in -itemCount..itemCount) {
            val index = ((i % itemCount) + itemCount) % itemCount
            val item = items[index]

            val angle = 180f + i * sweep + angleOffset
            val rad = Math.toRadians(angle.toDouble())

            val x = centerX + radius * cos(rad).toFloat()
            val y = centerY + radius * sin(rad).toFloat()

            val isCenter = index == selectedIndex

            if (isCenter) {
                canvas.drawCircle(x, y, dp(26f), centerPaint)
            }

            val drawable = ContextCompat.getDrawable(context, item.icon)!!
            val size = if (isCenter) dp(22f) else dp(20f)

            drawable.setBounds(
                (x - size).toInt(),
                (y - size).toInt(),
                (x + size).toInt(),
                (y + size).toInt()
            )
            drawable.setTint(if (isCenter) Color.WHITE else Color.parseColor("#B0BEC5"))
            drawable.draw(canvas)

            if (isCenter) {
                canvas.drawText(item.label, x, y + size + dp(16f), textPaint)
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                lastTouchX = event.x
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                angleOffset += (event.x - lastTouchX) / 4f
                lastTouchX = event.x
                normalizeAngle()
                invalidate()
                return true
            }
            MotionEvent.ACTION_UP,
            MotionEvent.ACTION_CANCEL -> {
                snapToNearest()
                return true
            }
        }
        return super.onTouchEvent(event)
    }

    private fun snapToNearest() {
        val sweep = 180f / (itemCount - 1)
        angleOffset = (angleOffset / sweep).roundToInt() * sweep

        val rawIndex =
            ((-angleOffset / sweep).roundToInt() + (itemCount - 1) / 2)

        // 🔁 infinite wrap
        selectedIndex = ((rawIndex % itemCount) + itemCount) % itemCount

        onItemSelected?.invoke(items[selectedIndex])
        invalidate()
    }

    private fun normalizeAngle() {
        val sweep = 180f / (itemCount - 1)
        angleOffset %= sweep * itemCount
    }

    private fun dp(v: Float) = v * resources.displayMetrics.density
}


