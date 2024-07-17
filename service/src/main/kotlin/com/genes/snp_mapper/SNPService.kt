package com.genes.snp_mapper

import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import java.io.IOException

class SNPService {

    private val client = OkHttpClient()

    fun fetchGeneForSNP(snp: SNP) {
        val url = "https://rest.ensembl.org/overlap/region/human/${snp.getChromosome()}:${snp.getPosition()}-${snp.getPosition()}?feature=gene;content-type=application/json"
        val request = Request.Builder().url(url).build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Unexpected code $response")

            val responseData = response.body?.string()
            if (responseData != null) {
                val jsonArray = JSONArray(responseData)
                if (jsonArray.length() > 0) {
                    val jsonObject = jsonArray.getJSONObject(0)
                    val gene = Gene(
                        geneId = jsonObject.getString("id"),
                        symbol = jsonObject.getString("external_name"),
                        geneType = jsonObject.getString("biotype")
                    )
                    snp.setGene(gene)
                }
            }
        }
    }
}
