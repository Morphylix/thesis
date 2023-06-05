package ru.petkeeper.database.pets_table

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.statements.api.ExposedBlob
import org.jetbrains.exposed.sql.transactions.transaction
import java.lang.Exception
import javax.sql.rowset.serial.SerialBlob

object PetsTable: Table("pets") {

    var idCounter = 0
    val petId = PetsTable.integer("petId")
    private val name = PetsTable.varchar("name", length = 25)
    private val type = PetsTable.varchar("type", length = 25)
    private val breed = PetsTable.varchar("breed", length = 25)
    private val gender = PetsTable.varchar("gender", length = 6)
    private val years = PetsTable.integer("years")
    private val months = PetsTable.integer("months")
    private val weight = PetsTable.double("weight")
    private val isFriendlyToPeople = PetsTable.bool("isFriendlyToPeople").nullable()
    private val isFriendlyToChildren = PetsTable.bool("isFriendlyToChildren").nullable()
    private val isFriendlyToCats = PetsTable.bool("isFriendlyToCats").nullable()
    private val isFriendlyToDogs = PetsTable.bool("isFriendlyToDogs").nullable()
    private val isFriendlyToOtherAnimals = PetsTable.bool("isFriendlyToOtherAnimals").nullable()
    private val canLiveWithDogs = PetsTable.bool("canLiveWithDogs")
    private val canLiveWithCats = PetsTable.bool("canLiveWithCats")
    private val canLiveWithOtherAnimals = PetsTable.bool("canLiveWithOtherAnimals")
    private val isLitterBoxTrained = PetsTable.bool("isLitterBoxTrained")
    private val marksThings = PetsTable.bool("marksThings")
    private val chewsThings = PetsTable.bool("chewsThings")
    private val isVaccinated = PetsTable.bool("isVaccinated")
    private val isSterilised = PetsTable.bool("isSterilized")
    private val diseases = PetsTable.text("diseases")
    private val canStayAlone = PetsTable.bool("canStayAlone")
    private val foodToEat = PetsTable.text("foodToEat")
    private val foodToAvoid = PetsTable.text("foodToAvoid")
    private val howOftenToFeed = PetsTable.text("howOftenToFeed")
    private val needsWalking = PetsTable.bool("needsWalking")
    private val howOftenToWalk = PetsTable.text("howOftenToWalk").nullable()
    private val warningsWhileWalking = PetsTable.text("warningsWhileWalking").nullable()
    private val needsPlaying = PetsTable.bool("needsPlaying")
    private val howToPlay = PetsTable.text("howToPlay").nullable()
    private val howToWash = PetsTable.text("howToWash")
    private val otherRecs = PetsTable.text("otherRecs")
    private val picture = PetsTable.blob("picture").nullable()


    fun insertPet(petDto: PetDto): PetDto? {
        var pet: PetDto? = null
        transaction {
            val newId = PetsTable.selectAll().count().toInt()
            idCounter = newId
            PetsTable.insert {
                it[petId] = newId
                it[name] = petDto.name
                it[type] = petDto.type
                it[breed] = petDto.breed
                it[gender] = petDto.gender
                it[years] = petDto.years
                it[months] = petDto.months
                it[weight] = petDto.weight
                it[isFriendlyToPeople] = petDto.isFriendlyToPeople
                it[isFriendlyToChildren] = petDto.isFriendlyToChildren
                it[isFriendlyToCats] = petDto.isFriendlyToCats
                it[isFriendlyToDogs] = petDto.isFriendlyToDogs
                it[isFriendlyToOtherAnimals] = petDto.isFriendlyToOtherAnimals
                it[canLiveWithDogs] = petDto.canLiveWithDogs
                it[canLiveWithCats] = petDto.canLiveWithCats
                it[canLiveWithOtherAnimals] = petDto.canLiveWithOtherAnimals
                it[isLitterBoxTrained] = petDto.isLitterBoxTrained
                it[marksThings] = petDto.marksThings
                it[chewsThings] = petDto.chewsThings
                it[isVaccinated] = petDto.isVaccinated
                it[isSterilised] = petDto.isSterilised
                it[diseases] = petDto.diseases
                it[canStayAlone] = petDto.canStayAlone
                it[foodToEat] = petDto.foodToEat
                it[foodToAvoid] = petDto.foodToAvoid
                it[howOftenToFeed] = petDto.howOftenToFeed
                it[needsWalking] = petDto.needsWalking
                it[howOftenToWalk] = petDto.howOftenToWalk
                it[warningsWhileWalking] = petDto.warningsWhileWalking
                it[needsPlaying] = petDto.needsPlaying
                it[howToPlay] = petDto.howToPlay
                it[howToWash] = petDto.howToWash
                it[otherRecs] = petDto.otherRecs
                it[picture] = pet?.picture?.let { it1 -> ExposedBlob(it1) }
            }
            pet = fetchPet(idCounter)
            println("idCounter inside transaction is $idCounter")
        }
        println("idCounter out transaction is $idCounter")
        return pet
    }

    fun fetchPet(id: Int): PetDto? {
        return try {
            transaction {
                val petModel = PetsTable.select {
                    petId.eq(id)
                }.single()

                PetDto(
                    petModel[petId],
                    petModel[name],
                    petModel[type],
                    petModel[breed],
                    petModel[gender],
                    petModel[years],
                    petModel[months],
                    petModel[weight],
                    petModel[isFriendlyToPeople],
                    petModel[isFriendlyToChildren],
                    petModel[isFriendlyToCats],
                    petModel[isFriendlyToDogs],
                    petModel[isFriendlyToOtherAnimals],
                    petModel[canLiveWithDogs],
                    petModel[canLiveWithCats],
                    petModel[canLiveWithOtherAnimals],
                    petModel[isLitterBoxTrained],
                    petModel[marksThings],
                    petModel[chewsThings],
                    petModel[isVaccinated],
                    petModel[isSterilised],
                    petModel[diseases],
                    petModel[canStayAlone],
                    petModel[foodToEat],
                    petModel[foodToAvoid],
                    petModel[howOftenToFeed],
                    petModel[needsWalking],
                    petModel[howOftenToWalk],
                    petModel[warningsWhileWalking],
                    petModel[needsPlaying],
                    petModel[howToPlay],
                    petModel[howToWash],
                    petModel[otherRecs],
                    if (petModel[picture] != null) (petModel[picture] as ExposedBlob).bytes else null
                )
            }
        } catch (e: Exception) {
            println("exception is $e")
            null
        }
    }

    fun postPetPic(id: Int, pic: ByteArray) {
        return try {
            transaction {
                val petModel = PetsTable.select {
                    petId.eq(id)
                }.single()
                petModel[picture] = pic
                PetsTable.update ({ petId eq id }) {
                    it[picture] = ExposedBlob(pic)
                }
                val a = 5
            }
        } catch (e: Exception) {

        }
    }
}