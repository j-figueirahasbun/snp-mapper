package com.genes.snp_mapper

import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.springframework.stereotype.Service
import java.io.IOException

@Service
class GeneService (

)

{

    //Create an OkHttpClient instance which is used to execute the http request.
    private val client = OkHttpClient()

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


    fun parseGeneData(responseData: String): List<Gene> {
        val jsonArray = JSONArray(responseData)
        val genes = mutableListOf<Gene>()
        for (i in 0 until jsonArray.length()) {
            val geneObject = jsonArray.getJSONObject(i)
            val gene = Gene(
                geneId = geneObject.getString("gene_id"),
                start = geneObject.getInt("start"),
                end = geneObject.getInt("end"),
                externalName = geneObject.getString("external_name"),
                strand = geneObject.getInt("strand"),
                id = geneObject.getString("id"),
                transcript = geneObject.getString("canonical_transcript"),
                type = geneObject.getString("biotype"),
                description = geneObject.getString("description"),
                seqRegionName = geneObject.getString("seq_region_name"),
                mapping = "positional"
            )
            genes.add(gene)
        }

        return genes
    }

}