package com.example.thestudents.data.local

import com.example.thestudents.data.CourseSection

object localCourseSectionProvider {
    val sections: List<CourseSection> = localInscriptionProvider.getCourseSectionsForUser(localStudentProvider.currentUser.id)
}
