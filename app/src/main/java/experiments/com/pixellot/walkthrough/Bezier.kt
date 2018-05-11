package experiments.com.pixellot.walkthrough

import android.graphics.PointF

/**
 * Created on 11.05.18.
 */
class Bezier(val first: PointF, val second: PointF, val third: PointF, val forth: PointF) {

    fun findFor(t: Float): PointF {

        val dt = 1f - t
        val dt2 = dt * dt
        val t2 = t * t
//        (1-t) * (1-t) * (1-t) * p0 + 3 * (1-t) * (1-t) * t * p1 + 3 * (1-t) * t * t * p2 + t * t * t * p3
        val x = dt2 * dt * first.x + 3 * dt2 * t * second.x + 3 * dt * t2 * third.x + t2 * t * forth.x
        val y = dt2 * dt * first.y + 3 * dt2 * t * second.y + 3 * dt * t2 * third.y + t2 * t * forth.y
        return PointF(x, y)
    }
}