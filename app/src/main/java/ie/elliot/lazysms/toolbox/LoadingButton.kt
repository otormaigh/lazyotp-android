package ie.elliot.lazysms.toolbox

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.core.view.isVisible
import ie.elliot.lazysms.R
import kotlinx.android.synthetic.main.view_loading_button.view.*

class LoadingButton : FrameLayout {
  var isLoading: Boolean = false
    set(value) {
      field = value
      button.isVisible = !value
      progressBar.isVisible = value

      invalidate()
    }

  constructor(context: Context) : this(context, null)
  constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
  constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle) {
    inflate(context, R.layout.view_loading_button, this)
  }
}