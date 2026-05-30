package com.example.trainsmart.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Delete
import com.example.trainsmart.data.entity.Seance

@Dao
interface SeanceDao {

    // Insérer une séance
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(seance: Seance)

    // Toutes les séances triées par date décroissante
    @Query("SELECT * FROM seances ORDER BY timestamp DESC")
    fun getAllSeances(): LiveData<List<Seance>>

    // Séances des 7 derniers jours
    @Query("SELECT * FROM seances WHERE timestamp >= :depuis ORDER BY timestamp DESC")
    fun getSeancesDepuis(depuis: Long): LiveData<List<Seance>>

    // Charge totale des 7 derniers jours (pour ACWR)
    @Query("SELECT COALESCE(SUM(chargeUA), 0) FROM seances WHERE timestamp >= :depuis")
    suspend fun getChargeTotaleDepuis(depuis: Long): Int

    // Séances des 28 derniers jours pour le calcul ACWR
    @Query("SELECT * FROM seances WHERE timestamp >= :depuis ORDER BY timestamp ASC")
    suspend fun getSeancesListDepuis(depuis: Long): List<Seance>

    // Modifier une séance
    @Update
    suspend fun update(seance: Seance)

    // Supprimer une séance
    @Delete
    suspend fun delete(seance: Seance)

    // Séance par ID
    @Query("SELECT * FROM seances WHERE id = :id")
    suspend fun getSeanceById(id: Int): Seance?
}