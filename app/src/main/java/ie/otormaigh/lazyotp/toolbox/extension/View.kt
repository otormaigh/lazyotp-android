package ie.otormaigh.lazyotp.toolbox.extension

import android.app.Activity
import android.view.View
import android.view.inputmethod.InputMethodManager

fun View.hideKeyboard() {
  (context.getSystemService(Activity.INPUT_METHOD_SERVICE) as? InputMethodManager)
    ?.hideSoftInputFromWindow(windowToken, 0)
}

fun View.showKeyboard() {
  (context.getSystemService(Activity.INPUT_METHOD_SERVICE) as? InputMethodManager)
    ?.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
}