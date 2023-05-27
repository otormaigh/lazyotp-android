package ie.otormaigh.lazyotp.toolbox.extension

import android.app.Activity
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.StringRes

fun View.hideKeyboard() {
  (context.getSystemService(Activity.INPUT_METHOD_SERVICE) as? InputMethodManager)
    ?.hideSoftInputFromWindow(windowToken, 0)
}

/**
 * FIXME
 * Deprecated -> Use showSoftInput(View, int) or hideSoftInputFromWindow(IBinder, int) explicitly instead.
 */
fun View.showKeyboard() {
  (context.getSystemService(Activity.INPUT_METHOD_SERVICE) as? InputMethodManager)
    ?.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
}

fun View?.setContentDescription(@StringRes resId: Int) {
  if (this == null) return
  contentDescription = context.getString(resId)
}