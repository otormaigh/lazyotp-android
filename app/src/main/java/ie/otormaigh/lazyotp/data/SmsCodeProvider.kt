package ie.otormaigh.lazyotp.data

import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.room.*

@Entity(tableName = "sms_code_provider")
data class SmsCodeProvider(
  @PrimaryKey
  val sender: String,
  val codeLength: String
) {

  companion object {
    val diffUtil = object : DiffUtil.ItemCallback<SmsCodeProvider>() {
      override fun areItemsTheSame(oldItem: SmsCodeProvider, newItem: SmsCodeProvider): Boolean =
        oldItem.sender == newItem.sender && oldItem.codeLength == newItem.codeLength

      override fun areContentsTheSame(oldItem: SmsCodeProvider, newItem: SmsCodeProvider): Boolean =
        oldItem.sender == newItem.sender && oldItem.codeLength == newItem.codeLength
    }
  }
}

@Dao
interface SmsCodeproviderDao {
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun insert(data: SmsCodeProvider)

  @Query(
    """
    SELECT * FROM sms_code_provider
    ORDER BY sender ASC
  """
  )
  fun fetchAllLive(): LiveData<List<SmsCodeProvider>>

  @Query(
    """
    SELECT * FROM sms_code_provider
  """
  )
  suspend fun fetchAllAsync(): List<SmsCodeProvider>

  @Transaction
  suspend fun upsert(providerId: String, newData: SmsCodeProvider) {
    delete(providerId)
    insert(newData)
  }

  @Query(
    """
    DELETE from sms_code_provider 
    WHERE sender = :id
  """
  )
  suspend fun delete(id: String)
}