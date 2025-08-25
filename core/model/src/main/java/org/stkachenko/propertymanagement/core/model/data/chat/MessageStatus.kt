package org.stkachenko.propertymanagement.core.model.data.chat

enum class MessageStatus(
    val status: String,
) {
    READ("read"),
    UNREAD("unread"),
    SENT("sent"),
    FAILED("failed"),
    PENDING("pending");

    companion object
}
