package com.github.sdp.ratemyepfl.database

import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.model.ImageFile
import com.github.sdp.ratemyepfl.utils.TestUtils.drawableToBitmap
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import junit.framework.Assert.*
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class ImageStorageTest {
    val pic = ImageFile("testPic", drawableToBitmap(R.raw.pp1))
    val bigPic = ImageFile("testPic2", drawableToBitmap(R.raw.arcadie))

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var imageStorage: ImageStorage

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

    @Test(expected = IllegalArgumentException::class)
    fun imageTooBigFails() {
        runTest {
            imageStorage.add(bigPic)
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