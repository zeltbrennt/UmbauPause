package de.pause.features.shop.routes.auth

import de.pause.database.suspendTransaction
import de.pause.features.shop.data.dao.DishTagTable
import de.pause.features.shop.data.dao.Tag
import de.pause.features.shop.data.dao.TagTable
import de.pause.features.shop.data.dto.MenuInfo
import de.pause.features.shop.data.repo.DishRepository
import de.pause.features.shop.data.repo.MenuRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.jetbrains.exposed.sql.insert

fun Route.manageShopContent(dishRepository: DishRepository, menuRepository: MenuRepository) {

    authenticate("admin") {
        route("/content") {
            route("/new-menu") {
                post {

                    val newMenu = call.receive<MenuInfo>()
                    call.application.environment.log.info(newMenu.toString())
                    newMenu.dishes.forEach {
                        val dish = try {
                            dishRepository.addDishByName(it.name)
                        } catch (e: ExposedSQLException) {
                            call.application.environment.log.info("Dish already exists")
                            dishRepository.findByName(it.name)
                        }
                        /*
                       call tag service with it.name
                       add tags to dish via dishrepository
                        */
                        val tags = listOf("vegan", "vegetarian", "gluten-free", "lactose-free")
                        tags.forEach {
                            val tag = try {
                                suspendTransaction {
                                    Tag.new {
                                        name = it
                                    }
                                }
                            } catch (e: ExposedSQLException) {
                                call.application.environment.log.info("Tag already exists")
                                suspendTransaction {
                                    Tag.find { TagTable.name eq it }.first()
                                }
                            }
                            try {
                                suspendTransaction {
                                    DishTagTable.insert {
                                        it[DishTagTable.dish] = dish.id
                                        it[DishTagTable.tag] = tag.id
                                    }
                                }
                            } catch (e: ExposedSQLException) {
                                call.application.environment.log.info("Tag already exists")
                            }
                        }
                        try {
                            call.application.environment.log.info("new Menu: ${newMenu.validFrom}, ${it.day} , ${dish.id.value}")
                            menuRepository.addNewMenu(newMenu.validFrom, newMenu.validTo, it.day, dish.id.value)
                        } catch (e: ExposedSQLException) {
                            call.application.environment.log.info("Menu already exists")
                            call.respond(HttpStatusCode.BadRequest)
                        }

                    }
                    call.respond(HttpStatusCode.Created)
                }
            }
            route("/edit-menu") {
                put {
                    val updatedMenu = call.receive<MenuInfo>()
                    call.application.environment.log.info(updatedMenu.toString())
                    updatedMenu.dishes.forEach {
                        val dish = try {
                            dishRepository.addDishByName(it.name)
                        } catch (e: ExposedSQLException) {
                            call.application.environment.log.info("Dish already exists")
                            dishRepository.findByName(it.name)
                        }
                        try {
                            call.application.environment.log.info("new Menu: ${updatedMenu.validFrom}, ${it.day} , ${dish.id.value}")
                            menuRepository.updateMenu(updatedMenu.validFrom, updatedMenu.validTo, it.day, dish.id.value)
                        } catch (e: ExposedSQLException) {
                            call.application.environment.log.info("Failed to update menu")
                            call.respond(HttpStatusCode.BadRequest)
                        }
                    }
                    call.respond(HttpStatusCode.Created)
                }
            }
        }
        route("/info") {
            route("/dishes") {
                get {
                    call.respond(dishRepository.allDishes())
                }
            }
        }
    }

}

