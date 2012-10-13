package views

object Test {

    case class Employee(name: String,
                        age: Int,
                        yearsEmployed: Int,
                        salary: Int)

    val empl = List (
        Employee("Adam", 42, 20, 100000),
        Employee("Bob",  32, 10,  60000),
        Employee("Carl", 46,  6,  30000),
        Employee("Dan",  22,  3,  40000),
        Employee("Ed",   51, 27,  60000),
        Employee("Fred", 24, 10,  50000),
        Employee("Gus",  31,  9,  30000),
        Employee("Han",  39,  6,  50000),
        Employee("Ian",  23, 15,  30000),
        Employee("Jim",  59, 19,  70000),
        Employee("Kurt", 41,  8,  90000),
        Employee("Liam", 60, 40,  40000),
        Employee("Mel",  65, 10,  80000),
        Employee("Nina", 47,  4,  30000),
        Employee("Otis", 34,  8,  70000),
        Employee("Pia",  31, 12,  50000)
    )

}