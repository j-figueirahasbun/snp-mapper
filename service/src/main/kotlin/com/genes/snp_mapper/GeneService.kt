package com.genes.snp_mapper

import kotlinx.serialization.json.*
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONArray
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.io.IOException

@Service
class GeneService {

    private val client = OkHttpClient()

    @Autowired
    private val snpService = SNPService()

    fun positionalMapping(snp : String): Gene {
        val request = genePositionalRequest(snpService.obtainSNPCoordinates(snp))

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Response from ensembl was unsuccessful: $response")
            return mapJsonArrayToGene(parsePositionalGeneResponseToJsonArray(response), snp)
        }

    }

    private fun genePositionalRequest(coordinates : Pair<String, Int>): Request {
        val (chromosome, position) = coordinates
        val start = (position - 10000).coerceAtLeast(0)
        val end = (position+ 10000)
        val url = "https://rest.ensembl.org/overlap/region/human/$chromosome:$start-$end?feature=gene;content-type=application/json"
        return Request.Builder().url(url).build()
    }

    private fun parsePositionalGeneResponseToJsonArray(response: Response): JsonArray {
        return response.body!!.string().let{ Json.parseToJsonElement(it).jsonArray}
    }

    private fun mapJsonArrayToGene(jsonArray: JsonArray, rsId: String) : Gene {
        val resultingGene = jsonArray[0].jsonObject
        return Gene(
            snp = rsId,
            symbol = resultingGene["external_name"]!!.jsonPrimitive.content,
            type= resultingGene["biotype"]!!.jsonPrimitive.content,
            mappingType= "Positional",
        )
    }

    private fun geneFunctionalRequest(rsId : String): Request {
        val url = "https://rest.ensembl.org//vep/human/id/$rsId?content-type=application/json"
        return Request.Builder().url(url).build()
    }

    //TODO: refactor functional mapping
//    fun functionalMappingOfVariantUsingVEP(rsId: String): List<Gene>{
//        val url = "https://rest.ensembl.org//vep/human/id/$rsId?content-type=application/json"
//
//        val request = Request.Builder().url(url).build()
//
//        client.newCall(request).execute().use { response ->
//            if(!response.isSuccessful) throw IOException("Unexpected code $response")
//
//            val responseData = response.body?.string()
//            if (responseData != null) {
//                return parseGeneDataFunctional(responseData)
//            }
//        }
//        return emptyList()
//    }

//    fun parseGeneDataFunctional(responseData:String): List<Gene> {
//        val jsonArray = JSONArray(responseData)
//        val genes = mutableListOf<Gene>()
//        for (i in 0 until jsonArray.length()) {
//            val variantObject = jsonArray.getJSONObject(i)
//            val transcriptConsequences = variantObject.optJSONArray("transcript_consequences")
//
//            if (transcriptConsequences != null) {
//                for (j in 0 until transcriptConsequences.length()) {
//                    val transcriptObject = transcriptConsequences.getJSONObject(j)
//                    val gene = Gene(
//                        geneId = transcriptObject.optString("gene_id"),
//                        start = variantObject.optInt("start"), // Use start from the variant level
//                        end = variantObject.optInt("end"), // Use end from the variant level
//                        externalName = transcriptObject.optString("gene_symbol"),
//                        strand = transcriptObject.optInt("strand"),
//                        id = variantObject.optString("id"),
//                        transcript = transcriptObject.optString("transcript_id"),
//                        type = transcriptObject.optString("biotype"),
//                        description = transcriptObject.optJSONArray("consequence_terms")?.join(", ") ?: "N/A",
//                        seqRegionName = variantObject.optString("seq_region_name"),
//                        mapping = "functional"
//                    )
//                    genes.add(gene)
//                }
//            }
//        }
//
//        return genes
//    }

}