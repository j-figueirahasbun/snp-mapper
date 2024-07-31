package com.genes.snp_mapper

import kotlinx.serialization.Serializable

@Serializable
class Gene (
    private var snp: String,
    private var symbol: String,
    private var type: String,
    private var mappingType: String,
    )
{

}