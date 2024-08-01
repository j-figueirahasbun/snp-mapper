package com.genes.snp_mapper.controller

import com.genes.snp_mapper.Gene
import com.genes.snp_mapper.GeneService
import com.genes.snp_mapper.SNP
import com.genes.snp_mapper.SNPService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/snp")
class SNPController (private val snpService: SNPService, private val geneService: GeneService){

    @GetMapping("/info/{snp}")
    fun getSNPInformation(@PathVariable snp:String): SNP {
        return snpService.obtainSNPInformation(snp)
    }

    @GetMapping("/mapping/info/{snp}")
    fun positionalMappingSNP(@PathVariable snp:String): Gene {
        return geneService.positionalMapping(snp)
    }

    @GetMapping("/info/{snp}")
    fun functionalMappingSNP(@PathVariable snp:String): List<Gene> {
        return geneService.functionalMapping(snp)
    }
}
