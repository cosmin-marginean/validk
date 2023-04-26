package io.validk

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.validk.ValidationError.Companion.indexedProperty

class IndexedPropertyTest : StringSpec({

    "indexed property" {
        "name".indexedProperty("name", 1) shouldBe "name[1]"
        "name.child".indexedProperty("name", 1) shouldBe "name[1].child"
        "address.postCodes.value".indexedProperty("postCodes", 1) shouldBe "address.postCodes[1].value"
    }
})