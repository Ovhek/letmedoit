package cat.copernic.letmedoit.domain.usecases.user

import cat.copernic.letmedoit.Utils.DataState
import cat.copernic.letmedoit.domain.repositories.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Clase encargada de agregar un perfil a los favoritos de un usuario
 * @property userRepository Repositorio de usuarios para hacer las operaciones necesarias
*/
class AddFavoriteProfileUseCase @Inject constructor(
    private val userRepository: UserRepository
){
    /**
     * Función que agrega un perfil a los favoritos de un usuario
     * @param idProfile Id del perfil a ser agregado
     * @return Flow<DataState<Boolean>> representando el estado de la operación
    */
    suspend operator fun invoke(idProfile: String) : Flow<DataState<Boolean>> = userRepository.addFavoriteProfiles(idProfile)
}