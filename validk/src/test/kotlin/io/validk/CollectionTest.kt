package io.validk

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

class CollectionTest : StringSpec({

    "empty, min/max size" {
        validation<List<String>> { notEmptyCollection() }.validate(emptyList()) shouldNotBe null
        validation<List<String>> { notEmptyCollection() }.validate(listOf("AA")) shouldBe null

        validation<List<String>> { minSize(2) }.validate(listOf("AA")) shouldNotBe null
        validation<List<String>> { minSize(2) }.validate(listOf("AA", "BB")) shouldBe null
        validation<List<String>> { minSize(2) }.validate(listOf("AA", "BB", "C")) shouldBe null

        validation<List<String>> { maxSize(2) }.validate(listOf("AA")) shouldBe null
        validation<List<String>> { maxSize(2) }.validate(listOf("AA", "BB")) shouldBe null
        validation<List<String>> { maxSize(2) }.validate(listOf("AA", "BB", "C")) shouldNotBe null
    }
})