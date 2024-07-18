package com.genes.snp_mapper.controller

import com.genes.snp_mapper.SNPService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/snp")
class SNPController (private val snpService: SNPService){

    @GetMapping("/info/{snp}")
    fun getSNPInformation(@PathVariable snp:String): String {
        return snpService.fetchSNPInformation(snp)
    }

}