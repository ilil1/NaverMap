package com.project.navermap.data.repository.firebaserealtime

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.project.navermap.data.entity.firebase.ReviewEntity
import com.project.navermap.di.annotation.dispatchermodule.IoDispatcher
import com.project.navermap.presentation.mainActivity.myinfo.MyInfoFragment.Companion.TAG
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.withContext
import java.util.concurrent.atomic.AtomicLong
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReviewRepositoryImpl @Inject constructor(
  private val database : FirebaseDatabase,
 @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) :ReviewRepository {

    private val nextId = AtomicLong(0L)

    @Volatile
    private lateinit var ref: DatabaseReference

    override fun getReviewData(marketId: String): Flow<List<ReviewEntity>> = callbackFlow {
        ref = database.getReference("/reviews/${marketId}")

        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val reviews = (snapshot.getValue<List<ReviewEntity>>() ?: emptyList()).also {
                    nextId.set(it.size.toLong())
                }
                trySend(reviews)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d(TAG, "onCancelled: $error")
            }

        })

        awaitClose()
    }

    override suspend fun writeReviewData(marketId: String, title: String, content: String, rating: Int) = withContext<Unit>(ioDispatcher) {
        val reviewContent = ReviewEntity(
            id = nextId.getAndIncrement(),
            marketId = marketId.toLong(),
            title = title,
            rating = rating,
        )
        ref.child(reviewContent.id.toString()).setValue(reviewContent)
        Log.d(TAG, "reviewContent: $reviewContent")
    }
}