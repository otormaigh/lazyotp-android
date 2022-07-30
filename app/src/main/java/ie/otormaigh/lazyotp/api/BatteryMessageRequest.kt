package ie.otormaigh.lazyotp.api

import ie.otormaigh.lazyotp.BuildConfig

class BatteryMessageRequest(private val deviceName: String) {
  val jsonString: String
    get() = """
        {
          "attachments": [
            {
              "text": "My Battery Is Low and It’s Getting Dark",
              "footer": "Version: ${BuildConfig.VERSION_NAME}",
              "fields": [
                {
                  "title" : "Phone",
                  "value" : "$deviceName",
                  "short" : true
                }
              ]
            },
          ]
        }
    """.trimIndent()
}