package experiments.com.pixellot.walkthrough

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView

/**
 * Created on 15.05.18.
 */
class HintLayout @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    val hint: TextView
    val description: TextView

    init {
        val inflater = LayoutInflater.from(context)
        orientation = VERTICAL
        inflater.inflate(R.layout.hint_layout, this)
        hint = findViewById(R.id.hint)
        description = findViewById(R.id.description)
    }
}