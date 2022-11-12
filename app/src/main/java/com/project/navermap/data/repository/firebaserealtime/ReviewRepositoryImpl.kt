package com.project.navermap.data.repository.firebaserealtime

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.project.navermap.data.entity.firebase.ReviewEntity
import com.project.navermap.presentation.mainActivity.myinfo.MyInfoFragment.Companion.TAG
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class ReviewRepositoryImpl @Inject constructor(
  private val database : FirebaseDatabase
) :ReviewRepository {

    @Volatile
    private lateinit var ref: DatabaseReference

    override fun getReviewData(marketId: String): Flow<List<ReviewEntity>> = callbackFlow {
        ref = database.getReference("/reviews/${marketId}")

        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                trySend(snapshot.getValue<List<ReviewEntity>>() ?: emptyList())
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d(TAG, "onCancelled: $error")
            }

        })

        awaitClose()
    }
}