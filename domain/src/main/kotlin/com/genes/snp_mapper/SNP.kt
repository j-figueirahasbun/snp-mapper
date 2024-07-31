package com.genes.snp_mapper

import kotlinx.serialization.Serializable

@Serializable
data class SNP (
    private var rsId: String,
    var chromosome: String,
    var position: Int,
    private var referenceAllele: String,
    private var alternateAllele: String,
    )
{

}