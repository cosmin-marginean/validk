# Validk
![GitHub release (latest by date)](https://img.shields.io/github/v/release/cosmin-marginean/validk)
![Coveralls](https://img.shields.io/coverallsCoverage/github/cosmin-marginean/validk)

Validk is a validation framework for Kotlin (JVM), largely inspired by [Konform](https://github.com/konform-kt/konform). Among other things,
the design aims to solve some specialised use cases like context-aware and conditional validation.

The framework provides a typesafe DSL and has zero dependencies.

## Dependency
```groovy
implementation "io.resoluteworks:validk:${validkVersion}"
```

## The basics

```kotlin
data class Employee(val name: String, val email: String?)
data class Organisation(val name: String, val employees: List<Employee>)

val validation = Validation(failFast = false) {
    Organisation::name { minLength(5) }
    Organisation::employees each {
        Employee::name { minLength(10) }
        Employee::email ifNotNull { email() }
    }
}

val org = Organisation(
    "A", listOf(
        Employee("John", "john@test.com"),
        Employee("Hannah Johnson", "hanna")
    )
)
val errors = validation.validate(org)
errors?.errors?.forEach { println(it) }
```

This would print
```text
ValidationError(propertyPath=name, errorMessage=must be at least 5 characters)
ValidationError(propertyPath=employees[0].name, errorMessage=must be at least 10 characters)
ValidationError(propertyPath=employees[1].email, errorMessage=must be a valid email)
```

Validating an object returns a `ValidationErrors` which is `null` when validation succeeds.
In other words, validation is successful when the response is `null`, or an instance of `ValidationErrors` when it fails.   

Please check the [tests](https://github.com/cosmin-marginean/validk/tree/main/validk/src/test/kotlin/io/validk) for more examples and the [documentation](https://cosmin-marginean.github.io/validk/dokka/validk/validk/io.validk/index.html) for a full list of constraints.

## Context-aware and conditional validation
Validk provides the ability to access the object being validated using the `withValue` construct.
```kotlin
private data class Entity(
    val entityType: String,
    val registeredOffice: String,
    val proofOfId: String
)

private enum class EntityType { COMPANY, PERSON }

val validation: Validation<Entity> = Validation<Entity> {
    Entity::entityType { enum<EntityType>() }
    withValue { entity ->
        when (entity.entityType) {
            "PERSON" -> Entity::proofOfId { minLength(10) }
            "COMPANY" -> Entity::registeredOffice { minLength(5) }
        }
    }
}
```

Alternatively, you can add validation logic based on the value of a specific property using `whenIs`.
```kotlin
val validation: Validation<Entity> = Validation {
    Entity::entityType { enum<EntityType>() }

    Entity::entityType.whenIs("PERSON") {
        Entity::proofOfId { minLength(10) }
    }

    Entity::entityType.whenIs("COMPANY") {
        Entity::registeredOffice { minLength(5) }
    }
}
```

## ValidObject
`ValidObject` provides a basic mechanism for storing the validation logic within the object itself.
```kotlin
data class MyObject(val name: String, val age: Int) : ValidObject<MyObject> {
    override val validation: Validation<MyObject> = Validation {
        MyObject::name { notBlank() }
        MyObject::age { min(18) }
    }
}

val result = MyObject("John Smith", 12).validate()
```

## Custom messages
```kotlin
Validation<Person> {
    Person::name {
        notBlank() message "A person needs a name"
        matches("[a-zA-Z\\s]+") message "Letters only please"
    }
}
```

## Eager errors
It's often only required to return the first failure (failed constraint) message when validating a field. This is usually the case when displaying user errors in an application and when the order of the constraints implies the next one would fail: `notBlank()` failing implies `email()` will fail, but we first want to respond with "Email is required" rather than ["Email is required", "This is not a valid email"].

For this purpose `ValidationErrors` provides `eager*` versions of its properties, including `eagerErrors` and `eagerErrorMessages`. For a full list of properties please check the [ValiationErrors docs](https://cosmin-marginean.github.io/validk/dokka/validk/validk/io.validk/-validation-errors/index.html)
