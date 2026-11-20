package com.example.thestudents.data.repository

import com.example.thestudents.data.NotificationType
import com.example.thestudents.data.TeamMatchCandidate
import com.example.thestudents.data.datasource.impl.firestore.InscripcionFirestoreDataSourceImpl
import com.example.thestudents.data.datasource.impl.firestore.NotificationFirestoreDataSourceImpl
import com.example.thestudents.data.datasource.impl.firestore.ReviewFirestoreDataSourceImpl
import com.example.thestudents.data.datasource.impl.firestore.TeamSearchFirestoreDataSourceImpl
import com.example.thestudents.data.datasource.impl.firestore.UserFirestoreDataSourceImpl
import com.example.thestudents.data.dtos.NotificationDto
import com.example.thestudents.data.dtos.ReviewDto
import com.example.thestudents.data.dtos.StudentSummaryDto
import com.example.thestudents.data.dtos.toStudent
import com.example.thestudents.data.dtos.toSummaryDto
import com.example.thestudents.data.CandidateSignals
import com.example.thestudents.data.ReceivedRating
import com.example.thestudents.data.repository.TeamMatchScorer
import com.google.firebase.Timestamp
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

/** Materias que el usuario puede elegir para armar equipo el próximo semestre. */
data class TeamCourseOptions(
    val availableCourses: List<String>, // del catálogo, sin las que ya vio
    val mySearches: List<String>        // materias en las que ya está buscando equipo
)

/**
 * "Arma tu equipo": el estudiante elige una materia que va a ver el próximo semestre,
 * queda "buscando equipo" en ella y se le recomiendan solo los compañeros que también
 * la marcaron. El orden lo decide [TeamMatchScorer] con las reseñas que ya existen.
 */
