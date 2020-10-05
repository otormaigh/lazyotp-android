package ie.otormaigh.lazyotp.feature

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ie.otormaigh.lazyotp.R
import ie.otormaigh.lazyotp.data.SmsCodeProvider
import ie.otormaigh.lazyotp.toolbox.extension.getString
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.list_item_sms_provider.*

class SmsProviderRecyclerAdapter(private val clickListener: (provider: SmsCodeProvider) -> Unit) :
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

      containerView.setOnClickListener {
        clickListener(smsCodeProvider)
      }
    }
  }
}