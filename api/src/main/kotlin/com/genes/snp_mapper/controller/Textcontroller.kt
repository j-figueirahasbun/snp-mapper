package com.genes.snp_mapper.controller

import com.genes.snp_mapper.TextService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/text")
class Textcontroller(private val textService: TextService) {

    @GetMapping("/hello")
    fun getHelloWorld(): String {
        return textService.helloWorld()
    }
}
