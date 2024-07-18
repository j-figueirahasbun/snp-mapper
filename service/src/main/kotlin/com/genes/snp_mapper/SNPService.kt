package com.genes.snp_mapper

import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

// Data class for the JSON parsing
data class FetchResults (
    val total: Int,
    val otherRsIDs: List<String>,
    val extraInfo: String?,
    val snpInfo: List<List<String>>
)

class SNPService {

    //Create an okhttpclient instance which is used to execute the http request.
    private val client = OkHttpClient()

    fun fetchSNPInformation(snp: String) : String {

        // Use the input SNP in the function in the REST API url
        val url = "https://clinicaltables.nlm.nih.gov/api/snps/v3/search?terms=${snp}"

        // This makes a new instance of request builder which is used to construct an HTTP request.
        // .url(url) sets the URL for the request, the rest api url described previously is used
        // .build finalizes the construction of the request object and returns an instance of the request class. The
        // resulting request object can then be used to execute the HTTP request.
        val request = Request.Builder().url(url).build()

        var result: String? = null

        //This executes the request and automatically closes the response when done.
        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException(" Response was unsuccessful. Unexpected code $response")

            // The response body of the JSON is a string so define it as string
            val responseData = response.body?.string()
            // If it is not null...
            if (responseData != null) {
                // Parse the JSON response, decode it and match it with the fetch result structure defined above
                val snpResult = Json.decodeFromString<FetchResults>(responseData)
                // If the field we want is not empty (so there is data on the snp)
                result = if (snpResult.snpInfo.isNotEmpty()) {
                    // We want the first result because that should be matching the provided rsID
                    val firstResult = snpResult.snpInfo[0]
                    "rsId: ${firstResult[0]}, Chromosome: ${firstResult[1]}, Position: ${firstResult[2]}, " +
                            "Alleles: ${firstResult[3]}, Gene: ${firstResult[4]}"
                } else {
                     "No results found for rsId $snp"
                }
            }
        }
        return result ?: "Error fetching data"
    }
}
