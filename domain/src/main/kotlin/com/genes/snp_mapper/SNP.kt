package com.genes.snp_mapper

import kotlinx.serialization.Serializable

@Serializable
data class SNP (
    private var rsId: String,
    private var chromosome: String,
    private var position: Int,
    private var referenceAllele: String,
    private var alternateAllele: String,
    )
{

}