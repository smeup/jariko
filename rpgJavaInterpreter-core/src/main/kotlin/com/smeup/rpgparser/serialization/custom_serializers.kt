/*
 * Copyright 2019 Sme.UP S.p.A.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.smeup.rpgparser.serialization

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.math.BigDecimal
import java.time.LocalDateTime

object BigDecimalSerializer : KSerializer<BigDecimal> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("BigDecimal", PrimitiveKind.STRING)

    override fun serialize(
        encoder: Encoder,
        value: BigDecimal,
    ) = encoder.encodeString(value.toString())

    override fun deserialize(decoder: Decoder): BigDecimal = BigDecimal(decoder.decodeString())
}

object LocalDateTimeSerializer : KSerializer<LocalDateTime> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("LocalDateTime", PrimitiveKind.STRING)

    override fun serialize(
        encoder: Encoder,
        value: LocalDateTime,
    ) = encoder.encodeString(value.toString())

    override fun deserialize(decoder: Decoder): LocalDateTime = LocalDateTime.parse(decoder.decodeString())
}

object StringBuilderSerializer : KSerializer<StringBuilder> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("StringBuilder", PrimitiveKind.STRING)

    override fun serialize(
        encoder: Encoder,
        value: StringBuilder,
    ) = encoder.encodeString(value.toString())

    override fun deserialize(decoder: Decoder): StringBuilder = StringBuilder(decoder.decodeString())
}
