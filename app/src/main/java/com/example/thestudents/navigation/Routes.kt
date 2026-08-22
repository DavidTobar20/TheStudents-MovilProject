package com.example.thestudents.navigation

/**
 * Rutas de navegacion de la app.
 *
 * Estaban escritas como cadenas sueltas en cada pantalla y en la barra inferior, asi que un
 * error de dedo solo se notaba al ejecutar. Centralizarlas deja que el compilador las revise.
 */
object Routes {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val HOME = "home"
    const val SEARCH = "search"
    const val NOTIFICATIONS = "notifications"
    const val PROFILE = "profile"
    const val EDIT_PROFILE = "edit_profile"
    const val REVIEWS = "reviews"

    const val REVIEW_INDEX_ARG = "reviewIndex"
    const val COMMENTS_REVIEW = "comments_review/{$REVIEW_INDEX_ARG}"

    /** Construye la ruta concreta hacia los comentarios de una resena. */
    fun commentsReview(reviewIndex: Int) = "comments_review/$reviewIndex"

    /** Rutas que muestran la barra de navegacion inferior. */
    val withBottomBar = setOf(HOME, SEARCH, NOTIFICATIONS, PROFILE, EDIT_PROFILE, REVIEWS, COMMENTS_REVIEW)

    /**
     * Pestana que debe verse activa en la barra inferior.
     *
     * Editar perfil y los comentarios de una resena son pantallas de detalle: no tienen pestana
     * propia, pero se llega a ellas desde Perfil, asi que esa es la que sigue resaltada.
     */
    fun selectedTabFor(route: String?): String? = when (route) {
        EDIT_PROFILE, COMMENTS_REVIEW -> PROFILE
        else -> route
    }
}
