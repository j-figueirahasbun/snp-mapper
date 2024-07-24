package com.genes.snp_mapper

class Gene (
    private var geneId: String,
    private var start: Int,
    private var end: Int,
    private var externalName: String,
    private var strand: Int,
    private var id: String,
    private var transcript: String,
    private var type: String,
    private var description: String,
    private var seqRegionName: String,
    private var mapping: String,

    )

{
    fun getGeneId(): String {
        return geneId
    }

    fun getStart(): Int {
        return start
    }

    fun getEnd(): Int {
        return end
    }

    fun getExternalName(): String {
        return externalName
    }
    fun getStrand(): Int {
        return strand
    }
    fun getID(): String {
        return id
    }
    fun getTranscript(): String {
        return transcript
    }
    fun getType(): String {
        return type
    }
    fun getDescription(): String {
        return description
    }

    fun getSeqRegionName(): String {
        return seqRegionName
    }

    fun getMapping(): String {
        return mapping
    }

    fun setGeneID(value: String) {
        geneId = value
    }

    fun setStart(value: Int) {
        start = value
    }

    fun setEnd(value: Int) {
        end = value
    }

    fun setExternalName(value: String) {
        externalName = value
    }

    fun setStrand(value: Int) {
        strand = value
    }

    fun setId(value: String) {
        id = value
    }

    fun setType(value: String) {
        type = value
    }

    fun setTranscript(value: String) {
        transcript = value
    }

    fun setDescription(value: String) {
        description = value
    }

    fun setSeqRegionName(value: String) {
        seqRegionName = value
    }

    fun setMapping(value: String) {
        mapping = value
    }

    override fun toString(): String {
        return "Gene ID: $geneId\n" +
                "Start: $start\n" +
                "End: $end\n" +
                "External Name: $externalName\n" +
                "Strand: $strand\n" +
                "ID: $id\n" +
                "Transcript: $transcript\n" +
                "Type: $type\n" +
                "Description: $description\n" +
                "SeqRegionName: $seqRegionName\n" +
                "Mapping: $mapping\n"
    }

}