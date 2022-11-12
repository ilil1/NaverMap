package com.project.navermap.data.repository.firebaserealtime

import android.content.ContentValues.TAG
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.project.navermap.data.entity.firebase.FirebaseEntity
import com.project.navermap.domain.model.FirebaseModel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class firebaseRepositoryImpl @Inject constructor(
    private val database: FirebaseDatabase
) : firebaseRepository {

    private val ref = database.getReference("markets")

    override fun getMarkets(): Flow<List<FirebaseEntity>> = callbackFlow {
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val arrayList = arrayListOf<FirebaseEntity>()
                for (dataModel in dataSnapshot.children) {
                    Log.d(TAG, dataModel.toString())
                    val item = dataModel.getValue(FirebaseEntity::class.java) as FirebaseEntity
                    arrayList.add(item)
                }

                trySend(arrayList)
            }
            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        ref.addValueEventListener(postListener)

        awaitClose()
    }



}