package ie.otormaigh.lazyotp.feature

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.MutableLiveData
import ie.otormaigh.lazyotp.app
import ie.otormaigh.lazyotp.feature.settings.SettingsActivity
import ie.otormaigh.lazyotp.theme.LazyOtpTheme
import ie.otormaigh.lazyotp.view.SmsProviderAction
import ie.otormaigh.lazyotp.view.SmsProviderList

class MainComposeActivity : ComponentActivity() {
  private val action = MutableLiveData<SmsProviderAction>()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      LazyOtpTheme {
        SmsProviderList(smsProviders = app.database.smsCodeProviderDao().fetchAllAsync(), action)
      }
    }

    action.observe(this, ::processAction)
  }

  private fun processAction(action: SmsProviderAction) {
    when (action) {
      is SmsProviderAction.GoToSettings -> SettingsActivity.start(this)
      is SmsProviderAction.ItemClick -> Toast.makeText(this, "List item onClick", Toast.LENGTH_LONG).show()
    }
  }
}