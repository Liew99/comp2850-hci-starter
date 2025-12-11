package utils

/**
 * Stores the participant/session identifier.
 *
 * IMPORTANT:
 * - The ID is assigned in Main.kt (e.g., "P1", "P2", "P3").
 * - Do NOT generate UUIDs here or the ID will change.
 */
data class SessionData(
    val id: String
)
