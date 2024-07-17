package de.pause.model

import de.pause.db.MenuDao
import de.pause.db.MenuTable
import de.pause.db.daoToModel
import de.pause.db.suspendTransaction
import org.jetbrains.exposed.sql.and
import org.joda.time.DateTime

class MenuRepository {

    suspend fun getCurrentMenu(): Menu = suspendTransaction {
        val today = DateTime.now()
        MenuDao.find {
            (MenuTable.validFrom lessEq today) and (MenuTable.validTo greaterEq today)
        }.firstOrNull()?.let(::daoToModel) ?: Menu(
            Montag = "Kantine geschlossen",
            Dienstag = "Kantine geschlossen",
            Mittwoch = "Kantine geschlossen",
            Donnerstag = "Kantine geschlossen",
            Freitag = "Kantine geschlossen",
            validFrom = today.toString(),
            validTo = today.toString()
        )
    }

    suspend fun getAllMenus(): List<Menu> = suspendTransaction {
        MenuDao.all().map(::daoToModel)
    }
}
