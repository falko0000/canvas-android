package com.instructure.teacher.holders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.instructure.canvasapi2.models.BasicUser
import com.instructure.canvasapi2.models.uchproc.PointJournalGroup
import com.instructure.canvasapi2.models.uchproc.WeekPoints
import com.instructure.pandautils.utils.ProfileUtils
import com.instructure.teacher.R
import com.instructure.teacher.interfaces.AdapterToFragmentCallback
import kotlinx.android.synthetic.main.point_journal_students.view.*
import java.text.FieldPosition

class PointJournalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    companion object {
        fun holderResId(): Int{
            return R.layout.point_journal_students
        }
    }
    fun bind(pointJournalGroup: PointJournalGroup, mCallback: AdapterToFragmentCallback<PointJournalGroup>, position: Int) = with(itemView){
        val basicUser = BasicUser()
        basicUser.name = pointJournalGroup.fullName
        basicUser.pronouns = pointJournalGroup.pronouns
        basicUser.avatarUrl = pointJournalGroup.avatarUrl
        ProfileUtils.loadAvatarForUser(itemView.studentAvatar, basicUser)
        studentFullName.text = pointJournalGroup.fullName
        val selectedWeek: ArrayList<WeekPoints> = pointJournalGroup.rating!!.firstRatingPoints!!.pointWeeks!!.filter { it.weekNumber == pointJournalGroup.selectedWeek } as ArrayList<WeekPoints>
        selectedWeek.addAll(pointJournalGroup.rating!!.secondRatingPoints!!.pointWeeks!!.filter { it.weekNumber == pointJournalGroup.selectedWeek })
        if (selectedWeek.size > 0){
            totalPoint.text      = selectedWeek[0].totalPoints.toString()
        }
        itemView.setOnClickListener { mCallback.onRowClicked(pointJournalGroup, position) }
    }
}