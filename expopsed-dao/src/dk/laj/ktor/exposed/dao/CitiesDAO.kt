package dk.laj.ktor.exposed.dao

import com.fasterxml.jackson.annotation.JsonProperty
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.dao.load
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.SizedIterable
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.transaction

class CitiesDAO{
    fun initDatabase(){
        val t = transaction {
            addLogger(StdOutSqlLogger)

            SchemaUtils.create(Cities)

            if(City.count() == 0L) {
                val stPete = City.new {
                    name = "St. Petersburg"
                }

                City.new {
                    name = "Borup"
                }
            }
            // 'select *' SQL: SELECT Cities.id, Cities.name FROM Cities

        }
    }


    fun add(cityDto: CityDto): CityDto{
        return transaction {
            City.new {

                this.name = cityDto.name
            }.toDTO()

        }
    }

    fun all(): List<CityDto> {
        return transaction {
            City.all().map { it.toDTO() }

        }
    }

}

object Cities : IntIdTable() {
    val name = varchar("name", 50)
}

class City(id: EntityID<Int>) : IntEntity(id) {


    companion object : IntEntityClass<City>(Cities)

    var name by Cities.name

    override fun toString(): String {
        return "name: $name"
    }

    fun toDTO(): CityDto{
        return CityDto(this.id.value, this.name)
    }
}

data class CityDto(val id: Int, val name: String)