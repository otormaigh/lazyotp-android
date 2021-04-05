package ie.otormaigh.lazyotp.data

import androidx.room.Room
import androidx.room.testing.MigrationTestHelper
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MigrationsTest {
  private val TEST_DB = "migration-test"

  @get:Rule
  val helper: MigrationTestHelper = MigrationTestHelper(
    InstrumentationRegistry.getInstrumentation(),
    LazySmsDatabase::class.java.canonicalName,
    FrameworkSQLiteOpenHelperFactory()
  )

  @Test
  fun testMigrateAll() {
    helper.createDatabase(TEST_DB, 1).apply {
      close()
    }

    Room.databaseBuilder(
      InstrumentationRegistry.getInstrumentation().targetContext,
      LazySmsDatabase::class.java,
      TEST_DB
    ).addMigrations(*Migrations.ALL).build().apply {
      openHelper.writableDatabase
      close()
    }
  }
}