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
                symbol = transcriptElement.jsonObject["gene_symbol"]!!.jsonPrimitive!!.content,
                type = transcriptElement.jsonObject["biotype"]!!.jsonPrimitive!!.content,
                mappingType = "Functional"
            )

            genes.add(gene)
        }
        return genes
    }

    private fun obtainGTExVariantId (snp: String): String {
        val url = "https://gtexportal.org/api/v2/dataset/variant?&snpId=$snp&page=0&itemsPerPage=250"
        val gtexVariantId : String = "Not available"
        client.newCall(Request.Builder().url(url).build()).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Response from : $response")
            val responseInArray = response.body!!.string().let { Json.parseToJsonElement(it).jsonArray }
            val gtexVariantId = responseInArray[0].jsonObject["variantId"]?.jsonPrimitive?.content
        }
        return gtexVariantId
    }

    private fun geneEQTLRequest(gtexVariantId: String): Request {
        val url = "https://gtexportal.org/api/v2/association/singleTissueEqtl?variantId=$gtexVariantId&tissueSiteDetailId=Adipose_Subcutaneous&tissueSiteDetailId=Adipose_Visceral_Omentum&tissueSiteDetailId=Adrenal_Gland&tissueSiteDetailId=Artery_Aorta&tissueSiteDetailId=Artery_Coronary&tissueSiteDetailId=Artery_Tibial&tissueSiteDetailId=Bladder&tissueSiteDetailId=Brain_Amygdala&tissueSiteDetailId=Brain_Anterior_cingulate_cortex_BA24&tissueSiteDetailId=Brain_Caudate_basal_ganglia&tissueSiteDetailId=Brain_Cerebellar_Hemisphere&tissueSiteDetailId=Brain_Cerebellum&tissueSiteDetailId=Brain_Cortex&tissueSiteDetailId=Brain_Frontal_Cortex_BA9&tissueSiteDetailId=Brain_Hippocampus&tissueSiteDetailId=Brain_Hypothalamus&tissueSiteDetailId=Brain_Nucleus_accumbens_basal_ganglia&tissueSiteDetailId=Brain_Putamen_basal_ganglia&tissueSiteDetailId=Brain_Spinal_cord_cervical_c-1&tissueSiteDetailId=Brain_Substantia_nigra&tissueSiteDetailId=Breast_Mammary_Tissue&tissueSiteDetailId=Cells_EBV-transformed_lymphocytes&tissueSiteDetailId=Cells_Cultured_fibroblasts&tissueSiteDetailId=Cervix_Ectocervix&tissueSiteDetailId=Cervix_Endocervix&tissueSiteDetailId=Colon_Sigmoid&tissueSiteDetailId=Colon_Transverse&tissueSiteDetailId=Esophagus_Gastroesophageal_Junction&tissueSiteDetailId=Esophagus_Mucosa&tissueSiteDetailId=Esophagus_Muscularis&tissueSiteDetailId=Fallopian_Tube&tissueSiteDetailId=Heart_Atrial_Appendage&tissueSiteDetailId=Heart_Left_Ventricle&tissueSiteDetailId=Kidney_Cortex&tissueSiteDetailId=Kidney_Medulla&tissueSiteDetailId=Liver&tissueSiteDetailId=Lung&tissueSiteDetailId=Minor_Salivary_Gland&tissueSiteDetailId=Muscle_Skeletal&tissueSiteDetailId=Nerve_Tibial&tissueSiteDetailId=Ovary&tissueSiteDetailId=Pancreas&tissueSiteDetailId=Pituitary&tissueSiteDetailId=Prostate&tissueSiteDetailId=Skin_Not_Sun_Exposed_Suprapubic&tissueSiteDetailId=Skin_Sun_Exposed_Lower_leg&tissueSiteDetailId=Small_Intestine_Terminal_Ileum&tissueSiteDetailId=Spleen&tissueSiteDetailId=Stomach&tissueSiteDetailId=Testis&tissueSiteDetailId=Thyroid&tissueSiteDetailId=Uterus&tissueSiteDetailId=Vagina&tissueSiteDetailId=Whole_Blood&page=0&itemsPerPage=250"
        return Request.Builder().url(url).build()
    }

    private fun mapEQTLResultsArrayToGenes ( jsonArray: JsonArray) : List<Gene> {
        val genes = mutableListOf<Gene>()
        jsonArray.forEach{ mappingElement ->
            val mappingElement = mappingElement.jsonObject
            val gene = Gene (
                snp = mappingElement["snpId"]!!.jsonPrimitive.content,
                symbol = mappingElement["geneSymbol"]!!.jsonPrimitive!!.content,
                type = mappingElement["tissueSiteDetailId"]!!.jsonPrimitive!!.content,
                mappingType = "eQTL Mapping"
            )
            genes.add(gene)
        }
        return genes
    }

    fun eQTLMapping (snp: String): List<Gene> {
        val request = geneEQTLRequest(obtainGTExVariantId(snp))
        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Response from GTEx database was unsuccessful: $response")
            return mapEQTLResultsArrayToGenes(parseMappingResponseToJsonArray(response))
        }
    }
}
