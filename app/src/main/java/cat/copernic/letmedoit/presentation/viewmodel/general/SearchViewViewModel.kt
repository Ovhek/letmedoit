package cat.copernic.letmedoit.presentation.viewmodel.general

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * Clase viewModel que conecta la vista con la busqueda de servicios el home
 */
class SearchViewViewModel : ViewModel() {
    val message = MutableLiveData<String>()

    /**
     * Funcion que lanza una busqueda con los campos requeridos
     * @param query campo de busqueda
     */
    fun sendQuery(query: String) {
        message.value = query
    }
}