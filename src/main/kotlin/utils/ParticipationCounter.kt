package utils

object ParticipantCounter {
    private var counter = 0

    fun next(): Int {
        counter += 1
        return counter
    }
}
