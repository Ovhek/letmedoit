package cat.copernic.letmedoit.data.provider

import cat.copernic.letmedoit.data.model.Category
import cat.copernic.letmedoit.data.model.Subcategory

class CategoryProvider {
    companion object {
        fun obtenerCategorias(): ArrayList<Category> {
            return arrayListOf(
                Category(
                    "Informática",
                    "Servicios enfocados en la ciencia de la computación",
                    arrayListOf(
                        Subcategory("Web", "AAA","100"),
                        Subcategory("Aplicaciones_Android", "AAA","100"),
                        Subcategory("Sistemas y_redes", "AAA","100"),
                        Subcategory("Ciberseguridad", "AAA","100"),
                        Subcategory("Videojuegos", "AAA","100"),
                        Subcategory("Aplicaciones_IOS", "AAA","100"),
                        Subcategory("Reparacion de_hardware","AAA", "100"),
                        Subcategory("Base de datos","AAA", "100"),
                        Subcategory("Inteligencia_artifical", "AAA","100"),
                        Subcategory("Robotica", "AAA","100"),
                        Subcategory("Electronica", "AAA","100"),
                        Subcategory("Cursos", "AAA","100"),
                    ),
                    "email_icon",
                    "1"
                ),
                Category(
                    "Cocina",
                    "Servicios dentro del ambito de la cocina",
                    arrayListOf(
                        Subcategory("Reposteria", "AAA","100"),
                        Subcategory("Cocina_Italiana", "AAA","100"),
                        Subcategory("Cocina_Mediterranea", "AAA","100"),
                        Subcategory("Bebidas_Alcoholicas", "AAA","100"),
                        Subcategory("Cocina_Francesa", "AAA","100"),
                        Subcategory("Cocina_Asiatica", "AAA","100"),
                        Subcategory("Cocina_Mejicana", "AAA","100"),
                        Subcategory("Pescaderia", "AAA","100"),
                        Subcategory("Carniceria", "AAA","100"),
                        Subcategory("Verduras", "AAA","100"),
                        Subcategory("Confiteria", "AAA","100"),
                        Subcategory("Cursos", "AAA","100"),
                    ),
                    "favorites_icon",
                    "2"
                ),
                Category(
                    "Vehiculos",
                    "Pepe pepepepe pepe pepepe Pepepe, pepepe pepepe",
                    arrayListOf(
                        Subcategory("Gasolina", "AAA","100"),
                        Subcategory("Diesel", "AAA","100"),
                        Subcategory("Mecanica", "AAA","100"),
                        Subcategory("Deportivos", "AAA","100"),
                        Subcategory("Todoterrenos", "AAA","100"),
                        Subcategory("Todocaminos", "AAA","100"),
                        Subcategory("Motocicletas", "AAA","100"),
                        Subcategory("Competiciones", "AAA","100"),
                        Subcategory("Clasicos", "AAA","100"),
                        Subcategory("Accesorios", "AAA","100"),
                        Subcategory("Alquiler", "AAA","100"),
                        Subcategory("Cursos", "AAA","100"),
                    ),
                    "filter_icon",
                    "3"
                ),
                Category(
                    "Informática",
                    "Pepe pepepepe pepe pepepe Pepepe, pepepe pepepe",
                    arrayListOf(
                        Subcategory("Web", "AAA","100"),
                        Subcategory("Aplicaciones_Android", "AAA","100"),
                        Subcategory("Sistemas y_redes", "AAA","100"),
                        Subcategory("Ciberseguridad", "AAA","100"),
                        Subcategory("Videojuegos", "AAA","100"),
                        Subcategory("Aplicaciones_IOS", "AAA","100"),
                        Subcategory("Reparacion de_hardware", "AAA","100"),
                        Subcategory("Base de datos", "AAA","100"),
                        Subcategory("Inteligencia_artifical","AAA", "100"),
                        Subcategory("Robotica", "AAA","100"),
                        Subcategory("Electronica", "AAA","100"),
                        Subcategory("Cursos", "AAA","100"),
                    ),
                    "email_icon",
                    "1"
                ),
                Category(
                    "Cocina",
                    "Pepe pepepepe pepe pepepe Pepepe, pepepe pepepe",
                    arrayListOf(
                        Subcategory("Reposteria", "AAA","100"),
                        Subcategory("Cocina_Italiana", "AAA","100"),
                        Subcategory("Cocina_Mediterranea", "AAA","100"),
                        Subcategory("Bebidas_Alcoholicas", "AAA","100"),
                        Subcategory("Cocina_Francesa", "AAA","100"),
                        Subcategory("Cocina_Asiatica", "AAA","100"),
                        Subcategory("Cocina_Mejicana", "AAA","100"),
                        Subcategory("Pescaderia", "AAA","100"),
                        Subcategory("Carniceria", "AAA","100"),
                        Subcategory("Verduras", "AAA","100"),
                        Subcategory("Confiteria", "AAA","100"),
                        Subcategory("Cursos", "AAA","100"),
                    ),
                    "favorites_icon",
                    "2"
                ),
                Category(
                    "Vehiculos",
                    "Pepe pepepepe pepe pepepe Pepepe, pepepe pepepe",
                    arrayListOf(
                        Subcategory("Gasolina", "AAA","100"),
                        Subcategory("Diesel", "AAA","100"),
                        Subcategory("Mecanica", "AAA","100"),
                        Subcategory("Deportivos", "AAA","100"),
                        Subcategory("Todoterrenos", "AAA","100"),
                        Subcategory("Todocaminos", "AAA","100"),
                        Subcategory("Motocicletas", "AAA","100"),
                        Subcategory("Competiciones", "AAA","100"),
                        Subcategory("Clasicos", "AAA","100"),
                        Subcategory("Accesorios", "AAA","100"),
                        Subcategory("Alquiler", "AAA","100"),
                        Subcategory("Cursos", "AAA","100"),
                    ),
                    "filter_icon",
                    "3"
                ),
                Category(
                    "Historia",
                    "Pepe pepepepe pepe pepepe Pepepe, pepepe pepepe",
                    arrayListOf(
                        Subcategory("Edad media", "AAA","100"),
                        Subcategory("Edad antigua", "AAA","100"),
                        Subcategory("Prehistoria", "AAA","100"),
                        Subcategory("Libros", "AAA","100"),
                        Subcategory("Museos", "AAA","100"),
                        Subcategory("Roma", "AAA","100"),
                        Subcategory("Europa", "AAA","100"),
                        Subcategory("Guerra mundial_II", "AAA","100"),
                        Subcategory("Gran guerra", "AAA","100"),
                        Subcategory("Guerras_civiles", "AAA","100"),
                        Subcategory("Revolucion_francesa", "AAA","100"),
                        Subcategory("Clases", "AAA","100"),
                    ),
                    "favorites_icon",
                    "3"
                ),
                Category(
                    "Mantenimiento",
                    "Pepe pepepepe pepe pepepe Pepepe, pepepe pepepe",
                    arrayListOf(
                        Subcategory("Fontaneria", "AAA","100"),
                        Subcategory("Electricidad", "AAA","100"),
                        Subcategory("Piscinas", "AAA","100"),
                        Subcategory("Jardineria", "AAA","100"),
                        Subcategory("Pintura", "AAA","100"),
                        Subcategory("Limpieza", "AAA","100"),
                        Subcategory("Fachada", "AAA","100"),
                        Subcategory("Seguridad del hogar", "AAA","100"),
                        Subcategory("Tejados", "AAA","100"),
                        Subcategory("Reparación", "AAA","100"),
                        Subcategory("Alquiler", "AAA","100"),
                        Subcategory("Cursos", "AAA","100"),
                    ),
                    "email_icon",
                    "3"
                ),
                Category(
                    "Deportes",
                    "Pepe pepepepe pepe pepepe Pepepe, pepepe pepepe",
                    arrayListOf(
                        Subcategory("Futbol", "AAA","100"),
                        Subcategory("Tenis", "AAA","100"),
                        Subcategory("Tenis mesa", "AAA","100"),
                        Subcategory("Natacion", "AAA","100"),
                        Subcategory("Basquet", "AAA","100"),
                        Subcategory("Futbol_americano", "AAA","100"),
                        Subcategory("Atletismo", "AAA","100"),
                        Subcategory("Gimnasia", "AAA","100"),
                        Subcategory("Hockey", "AAA","100"),
                        Subcategory("Balonmano", "AAA","100"),
                        Subcategory("Artes_marciales", "AAA","100"),
                        Subcategory("Clubs", "AAA","100"),
                    ),
                    "filter_icon",
                    "3"
                ),
                Category(
                    "Musica",
                    "Pepe pepepepe pepe pepepe Pepepe, pepepe pepepe",
                    arrayListOf(
                        Subcategory("Composición", "AAA","100"),
                        Subcategory("Instrumentos", "AAA","100"),
                        Subcategory("M.Electronica", "AAA","100"),
                        Subcategory("Heavy Meatl", "AAA","100"),
                        Subcategory("Rock", "AAA","100"),
                        Subcategory("Altavoces", "AAA","100"),
                        Subcategory("Microfonos", "AAA","100"),
                        Subcategory("Software de_sonido", "AAA","100"),
                        Subcategory("M.Latina", "AAA","100"),
                        Subcategory("Rap", "AAA","100"),
                        Subcategory("Pop", "AAA","100"),
                        Subcategory("Clases", "AAA","100"),
                    ),
                    "favorites_icon",
                    "3"
                ),
                Category(
                    "Reforma",
                    "Pepe pepepepe pepe pepepe Pepepe, pepepe pepepe",
                    arrayListOf(
                        Subcategory("Fontaneria", "AAA","100"),
                        Subcategory("Electricidad", "AAA","100"),
                        Subcategory("Fachadas", "AAA","100"),
                        Subcategory("Tejados", "AAA","100"),
                        Subcategory("Suelos", "AAA","100"),
                        Subcategory("Baños", "AAA","100"),
                        Subcategory("Pintura", "AAA","100"),
                        Subcategory("Piscinas", "AAA","100"),
                        Subcategory("Derribo", "AAA","100"),
                        Subcategory("Planches", "AAA","100"),
                        Subcategory("Herramientas", "AAA","100"),
                        Subcategory("Materiales", "AAA","100"),
                    ),
                    "filter_icon",
                    "3"
                ),
                Category(
                    "Botanica",
                    "Pepe pepepepe pepe pepepe Pepepe, pepepe pepepe",
                    arrayListOf(
                        Subcategory("Huerto", "AAA","100"),
                        Subcategory("Flores", "AAA","100"),
                        Subcategory("Fertilizantes", "AAA","100"),
                        Subcategory("Riego", "AAA","100"),
                        Subcategory("Herramientas", "AAA","100"),
                        Subcategory("Semillas", "AAA","100"),
                        Subcategory("Servicios", "AAA","100"),
                        Subcategory("Materiales", "AAA","100"),
                        Subcategory("Arboles", "AAA","100"),
                        Subcategory("Arbustos", "AAA","100"),
                        Subcategory("Plantas mutantes", "AAA","100"),
                        Subcategory("Cursos", "AAA","100"),
                    ),
                    "email_icon",
                    "3"
                ),
                Category(
                    "Animales",
                    "Pepe pepepepe pepe pepepe Pepepe, pepepe pepepe",
                    arrayListOf(
                        Subcategory("Comidad", "AAA","100"),
                        Subcategory("Accesorios", "AAA","100"),
                        Subcategory("Paseo", "AAA","100"),
                        Subcategory("Pipican", "AAA","100"),
                        Subcategory("Peluqueria", "AAA","100"),
                        Subcategory("Veterinaria", "AAA","100"),
                        Subcategory("Juguetes", "AAA","100"),
                        Subcategory("Perros", "AAA","100"),
                        Subcategory("Gatos", "AAA","100"),
                        Subcategory("Caballos", "AAA","100"),
                        Subcategory("Pajaros", "AAA","100"),
                        Subcategory("Cursos", "AAA","100"),
                    ),
                    "favorites_icon",
                    "3"
                ),
                Category(
                    "Geologia",
                    "Pepe pepepepe pepe pepepe Pepepe, pepepe pepepe",
                    arrayListOf(
                        Subcategory("Rocas", "AAA","100"),
                        Subcategory("Minerales", "AAA","100"),
                        Subcategory("Excavacion", "AAA","100"),
                        Subcategory("Anilisis de terreno", "AAA","100"),
                        Subcategory("Volcanes", "AAA","100"),
                        Subcategory("Tasascion", "AAA","100"),
                        Subcategory("Rocas", "AAA","100"),
                        Subcategory("Minerales", "AAA","100"),
                        Subcategory("Excavacion", "AAA","100"),
                        Subcategory("Anilisis de terreno", "AAA","100"),
                        Subcategory("Volcanes", "AAA","100"),
                        Subcategory("Tasascion", "AAA","100"),
                    ),
                    "favorites_icon",
                    "3"
                ),
                Category(
                    "Literatura",
                    "Pepe pepepepe pepe pepepe Pepepe, pepepe pepepe",
                    arrayListOf(
                        Subcategory("Clasica", "AAA","100"),
                        Subcategory("Romance", "AAA","100"),
                        Subcategory("Edicion", "AAA","100"),
                        Subcategory("Terror", "AAA","100"),
                        Subcategory("Comedia", "AAA","100"),
                        Subcategory("Poesia", "AAA","100"),
                        Subcategory("Misterio", "AAA","100"),
                        Subcategory("Ciencia_ficcion", "AAA","100"),
                        Subcategory("Fantasia", "AAA","100"),
                        Subcategory("San Jordi", "AAA","100"),
                        Subcategory("Juvenil", "AAA","100"),
                        Subcategory("Cervantes", "AAA","100"),
                    ),
                    "favorites_icon",
                    "3"
                ),
                Category(
                    "Jardineria",
                    "Pepe pepepepe pepe pepepe Pepepe, pepepe pepepe",
                    arrayListOf(
                        Subcategory("Huerto", "AAA","100"),
                        Subcategory("Flores", "AAA","100"),
                        Subcategory("Fertilizantes", "AAA","100"),
                        Subcategory("Riego", "AAA","100"),
                        Subcategory("Herramientas", "AAA","100"),
                        Subcategory("Semillas", "AAA","100"),
                        Subcategory("Servicios", "AAA","100"),
                        Subcategory("Materiales", "AAA","100"),
                        Subcategory("Arboles", "AAA","100"),
                        Subcategory("Arbustos", "AAA","100"),
                        Subcategory("Plantas_mutantes", "AAA","100"),
                        Subcategory("Cursos", "AAA","100"),
                    ),
                    "favorites_icon",
                    "3"
                ),
                Category(
                    "Transporte",
                    "Pepe pepepepe pepe pepepe Pepepe, pepepe pepepe",
                    arrayListOf(
                        Subcategory("Publico", "AAA","100"),
                        Subcategory("Taxis", "AAA","100"),
                        Subcategory("Tren", "AAA","100"),
                        Subcategory("Nacional", "AAA","100"),
                        Subcategory("Internacional", "AAA","100"),
                        Subcategory("Publico", "AAA","100"),
                        Subcategory("Taxis", "AAA","100"),
                        Subcategory("Tren", "AAA","100"),
                        Subcategory("Nacional", "AAA","100"),
                        Subcategory("Internacional", "AAA","100"),
                        Subcategory("Publico", "AAA","100"),
                        Subcategory("Taxis", "AAA","100"),
                    ),
                    "favorites_icon",
                    "3"
                ),
                Category(
                    "Educación",
                    "Pepe pepepepe pepe pepepe Pepepe, pepepe pepepe",
                    arrayListOf(
                        Subcategory("Libros", "AAA","100"),
                        Subcategory("Material", "AAA","100"),
                        Subcategory("Clases", "AAA","100"),
                        Subcategory("Matematicas", "AAA","100"),
                        Subcategory("Ciencia", "AAA","100"),
                        Subcategory("Cursos", "AAA","100"),
                        Subcategory("Programas", "AAA","100"),
                        Subcategory("Instituciones", "AAA","100"),
                        Subcategory("Consejos", "AAA","100"),
                        Subcategory("Practicas", "AAA","100"),
                        Subcategory("Univeridad", "AAA","100"),
                        Subcategory("Cultura", "AAA","100"),
                    ),
                    "favorites_icon",
                    "3"
                ),
                Category(
                    "Ocio",
                    "Pepe pepepepe pepe pepepe Pepepe, pepepe pepepe",
                    arrayListOf(
                        Subcategory("Locales", "AAA","100"),
                        Subcategory("Alquiler", "AAA","100"),
                        Subcategory("Clubs", "AAA","100"),
                        Subcategory("Competiciones", "AAA","100"),
                        Subcategory("Eventos", "AAA","100"),
                        Subcategory("Hosteleria", "AAA","100"),
                        Subcategory("Turismo", "AAA","100"),
                        Subcategory("Cine", "AAA","100"),
                        Subcategory("Clubs_nocturnos", "AAA","100"),
                        Subcategory("Transporte", "AAA","100"),
                        Subcategory("Recreativos", "AAA","100"),
                        Subcategory("Formación", "AAA","100"),
                    ),
                    "favorites_icon",
                    "3"
                ),
            )
        }
    }
}