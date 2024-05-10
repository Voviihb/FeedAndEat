package com.vk_edu.feed_and_eat.features.dishes.data

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.toObject
import com.vk_edu.feed_and_eat.common.code.repoTryCatchBlock
import com.vk_edu.feed_and_eat.features.dishes.domain.models.CollectionsCards
import com.vk_edu.feed_and_eat.features.dishes.domain.models.PaginationResult
import com.vk_edu.feed_and_eat.features.dishes.domain.models.Recipe
import com.vk_edu.feed_and_eat.features.dishes.domain.models.RecipeCard
import com.vk_edu.feed_and_eat.features.dishes.domain.models.Review
import com.vk_edu.feed_and_eat.features.dishes.domain.models.SearchFilters
import com.vk_edu.feed_and_eat.features.dishes.domain.models.Tag
import com.vk_edu.feed_and_eat.features.dishes.domain.models.Type
import com.vk_edu.feed_and_eat.features.dishes.domain.repository.RecipesRepository
import com.vk_edu.feed_and_eat.features.login.domain.models.Response
import com.vk_edu.feed_and_eat.features.profile.domain.models.UserModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecipesRepoImpl @Inject constructor(
    private val db: FirebaseFirestore
) : RecipesRepository {
    override fun loadRecipeById(id: String): Flow<Response<Recipe?>> = repoTryCatchBlock {
        val query = db.collection(RECIPES_COLLECTION).document(id)
        val document = query.get().await()
        return@repoTryCatchBlock document.toObject<Recipe>()?.copy(id = document.id)
    }.flowOn(Dispatchers.IO)

    /**
     * Filters recipes with pagination
     * @param filters pass all filters in DTO here
     * */
    override fun loadSearchRecipes(
        filters: SearchFilters,
        type: Type?,
        documentSnapshot: DocumentSnapshot?
    ): Flow<Response<PaginationResult>> = repoTryCatchBlock {
        var query = db.collection(RECIPES_COLLECTION)
            .where(
                Filter.and(
                    Filter.greaterThanOrEqualTo(NUTRIENTS_CALORIES_FIELD, filters.caloriesMin),
                    Filter.lessThanOrEqualTo(NUTRIENTS_CALORIES_FIELD, filters.caloriesMax),
                    Filter.greaterThanOrEqualTo(
                        NUTRIENTS_CARBOHYDRATES_FIELD,
                        filters.carbohydratesMin
                    ),
                    Filter.lessThanOrEqualTo(
                        NUTRIENTS_CARBOHYDRATES_FIELD,
                        filters.carbohydratesMax
                    ),
                    Filter.greaterThanOrEqualTo(NUTRIENTS_FAT_FIELD, filters.fatMin),
                    Filter.lessThanOrEqualTo(NUTRIENTS_FAT_FIELD, filters.fatMax),
                    Filter.greaterThanOrEqualTo(NUTRIENTS_PROTEIN_FIELD, filters.proteinMin),
                    Filter.lessThanOrEqualTo(NUTRIENTS_PROTEIN_FIELD, filters.proteinMax),
                    Filter.greaterThanOrEqualTo(NUTRIENTS_SUGAR_FIELD, filters.sugarMin),
                    Filter.lessThanOrEqualTo(NUTRIENTS_SUGAR_FIELD, filters.sugarMax),
                )
            )

        if (filters.startsWith != "")
            query = query.whereGreaterThanOrEqualTo(NAME_FIELD, filters.startsWith)
                .whereLessThanOrEqualTo(NAME_FIELD, "${filters.startsWith}\uf8ff")
        if (filters.tags.isNotEmpty())
            query = query.whereArrayContainsAny(TAGS_FIELD, filters.tags)

        query = when (filters.sort) {
            SORT_NEWNESS -> {
                query.orderBy(CREATED, Query.Direction.DESCENDING)
            }

            SORT_RATING -> {
                query.orderBy(RATING, Query.Direction.DESCENDING)
            }

            SORT_POPULARITY -> {
                query.orderBy(COOKED, Query.Direction.DESCENDING)
            }

            else -> {
                query.orderBy(CREATED, Query.Direction.DESCENDING)
            }
        }

        val snapshot = when (type) {
            Type.EXCLUDED_FIRST -> {
                query.startAfter(documentSnapshot!!).limit(filters.limit.toLong()).get().await()
            }

            Type.INCLUDED_FIRST -> {
                query.startAt(documentSnapshot!!).limit(filters.limit.toLong()).get().await()
            }

            Type.INCLUDED_LAST -> {
                query.endAt(documentSnapshot!!).limitToLast(filters.limit.toLong()).get().await()
            }

            Type.EXCLUDED_LAST -> {
                query.endBefore(documentSnapshot!!).limitToLast(filters.limit.toLong()).get()
                    .await()
            }

            else -> {
                query.limit(filters.limit.toLong()).get().await()
            }
        }

        return@repoTryCatchBlock PaginationResult(
            recipes = snapshot.map { it.toObject<Recipe>().copy(id = it.id) },
            startDocument = snapshot.documents.ifEmpty { null }?.get(0),
            endDocument = snapshot.documents.ifEmpty { null }?.get(snapshot.size() - 1)
        )
    }.flowOn(Dispatchers.IO)

    override fun loadTags(): Flow<Response<List<Tag>>> = repoTryCatchBlock {
        val query = db.collection(TAGS_COLLECTION)
        val tags = query.get().await()
        return@repoTryCatchBlock tags.map { it.toObject<Tag>() }
    }.flowOn(Dispatchers.IO)

    override fun loadCollectionRecipes(id: String): Flow<Response<CollectionsCards?>> =
        repoTryCatchBlock {
            val query = db.collection(COLLECTIONS_COLLECTION).document(id)
            val collections = query.get().await()
            return@repoTryCatchBlock collections.toObject<CollectionsCards>()
        }.flowOn(Dispatchers.IO)

    /**
     * Adds new recipes to already existing collection
     * @param collectionId pass here id of collection
     * @param recipe pass here new recipe
     * */
    override fun addRecipeToUserCollection(
        userId: String,
        collectionId: String,
        recipe: RecipeCard
    ): Flow<Response<Void>> = repoTryCatchBlock {
        val docRef = db.collection(COLLECTIONS_COLLECTION).document(collectionId)
        docRef.update(RECIPE_CARDS_FIELD, FieldValue.arrayUnion(recipe)).await()
        val userDoc = db.collection(USERS_COLLECTION).document(userId)
        val user = userDoc.get().await().toObject<UserModel>()
        user?.collectionsIdList?.filter { it.id == collectionId }?.map { it.picture = recipe.image }
        userDoc.update(COLLECTIONS_ID_LIST_FIELD, user?.collectionsIdList).await()
    }.flowOn(Dispatchers.IO)

    override fun removeRecipeFromUserCollection(
        collectionId: String,
        recipe: RecipeCard
    ): Flow<Response<Void>> = repoTryCatchBlock {
        val docRef = db.collection(COLLECTIONS_COLLECTION).document(collectionId)
        docRef.update(RECIPE_CARDS_FIELD, FieldValue.arrayRemove(recipe)).await()
    }.flowOn(Dispatchers.IO)

    override fun createNewCollection(): Flow<Response<String>> = repoTryCatchBlock {
        val result = db.collection(COLLECTIONS_COLLECTION).document()
        result.set(
            hashMapOf<String, List<RecipeCard>>(RECIPE_CARDS_FIELD to listOf())
        ).await()
        return@repoTryCatchBlock result.id
    }.flowOn(Dispatchers.IO)

    override fun addNewReviewOnRecipe(id: String, review: Review): Flow<Response<Void>> =
        repoTryCatchBlock {
            val recipeDoc = db.collection(RECIPES_COLLECTION).document(id)
            val recipe = recipeDoc.get().await().toObject<Recipe>()
            var newRating = 0.0
            if (recipe != null) {
                newRating = (recipe.rating * (recipe.reviews?.size ?: 0)
                        + review.mark) / ((recipe.reviews?.size ?: 0) + 1)
            }
            recipeDoc.update(
                REVIEWS_LIST_FIELD, FieldValue.arrayUnion(review),
                RATING_FIELD, newRating
            ).await()
        }.flowOn(Dispatchers.IO)

    override fun updateReviewOnRecipe(
        id: String,
        oldReview: Review,
        newReview: Review
    ): Flow<Response<Void>> =
        repoTryCatchBlock {
            val recipeDoc = db.collection(RECIPES_COLLECTION).document(id)
            val recipe = recipeDoc.get().await().toObject<Recipe>()
            var newRating = 0.0
            if (recipe != null) {
                newRating = (recipe.rating * (recipe.reviews?.size ?: 0)
                        - oldReview.mark + newReview.mark) / ((recipe.reviews?.size ?: 0))
            }
            recipeDoc.update(
                REVIEWS_LIST_FIELD, FieldValue.arrayRemove(oldReview),
                REVIEWS_LIST_FIELD, FieldValue.arrayUnion(newReview),
                RATING_FIELD, newRating
            ).await()
        }.flowOn(Dispatchers.IO)

    override fun incrementCookedCounter(id: String): Flow<Response<Void>> = repoTryCatchBlock {
        val recipeDoc = db.collection(RECIPES_COLLECTION).document(id)
        recipeDoc.update(COOKED_FIELD, FieldValue.increment(1)).await()
    }

    companion object {
        private const val TAGS_COLLECTION = "tags"
        private const val RECIPES_COLLECTION = "recipes"
        private const val COLLECTIONS_COLLECTION = "collections"
        private const val USERS_COLLECTION = "users"
        private const val RECIPE_CARDS_FIELD = "recipeCards"
        private const val COLLECTIONS_ID_LIST_FIELD = "collectionsIdList"
        private const val REVIEWS_LIST_FIELD = "reviews"
        private const val RATING_FIELD = "rating"
        private const val COOKED_FIELD = "cooked"

        private const val NAME_FIELD = "name"
        private const val TAGS_FIELD = "tags"
        private const val NUTRIENTS_CALORIES_FIELD = "nutrients.Calories"
        private const val NUTRIENTS_CARBOHYDRATES_FIELD = "nutrients.Carbohydrates"
        private const val NUTRIENTS_FAT_FIELD = "nutrients.Fat"
        private const val NUTRIENTS_PROTEIN_FIELD = "nutrients.Protein"
        private const val NUTRIENTS_SUGAR_FIELD = "nutrients.Sugar"

        private const val SORT_NEWNESS = 0
        private const val SORT_RATING = 1
        private const val SORT_POPULARITY = 2

        private const val CREATED = "created"
        private const val RATING = "rating"
        private const val COOKED = "cooked"
    }
}