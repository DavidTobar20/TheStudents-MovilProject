package com.example.thestudents.data

data class CourseSection(
    val title: String,
    val period: String,
    val inscriptions: List<Inscription>
)
