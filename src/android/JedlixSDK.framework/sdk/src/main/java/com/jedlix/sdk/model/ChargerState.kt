/*
 * Copyright 2022 Jedlix B.V. The Netherlands
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */

package com.jedlix.sdk.model

import com.jedlix.sdk.serializer.TimeStampSerializer
import kotlinx.serialization.Serializable
import java.util.*

/**
 * State of a [Charger]
 * @property chargeState The [ChargeState] of the [Charger]
 * @property chargePower Indicates the charge power of the charger in kW at the indicated time
 * @property timeStamp Timestamp of the charge state and charge power in UTC
 */
@Serializable
data class ChargerState(
    @Serializable(with = ChargeState.Serializer::class)
    val chargeState: ChargeState,
    val chargePower: Double?,
    @Serializable(with = TimeStampSerializer::class)
    val timeStamp: Date?
)
