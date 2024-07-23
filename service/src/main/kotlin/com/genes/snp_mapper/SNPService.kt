package com.genes.snp_mapper

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.*
import okhttp3.OkHttpClient
import okhttp3.Request
import org.springframework.stereotype.Service
import java.io.IOException

// Data class for the JSON parsing
@Serializable
data class FetchResults (
    val total: Int,
    val otherRsIDs: List<String>,
    val extraInfo: String?,
    val snpInfo: List<List<String>>
)

@Service
class SNPService {

    //Create an OkHttpClient instance which is used to execute the http request.
    private val client = OkHttpClient()

    fun fetchSNPInformation(snp: String): String {

        // Use the input SNP in the function in the REST API url
        val url = "https://clinicaltables.nlm.nih.gov/api/snps/v3/search?terms=${snp}"

        // This makes a new instance of request builder which is used to construct an HTTP request.
        // .url(url) sets the URL for the request, the rest api url described previously is used
        // .build finalizes the construction of the request object and returns an instance of the request class. The
        // resulting request object can then be used to execute the HTTP request.
        val request = Request.Builder().url(url).build()

        var result: String? = null

        // This executes the request and automatically closes the response when done.
        // This creates a new HTTP call using the request object, sends the HTTP request and return the HTTP response.
        // use is a Kotlin extension function that ensures the response is closed after the block of code is executed.
        client.newCall(request).execute().use { response ->

            //This checks if the response was successful, if not, it throws an IOException
            if (!response.isSuccessful) throw IOException(" Response was unsuccessful. Unexpected code $response")

            // This extracts the body of the response as a string. It can be 'null' so its safely accessed using the ?
            val responseData = response.body?.string()
            // If it is not null...
            if (responseData != null) {
                //Parse the response data string into a JSONArray using the JSON.parseToJsonElement method
                val jsonArray = Json.parseToJsonElement(responseData).jsonArray
                // This calls the mapJSONArrayToFetchResults function to map the array to a FetchResults object
                val snpResult = mapJsonArrayToFetchResults(jsonArray)
                // This checks if the snpInfo list in the snpResult object is not empty
                if (snpResult.snpInfo.isNotEmpty()) {
                    // This extracts the first result from the snpInfo list
                    val firstResult = snpResult.snpInfo[0]
                    // This constructs a result string using the information from the first SNP result
                    result = "rsId: ${firstResult[0]}, Chromosome: ${firstResult[1]}, Position: ${firstResult[2]}, " +
                            "Alleles: ${firstResult[3]}, Gene: ${firstResult[4]}"
                } else {
                    // If snpInfo is empty, it sets the result to a message saying there is no results found
                    result = "No results found for rsId $snp"
                }
            }
        }
        // returns the result. and if it is null it returns the following string:
        return result ?: "Error fetching data"
    }

    fun getSNPCoordinates (snp: String): Triple<String, String, Int>? {
        //REST API from Ensembl
        val url = "http://rest.ensembl.org/variation/human/$snp?content-type=application/json"
        //Make the request with the url
        val request = Request.Builder().url(url).build()

        // make the new call
        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Unexpected code $response")

            val responseData = response.body?.string()
            if (responseData != null) {
                val json = Json.parseToJsonElement(responseData).jsonObject
                val location = json["mappings"]?.jsonArray?.firstOrNull()?.jsonObject
                if (location != null) {
                    val chromosome = location["seq_region_name"]?.jsonPrimitive?.content ?: return null
                    val start = location["start"]?.jsonPrimitive?.int ?: return null
                    return Triple(snp, chromosome, start)
                }
            }
        }
        return null
    }

    fun positionalMappingNearVariant (coordinates: Triple<String, String, Int>, distance: Int = 10000): String {
        val (rsId, chromosome, position) = coordinates
        val snp = rsId
        val start = (position-distance).coerceAtLeast(0)
        val end = (position+distance)
        val url = "https://rest.ensembl.org/overlap/region/human/$chromosome:$start-$end?feature=gene;content-type=application/json"

        val request = Request.Builder().url(url).build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Unexpected code $response")

            val responseData = response.body?.string()
            if (responseData != null) {
                return responseData
            }
        }
        return "Error fetching data"
    }


    // Function to map the array elements from the JSON to FetchResults object
    private fun mapJsonArrayToFetchResults(jsonArray: JsonArray): FetchResults {
        // This extracts the total number of results from the first element
        val total = jsonArray[0].jsonPrimitive.int
        // Extract a list of other rsIDs from the second element of the jsonArray.
        val otherRsIDs = jsonArray[1].jsonArray.map { it.jsonPrimitive.content }
        // Extract the extra info if needed, and this can be null.
        val extraInfo = jsonArray[2].jsonPrimitive.contentOrNull
        // Extract information from the 3rd element as a list of lists of strings from the 4th element
        val snpInfo = jsonArray[3].jsonArray.map { it.jsonArray.map { it.jsonPrimitive.content } }

        // Returns a FetchResults object constructed with the extracted data
        return FetchResults(total, otherRsIDs, extraInfo, snpInfo)
    }
}