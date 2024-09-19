package com.genes.snp_mapper

import okhttp3.*
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import org.mockito.kotlin.mock

class SNPServiceTest {

    // Declare two class level variables, a mock version of the HTTPClient used by SNP service, which will be used to
    // intercept HTTP requests and the SNPService instance to be tested
    private lateinit var mockClient: OkHttpClient // mocked version of HTTPClient needed to mock the real HTTP requests
    private lateinit var snpService: SNPService // what will be tested and where the mocked HTTPClient will be used
    private lateinit var mockResponse: Response // mocked version of a response which we would expect from the client
    private lateinit var mockCall: Call

    @BeforeEach
    fun setup(){
        //Create a spy of SNPService
        //A spy is like a partial mock of a class, which allows you to mock some methods while still calling real
        //methods. Here we spy on SNPService, meaning it is mostly the real SNPService, but replacing its internal
        //OKHTTPClient with a mock
        snpService = spy(SNPService::class.java)

        // Create a mock OKHttpClient and response
        mockClient = mock(OkHttpClient::class.java) //this line creates a mock version of okHttpClient so we can control
        // its behavior during the test
        mockResponse = mock(Response::class.java)

        mockCall = mock()

        //Use Reflection to replace the internal client in SNPService with the MockClient
        //We use reflection because the SNPService class has a private client field that creates a real OKHttpClient
        // we cant replace it directly, so we use reflection to access and modify the private client field.
        val clientField = SNPService::class.java.getDeclaredField("client")
        //getDeclaredField("client") finds the private client field in the class
        clientField.isAccessible = true
        //this makes the private field accessible, so we can modify it
        clientField.set(snpService, mockClient)
        //this makes it so that we replace the real okHttpClient with the mock created earlier


        //With this reflection and spying, we can allow the rest of the SNPService to remain the same, whilst using the
        //mocked client to be used when calls are made to client.newCall()

    }

    @Test
    fun obtainSNPInformation() {

        //Mocking the behavior of client.newCall(request) and response
        val mockCall = mock(okhttp3.Call::class.java)
        whenever(mockClient.newCall(any<Request>())).thenReturn(mockCall)
        whenever(mockCall.execute()).thenReturn(mockResponse)

        //Mock response behavior
        whenever(mockResponse.isSuccessful).thenReturn(true)

        //create a mock responsebody as a separate mock apparently
        val mockBody = mock(ResponseBody::class.java)
        whenever(mockResponse.body).thenReturn(mockBody)
        whenever(mockBody.string()).thenReturn("""
                ["response1", "response2", "response3", [["rs123", "1", "12345", "A/T"]]]
        """.trimIndent())

        //Call the real method and test
        val result = snpService.obtainSNPInformation("rs123")

        // Assertions
        assertEquals("rs123", result.getRsId())
        assertEquals("1", result.chromosome)
        assertEquals("12345", result.position)
        assertEquals("A", result.getReferenceAllele())
        assertEquals("T", result.getAlternateAllele())


//        // Prepare the mock response for MockWebServer
//        val mockJsonResponse = """
//            ["response1", "response2", "response3", [["rs123", "1", "12345", "A/T"]]]
//        """.trimIndent()
//
//
//
//        // Enqueue the mock response
//        mockWebServer.enqueue(MockResponse().setBody(mockJsonResponse).setResponseCode(200))
//
//        // Instead of calling the real API, we'll replace the URL in the test by overriding DNS resolution
//        // We'll redirect the real API call to our mock server
//        val originalUrl = "https://clinicaltables.nlm.nih.gov"
//        val mockServerUrl = mockWebServer.url("/")
//
//        // In tests, replace the hardcoded URL by mocking DNS lookup or intercepting URLs
//        // This requires test environment tooling, but here's a simple solution: using the mock server's URL in place of the original in a manual way.
//        val snpRequestField = SNPService::class.java.getDeclaredMethod("snpInfoRequest", String::class.java)
//        snpRequestField.isAccessible = true
//        snpRequestField.invoke(snpService, "rs123")
//
//        // Call the obtainSNPInformation method with the mocked response
//        val result = snpService.obtainSNPInformation("rs123")


    }

    @Test
    fun obtainSNPCoordinates() {
    }
}