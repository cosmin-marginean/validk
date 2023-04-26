package io.validk

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class NumberConstraintsTest : StringSpec({

    "min" {
        validation<Int> { min(3) }.validate(2).success shouldBe false
        validation<Int> { min(3) }.validate(3).success shouldBe true
        validation<Int> { min(3) }.validate(100).success shouldBe true

        validation<Double> { min(3.0) }.validate(2.99999).success shouldBe false
        validation<Double> { min(3.0) }.validate(3.0).success shouldBe true
        validation<Double> { min(3.0) }.validate(100.0).success shouldBe true

        validation<Long> { min(3L) }.validate(2L).success shouldBe false
        validation<Long> { min(3L) }.validate(3L).success shouldBe true
        validation<Long> { min(3L) }.validate(100L).success shouldBe true
    }

    "minExclusive" {
        validation<Int> { minExclusive(3) }.validate(2).success shouldBe false
        validation<Int> { minExclusive(3) }.validate(3).success shouldBe false
        validation<Int> { minExclusive(3) }.validate(4).success shouldBe true

        validation<Double> { minExclusive(3.0) }.validate(2.99999).success shouldBe false
        validation<Double> { minExclusive(3.0) }.validate(3.0).success shouldBe false
        validation<Double> { minExclusive(3.0) }.validate(3.000001).success shouldBe true

        validation<Long> { minExclusive(3L) }.validate(2L).success shouldBe false
        validation<Long> { minExclusive(3L) }.validate(3L).success shouldBe false
        validation<Long> { minExclusive(3L) }.validate(4).success shouldBe true
    }

    "max" {
        validation<Int> { max(3) }.validate(2).success shouldBe true
        validation<Int> { max(3) }.validate(3).success shouldBe true
        validation<Int> { max(3) }.validate(4).success shouldBe false

        validation<Double> { max(3.0) }.validate(2.99999).success shouldBe true
        validation<Double> { max(3.0) }.validate(3.0).success shouldBe true
        validation<Double> { max(3.0) }.validate(3.00001).success shouldBe false

        validation<Long> { max(3L) }.validate(2L).success shouldBe true
        validation<Long> { max(3L) }.validate(3L).success shouldBe true
        validation<Long> { max(3L) }.validate(4L).success shouldBe false
    }

    "maxExclusive" {
        validation<Int> { maxExclusive(3) }.validate(2).success shouldBe true
        validation<Int> { maxExclusive(3) }.validate(3).success shouldBe false
        validation<Int> { maxExclusive(3) }.validate(4).success shouldBe false

        validation<Double> { maxExclusive(3.0) }.validate(2.99999).success shouldBe true
        validation<Double> { maxExclusive(3.0) }.validate(3.0).success shouldBe false
        validation<Double> { maxExclusive(3.0) }.validate(3.00001).success shouldBe false

        validation<Long> { maxExclusive(3L) }.validate(2L).success shouldBe true
        validation<Long> { maxExclusive(3L) }.validate(3L).success shouldBe false
        validation<Long> { maxExclusive(3L) }.validate(4L).success shouldBe false
    }

})
