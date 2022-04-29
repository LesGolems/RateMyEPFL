package com.github.sdp.ratemyepfl.model.items

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

class CourseTest {

    val EXPECTED_COURSE = Course("SDP", "IC", "Candea", 4, "CS-306", 15, 2.5)
    val EXPECTED_JSON = Json.encodeToString(EXPECTED_COURSE)

    @Test
    fun constructorWithDefaultValuesWorks() {
        val c = Course("SDP", "IC", "Candea", 4, "CS-306", 15, 2.5)
        assertEquals("SDP", c.title)
        assertEquals("IC", c.section)
        assertEquals("Candea", c.teacher)
        assertEquals(4, c.credits)
        assertEquals("CS-306", c.courseCode)
        assertEquals(15, c.numReviews)
        assertEquals(2.5, c.averageGrade, 0.01)
        assertEquals(null, c.cycle)
    }

    @Test
    fun constructorWithAllValuesWorks() {
        val c = Course(
            "SDP",
            "IC",
            "Candea",
            4,
            "CS-306",
            15,
            2.5,
            "bachelor",
            "Fall",
            "During the semester",
            "ENG"
        )
        assertEquals("SDP", c.title)
        assertEquals("IC", c.section)
        assertEquals("Candea", c.teacher)
        assertEquals(4, c.credits)
        assertEquals("CS-306", c.courseCode)
        assertEquals(15, c.numReviews)
        assertEquals(2.5, c.averageGrade, 0.01)
        assertEquals("bachelor", c.cycle)
        assertEquals("Fall", c.session)
        assertEquals("During the semester", c.grading)
        assertEquals("ENG", c.language)
    }

    @Test
    fun toStringWorks() {
        assertEquals(EXPECTED_COURSE.toString(), "CS-306 SDP")
    }

    @Test
    fun serializationWorks() {
        val json = Json.encodeToString(EXPECTED_COURSE)
        assertEquals(EXPECTED_JSON, json)
    }

    @Test
    fun deserializationWorks() {
        val course = Json.decodeFromString<Course>(EXPECTED_JSON)
        assertEquals(EXPECTED_COURSE, course)
    }

    @Test
    fun builderThrowsForMissingId() {
        val fake = "fake"
        val builder = Course.Builder()
            .setTitle(fake)
            .setCredits(0)
            .setCycle(fake)
            .setGrading(fake)
            .setLanguage(fake)
            .setSection(fake)
            .setSession(fake)
            .setTeacher(fake)
            .setNumReviews(15)
            .setAverageGrade(2.5)

        assertThrows(IllegalStateException::class.java) {
            builder.build()
        }
    }

    @Test
    fun builderThrowsForMissingTitle() {
        val fake = "fake"
        val builder = Course.Builder()
            .setCourseCode(fake)
            .setCredits(0)
            .setCycle(fake)
            .setGrading(fake)
            .setLanguage(fake)
            .setSection(fake)
            .setSession(fake)
            .setTeacher(fake)
            .setNumReviews(15)
            .setAverageGrade(2.5)

        assertThrows(IllegalStateException::class.java) {
            builder.build()
        }
    }

    @Test
    fun builderThrowsForMissingTeacher() {
        val fake = "fake"
        val builder = Course.Builder()
            .setCourseCode(fake)
            .setTitle(fake)
            .setCredits(0)
            .setCycle(fake)
            .setGrading(fake)
            .setLanguage(fake)
            .setSection(fake)
            .setSession(fake)
            .setNumReviews(15)
            .setAverageGrade(2.5)

        assertThrows(IllegalStateException::class.java) {
            builder.build()
        }
    }

    @Test
    fun builderThrowsForMissingCredits() {
        val fake = "fake"
        val builder = Course.Builder()
            .setCourseCode(fake)
            .setTitle(fake)
            .setCycle(fake)
            .setGrading(fake)
            .setLanguage(fake)
            .setSection(fake)
            .setSession(fake)
            .setTeacher(fake)
            .setNumReviews(15)
            .setAverageGrade(2.5)

        assertThrows(IllegalStateException::class.java) {
            builder.build()
        }
    }

    @Test
    fun builderThrowsForMissingSection() {
        val fake = "fake"
        val builder = Course.Builder()
            .setCourseCode(fake)
            .setTitle(fake)
            .setCredits(0)
            .setCycle(fake)
            .setGrading(fake)
            .setLanguage(fake)
            .setSession(fake)
            .setTeacher(fake)
            .setNumReviews(15)
            .setAverageGrade(2.5)

        assertThrows(IllegalStateException::class.java) {
            builder.build()
        }
    }

    @Test
    fun builderThrowsForMissingNumReviews() {
        val fake = "fake"
        val builder = Course.Builder()
            .setCourseCode(fake)
            .setTitle(fake)
            .setCredits(0)
            .setCycle(fake)
            .setGrading(fake)
            .setLanguage(fake)
            .setSession(fake)
            .setTeacher(fake)
            .setCourseCode(fake)
            .setAverageGrade(2.5)

        assertThrows(IllegalStateException::class.java) {
            builder.build()
        }
    }

    @Test
    fun builderThrowsForMissingAverageGrade() {
        val fake = "fake"
        val builder = Course.Builder()
            .setCourseCode(fake)
            .setTitle(fake)
            .setCredits(0)
            .setCycle(fake)
            .setGrading(fake)
            .setLanguage(fake)
            .setSession(fake)
            .setTeacher(fake)
            .setCourseCode(fake)
            .setNumReviews(15)

        assertThrows(IllegalStateException::class.java) {
            builder.build()
        }
    }


    @Test
    fun builderSucceedForMissingNonMandatoryProperties() {
        val fake = "fake"
        val builder = Course.Builder()
            .setCourseCode(fake)
            .setTitle(fake)
            .setCredits(0)
            .setSection(fake)
            .setTeacher(fake)
            .setNumReviews(15)
            .setAverageGrade(2.5)


        val course = Course(fake, fake, fake, 0, fake, 15, 2.5)
        assertEquals(course, builder.build())
    }

    @Test
    fun asMandatoryThrowsForNullValue() {
        val builder = Course.Builder()

        assertThrows(IllegalStateException::class.java) {
            builder asMandatory null
        }
    }
}