package com.timecat.middle.block.tags

import com.timecat.data.room.record.RoomRecord

enum class SortingState {
  LAST_MODIFIED,
  NEWEST_FIRST,
  OLDEST_FIRST,
  ALPHABETICAL,
  NOTE_COLOR,
  NOTE_TAGS,
}

/**
 * Helper class which allow comparison of a pair of objects
 */
class ComparablePair<T : Comparable<T>, U : Comparable<U>>(val first: T, val second: U) : Comparable<ComparablePair<T, U>> {
  override fun compareTo(other: ComparablePair<T, U>): Int {
    val firstComparison = first.compareTo(other.first)
    return when {
      firstComparison == 0 -> second.compareTo(other.second)
      else -> firstComparison
    }
  }
}

fun sort(notes: List<RoomRecord>, sortingState: SortingState): List<RoomRecord> {
  // Notes returned from DB are always sorted newest first. Reduce computational load
  return when (sortingState) {
    SortingState.LAST_MODIFIED -> notes.sortedByDescending { note ->
      if (note.isPined()) Long.MAX_VALUE
      else note.updateTime
    }
    SortingState.OLDEST_FIRST -> notes.sortedBy { note ->
      if (note.isPined()) Long.MIN_VALUE
      else note.createTime
    }
    SortingState.ALPHABETICAL -> notes.sortedBy { note ->
      val content = note.title.trim().filter {
        ((it in 'a'..'z') || (it in 'A'..'Z'))
      }

      val sortValue = when {
        (note.isPined() || content.isBlank()) -> 0
        else -> content[0].toUpperCase().toInt()
      }
      ComparablePair(sortValue, note.updateTime)
    }
    SortingState.NOTE_COLOR -> notes.sortedBy { note ->
      ComparablePair(note.color, note.updateTime)
    }
    SortingState.NOTE_TAGS -> {
      val tagCounterMap = HashMap<String, Int>()
      notes.map { it.tagsIdList }.forEach { tags ->
        tags.forEach { tag ->
          tagCounterMap[tag] = (tagCounterMap[tag] ?: 0) + 1
        }
      }
      notes.sortedByDescending {
        val noteTagScore = it.tagsIdList.sumBy { tag ->
          tagCounterMap[tag] ?: 0
        }
        ComparablePair(ComparablePair(noteTagScore, it.tags ?: ""), it.updateTime)
      }
    }
    SortingState.NEWEST_FIRST -> notes.sortedByDescending { note ->
      if (note.isPined()) Long.MAX_VALUE
      else note.createTime
    }
  }
}