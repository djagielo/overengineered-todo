package dev.bettercode.dynamicprojects.infra.adapter.rest

import dev.bettercode.dynamicprojects.DynamicProjectDto
import dev.bettercode.dynamicprojects.DynamicProjectsFacade
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class DynamicProjectsController(private val dynamicProjectsFacade: DynamicProjectsFacade) {

    @GetMapping("/dynamic-projects")
    fun getDynamicProjects(): Page<DynamicProjectDto> {
        return dynamicProjectsFacade.getProjects(PageRequest.of(0, 100))
    }

}