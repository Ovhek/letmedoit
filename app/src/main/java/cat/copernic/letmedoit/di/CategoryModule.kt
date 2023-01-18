package cat.copernic.letmedoit.di

import cat.copernic.letmedoit.data.remote.CategoryRepositoryImpl
import cat.copernic.letmedoit.domain.repositories.CategoryRepository
import com.google.firebase.firestore.CollectionReference
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

 /**
 * Módulo que proporciona una instancia de [CategoryRepositoryImpl] como Singleton
 */
@Module
@InstallIn(SingletonComponent::class)
object CategoryModule {

     /**
      * Proporciona una instancia de [CategoryRepositoryImpl]
      * @param categoryCollection Referencia de la colección de categorías en Firebase
      * @return instancia de [CategoryRepositoryImpl]
      */
    @Provides
    @Singleton
    fun provideCategoryRepository(
        @FirebaseModule.CategoryCollection categoryCollection: CollectionReference
    ): CategoryRepository {
        return CategoryRepositoryImpl(
            categoryCollection
        )
    }
}