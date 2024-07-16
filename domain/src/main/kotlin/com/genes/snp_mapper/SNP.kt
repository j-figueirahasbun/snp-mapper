package com.genes.snp_mapper

class SNP (
    private var rsId: String,
    private var chromosome: String,
    private var position: Int,
    private var referenceAllele: String,
    private var alternateAllele: String,

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

    fun setRsID(value: String) {
        rsId = value
    }

    fun setChromosome(value: String) {
        chromosome = value
    }

    fun setPosition(value: Int) {
        position = value
    }

    fun setReferenceAllele(value: String) {
        referenceAllele = value
    }

    fun setAlternateAllele(value: String) {
        alternateAllele = value
    }
}