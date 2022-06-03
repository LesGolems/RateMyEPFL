package com.github.sdp.ratemyepfl.model.post

import androidx.annotation.RawRes
import com.github.sdp.ratemyepfl.R

enum class SubjectKind(val id: String, @RawRes val icon: Int) {
    FOOD("Food", R.raw.emoji_burrito),
    HELP("Help", R.raw.emoji_question),
    EXAMS("Exams", R.raw.emoji_books),
    EVENTS("Events", R.raw.emoji_fire),
    HOMEWORK("Homework", R.raw.emoji_writing_hand),
    MEMES("Memes", R.raw.emoji_gorilla),
    LOST_FOUND("Lost/Found", R.raw.emoji_lost_found),
    CRUSH("Crush", R.raw.emoji_heart),
    SPORT("Sport", R.raw.emoji_woman_running),
    TRANSPORT("Transport", R.raw.emoji_tram),
    EXCHANGE("Exchange", R.raw.emoji_globe),
    PROJECTS("Projects", R.raw.emoji_scientist),
    COMPLAINT("Complaint", R.raw.emoji_facepalming),
    WARNING("Warning", R.raw.emoji_warning),
    DEBATE("Debate", R.raw.emoji_debate),
    OTHER("Other", R.raw.emoji_other);

    companion object {
        fun fromId(id: String): SubjectKind? =
            values().find { it.id == id }
    }

}