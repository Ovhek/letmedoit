package cat.copernic.letmedoit.presentation.viewmodel.general

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 *Clase viewModel que guarda el apartado (Servics/Profiles) a
 * Desde esta clase se obtiene si el usuario esta en perfiles favoritos o servicios favoritos
 */
class BarProfileOrServicesViewModel :  ViewModel() {
    val option = MutableLiveData<Int>()

    /**
     * Funcion que introduce el apartado indicado
     * @param option
     */
    fun sendOption(option : Int){
        this.option.value = option
    }
}