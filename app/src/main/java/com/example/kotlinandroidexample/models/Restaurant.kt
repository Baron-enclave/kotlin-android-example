package com.example.kotlinandroidexample.models

class Restaurant(
    val id: String,
    val name: String,
    val address: String? = null,
    val latitude: Double,
    val longitude: Double,
    val imageUrl: String? = null,
    val rating: Float,
    val comment: Comment? = null,
)

val mRestaurants = mutableSetOf(
    Restaurant(
        "1",
        "QUÁN CHAY SUKHA",
        null,
        18.464176,
        107.605988,
        "https://images.happycow.net/venues/200x200/35/23/hcmp352305_2150589.jpeg",
        2f,
        comment = Comment(
            "https://images.happycow.net/venues/200x200/35/23/hcmp352305_2150589.jpeg",
            "Quán chay này rất ngon, mình rất thích",
        )
    ),
    Restaurant(
        "2",
        "THUAN CHAY - VEGAN",
        null,
        14.450260,
        107.605232,
        "https://images.happycow.net/venues/200x200/28/81/hcmp28812_379199.jpeg",
        3f,
        comment = Comment(
            "https://images.happycow.net/venues/200x200/28/81/hcmp28812_379199.jpeg",
            "A very nice place to eat vegan food. I love it!",
        )
    ),
    Restaurant(
        "3",
        "AN NHIEN",
        null,
        16.450260,
        107.605232,
        "https://images.happycow.net/venues/200x200/35/23/hcmp352305_2150589.jpeg",
        4f,
        comment = Comment(
            "https://images.happycow.net/venues/200x200/35/23/hcmp352305_2150589.jpeg",
            "Whether or not this restaurant authentically represents vegan food is something I can’t comment on, but the pho (nuoc) noodle soup I ordered was small, not particularly flavourful and also not good value, as was the case with the other thing I ordered - “spring rolls”.\n" +
                    "I could see a mistranslation in the menu from the start, but decided to go with it - to see what I would ultimately get. Up came a few tiny prawn cracker type things with a strange and tangy filling. Not impressed at all - especially at a grand total of 40,000 VND, when there are better, more serious places just around the corner as well as a short stroll away.",
        )
    ),
    Restaurant(
        "4",
        "HUONG MAI",
        null,
        16.469543,
        107.598167,
        "https://images.happycow.net/venues/200x200/35/23/hcmp352305_2150589.jpeg",
        5f,
        comment = Comment(
            "https://images.happycow.net/venues/200x200/35/23/hcmp352305_2150589.jpeg",
            "I had a great experience at this restaurant. The food was delicious and the staff was very friendly. I would definitely recommend this place to anyone looking for a good vegan meal.",
        )
    ),
    Restaurant(
        "5",
        "THỰC DƯỠNG MAYLA",
        null,
        16.467367,
        107.60000,
        "https://images.happycow.net/venues/200x200/35/23/hcmp352305_2150589.jpeg",
        6f,
        comment = Comment(
            "https://images.happycow.net/venues/200x200/35/23/hcmp352305_2150589.jpeg",
            "Từ ngày tình cờ biết đến quán, vậy là mỗi lần thăm Huế, mình đều chọn ghé quán.\n" +
                    "Món ăn bình dân, mình thích cái vị tự nhiên của rau củ tươi, không dùng đồ chế biến sẵn.\n" +
                    "Quán không rộng lắm nhưng với mình là vừa đủ để cảm nhận món ăn cũng như cảm thấy nhẹ nhàng, giản dị lạ lùng\uD83D\uDE04\n" +
                    "Ấy vậy mà quán đã hoạt động hơn 20 năm rồi đó!\n" +
                    "1 năm rồi mình chưa ghé lại, còn bạn hãy thử trải nghiệm theo cách của mình hen!"
        )
    ),
    Restaurant(
        "6",
        "THIEN PHU",
        null,
        16.469367,
        107.608053,
        "https://images.happycow.net/venues/200x200/35/23/hcmp352305_2150589.jpeg",
        7f,
        comment = Comment(
            "",
            "Simple quan chay vegetarian restaurant. Menu has English but none spoken by staff. Serves vegan noodle soups, mock meats, tofu in dishes, and desserts for which dairy might be used so ask i unsure. Note: Reported in January 2023 that English menu might not list ingredients correctly for some dishes - ask if unsure. Open Mon-Sun 9:00am-9:00pm."
        )
    ),
    // add more, random latlong
    Restaurant(
        "7",
        "QUÁN CHAY SUKHA",
        null,
        16.461176,
        107.606088,
        "https://images.happycow.net/venues/200x200/35/23/hcmp352305_2150589.jpeg",
        2f,
        comment = Comment(
            "https://images.happycow.net/venues/200x200/35/23/hcmp352305_2150589.jpeg",
            "Quán chay này rất ngon, mình rất thích",
        )
    ),
    Restaurant(
        "8",
        "THUAN CHAY - VEGAN",
        null,
        16.450260,
        107.605232,
        "https://images.happycow.net/venues/200x200/28/81/hcmp28812_379199.jpeg",
        3f,
        comment = Comment(
            "https://images.happycow.net/venues/200x200/28/81/hcmp28812_379199.jpeg",
            "A very nice place to eat vegan food. I love it!",
        )
    ),
    Restaurant(
        "9",
        "AN NHIEN",
        null,
        16.450360,
        107.604232,
        "https://images.happycow.net/venues/200x200/35/23/hcmp352305_2150589.jpeg",
        4f,
        comment = Comment(
            "https://images.happycow.net/venues/200x200/35/23/hcmp352305_2150589.jpeg",
            "Whether or not this restaurant authentically represents vegan food is something I can’t comment on, but the pho (nuoc) noodle soup I ordered was small, not particularly flavourful and also not good value, as was the case with the other thing I ordered - “spring rolls”.\n" +
                    "I could see a mistranslation in the menu from the start, but decided to go with it - to see what I would ultimately get. Up came a few tiny prawn cracker type things with a strange and tangy filling. Not impressed at all - especially at a grand total of 40,000 VND, when there are better, more serious places just around the corner as well as a short stroll away.",
        )
    ),
    Restaurant(
        "10",
        "HUONG MAI",
        null,
        16.469543,
        107.598167,
        "https://images.happycow.net/venues/200x200/35/23/hcmp352305_2150589.jpeg",
        5f,
        comment = Comment(
            "https://images.happycow.net/venues/200x200/35/23/hcmp352305_2150589.jpeg",
            "I had a great experience at this restaurant. The food was delicious and the staff was very friendly. I would definitely recommend this place to anyone looking for a good vegan meal.",
        )
    ),
    Restaurant(
        "11",
        "THỰC DƯỠNG MAYLA",
        null,
        16.418367,
        107.537923,
        "https://images.happycow.net/venues/200x200/35/23/hcmp352305_2150589.jpeg",
        6f,
        comment = Comment(
            "https://images.happycow.net/venues/200x200/35/23/hcmp352305_2150589.jpeg",
            "Từ ngày tình cờ biết đến quán, vậy là mỗi lần thăm Huế, mình đều chọn ghé quán.\n" +
                    "Món ăn bình dân, mình thích cái vị tự nhiên của rau củ tươi, không dùng đồ chế biến sẵn.\n" +
                    "Quán không rộng lắm nhưng với mình là vừa đủ để cảm nhận món ăn cũng như cảm thấy nhẹ nhàng, giản dị lạ lùng\uD83D\uDE04\n" +
                    "Ấy vậy mà quán đã hoạt động hơn 20 năm rồi đó!\n" +
                    "1 năm rồi mình chưa ghé lại, còn bạn hãy thử trải nghiệm theo cách của mình hen!"
        )
    ),
    Restaurant(
        "12",
        "THIEN PHU",
        null,
        16.469500,
        107.608100,
        "https://images.happycow.net/venues/200x200/35/23/hcmp352305_2150589.jpeg",
        7f,
        comment = Comment(
            "",
            "Simple quan chay vegetarian restaurant. Menu has English but none spoken by staff. Serves vegan noodle soups, mock meats, tofu in dishes, and desserts for which dairy might be used so ask i unsure. Note: Reported in January 2023 that English menu might not list ingredients correctly for some dishes - ask if unsure. Open Mon-Sun 9:00am-9:00pm."
        )
    ),
    // add more, random latlong
    Restaurant(
        "13",
        "QUÁN CHAY SUKHA",
        null,
        16.461176,
        107.605988,
        "https://images.happycow.net/venues/200x200/35/23/hcmp352305_2150589.jpeg",
        2f,
        comment = Comment(
            "https://images.happycow.net/venues/200x200/35/23/hcmp352305_2150589.jpeg",
            "Quán chay này rất ngon, mình rất thích",
        )
    ),

    )