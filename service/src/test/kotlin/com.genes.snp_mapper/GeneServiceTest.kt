package com.genes.snp_mapper

import okhttp3.*
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.mock
import org.mockito.Mockito.spy
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.io.IOException

class GeneServiceTest {

    // Declare two class level variables, a mock version of the HTTPClient used by SNP service, which will be used to
    // intercept HTTP requests and the SNPService instance to be tested
    private lateinit var mockClient: OkHttpClient // mocked version of HTTPClient needed to mock the real HTTP requests
    private lateinit var geneService: GeneService // what will be tested and where the mocked HTTPClient will be used
    private lateinit var snpService: SNPService // what will be tested and where the mocked HTTPClient will be used
    private lateinit var mockResponse: Response // mocked version of a response which we would expect from the client
    private lateinit var mockCall: Call

    @BeforeEach
    fun setup(){
        //Create a spy of GeneService
        //A spy is like a partial mock of a class, which allows you to mock some methods while still calling real
        //methods. Here we spy on GeneService, meaning it is mostly the real GeneService, but replacing its internal
        //OKHTTPClient with a mock
        geneService = spy(GeneService::class.java)

        //Also create a mock of SNPService as it needs to be used
        snpService = mock(SNPService::class.java)

        // Create a mock OKHttpClient and response
        mockClient = mock(OkHttpClient::class.java) //this line creates a mock version of okHttpClient, so we can control
        // its behavior during the test
        mockResponse = mock(Response::class.java)

        mockCall = mock()

        //Use Reflection to replace the internal client in GeneService with the MockClient
        //We use reflection because the GeneService class has a private client field that creates a real OKHttpClient
        // we cant replace it directly, so we use reflection to access and modify the private client field.
        val clientField = GeneService::class.java.getDeclaredField("client")
        //getDeclaredField("client") finds the private client field in the class
        clientField.isAccessible = true
        //this makes the private field accessible, so we can modify it
        clientField.set(geneService, mockClient)
        //this makes it so that we replace the real okHttpClient with the mock created earlier


        //With this reflection and spying, we can allow the rest of the GeneService to remain the same, whilst using the
        //mocked client to be used when calls are made to client.newCall()

        //Also inject the mock SNPService in the spied GeneService
        val snpServiceField = GeneService::class.java.getDeclaredField("snpService")
        snpServiceField.isAccessible = true
        snpServiceField.set(geneService, snpService)

    }

    @Test
    fun positionalMappingShouldReturnGeneObject() {

        //Mock the behavior of the SNPService.obtainCoordinates
        whenever(snpService.obtainSNPCoordinates(any())).thenReturn(Pair("1", "12345"))

        //Mocking the behavior of client.newCall(request) and response
        val mockCall = mock(Call::class.java)
        whenever(mockClient.newCall(any<Request>())).thenReturn(mockCall)
        whenever(mockCall.execute()).thenReturn(mockResponse)

        //Mock response behavior
        whenever(mockResponse.isSuccessful).thenReturn(true)

        //create a mock ResponseBody as a separate mock apparently
        val mockBody = mock(ResponseBody::class.java)
        whenever(mockResponse.body).thenReturn(mockBody)
        whenever(mockBody.string()).thenReturn(
            """
                [
                    {
                        "external_name": "BRCA1",
                        "biotype": "protein_coding"
                    }
                ]
            """.trimIndent()
        )

        val result = geneService.positionalMapping("rs123")

        //Assertions
        assertEquals("rs123", result.getSnp())
        assertEquals("BRCA1", result.symbol)
        assertEquals("protein_coding", result.getType())
        assertEquals("Positional", result.getMappingType())


    }

    @Test
    fun positionalMappingThrowsExceptionWhenResponseIsNotSuccessful(){
        whenever(snpService.obtainSNPCoordinates(any())).thenReturn(Pair("1", "12345"))

        whenever(mockClient.newCall(any())).thenReturn(mockCall)
        whenever(mockCall.execute()).thenReturn(mockResponse)
        whenever(mockResponse.isSuccessful).thenReturn(false)

        assertThrows<IOException> {
            geneService.positionalMapping("rs123")
        }
    }

    @Test
    fun functionalMappingReturnsListOfGenes() {
        whenever(mockClient.newCall(any())).thenReturn(mockCall)
        whenever(mockCall.execute()).thenReturn(mockResponse)

        val mockBody = mock(ResponseBody::class.java)
        whenever(mockResponse.body).thenReturn(mockBody)
        whenever(mockResponse.isSuccessful).thenReturn(true)
        whenever(mockBody.string()).thenReturn("""
            [{
                "transcript_consequences": [{
                    "gene_symbol": "GENE1",
                    "biotype": "protein_coding"
                }]
            }]
        """.trimIndent())

        val result = geneService.functionalMapping("rs123")

        assertEquals(1, result.size)
        assertEquals("GENE1", result[0].symbol)
        assertEquals("protein_coding", result[0].getType())
        assertEquals("Functional", result[0].getMappingType())
        assertEquals("rs123", result[0].getSnp())
    }

    @Test
    fun functionalMappingThrowsExceptionWhenResponseIsNotSuccessful(){
        whenever(mockClient.newCall(any())).thenReturn(mockCall)
        whenever(mockCall.execute()).thenReturn(mockResponse)
        whenever(mockResponse.isSuccessful).thenReturn(false)

        assertThrows<IOException> {
            geneService.functionalMapping("rs123")
        }
    }

    @Test
    fun eQTLMappingReturnsListOfGenes() {

        val mockBody = mock(ResponseBody::class.java)

        whenever(mockClient.newCall(any())).thenReturn(mockCall)
        whenever(mockCall.execute()).thenReturn(mockResponse)
        whenever(mockResponse.body).thenReturn(mockBody)
        whenever(mockResponse.isSuccessful).thenReturn(true)
        whenever(mockBody.string()).thenReturn("""
            {
                "data": [{
                    "snpId": "rs123",
                    "geneSymbol": "GENE1",
                    "tissueSiteDetailId": "Liver"
                }]
            }
        """.trimIndent())

        val result = geneService.eQTLMapping("rs123")

        assertEquals(1, result.size)
        assertEquals("GENE1", result[0].symbol)
        assertEquals("Liver", result[0].getType())
        assertEquals("eQTL Mapping", result[0].getMappingType())
        assertEquals("rs123", result[0].getSnp())
    }

    @Test
    fun eQTLMappingThrowsExceptionWhenRequestFails(){
        whenever(mockClient.newCall(any())).thenReturn(mockCall)
        whenever(mockCall.execute()).thenReturn(mockResponse)
        whenever(mockResponse.isSuccessful).thenReturn(false)

        assertThrows<IOException> {
            geneService.eQTLMapping("rs123")
        }
    }

}