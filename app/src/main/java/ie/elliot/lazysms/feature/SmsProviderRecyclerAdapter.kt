package ie.elliot.lazysms.feature

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ie.elliot.lazysms.R
import ie.elliot.lazysms.data.SmsCodeProvider
import ie.elliot.lazysms.toolbox.extension.getString
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.list_item_sms_provider.*

class SmsProviderRecyclerAdapter :
  ListAdapter<SmsCodeProvider, SmsProviderRecyclerAdapter.ViewHolder>(SmsCodeProvider.diffUtil) {
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
    ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item_sms_provider, parent, false))

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    holder.bind(getItem(position))
  }

  inner class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {
    fun bind(smsCodeProvider: SmsCodeProvider) {
      tvSender.text = smsCodeProvider.sender
      tvCodeLength.text = getString(R.string.format_digit_count, smsCodeProvider.codeLength)
    }
  }
}