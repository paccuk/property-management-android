package org.stkachenko.propertymanagement.core.model.data.chat

enum class MessageType(
    val type: String,
) {
    TEXT("text"),
    IMAGE("image"),
    VIDEO("video"),
    AUDIO("audi"),
    FILE("file"),
    LOCATION("location"),
    STICKER("sticker"),
    CONTACT("contact");

    companion object
}
