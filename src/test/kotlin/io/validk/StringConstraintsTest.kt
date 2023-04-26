package io.validk

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class StringConstraintsTest : StringSpec({

    "notEmpty" {
        validation { notEmpty() }.validate("abc").success shouldBe true
        validation { notEmpty() }.validate("  ").success shouldBe true
        validation { notEmpty() }.validate("").success shouldBe false
    }

    "notBlank" {
        validation { notBlank() }.validate("abc").success shouldBe true
        validation { notBlank() }.validate("  abc ").success shouldBe true
        validation { notBlank() }.validate("  ").success shouldBe false
        validation { notBlank() }.validate("").success shouldBe false
    }

    "minLength" {
        validation { minLength(5) }.validate("abcde").success shouldBe true
        validation { minLength(5) }.validate("abcd").success shouldBe false
        validation { minLength(5) }.validate("a").success shouldBe false
        validation { minLength(5) }.validate(" ").success shouldBe false
        validation { minLength(5) }.validate("").success shouldBe false
    }

    "maxLength" {
        validation { maxLength(5) }.validate("abcdef").success shouldBe false
        validation { maxLength(5) }.validate("abcde").success shouldBe true
        validation { maxLength(5) }.validate("abcd").success shouldBe true
        validation { maxLength(5) }.validate("a").success shouldBe true
        validation { maxLength(5) }.validate(" ").success shouldBe true
        validation { maxLength(5) }.validate("").success shouldBe true
    }

    "matches" {
        validation { matches("[a-zA-Z]+") }.validate("abcdef").success shouldBe true
        validation { matches("[a-zA-Z]+") }.validate("abcdef ").success shouldBe false
        validation { matches("[a-zA-Z]+") }.validate("abc1ef").success shouldBe false
    }

    "email" {
        validation { email() }.validate("test").success shouldBe false
        validation { email() }.validate("test@domain").success shouldBe false
        validation { email() }.validate("test@domain.com").success shouldBe true

        validation { email() }.validate("").success shouldBe false
        validation { email() }.validate("").eagerErrors shouldBe listOf(ValidationError("Object", "cannot be blank"))
        validation { email() }.validate("test").eagerErrors shouldBe listOf(ValidationError("Object", "must be a valid email"))
    }

    "enum" {
        validation { enum("PERSON", "COMPANY") }.validate("PERSON").success shouldBe true
        validation { enum("PERSON", "COMPANY") }.validate("OTHER").success shouldBe false
        validation { enum<TestEnum>() }.validate("COMPANY").success shouldBe true
        validation { enum<TestEnum>() }.validate("OTHER").success shouldBe false
    }
}) {
    private enum class TestEnum {
        PERSON,
        COMPANY
    }
}