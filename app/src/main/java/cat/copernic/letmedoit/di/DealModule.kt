package cat.copernic.letmedoit.di

import cat.copernic.letmedoit.data.remote.DealRepositoryImpl
import cat.copernic.letmedoit.domain.repositories.DealRepository
import com.google.firebase.firestore.CollectionReference
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

//Module -> Encargado de construir dependencias
//Lo instalamos como un singleton, de esta forma es la misma clase para toda la app
/**
 * Módulo que proporciona una instancia de [DealRepositoryImpl] como Singleton
 */
@Module
@InstallIn(SingletonComponent::class)
object DealModule {

    //Provider que proporciona un Singleton el cual permite obtener una implementación del deal repository.
    /**
     * Proporciona una instancia de [DealRepositoryImpl]
     * @param DealRepositoryImpl Referencia de la colección de categorías en Firebase
     * @return instancia de [DealRepositoryImpl]
     */
    @Provides
    @Singleton
    fun dealRepositoryProvider(
        @FirebaseModule.DealCollection dealCollection: CollectionReference
    ): DealRepository {
        return DealRepositoryImpl(
            dealCollection
        )
    }

}