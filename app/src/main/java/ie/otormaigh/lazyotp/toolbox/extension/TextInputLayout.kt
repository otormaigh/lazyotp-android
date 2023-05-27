package ie.otormaigh.lazyotp.toolbox.extension

import com.google.android.material.textfield.TextInputLayout

fun TextInputLayout?.clearError() {
  if (this == null) return
  error = ""
}