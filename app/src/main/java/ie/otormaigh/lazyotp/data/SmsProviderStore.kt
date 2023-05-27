package ie.otormaigh.lazyotp.data

import ie.otormaigh.lazyotp.SmsCodeProvider
import ie.otormaigh.lazyotp.SmsCodeProviderQueries
import javax.inject.Inject

class SmsProviderStore @Inject constructor(
  private val smsCodeProviderQueries: SmsCodeProviderQueries
) {

  fun upsertProvider(id: String, provider: SmsCodeProvider) {
    smsCodeProviderQueries.delete(id)
    smsCodeProviderQueries.delete(provider.sender)

    smsCodeProviderQueries.insert(provider)
  }

  fun isSenderKnown(sender: String): Boolean =
    fetchWithId(sender) != null

  fun fetchWithId(sender: String): SmsCodeProvider? = smsCodeProviderQueries
    .fetchAll()
    .executeAsList()
    .firstOrNull { provider ->
      val sanitisedProvider = provider.sender.replace(" ", "")
      val sanitisedSender = sender.replace(" ", "")

      sanitisedProvider.equals(sanitisedSender, true)
    }
}