package ru.petkeeper.util

import java.security.MessageDigest
import javax.xml.bind.DatatypeConverter

fun sha256Hash(input: String): String {
    val bytes = MessageDigest
        .getInstance(HASH_TYPE)
        .digest(input.toByteArray())
    return DatatypeConverter.printHexBinary(bytes).uppercase()
}