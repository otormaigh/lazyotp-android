package ie.elliot.lazysms.toolbox

import androidx.lifecycle.LiveData
import androidx.work.*

object WorkScheduler {
  inline fun <reified W : CoroutineWorker> oneTimeRequest(inputData: Data): LiveData<WorkInfo> {
    val workRequest = OneTimeWorkRequestBuilder<W>()
      .setInputData(inputData)
      .build()

    WorkManager.getInstance()
      .enqueue(workRequest)

    return WorkManager.getInstance().getWorkInfoByIdLiveData(workRequest.id)
  }
}