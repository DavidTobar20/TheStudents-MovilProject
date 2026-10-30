package com.example.thestudents.data.dtos

data class MateriaDto(
    val id: String? = null,
    val nombre: String = "",
    val periodos: List<String> = emptyList()
)
