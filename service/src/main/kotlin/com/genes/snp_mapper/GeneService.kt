package com.genes.snp_mapper

import kotlinx.serialization.json.*
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.io.IOException

@Service
class GeneService {

    private val client = OkHttpClient()

    @Autowired
    private val snpService = SNPService()

    fun positionalMapping(snp: String): Gene {
        val request = genePositionalRequest(snpService.obtainSNPCoordinates(snp))

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Response from ENSEMBL was unsuccessful: $response")
            return mapPositionalJsonArrayResultsToGene(parseMappingResponseToJsonArray(response), snp)
        }

    }

    private fun genePositionalRequest(coordinates: Pair<String, Int>): Request {
        val (chromosome, position) = coordinates
        val start = (position - 10000).coerceAtLeast(0)
        val end = (position + 10000)
        val url =
            "https://rest.ensembl.org/overlap/region/human/$chromosome:$start-$end?feature=gene;content-type=application/json"
        return Request.Builder().url(url).build()
    }

    private fun parseMappingResponseToJsonArray(response: Response): JsonArray {
        return response.body!!.string().let { Json.parseToJsonElement(it).jsonArray }
    }

    private fun mapPositionalJsonArrayResultsToGene(jsonArray: JsonArray, rsId: String): Gene {
        val resultingGene = jsonArray[0].jsonObject
        return Gene(
            snp = rsId,
            symbol = resultingGene["external_name"]!!.jsonPrimitive.content,
            type = resultingGene["biotype"]!!.jsonPrimitive.content,
            mappingType = "Positional",
        )
    }

    fun functionalMapping(snp: String): List<Gene> {
        val request = geneFunctionalRequest(snp)
        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Response from ENSEMBL was unsuccessful: $response")
            return mapJsonArrayTranscriptsToGenes(parseMappingResponseToJsonArray(response), snp)
        }

    }

    private fun geneFunctionalRequest(rsId: String): Request {
        val url = "https://rest.ensembl.org//vep/human/id/$rsId?content-type=application/json"
        return Request.Builder().url(url).build()
    }

    private fun mapJsonArrayTranscriptsToGenes(jsonArray: JsonArray, rsId: String): List<Gene> {
        val genes = mutableListOf<Gene>()
        val transcripts = jsonArray[0].jsonObject["transcript_consequences"]?.jsonArray

        transcripts?.forEach { transcriptElement ->

            val gene = Gene(
                snp = rsId,
                symbol = transcriptElement.jsonObject["gene_symbol"]?.jsonPrimitive?.content,
                type = transcriptElement.jsonObject["biotype"]?.jsonPrimitive?.content,
                mappingType = "Functional"
            )

            genes.add(gene)
        }
        return genes
    }


}