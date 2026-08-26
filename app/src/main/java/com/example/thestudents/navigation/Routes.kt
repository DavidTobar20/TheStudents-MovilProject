package com.example.thestudents.navigation

/**
 * Rutas de navegacion de la app usando Sealed Class.
 *
 * Centralizar las rutas permite que el compilador las revise y facilita el paso de argumentos.
 */
sealed class Routes(val route: String) {
    object Login : Routes("login")
    object Register : Routes("register")
    object Home : Routes("home")
    object Search : Routes("search")
    object Notifications : Routes("notifications")
    object Profile : Routes("profile")
    object EditProfile : Routes("edit_profile")
    object Reviews : Routes("reviews")
    
    object StudentDetail : Routes("student_detail/{$STUDENT_ID_ARG}") {
        fun createRoute(studentId: String) = "student_detail/$studentId"
    }

    object CommentsReview : Routes("comments_review/{$REVIEW_INDEX_ARG}") {
        fun createRoute(reviewIndex: Int) = "comments_review/$reviewIndex"
    }

    companion object {
        const val STUDENT_ID_ARG = "studentId"
        const val REVIEW_INDEX_ARG = "reviewIndex"

        /** Rutas que muestran la barra de navegacion inferior. */
        val withBottomBar = setOf(
            Home.route, 
            Search.route, 
            Notifications.route, 
            Profile.route, 
            EditProfile.route, 
            Reviews.route, 
            CommentsReview.route, 
            StudentDetail.route
        )

        /**
         * Pestana que debe verse activa en la barra inferior.
         */
        fun selectedTabFor(route: String?): String? = when (route) {
            EditProfile.route, CommentsReview.route, StudentDetail.route -> Profile.route
            else -> route
        }
    }
}
