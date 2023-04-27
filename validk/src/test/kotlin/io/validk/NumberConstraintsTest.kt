package io.validk

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

class NumberConstraintsTest : StringSpec({

    "min" {
        validation<Int> { min(3) }.validate(2) shouldNotBe null
        validation<Int> { min(3) }.validate(3) shouldBe null
        validation<Int> { min(3) }.validate(100) shouldBe null

        validation<Double> { min(3.0) }.validate(2.99999) shouldNotBe null
        validation<Double> { min(3.0) }.validate(3.0) shouldBe null
        validation<Double> { min(3.0) }.validate(100.0) shouldBe null

        validation<Long> { min(3L) }.validate(2L) shouldNotBe null
        validation<Long> { min(3L) }.validate(3L) shouldBe null
        validation<Long> { min(3L) }.validate(100L) shouldBe null
    }

    "minExclusive" {
        validation<Int> { minExclusive(3) }.validate(2) shouldNotBe null
        validation<Int> { minExclusive(3) }.validate(3) shouldNotBe null
        validation<Int> { minExclusive(3) }.validate(4) shouldBe null

        validation<Double> { minExclusive(3.0) }.validate(2.99999) shouldNotBe null
        validation<Double> { minExclusive(3.0) }.validate(3.0) shouldNotBe null
        validation<Double> { minExclusive(3.0) }.validate(3.000001) shouldBe null

        validation<Long> { minExclusive(3L) }.validate(2L) shouldNotBe null
        validation<Long> { minExclusive(3L) }.validate(3L) shouldNotBe null
        validation<Long> { minExclusive(3L) }.validate(4) shouldBe null
    }

    "max" {
        validation<Int> { max(3) }.validate(2) shouldBe null
        validation<Int> { max(3) }.validate(3) shouldBe null
        validation<Int> { max(3) }.validate(4) shouldNotBe null

        validation<Double> { max(3.0) }.validate(2.99999) shouldBe null
        validation<Double> { max(3.0) }.validate(3.0) shouldBe null
        validation<Double> { max(3.0) }.validate(3.00001) shouldNotBe null

        validation<Long> { max(3L) }.validate(2L) shouldBe null
        validation<Long> { max(3L) }.validate(3L) shouldBe null
        validation<Long> { max(3L) }.validate(4L) shouldNotBe null
    }

    "maxExclusive" {
        validation<Int> { maxExclusive(3) }.validate(2) shouldBe null
        validation<Int> { maxExclusive(3) }.validate(3) shouldNotBe null
        validation<Int> { maxExclusive(3) }.validate(4) shouldNotBe null

        validation<Double> { maxExclusive(3.0) }.validate(2.99999) shouldBe null
        validation<Double> { maxExclusive(3.0) }.validate(3.0) shouldNotBe null
        validation<Double> { maxExclusive(3.0) }.validate(3.00001) shouldNotBe null

        validation<Long> { maxExclusive(3L) }.validate(2L) shouldBe null
        validation<Long> { maxExclusive(3L) }.validate(3L) shouldNotBe null
        validation<Long> { maxExclusive(3L) }.validate(4L) shouldNotBe null
    }

})
