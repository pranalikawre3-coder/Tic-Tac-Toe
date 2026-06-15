package com.example.tictactoe.data.mapper

import com.example.tictactoe.data.local.entity.GameRecordEntity
import com.example.tictactoe.domain.model.GameRecord
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object GameRecordMapper {
    fun toDomain(entity: GameRecordEntity): GameRecord {
        return GameRecord(
            id = entity.id,
            winner = entity.winner,
            winnerSymbol = entity.winnerSymbol,
            date = entity.date,
            dateFormatted = formatDate(entity.date),
            totalMoves = entity.totalMoves,
            gameMode = entity.gameMode
        )
    }
    fun toEntity(record: GameRecord): GameRecordEntity {
        return GameRecordEntity(
            id = record.id,
            winner = record.winner,
            winnerSymbol = record.winnerSymbol,
            date = record.date,
            totalMoves = record.totalMoves,
            gameMode = record.gameMode
        )
    }

    fun toDomainList(entities: List<GameRecordEntity>): List<GameRecord> {
        return entities.map { toDomain(it) }
    }

    private fun formatDate(timestamp: Long): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val date = Date(timestamp)
        return sdf.format(date)
    }
    }
fun GameRecordEntity.toDomain(): GameRecord = GameRecordMapper.toDomain(this)
fun GameRecord.toEntity(): GameRecordEntity = GameRecordMapper.toEntity(this)
fun List<GameRecordEntity>.toDomainList(): List<GameRecord> = GameRecordMapper.toDomainList(this)