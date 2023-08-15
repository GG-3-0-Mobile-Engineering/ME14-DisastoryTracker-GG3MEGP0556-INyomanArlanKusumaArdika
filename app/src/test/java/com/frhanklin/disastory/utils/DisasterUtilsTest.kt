package com.frhanklin.disastory.utils

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.mockk.mockk
import org.junit.Assert.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class DisasterUtilsTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var resourceProvider: ResourceProvider
    private lateinit var disasterUtils: DisasterUtils

    @Before
    fun setUp() {
        resourceProvider = mockk()
        disasterUtils = DisasterUtils(resourceProvider)
    }

    @After
    fun tearDown() {
    }

    @Test
    fun `getRegionCode, when invoked should return Region code`() {
        val expectedValue = "ID-BA"

        val input = "Bali"
        val result = disasterUtils.getRegionCode(input)

        assertEquals(expectedValue, result)
    }

    @Test
    fun getRegionString() {
        val expectedValue = "Aceh"

        val input = "ID-AC"
        val result = disasterUtils.getRegionString(input)

        assertEquals(expectedValue, result)
    }

    @Test
    fun getDisasterType() {
        val expectedValue = "Banjir"

        val input = "flood"
        val result = disasterUtils.getDisasterType(input)

        assertEquals(expectedValue, result)
    }
}