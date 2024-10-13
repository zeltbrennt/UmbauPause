package de.pause.features.shop.routes.auth

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

