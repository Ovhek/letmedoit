package cat.copernic.letmedoit.Users.view

class LenguagesProvider {
    companion object {
        fun obtenerLenguages(): ArrayList<cat.copernic.letmedoit.Users.view.Lenguages> {
            return arrayListOf(
                cat.copernic.letmedoit.Users.view.Lenguages(
                    "English",
                    "imagen 1",
                ),cat.copernic.letmedoit.Users.view.Lenguages(
                    "Español",
                    "imagen 2",
                ),cat.copernic.letmedoit.Users.view.Lenguages(
                    "Català",
                    "imagen 3",
                ),
            )
        }
    }
}