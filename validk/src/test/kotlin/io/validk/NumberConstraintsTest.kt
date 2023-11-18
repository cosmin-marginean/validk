package io.validk

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

class NumberConstraintsTest : StringSpec({

    "min" {
        Validation<Int> { min(3) }.validate(2) shouldNotBe null
        Validation<Int> { min(3) }.validate(3) shouldBe null
        Validation<Int> { min(3) }.validate(100) shouldBe null

        Validation<Double> { min(3.0) }.validate(2.99999) shouldNotBe null
        Validation<Double> { min(3.0) }.validate(3.0) shouldBe null
        Validation<Double> { min(3.0) }.validate(100.0) shouldBe null

        Validation<Long> { min(3L) }.validate(2L) shouldNotBe null
        Validation<Long> { min(3L) }.validate(3L) shouldBe null
        Validation<Long> { min(3L) }.validate(100L) shouldBe null
    }

    "minExclusive" {
        Validation<Int> { minExclusive(3) }.validate(2) shouldNotBe null
        Validation<Int> { minExclusive(3) }.validate(3) shouldNotBe null
        Validation<Int> { minExclusive(3) }.validate(4) shouldBe null

        Validation<Double> { minExclusive(3.0) }.validate(2.99999) shouldNotBe null
        Validation<Double> { minExclusive(3.0) }.validate(3.0) shouldNotBe null
        Validation<Double> { minExclusive(3.0) }.validate(3.000001) shouldBe null

        Validation<Long> { minExclusive(3L) }.validate(2L) shouldNotBe null
        Validation<Long> { minExclusive(3L) }.validate(3L) shouldNotBe null
        Validation<Long> { minExclusive(3L) }.validate(4) shouldBe null
    }

    "max" {
        Validation<Int> { max(3) }.validate(2) shouldBe null
        Validation<Int> { max(3) }.validate(3) shouldBe null
        Validation<Int> { max(3) }.validate(4) shouldNotBe null

        Validation<Double> { max(3.0) }.validate(2.99999) shouldBe null
        Validation<Double> { max(3.0) }.validate(3.0) shouldBe null
        Validation<Double> { max(3.0) }.validate(3.00001) shouldNotBe null

        Validation<Long> { max(3L) }.validate(2L) shouldBe null
        Validation<Long> { max(3L) }.validate(3L) shouldBe null
        Validation<Long> { max(3L) }.validate(4L) shouldNotBe null
    }

    "maxExclusive" {
        Validation<Int> { maxExclusive(3) }.validate(2) shouldBe null
        Validation<Int> { maxExclusive(3) }.validate(3) shouldNotBe null
        Validation<Int> { maxExclusive(3) }.validate(4) shouldNotBe null

        Validation<Double> { maxExclusive(3.0) }.validate(2.99999) shouldBe null
        Validation<Double> { maxExclusive(3.0) }.validate(3.0) shouldNotBe null
        Validation<Double> { maxExclusive(3.0) }.validate(3.00001) shouldNotBe null

        Validation<Long> { maxExclusive(3L) }.validate(2L) shouldBe null
        Validation<Long> { maxExclusive(3L) }.validate(3L) shouldNotBe null
        Validation<Long> { maxExclusive(3L) }.validate(4L) shouldNotBe null
    }

})
