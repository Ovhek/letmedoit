package cat.copernic.letmedoit.di

import cat.copernic.letmedoit.data.remote.LoginRepositoryImpl
import cat.copernic.letmedoit.data.remote.ServiceRepositoryImpl
import cat.copernic.letmedoit.data.remote.UserRepositoryImpl
import cat.copernic.letmedoit.domain.repositories.LoginRepository
import cat.copernic.letmedoit.domain.repositories.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

//Module -> Encargado de construir dependencias
//Lo instalamos como un singleton, de esta forma es la misma clase para toda la app

/**
 * Módulo que proporciona una instancia de [UserRepositoryImpl] como Singleton
 */
@Module
@InstallIn(SingletonComponent::class)
object UserModule {

    //Provider que proporciona un Singleton el cual permite obtener una implementación del login repository.
    /**
     * Proporciona una instancia de [UserRepositoryImpl]
     * @param usersCollection Referencia de la colección de usuarios en Firebase
     * @return instancia de [UserRepositoryImpl]
     */
    @Provides
    @Singleton
    fun userRepositoryProvider(
        @FirebaseModule.UsersCollection usersCollection: CollectionReference
    ): UserRepository {
        return UserRepositoryImpl(usersCollection)
    }
}