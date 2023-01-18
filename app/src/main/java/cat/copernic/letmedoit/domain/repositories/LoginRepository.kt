package cat.copernic.letmedoit.domain.repositories

import cat.copernic.letmedoit.data.model.Users
import cat.copernic.letmedoit.Utils.DataState
import kotlinx.coroutines.flow.Flow


/**
 * Interfaz que define los métodos para interactuar con un repositorio de Login.
 */
interface LoginRepository {
    //Funciones de tipo suspend (funciones que pueden ser pausadas y resumidas dentro de las corrutinas).

    /**
     * Método para hacer el login del usuario
     * @param email el correo del usuario
     * @param password la contraseña del usuario
     * @return un flujo de datos que indica el estado de la operación (success, error, loading) y el resultado de la misma
     */
    suspend fun login(email : String, password : String) : Flow<DataState<Boolean>>

    /**
     * Método para registrar el usuario
     * @param user el objeto de tipo usuario a guardar en la base de datos.
     * @param password la contraseña del usuario
     * @return un flujo de datos que indica el estado de la operación (success, error, loading) y el resultado de la misma
     */
    suspend fun signUp(user : Users, password: String) : Flow<DataState<Users>>

    /**
     * Método para cerrar la sessión del usuario actual
     * @return un flujo de datos que indica el estado de la operación (success, error, loading) y el resultado de la misma
     */
    suspend fun logOut() : Flow<DataState<Boolean>>

    /**
     * Método para hacer obtener los datos del usuario actual
     * @return un flujo de datos que indica el estado de la operación (success, error, loading) y el resultado de la misma
     */
    suspend fun getUserData() : Flow<DataState<Boolean>>

    /**
     * Método para guardar un usuario en la base de datos
     * @param user el usuario a guardar
     * @return un flujo de datos que indica el estado de la operación (success, error, loading) y el resultado de la misma
     */
    suspend fun saveUser(user : Users) : Flow<DataState<Boolean>>
}