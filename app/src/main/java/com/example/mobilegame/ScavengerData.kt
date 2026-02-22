package com.example.mobilegame
data class ScavengerTarget(
    val synonyms: List<String>,
    val threshold: Float = 0.7f
)

//DEFAULT THRESHOLD 0.7F, LOWER THRESHOLD IF FAIL TO DETECT VICE VERSA
object GameLibrary {
    val categoryTargets = mapOf(
        "Nature" to mapOf(
            "Houseplant" to ScavengerTarget(listOf("Houseplant", "Potted plant", "Vase")),
            "Flower" to ScavengerTarget(listOf("Flower", "Petal", "Blossom", "Rose", "Daisy")),
            "Bird" to ScavengerTarget(listOf("Bird", "Beak", "Feather", "Wing")),
            "Soil" to ScavengerTarget(listOf("Soil", "Dirt", "Ground", "Mud"))
        ),
        "Kitchen" to mapOf(
            "Coffee Mug" to ScavengerTarget(listOf("Mug", "Cup", "Coffee cup")),
            "Fruit" to ScavengerTarget(listOf("Fruit", "Apple", "Banana", "Orange", "Citrus")),
            "Bread" to ScavengerTarget(listOf("Bread", "Toast", "Bakery", "Loaf", "Baguette"))
        ),
        "Office" to mapOf(
            "Television" to ScavengerTarget(listOf("Television", "Monitor", "Screen", "Tv")),
            "Chair" to ScavengerTarget(listOf("Chair", "Seat", "Stool", "Armchair")),
            "Mobile phone" to ScavengerTarget(listOf("Mobile phone", "Smartphone", "Telephone", "Cell phone")),
            "Computer" to ScavengerTarget(listOf("Computer", "Laptop", "Keyboard", "Desktop"))
        ),
        "Pets" to mapOf(
            "Dog" to ScavengerTarget(listOf("Dog", "Canine", "Puppy", "Hound"), 0.85f),
            "Cat" to ScavengerTarget(listOf("Cat", "Feline", "Kitten", "Tabby"), 0.85f)
        )
    )
}