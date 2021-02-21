package ie.otormaigh.lazyotp.feature

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ie.otormaigh.lazyotp.R
import ie.otormaigh.lazyotp.data.SmsCodeProvider
import ie.otormaigh.lazyotp.databinding.ListItemSmsProviderBinding
import ie.otormaigh.lazyotp.toolbox.extension.getString

class SmsProviderRecyclerAdapter(private val clickListener: (provider: SmsCodeProvider) -> Unit) :
  ListAdapter<SmsCodeProvider, SmsProviderRecyclerAdapter.ViewHolder>(SmsCodeProvider.diffUtil) {
  private lateinit var binding: ListItemSmsProviderBinding

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    binding = ListItemSmsProviderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    return ViewHolder(binding)
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    holder.bind(getItem(position))
  }

  inner class ViewHolder(private val binding: ListItemSmsProviderBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(smsCodeProvider: SmsCodeProvider) {
      binding.tvSender.text = smsCodeProvider.sender
      binding.tvCodeLength.text = getString(R.string.format_digit_count, smsCodeProvider.codeLength)

      binding.root.setOnClickListener {
        clickListener(smsCodeProvider)
      }
    }
  }
}