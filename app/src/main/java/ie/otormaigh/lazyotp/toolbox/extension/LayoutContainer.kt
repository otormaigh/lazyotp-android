package ie.otormaigh.lazyotp.toolbox.extension

import kotlinx.android.extensions.LayoutContainer

fun LayoutContainer.getString(resId: Int): String =
  containerView?.context?.getString(resId) ?: ""

fun LayoutContainer.getString(resId: Int, vararg formatArgs: Any): String =
  containerView?.context?.getString(resId, *formatArgs) ?: ""