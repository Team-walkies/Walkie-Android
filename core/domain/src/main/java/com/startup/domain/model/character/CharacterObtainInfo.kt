package com.startup.domain.model.character

import java.time.LocalDate

data class CharacterObtainInfo(
    val obtainedPosition: String,
    /** yyyy-MM-dd */
    val obtainedDate: LocalDate,
)