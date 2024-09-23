package com.genes.snp_mapper

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.mockito.Mockito.*
import org.mockito.kotlin.whenever

class RankingServiceTest {

    private lateinit var geneService:GeneService
    private lateinit var rankingService: RankingService

    private var positionalGene = Gene("rs123", "BRCA1", "protein_coding", "Positional")
    private var functionalGene = Gene("rs123", "FYCO1", "protein_coding", "Functional")
    private var eQTLGene = Gene("rs123", "ABO", "Liver", "eQTL")

    @BeforeEach
    fun setUp(){
        geneService = mock(GeneService::class.java)
        rankingService = spy(RankingService::class.java)

        val geneServiceField = RankingService::class.java.getDeclaredField("geneService")
        geneServiceField.isAccessible = true
        geneServiceField.set(rankingService, geneService)

        whenever(geneService.positionalMapping(anyString())).thenReturn(positionalGene)
        whenever(geneService.functionalMapping(anyString())).thenReturn(listOf(functionalGene))
        whenever(geneService.eQTLMapping(anyString())).thenReturn(listOf(eQTLGene))

    }


    @Test
    fun rankGeneMappingsGivesTheMostCommonGene() {
        whenever(geneService.functionalMapping(anyString())).thenReturn(listOf(Gene("rs123", "BRCA1",
            "protein_coding", "Functional")))
        whenever(geneService.eQTLMapping(anyString())).thenReturn(listOf(Gene("rs123", "BRCA1",
            "Liver", "eQTL")))


        val result = rankingService.rankGeneMappings("rs123")

        assertEquals("BRCA1", result)
    }

    @Test
    fun rankGeneMappingsHasTheMostCommonGeneFromFunctionalAndEQTL() {
        whenever(geneService.functionalMapping(anyString())).thenReturn(listOf(Gene("rs123", "ABO",
            "protein_coding", "Functional")))

        val result = rankingService.rankGeneMappings("rs123")

        assertEquals("ABO", result)
    }

    @Test
    fun rankGeneMappingsHasTheMostCommonGeneFromPositionalAndEQTL() {
        whenever(geneService.positionalMapping(anyString())).thenReturn((Gene("rs123", "ABO",
            "protein_coding", "Positional")))

        val result = rankingService.rankGeneMappings("rs123")

        assertEquals("ABO", result)
    }

    @Test
    fun rankGeneMappingsHasTheMostCommonGeneFromPositionalAndFunctional() {
        whenever(geneService.functionalMapping(anyString())).thenReturn(listOf(Gene("rs123", "BRCA1",
            "protein_coding", "Functional")))

        val result = rankingService.rankGeneMappings("rs123")

        assertEquals("BRCA1", result)
    }

}
