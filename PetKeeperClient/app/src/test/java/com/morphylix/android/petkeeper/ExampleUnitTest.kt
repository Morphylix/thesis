package com.morphylix.android.petkeeper

import android.content.pm.PackageInfo
import com.morphylix.android.petkeeper.util.formatDate
import com.morphylix.android.petkeeper.util.test
import dagger.hilt.android.internal.Contexts.getApplication
import org.junit.Assert.*
import org.junit.Test
import java.util.*


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun test1() {
        test()
    }

    @Test
    fun testUtils() {
        assertEquals(Date(System.currentTimeMillis()).toString(), formatDate(Date()))
    }

    private var application: PetKeeperApp? = null

    fun ApplicationTest() {
        super(MyApplication::class.java)
    }

    @Throws(Exception::class)
    protected fun setUp() {
        super.setUp()
        createApplication()
        application = getApplication()
    }

    @Throws(Exception::class)
    fun testCorrectVersion() {
        val info: PackageInfo =
            application.getPackageManager().getPackageInfo(application.getPackageName(), 0)
        assertNotNull(info)
        MoreAsserts.assertMatchesRegex("\\d\\.\\d", info.versionName)
    }
}