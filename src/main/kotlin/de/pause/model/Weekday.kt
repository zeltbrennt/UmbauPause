package de.pause.model

enum class Weekday {
    MONTAG, DIENSTAG, MITTWOCH, DONNERSTAG, FREITAG;

    @Override
    override fun toString(): String {
        return super.toString().lowercase().replaceFirstChar { it.uppercaseChar() }
    }
}