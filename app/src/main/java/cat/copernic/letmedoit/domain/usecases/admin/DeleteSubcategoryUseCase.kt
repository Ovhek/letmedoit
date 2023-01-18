package cat.copernic.letmedoit.domain.usecases.admin

import cat.copernic.letmedoit.Utils.DataState
import cat.copernic.letmedoit.domain.repositories.CategoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Clase para el caso de uso de eliminar una subcategoria.
 * @constructor Inyecta una instancia del repositorio de categorías
 * @property categoryRepository el repositorio de categorías para realizar la operación de eliminar una categoria
 */
class DeleteSubcategoryUseCase @Inject constructor(
    val categoryRepository: CategoryRepository
) {
    /**
     * Método para invocar el caso de uso de eliminar una subcategoria.
     * @param categoryId el id de la categoria donde se encuentra la subcategoria
     * @param subcategoryId el id de la subcategoria a eliminar.
     * @return un flujo de datos que indica el estado de la operación (success, error, loading) y el resultado de la misma
     * */
    suspend operator fun invoke(
        categoryId: String,
        subcategoryId: String
    ): Flow<DataState<Boolean>> =
        categoryRepository.deleteSubcategory(categoryId, subcategoryId)
}