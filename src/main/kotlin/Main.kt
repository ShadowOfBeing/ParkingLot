package parking

object Parking {
    var line = readln()
    var parkingSpots = MutableList(1) { "free" }
    var freeIndex = 0
    var parkingIsEmpty = true
    var parkingExists = false
    var lotsCount = 0
    var freeCount = 0

    init {
        this.run()
    }

    fun createParking(split_line_: List<String>) {
        if (split_line_.size > 2 || !Regex("[\\d]+").matches(split_line_[1])) {
            println("Incorrect format!")
        } else {
            this.parkingSpots = MutableList(split_line_[1].toInt()) { "free" }
            parkingExists = true
            lotsCount = split_line_[1].toInt()
            freeCount = lotsCount
            freeIndex = 0
            parkingIsEmpty = true
            println("Created a parking lot with $lotsCount spots.")
        }
    }

    fun findFreeIndex(): Int {
        for ((i, item) in parkingSpots.withIndex()) {
            if (item == "free") {
                return i
            }
        }
        return lotsCount + 1
    }

    fun viewAllCars() {
        if (this.parkingIsEmpty) {
            println("Parking lot is empty.")
        } else {
            this.parkingSpots.forEachIndexed { index, item ->
                if (item != "free") println("${index + 1} $item")
            }
        }
    }

    fun getElementsByOption(option: String, value: String): String {
        var returnString = ""
        var tmp = ""

        if (option == "regsByColor") {
            this.parkingSpots.forEachIndexed { index, item ->
                if (item != "free") {
                    tmp = item.slice(item.lastIndexOf(" ") + 1..item.lastIndex)
                    if (tmp.lowercase() == value) returnString += "${item.split(" ")[0]}, "
                }
            }
        } else if (option == "spotsByColor") {
            this.parkingSpots.forEachIndexed { index, item ->
                if (item != "free") {
                    tmp = item.slice(item.lastIndexOf(" ") + 1..item.lastIndex)
                    if (tmp.lowercase() == value) returnString += "${index + 1}, "
                }
            }
        } else if (option == "spotByReg") {
            this.parkingSpots.forEachIndexed { index, item ->
                if (item != "free") {
                    tmp = item.slice(0 until item.indexOf(" "))
                    if (tmp == value) return "${index + 1}"
                }
            }
        }
        return returnString.slice(0..returnString.length - 3)
    }

    fun viewAllRegsByColor(color: String) {
        val answer = getElementsByOption("regsByColor", color)
        if (this.parkingIsEmpty || answer == "") {
            println("No cars with color $color were found.")
        } else { println(answer) }
    }

    fun viewAllSpotsByColor(color: String) {
        val answer = getElementsByOption("spotsByColor", color)
        if (this.parkingIsEmpty || answer == "") {
            println("No cars with color $color were found.")
        } else { println(answer) }
    }

    fun viewSpotByReg(reg: String) {
        val answer = getElementsByOption("spotByReg", reg)
        if (this.parkingIsEmpty || answer == "") {
            println("No cars with registration number $reg were found.")
        } else { println(answer) }
    }

    fun run() {
        val split_line = line.split(" ")
        if (line != "exit") {
            if (split_line[0] == "create") {
                this.createParking(split_line)
            } else if (!parkingExists) {
                println("Sorry, a parking lot has not been created.")
            } else if (split_line[0] == "park") {
                if (split_line.size != 3) {
                    println("Incorrect command arguments!")
                } else {
                    if (freeIndex >= lotsCount) {
                        println("Sorry, the parking lot is full.")
                    } else {
                        parkingSpots[freeIndex] = "${split_line[1]} ${split_line[2]}"
                        println("${split_line[2]} car parked in spot ${freeIndex + 1}.")
                        freeIndex = this.findFreeIndex()
                        parkingIsEmpty = false
                        freeCount--
                    }
                }
            } else if (split_line[0] == "leave") {
                val leaveIndex = split_line[1].toInt() - 1
                if (split_line.size != 2 || (leaveIndex !in 0..lotsCount - 1)) {
                    println("Incorrect command arguments!")
                } else {
                    if (parkingSpots[leaveIndex] == "free") {
                        println("There is no car in spot ${leaveIndex + 1}.")
                    } else {
                        parkingSpots[leaveIndex] = "free"
                        freeIndex = this.findFreeIndex()
                        println("Spot ${leaveIndex + 1} is free.")
                        if (freeCount == lotsCount) { parkingIsEmpty = true }
                    }
                }
            }  else if (split_line[0] == "reg_by_color") {
                this.viewAllRegsByColor(split_line[1].lowercase())
            } else if (split_line[0] == "spot_by_color") {
                this.viewAllSpotsByColor(split_line[1].lowercase())
            } else if (split_line[0] == "spot_by_reg") {
                this.viewSpotByReg(split_line[1])
            } else if (split_line[0] == "status") {
                this.viewAllCars()
            }
            line = readln()
            this.run()
        }
    }
}

fun main() {
    Parking
}