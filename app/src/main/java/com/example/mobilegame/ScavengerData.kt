package com.example.mobilegame
data class ScavengerTarget(
    val synonyms: List<String>,
    val threshold: Float = 0.7f,

    //FOR MYSTERY HUNT MODE
    val hint1Vague: String = "",
    val hint2Direct: String = ""
)

//DEFAULT THRESHOLD 0.7F, LOWER THRESHOLD IF FAIL TO DETECT VICE VERSA
object GameLibrary {
    val categoryTargets = mapOf(
        "Nature" to mapOf(
            "Houseplant" to ScavengerTarget(
                synonyms = listOf("Houseplant", "Potted plant", "Vase"),
                hint1Vague = "Nature trapped in a container to decorate your room.",
                hint2Direct = "It's green, stays in a pot, and needs water to survive."
            ),
            "Flower" to ScavengerTarget(
                synonyms = listOf("Flower", "Petal", "Blossom", "Rose", "Daisy"),
                hint1Vague = "The most colorful and fragrant part of a garden.",
                hint2Direct = "It has soft petals and often attracts bees."
            ),
            "Bird" to ScavengerTarget(
                synonyms = listOf("Bird", "Beak", "Feather", "Wing"),
                hint1Vague = "A creature that sees the world from a high perspective.",
                hint2Direct = "Look for something with feathers and a beak."
            ),
            "Soil" to ScavengerTarget(
                synonyms = listOf("Soil", "Dirt", "Ground", "Mud"),
                hint1Vague = "The very foundation where everything begins to grow.",
                hint2Direct = "It's dark, earthy, and you find it under your feet or in a garden."
            )
        ),

        "Kitchen" to mapOf(
            "Coffee Mug" to ScavengerTarget(
                synonyms = listOf("Mug", "Cup", "Coffee cup"),
                hint1Vague = "A ceramic vessel that holds your morning energy.",
                hint2Direct = "It has a handle and is used for hot drinks."
            ),
            "Fruit" to ScavengerTarget(
                synonyms = listOf("Fruit", "Apple", "Banana", "Orange", "Citrus"),
                hint1Vague = "A natural snack that grows on trees or vines.",
                hint2Direct = "It's sweet, colorful, and full of vitamins."
            ),
            "Bread" to ScavengerTarget(
                synonyms = listOf("Bread", "Toast", "Bakery", "Loaf", "Baguette"),
                hint1Vague = "The basic ingredient for a sandwich.",
                hint2Direct = "It's baked dough that smells amazing when toasted."
            )
        ),

        "Office" to mapOf(
            "Television" to ScavengerTarget(
                synonyms = listOf("Television", "Monitor", "Screen", "Tv"),
                hint1Vague = "A window into stories and news that sits on a wall or stand.",
                hint2Direct = "A large black rectangle that lights up with moving pictures."
            ),
            "Chair" to ScavengerTarget(
                synonyms = listOf("Chair", "Seat", "Stool", "Armchair"),
                hint1Vague = "An object designed to support you when you're tired of standing.",
                hint2Direct = "It has legs and a back, but it cannot walk."
            ),
            "Mobile phone" to ScavengerTarget(
                synonyms = listOf("Mobile phone", "Smartphone", "Telephone", "Cell phone"),
                hint1Vague = "The entire world's knowledge fitting right in your pocket.",
                hint2Direct = "A hand-held device with a glowing touchscreen."
            ),
            "Computer" to ScavengerTarget(
                synonyms = listOf("Computer", "Laptop", "Keyboard", "Desktop"),
                hint1Vague = "The brain of the modern office.",
                hint2Direct = "It has a keyboard and a screen; used for work or gaming."
            )
        ),

        "Pets" to mapOf(
            "Dog" to ScavengerTarget(
                synonyms = listOf("Dog", "Canine", "Puppy", "Hound"),
                threshold = 0.85f,
                hint1Vague = "A loyal companion known as man's best friend.",
                hint2Direct = "It wags its tail and might bark at the mailman."
            ),
            "Cat" to ScavengerTarget(
                synonyms = listOf("Cat", "Feline", "Kitten", "Tabby"),
                threshold = 0.85f,
                hint1Vague = "A sophisticated hunter that loves to nap in sunbeams.",
                hint2Direct = "It has whiskers, pointy ears, and might purr when petted."
            )
        )
    )
}