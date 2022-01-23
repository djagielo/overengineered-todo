package dev.bettercode.commons.paging

import org.springframework.data.domain.Page

class PageResult<T>(page: Page<T>) {
    val content: List<T> = page.content
    val number: Int = page.number
    val size: Int = page.size
    val totalElements: Long = page.totalElements
    val totalPages: Int = page.totalPages
}