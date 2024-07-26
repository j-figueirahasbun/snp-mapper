package com.genes.snp_mapper

import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.springframework.stereotype.Service
import java.io.IOException

@Service
class GeneService {

    private val client = OkHttpClient()

    fun positionalMappingNearVariant (coordinates: Triple<String, String, Int>, distance: Int = 10000): List<Gene> {
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
                return parseGeneDataPositional(responseData)
            }
        }
        return emptyList()
    }

    fun functionalMappingOfVariantUsingVEP(rsId: String): List<Gene>{
        val url = "https://rest.ensembl.org//vep/human/id/$rsId?content-type=application/json"

        val request = Request.Builder().url(url).build()

        client.newCall(request).execute().use { response ->
            if(!response.isSuccessful) throw IOException("Unexpected code $response")

            val responseData = response.body?.string()
            if (responseData != null) {
                return parseGeneDataFunctional(responseData)
            }
        }
        return emptyList()
    }

    fun parseGeneDataPositional(responseData: String): List<Gene> {
        val jsonArray = JSONArray(responseData)
        val genes = mutableListOf<Gene>()
        for (i in 0 until jsonArray.length()) {
            val geneObject = jsonArray.getJSONObject(i)
            val gene = Gene(
                geneId = geneObject.optString("gene_id"),
                start = geneObject.optInt("start"),
                end = geneObject.optInt("end"),
                externalName = geneObject.optString("external_name"),
                strand = geneObject.optInt("strand"),
                id = geneObject.optString("id"),
                transcript = geneObject.optString("canonical_transcript"),
                type = geneObject.optString("biotype"),
                description = geneObject.optString("description"),
                seqRegionName = geneObject.optString("seq_region_name"),
                mapping = "positional"
            )
            genes.add(gene)
        }

        return genes
    }

    fun parseGeneDataFunctional(responseData:String): List<Gene> {
        val jsonArray = JSONArray(responseData)
        val genes = mutableListOf<Gene>()
        for (i in 0 until jsonArray.length()) {
            val variantObject = jsonArray.getJSONObject(i)
            val transcriptConsequences = variantObject.optJSONArray("transcript_consequences")

            if (transcriptConsequences != null) {
                for (j in 0 until transcriptConsequences.length()) {
                    val transcriptObject = transcriptConsequences.getJSONObject(j)
                    val gene = Gene(
                        geneId = transcriptObject.optString("gene_id"),
                        start = variantObject.optInt("start"), // Use start from the variant level
                        end = variantObject.optInt("end"), // Use end from the variant level
                        externalName = transcriptObject.optString("gene_symbol"),
                        strand = transcriptObject.optInt("strand"),
                        id = variantObject.optString("id"),
                        transcript = transcriptObject.optString("transcript_id"),
                        type = transcriptObject.optString("biotype"),
                        description = transcriptObject.optJSONArray("consequence_terms")?.join(", ") ?: "N/A",
                        seqRegionName = variantObject.optString("seq_region_name"),
                        mapping = "functional"
                    )
                    genes.add(gene)
                }
            }
        }

        return genes
    }

}