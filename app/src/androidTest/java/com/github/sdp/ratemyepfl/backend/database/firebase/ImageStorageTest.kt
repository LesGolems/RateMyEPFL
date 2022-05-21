package com.github.sdp.ratemyepfl.backend.database.firebase

import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.backend.database.firebase.FirebaseImageStorage
import com.github.sdp.ratemyepfl.model.ImageFile
import com.github.sdp.ratemyepfl.utils.TestUtils.resourceToBitmap
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
@ExperimentalCoroutinesApi
class ImageStorageTest {
    private val pic = ImageFile("testPic", resourceToBitmap(R.raw.pp1))

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var imageStorage: FirebaseImageStorage

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun getImageWorks() {
        runTest {
            imageStorage.add(pic)
            val image = imageStorage.get("testPic")
            assertNotNull(image)
            assertEquals(pic.id, image!!.id)
            assertEquals(pic.size, image.size)
            imageStorage.remove(pic.id)
        }
    }

    @Test
    fun getByDirectoryWorks() {
        runTest {
            imageStorage.addInDirectory(pic, "test")
            val image = imageStorage.getByDirectory("test")[0]
            assertNotNull(image)
            assertEquals(pic.id, image.id)
            assertEquals(pic.size, image.size)
            imageStorage.removeInDirectory(pic.id, "test")
        }
    }

    @Test
    fun imageNotInDbReturnsNull() {
        runTest {
            val image = imageStorage.get("not in db")
            assertNull(image)
        }
    }

}