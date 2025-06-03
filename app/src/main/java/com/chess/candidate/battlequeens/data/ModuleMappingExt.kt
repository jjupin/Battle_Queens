package com.chess.candidate.battlequeens.data

import com.chess.candidate.battlequeens.data.source.LocalGameStat

fun GameStat.toLocal() = LocalGameStat(
    id = id,
    numQueens = numQueens,
    time = timePlayed,
    date = datePlayed,
    usedEinstein = usedEinstein,
    winningBoard = winningBoard
)

fun List<GameStat>.toLocal() = map(GameStat::toLocal)

// Local to External
fun LocalGameStat.toExternal() = GameStat(
    id = id,
    numQueens = numQueens,
    timePlayed = time,
    datePlayed = date,
    usedEinstein = usedEinstein,
    winningBoard = winningBoard
)

@JvmName("localToExternal")
fun List<LocalGameStat>.toExternal() = map(LocalGameStat::toExternal)

