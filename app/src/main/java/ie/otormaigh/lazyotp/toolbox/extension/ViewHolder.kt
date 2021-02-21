package ie.otormaigh.lazyotp.toolbox.extension

import androidx.recyclerview.widget.RecyclerView

fun RecyclerView.ViewHolder.getString(resId: Int): String =
  itemView.context?.getString(resId) ?: ""

fun RecyclerView.ViewHolder.getString(resId: Int, vararg formatArgs: Any): String =
  itemView.context?.getString(resId, *formatArgs) ?: ""