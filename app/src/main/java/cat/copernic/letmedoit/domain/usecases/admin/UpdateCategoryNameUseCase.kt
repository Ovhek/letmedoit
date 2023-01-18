package cat.copernic.letmedoit.domain.usecases.admin

import cat.copernic.letmedoit.Utils.DataState
import cat.copernic.letmedoit.domain.repositories.CategoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Clase para el caso de uso de actualizar el nombre de una categoria.
 * @constructor Inyecta una instancia del repositorio de categorías
 * @property categoryRepository el repositorio de categorías para realizar la operación de actualizar el nombre de una categoria
*/
class UpdateCategoryNameUseCase @Inject constructor(
    private val categoryRepository: CategoryRepository
) {
    /**
     * Método para invocar el caso de uso de actualizar el nombre de una categoria.
     * @param idCategory el id de la categoria a actualizar
     * @param newName el nuevo nombre a actualizar
     * @return un flujo de datos que indica el estado de la operación (success, error, loading) y el resultado de la misma
    */
    suspend operator fun invoke(idCategory: String, newName: String): Flow<DataState<Boolean>> =
        categoryRepository.updateNombre(idCategory, newName)
}
