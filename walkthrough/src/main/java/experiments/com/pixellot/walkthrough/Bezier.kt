package experiments.com.pixellot.walkthrough

import android.graphics.PointF

/**
 * Class that is used to get point that is lie on the cubic bezier curve.
 *
 *
 * Created on 11.05.18.
 */
internal class Bezier(private val first: PointF, private val second: PointF, private val third: PointF,
                      private val forth: PointF) {

    /**
     * Detects position on Bezier curve.
     *
     * t [0, 1]. 0 - start of the curve, 1 - end.
     */
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