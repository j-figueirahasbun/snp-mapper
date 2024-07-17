package com.genes.snp_mapper

class Gene (
    private var geneId: String,
    private var symbol: String,
    private var chromosome: String,
    private var startPosition: Int,
    private var endPosition: Int,
    private var bioType: String,
    private var mapping: String,
)
{
    constructor(geneId: String, symbol: String, geneType: String) : this(geneId, "unknown", "unknown", 0, 0, "Unknown", "Unknown")

    fun getGeneId(): String {
        return geneId
    }

    fun getSymbol(): String {
        return symbol
    }

    fun getChromosome(): String {
        return chromosome
    }

    fun getStartPosition(): Int {
        return startPosition
    }

    fun getEndPosition(): Int {
        return endPosition
    }

    fun getMapping(): String {
        return mapping
    }

    fun getBioType(): String {
        return bioType
    }

    fun setGeneId(value: String) {
        geneId = value
    }

    fun setSymbol(value: String) {
        symbol = value
    }

    fun setChromosome(value: String) {
        chromosome = value
    }

    fun setStartPosition(value: Int) {
        startPosition = value
    }

    fun setEndPosition(value: Int) {
        endPosition = value
    }

    fun setMapping(value: String) {
        mapping = value
    }

    fun setBioType(value: String) {
        bioType = value
    }

}