class TeamMatchRepository @Inject constructor(
    private val inscripcionRemoteDataSource: InscripcionFirestoreDataSourceImpl,
    private val reviewRemoteDataSource: ReviewFirestoreDataSourceImpl,
    private val userRemoteDataSource: UserFirestoreDataSourceImpl,
    private val notificationRemoteDataSource: NotificationFirestoreDataSourceImpl,
    private val teamSearchRemoteDataSource: TeamSearchFirestoreDataSourceImpl
) {

    suspend fun getCourseOptions(userId: String, periodo: String): Result<TeamCourseOptions> {
        return try {
            coroutineScope {
                val catalogoDeferred = async { inscripcionRemoteDataSource.getMaterias() }
                val yaVistasDeferred = async { inscripcionRemoteDataSource.getMisInscripciones(userId) }
                val busquedasDeferred = async {
                    runCatching { teamSearchRemoteDataSource.getMySearches(userId, periodo) }.getOrDefault(emptyList())
                }

                val yaVistas = yaVistasDeferred.await().map { it.materia.lowercase() }.toSet()
                val disponibles = catalogoDeferred.await()
                    .map { it.nombre }
                    .filter { it.isNotBlank() && it.lowercase() !in yaVistas }
                    .distinct()
                    .sorted()

                Result.success(
                    TeamCourseOptions(
                        availableCourses = disponibles,
                        mySearches = busquedasDeferred.await().map { it.materia }.distinct().sorted()
                    )
                )
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /** Marca al usuario como "buscando equipo" en la materia, para que otros lo encuentren. */
    suspend fun startTeamSearch(userId: String, materia: String, periodo: String): Result<Unit> {
        return try {
            val usuario = runCatching { userRemoteDataSource.getUserById(userId)?.toSummaryDto() }
                .getOrNull() ?: StudentSummaryDto(id = userId)
            teamSearchRemoteDataSource.saveSearch(usuario.copy(id = userId), materia, periodo)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun stopTeamSearch(userId: String, materia: String, periodo: String): Result<Unit> {
        return try {
            teamSearchRemoteDataSource.deleteSearch(userId, materia, periodo)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getRecommendedTeammates(
        userId: String,
        materia: String,
        periodo: String
    ): Result<List<TeamMatchCandidate>> {
        return try {
            val candidates = coroutineScope {
                // 1. Lo que se puede pedir en paralelo
                val buscandoDeferred = async { teamSearchRemoteDataSource.getSearchesByCourse(materia, periodo) }
                val companerosPasadosDeferred = async {
                    runCatching { inscripcionRemoteDataSource.getInscripcionesPorUsuario(userId) }.getOrDefault(emptyList())
                }
                val recibidasPorMiDeferred = async { reviewRemoteDataSource.getReviewsByReviewedStudentId(userId) }
                val seguidosDeferred = async {
                    // Si falla la lista de seguidos, el match se calcula igual sin ese dato
                    runCatching { userRemoteDataSource.getFollowing(userId) }.getOrDefault(emptyList())
                }

                // 2. Solo se recomienda a quienes también marcaron que verán esta materia
                val buscando: Map<String, StudentSummaryDto> = buscandoDeferred.await()
                    .mapNotNull { busqueda -> busqueda.usuario?.takeIf { !it.id.isNullOrBlank() } }
                    .filter { it.id != userId }
                    .associateBy { it.id.orEmpty() }

                // 3. Cuántas materias ha visto el usuario con cada uno (suma afinidad)
                val materiasJuntos: Map<String, Int> = companerosPasadosDeferred.await()
                    .filter { !it.usuario?.id.isNullOrBlank() }
                    .groupBy { it.usuario?.id.orEmpty() }
                    .mapValues { (_, inscripciones) -> inscripciones.map { it.materia }.distinct().size }

                val idsSeguidos = seguidosDeferred.await().mapNotNull { it.id }.toSet()

                // Calificaciones que cada compañero me ha dado a mí
                val calificacionesQueMeDieron: Map<String, List<Double>> = recibidasPorMiDeferred.await()
                    .groupBy { it.autor?.id.orEmpty() }
                    .mapValues { (_, reviews) -> reviews.mapNotNull { it.ratingValue() } }

                // 4. Reseñas que recibió cada candidato (en paralelo)
                val senales = buscando.keys.map { companeroId ->
                    async {
                        val recibidas = runCatching {
                            reviewRemoteDataSource.getReviewsByReviewedStudentId(companeroId)
                        }.getOrDefault(emptyList())

                        CandidateSignals(
                            studentId = companeroId,
                            receivedRatings = recibidas.mapNotNull { review ->
                                review.ratingValue()?.let { valor ->
                                    ReceivedRating(
                                        value = valor,
                                        reviewerId = review.autor?.id.orEmpty(),
                                        course = review.materia.orEmpty(),
                                        period = review.periodo.orEmpty()
                                    )
                                }
                            },
                            ratingsTheyGaveMe = calificacionesQueMeDieron[companeroId].orEmpty(),
                            sharedCoursesWithMe = materiasJuntos[companeroId] ?: 0,
                            iFollowThem = companeroId in idsSeguidos
                        )
                    }
                }.awaitAll()

                // 5. Ranking unido a los datos del estudiante
                TeamMatchScorer.rank(senales, userId, materia).mapNotNull { match ->
                    buscando[match.studentId]?.let { resumen ->
                        TeamMatchCandidate(
                            student = resumen.toStudent().copy(
                                rating = match.average?.toFloat() ?: 0f,
                                reviewsCount = match.reviewsCount
                            ),
                            match = match
                        )
                    }
                }
            }
            Result.success(candidates)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Envía una notificación TEAM_INVITE a cada compañero elegido.
     * Devuelve cuántas invitaciones se enviaron.
     */
    suspend fun sendTeamInvites(
        fromUserId: String,
        toUserIds: List<String>,
        materia: String,
        periodo: String
    ): Result<Int> {
        return try {
            val destinatarios = toUserIds.distinct().filter { it.isNotBlank() && it != fromUserId }
            if (destinatarios.isEmpty()) return Result.success(0)

            // Datos de quien invita (nombre y foto) para mostrarlos en la notificación
            val emisor = runCatching { userRemoteDataSource.getUserById(fromUserId)?.toSummaryDto() }
                .getOrNull() ?: StudentSummaryDto(id = fromUserId)

            coroutineScope {
                destinatarios.map { destinatarioId ->
                    async {
                        notificationRemoteDataSource.createNotification(
                            NotificationDto(
                                destinatarioId = destinatarioId,
                                emisorId = fromUserId,
                                tipo = NotificationType.TEAM_INVITE.name,
                                materia = materia,
                                periodo = periodo,
                                emisor = emisor,
                                fechaCreacion = Timestamp.now()
                            )
                        )
                    }
                }.awaitAll()
            }
            Result.success(destinatarios.size)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // El rating se guarda como texto ("4", "4.0" o "4,5")
    private fun ReviewDto.ratingValue(): Double? =
        rating?.replace(',', '.')?.toDoubleOrNull()?.takeIf { it in 0.0..5.0 }
}
