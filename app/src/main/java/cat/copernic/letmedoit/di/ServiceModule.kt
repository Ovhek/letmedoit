package cat.copernic.letmedoit.di

import cat.copernic.letmedoit.data.remote.LoginRepositoryImpl
import cat.copernic.letmedoit.data.remote.ServiceRepositoryImpl
import cat.copernic.letmedoit.domain.repositories.ServiceRepository
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Módulo que proporciona una instancia de [ServiceRepositoryImpl] como Singleton
 */
@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {

    /**
     * Proporciona una instancia de [ServiceRepositoryImpl]
     * @param serviceCollection Referencia de la colección de servicios en Firebase
     * @return instancia de [ServiceRepositoryImpl]
     */
    @Provides
    @Singleton
    fun serviceRepositoryProvider(
        storage: FirebaseStorage,
        @FirebaseModule.ServiceCollection serviceCollection: CollectionReference
    ): ServiceRepository {
        return ServiceRepositoryImpl(storage, serviceCollection)
    }
}