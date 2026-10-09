package com.example.thestudents.data.dtos

import com.example.thestudents.data.Inscription
import com.google.gson.annotations.SerializedName

data class InscripcionCompartidaDto(
    val materia: String? = null,
    val periodo: String? = null
)

data class InscripcionDto(
    val id: Int? = null,
    val correo: String? = null,
    @SerializedName("nombre_usuario")
    val nombreUsuario: String? = null,
    val nombre: String? = null,
    val biografia: String? = null,
    @SerializedName("foto_url")
    val fotoUrl: String? = null,
    @SerializedName("color_perfil")
    val colorPerfil: String? = null,
    val carrera: String? = null,
    val semestre: Int? = null,
    val estado: String? = null,
    @SerializedName("fecha_creacion")
    val fechaCreacion: String? = null,
    @SerializedName("inscripciones_compartidas")
    val inscripcionesCompartidas: List<InscripcionCompartidaDto>? = null
)

fun InscripcionDto.toInscriptions(): List<Inscription> {
    val studentDto = StudentDto(
        id = id,
        correo = correo,
        nombreUsuario = nombreUsuario,
        nombre = nombre,
        biografia = biografia,
        fotoUrl = fotoUrl,
        colorPerfil = colorPerfil,
        carrera = carrera,
        semestre = semestre,
        estado = estado,
        fechaCreacion = fechaCreacion
    )
    val student = studentDto.toStudent()

    return inscripcionesCompartidas?.mapIndexed { index, item ->
        Inscription(
            id = "${id}_$index",
            student = student,
            className = item.materia.orEmpty(),
            period = item.periodo.orEmpty()
        )
    }.orEmpty()
}
