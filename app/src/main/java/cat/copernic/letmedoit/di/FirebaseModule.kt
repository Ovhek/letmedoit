package cat.copernic.letmedoit.di

import cat.copernic.letmedoit.Utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

//Module -> Encargado de construir dependencias
//Lo instalamos como un singleton, de esta forma es la misma clase para toda la app

/**
 * Módulo que proporciona diferentes instancias de funciones de Firebase como Singleton
 */
@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {

    //Componente Singleton que provee el FireBaseAuth
    /**
     * Proporciona una instancia Singleton de [FirebaseAuth.getInstance]
     * @return instancia de [FirebaseAuth.getInstance]
     */
    @Provides
    @Singleton
    fun firebaseAuthProvider() : FirebaseAuth{
        return FirebaseAuth.getInstance()
    }

    //Componente Singleton que provee la instacia de Firebase Firestiore
    /**
     * Proporciona una instancia Singleton de [FirebaseFirestore.getInstance]
     * @return instancia de [FirebaseFirestore.getInstance]
     */
    @Provides
    @Singleton
    fun firestoreProvider() : FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    /**
     * Proporciona una instancia Singleton de [FirebaseStorage.getInstance]
     * @return instancia de [FirebaseStorage.getInstance]
     */
    @Provides
    @Singleton
    fun storageProvider() : FirebaseStorage{
        return FirebaseStorage.getInstance()
    }

    /**
     * Proporciona una instancia Singleton de la colección de usuarios de firestore
     * @return referencia a la colección de usuarios.
     */
    @UsersCollection
    @Provides
    @Singleton
    fun usersProvider(firestore: FirebaseFirestore) : CollectionReference{
        return firestore.collection(Constants.USERS_COLLECTION)
    }

    /**
     * Proporciona una instancia Singleton de la colección de servicios de firestore
     * @return referencia a la colección de servicios.
     */
    @ServiceCollection
    @Provides
    @Singleton
    fun serviceProvider(firestore: FirebaseFirestore) : CollectionReference{
        return firestore.collection(Constants.SERVICES_COLLECTION)
    }

    /**
     * Proporciona una instancia Singleton de la colección de categorías de firestore
     * @return referencia a la colección de categorías.
     */
    @CategoryCollection
    @Provides
    @Singleton
    fun categoryProvider(firestore: FirebaseFirestore) : CollectionReference{
        return firestore.collection(Constants.CATEGORIES_COLLECTION)
    }

    /**
     * Proporciona una instancia Singleton de la colección de reportes de firestore
     * @return referencia a la colección de reportes.
     */
    @ReportsCollection
    @Provides
    @Singleton
    fun reportsProvider(firestore: FirebaseFirestore) : CollectionReference{
        return firestore.collection(Constants.REPORTS_COLLECTION)
    }

    /**
     * Proporciona una instancia Singleton de la colección de razones de reporte de firestore
     * @return referencia a la colección de razones de reporte.
     */
    @ReportsReasonCollection
    @Provides
    @Singleton
    fun reasonReportsCollection(firestore: FirebaseFirestore) : CollectionReference{
        return firestore.collection(Constants.REASONS_REPORTS_COLLECTION)
    }

    /**
     * Proporciona una instancia Singleton de la colección de tratos de firestore
     * @return referencia a la colección de tratos.
     */
    @DealCollection
    @Provides
    @Singleton
    fun dealCollection(firestore: FirebaseFirestore) : CollectionReference{
        return firestore.collection(Constants.DEALS_COLLECTION)
    }

    /**
     * Proporciona una instancia Singleton de la colección de chats de firestore
     * @return referencia a la colección de chats.
     */
    @ChatCollection
    @Provides
    @Singleton
    fun chatCollection(firestore: FirebaseFirestore) : CollectionReference{
        return firestore.collection(Constants.CHATS_COLLECTION)
    }

    //Annotation Class --> Utilizada para identificar un binding especifico para un tipo cuandoe ste tipo tiene multiples bindings definidos
    /**
     * Clase de anotación de los usuarios.
     */
    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class UsersCollection

    /**
     * Clase de anotación de los servicios.
     */
    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class ServiceCollection

    /**
     * Clase de anotación de los categorias.
     */
    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class CategoryCollection

    /**
     * Clase de anotación de los reportes.
     */
    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class ReportsCollection
    /**
     * Clase de anotación de los razones de reportes.
     */
    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class ReportsReasonCollection
    /**
     * Clase de anotación de los tratos.
     */
    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class DealCollection
    /**
     * Clase de anotación de los chats.
     */
    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class ChatCollection


}
