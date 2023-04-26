package io.validk

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class CollectionTest : StringSpec({

    "empty, min/max size" {
        validation<List<String>> { notEmptyCollection() }.validate(emptyList()).success shouldBe false
        validation<List<String>> { notEmptyCollection() }.validate(listOf("AA")).success shouldBe true

        validation<List<String>> { minSize(2) }.validate(listOf("AA")).success shouldBe false
        validation<List<String>> { minSize(2) }.validate(listOf("AA", "BB")).success shouldBe true
        validation<List<String>> { minSize(2) }.validate(listOf("AA", "BB", "C")).success shouldBe true

        validation<List<String>> { maxSize(2) }.validate(listOf("AA")).success shouldBe true
        validation<List<String>> { maxSize(2) }.validate(listOf("AA", "BB")).success shouldBe true
        validation<List<String>> { maxSize(2) }.validate(listOf("AA", "BB", "C")).success shouldBe false
    }
})