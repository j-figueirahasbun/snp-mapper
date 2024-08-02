package com.genes.snp_mapper

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class RankingService {

    @Autowired
    private val geneService = GeneService()

    fun rankGeneMappings(snp: String): String? { //could use refactoring
        val positionallyMappedGene = setOf(geneService.positionalMapping(snp).symbol!!)
        val functionallyMappedGenes = (geneService.functionalMapping(snp).map { it.symbol }).toSet()
        val eQTLMappedGenes = (geneService.eQTLMapping(snp).map { it.symbol }).toSet()

        return when {
            findCommonGene(positionallyMappedGene, functionallyMappedGenes, eQTLMappedGenes) != null ->
                findCommonGene(positionallyMappedGene, functionallyMappedGenes, eQTLMappedGenes)
            findCommonGene(positionallyMappedGene, functionallyMappedGenes) != null ->
                findCommonGene(positionallyMappedGene, functionallyMappedGenes)
            findCommonGene(functionallyMappedGenes, eQTLMappedGenes) != null ->
                findCommonGene(functionallyMappedGenes, eQTLMappedGenes)
            else -> findMostFrequentGene(geneService.positionalMapping(snp).symbol!!,
                geneService.functionalMapping(snp).map { it.symbol },
                geneService.eQTLMapping(snp).map { it.symbol })
        }
    }

    private fun findCommonGene (vararg geneSets: Set<String>) : String? { //
        return geneSets.reduce{acc, set -> acc intersect set}.firstOrNull()
    }

    private fun findMostFrequentGene(positional: String, functional: List<String>, eQTL : List<String>) : String {
        val geneFrequencyMap = mutableMapOf<String,Int>()

        positional.let{geneFrequencyMap.incrementCount(it)} //
        functional.forEach { geneFrequencyMap.incrementCount(it) }
        eQTL.forEach { geneFrequencyMap.incrementCount(it) }

        return geneFrequencyMap.maxBy{it.value}.key //
    }

    private fun MutableMap<String, Int>.incrementCount(gene : String){ //
        this[gene] = this.getOrDefault(gene, 0) + 1
    }
}