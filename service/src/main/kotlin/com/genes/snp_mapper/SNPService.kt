package com.genes.snp_mapper

import kotlinx.serialization.json.*
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.springframework.stereotype.Service
import java.io.IOException

@Service
class SNPService {

    private val client = OkHttpClient()

    fun obtainSNPInformation(snp: String): SNP {
        val request = snpInfoRequest(snp)
        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Response was unsuccessful from Clinical Tables:  $response")
            return mapJsonArrayToSnp(parseSNPInformationResponseToJsonArray(response))
        }
    }

    private fun snpInfoRequest(snp: String): Request{
        return Request.Builder().url("https://clinicaltables.nlm.nih.gov/api/snps/v3/search?terms=${snp}").build()
    }

    private fun parseSNPInformationResponseToJsonArray (response: Response) : JsonArray {
        return response.body!!.string().let{Json.parseToJsonElement(it).jsonArray}
    }

    private fun mapJsonArrayToSnp(jsonArray: JsonArray): SNP {
        val resultSNP = jsonArray[3].jsonArray[0].jsonArray
        return SNP(
            rsId = resultSNP[0].jsonPrimitive.content,
            chromosome = resultSNP[1].jsonPrimitive.content,
            position = resultSNP[2].jsonPrimitive.int,
            referenceAllele = resultSNP[3].jsonPrimitive.content.split("/")[0],
            alternateAllele = resultSNP[3].jsonPrimitive.content.split("/")[1],
        )
    }

    fun obtainSNPCoordinates(snp: String) : Pair<String, Int> {
        val request = snpInfoRequest(snp)
        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Response was unsuccessful from Clinical Tables:  $response")
            return Pair(mapJsonArrayToSnp(parseSNPInformationResponseToJsonArray(response)).chromosome, mapJsonArrayToSnp(parseSNPInformationResponseToJsonArray(response)).position)
        }
    }

}