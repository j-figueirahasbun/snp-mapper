package com.genes.snp_mapper

class SNP (
    private val rsId: String,
    private val chromosome: String,
    private val position: Int,
    private val referenceAllele: String,
    private val alternateAllele: String,

    )
{
 fun getRsID(): String {
     return rsId
 }

    fun getChromosome(): String {
        return chromosome
    }

    fun getPosition(): Int {
        return position
    }

    fun getReferenceAllele(): String {
        return referenceAllele
    }

    fun getAlternateAllele(): String {
        return alternateAllele
    }
}