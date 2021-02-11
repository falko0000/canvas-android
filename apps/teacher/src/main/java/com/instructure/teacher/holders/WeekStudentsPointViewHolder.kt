package com.instructure.teacher.holders

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.instructure.canvasapi2.models.BasicUser
import com.instructure.canvasapi2.models.uchproc.PointJournal
import com.instructure.canvasapi2.models.uchproc.WeekPoints
import com.instructure.pandautils.utils.ProfileUtils
import com.instructure.teacher.R
import kotlinx.android.synthetic.main.view_week_students_point.view.*

class WeekStudentsPointViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    companion object {
        const val HOLDER_RES_ID = R.layout.view_week_students_point
    }
    fun bind(position: Int, pointJournal: PointJournal) = with(itemView) {
        val basicUser = BasicUser()
        val pointJournalGroup = pointJournal.points[position]
        basicUser.name = pointJournalGroup.fullName
        basicUser.pronouns = pointJournalGroup.pronouns
        basicUser.avatarUrl = pointJournalGroup.avatarUrl
        ProfileUtils.loadAvatarForUser(itemView.avatarView, basicUser)
        studentNameView.text = pointJournalGroup.fullName
        /*val selectedWeek: ArrayList<WeekPoints> = pointJournalGroup.rating!!.firstRatingPoints!!.pointWeeks!!.filter { it.weekNumber == pointJournalGroup.selectedWeek } as ArrayList<WeekPoints>
        selectedWeek.addAll(pointJournalGroup.rating!!.secondRatingPoints!!.pointWeeks!!.filter { it.weekNumber == pointJournalGroup.selectedWeek })
        selectedWeek.get(0).pointDivided!!.lectureAttendance*/
    }
}