package io.validk

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

class CollectionTest : StringSpec({

    "empty, min/max size" {
        Validation<List<String>> { notEmpty() }.validate(emptyList()) shouldNotBe null
        Validation<List<String>> { notEmpty() }.validate(listOf("AA")) shouldBe null

        Validation<List<String>> { minSize(2) }.validate(listOf("AA")) shouldNotBe null
        Validation<List<String>> { minSize(2) }.validate(listOf("AA", "BB")) shouldBe null
        Validation<List<String>> { minSize(2) }.validate(listOf("AA", "BB", "C")) shouldBe null

        Validation<List<String>> { maxSize(2) }.validate(listOf("AA")) shouldBe null
        Validation<List<String>> { maxSize(2) }.validate(listOf("AA", "BB")) shouldBe null
        Validation<List<String>> { maxSize(2) }.validate(listOf("AA", "BB", "C")) shouldNotBe null
    }
})
