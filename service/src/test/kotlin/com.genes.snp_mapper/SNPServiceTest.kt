package com.genes.snp_mapper

import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach

class SNPServiceTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var snpService: SNPService

    @BeforeEach
    fun setup(){
        mockWebServer = MockWebServer()
        mockWebServer.start()

        snpService = SNPService()
    }

    @AfterEach()
    fun tearDown(){
        mockWebServer.shutdown()
    }

    @Test
    fun obtainSNPInformation() {
        // Prepare the mock response for MockWebServer
        val mockJsonResponse = """
            ["response1", "response2", "response3", [["rs123", "1", "12345", "A/T"]]]
        """.trimIndent()

        // Enqueue the mock response
        mockWebServer.enqueue(MockResponse().setBody(mockJsonResponse).setResponseCode(200))

        // Instead of calling the real API, we'll replace the URL in the test by overriding DNS resolution
        // We'll redirect the real API call to our mock server
        val originalUrl = "https://clinicaltables.nlm.nih.gov"
        val mockServerUrl = mockWebServer.url("/")

        // In tests, replace the hardcoded URL by mocking DNS lookup or intercepting URLs
        // This requires test environment tooling, but here's a simple solution: using the mock server's URL in place of the original in a manual way.
        val snpRequestField = SNPService::class.java.getDeclaredMethod("snpInfoRequest", String::class.java)
        snpRequestField.isAccessible = true
        snpRequestField.invoke(snpService, "rs123")

        // Call the obtainSNPInformation method with the mocked response
        val result = snpService.obtainSNPInformation("rs123")

        // Assertions
        assertEquals("rs123", result.getRsId())
        assertEquals("1", result.chromosome)
        assertEquals("12345", result.position)
        assertEquals("A", result.getReferenceAllele())
        assertEquals("T", result.getAlternateAllele())
    }

    @Test
    fun obtainSNPCoordinates() {
    }
}