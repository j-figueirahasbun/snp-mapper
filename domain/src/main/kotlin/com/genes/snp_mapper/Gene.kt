package com.genes.snp_mapper

import kotlinx.serialization.Serializable

@Serializable
data class Gene (
    private val snp: String,
    private var symbol: String? = "Not Available",
    private var type: String? = "Not Available",
    private var mappingType: String,
    )
{

}