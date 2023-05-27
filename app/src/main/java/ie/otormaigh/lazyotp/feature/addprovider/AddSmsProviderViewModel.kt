package ie.otormaigh.lazyotp.feature.addprovider

import android.text.Editable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ie.otormaigh.lazyotp.R
import ie.otormaigh.lazyotp.SmsCodeProvider
import ie.otormaigh.lazyotp.data.SmsProviderStore
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddSmsProviderViewModel @Inject constructor(
  private val smsProviderStore: SmsProviderStore
) : ViewModel() {
  private val _stateMachine = MutableLiveData<AddSmsProviderState>()
  val stateMachine: LiveData<AddSmsProviderState>
    get() = _stateMachine

  fun upsertProvider(providerId: String, sender: Editable?, digitCount: Editable?) {
    if (validateInput(sender, digitCount)) {
      viewModelScope.launch {
        smsProviderStore.upsertProvider(providerId, SmsCodeProvider(sender.toString(), digitCount.toString()))
        _stateMachine.postValue(AddSmsProviderState.Success)
      }
    }
  }

  private fun validateInput(sender: Editable?, digitCount: Editable?): Boolean {
    when {
      sender.isNullOrEmpty() -> _stateMachine.postValue(AddSmsProviderState.Fail.Sender(R.string.empty))
      digitCount.isNullOrEmpty() -> _stateMachine.postValue(AddSmsProviderState.Fail.DigitCount(R.string.empty))
      else -> return true
    }
    return false
  }
}