package cat.copernic.letmedoit.presentation.viewmodel.general

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * Clase viewModel que guarda el trato actual
 * De esta clase obtenemos la posicion del trao actual
 */
class ChatsDealsManagerViewModel : ViewModel() {
    val pagePosition = MutableLiveData<Int>()

    /**
     * Cambia la posicion
     * @param pagePosition posicion
     */
    fun sendPagePosition(pagePosition: Int) {
        this.pagePosition.value = pagePosition
    }
}