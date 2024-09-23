package com.genes.snp_mapper

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.mockito.Mockito.*
import org.mockito.kotlin.whenever

class RankingServiceTest {

    private lateinit var geneService:GeneService
    private lateinit var rankingService: RankingService

    private val positionalGene = "Gene1"
    private val functionalGene = "Gene2"
    private val eQTLGene = "Gene3"

    @BeforeEach
    fun setUp(){
        geneService = mock(GeneService::class.java)
        rankingService = spy(RankingService::class.java)

        val geneServiceField = RankingService::class.java.getDeclaredField("geneService")
        geneServiceField.isAccessible = true
        geneServiceField.set(rankingService, geneService)

        //whenever(geneService.positionalMapping(anyString())).thenReturn(mockGeneMapping(positionalGene))

        }
    }


    @Test
    fun rankGeneMappings() {
    }
}