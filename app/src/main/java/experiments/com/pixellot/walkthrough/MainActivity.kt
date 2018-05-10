package experiments.com.pixellot.walkthrough

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.MotionEvent
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MainActivity : AppCompatActivity() {
    val TAG = "MainActivity"
    private val random = Random(100)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        button.setOnClickListener { drawArrow(random.nextInt(500).toFloat(), random.nextInt(500).toFloat()) }

        arrow.post {
            setStartForArrow()
            setEndForArrow()
        }
        layout.setOnTouchListener { v, event -> buttonTouched(v, event) }
    }

    var oldX: Float = 0f
    var oldY: Float = 0f

    private fun buttonTouched(v: View, event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            oldX = event.x
            oldY = event.y
            return true
        }
        if (event.action == MotionEvent.ACTION_UP || event.action == MotionEvent.ACTION_POINTER_UP) {
            drawArrow(event.x, event.y)
        }
        return false
    }

    private fun setEndForArrow() {
        arrow.setEnd(button.x, button.y + button.height / 2)
    }

    private fun drawArrow(newX: Float, newY: Float) {
        button.x = newX
        button.y = newY
        arrow.setEnd(button.x, button.y + button.height / 2)
        arrow.invalidate()
    }

    private fun setStartForArrow() {
        var x = getRelativeLeft(hint)
        var y = getRelativeTop(hint)
        Log.d(TAG, "X: $x Y: $y")
        x += hint.width
        y += hint.height / 2
        arrow.setStart(x.toFloat(), y.toFloat())
    }

    private fun getRelativeLeft(myView: View): Int {
        Log.d(TAG, "relativeLeft: $myView")
        return if (myView.id == layout.id)
            myView.left
        else
            myView.left + getRelativeLeft(myView.parent as View)
    }

    private fun getRelativeTop(myView: View): Int {
        Log.d(TAG, "relativeTop: $myView")
        return if (myView.id == layout.id)
            myView.top
        else
            myView.top + getRelativeTop(myView.parent as View)
    }
}
