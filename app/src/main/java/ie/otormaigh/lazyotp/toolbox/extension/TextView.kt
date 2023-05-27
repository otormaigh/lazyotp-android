package ie.otormaigh.lazyotp.toolbox.extension

import android.widget.TextView

fun TextView?.clear() {
  if (this == null) return
  text = ""
}