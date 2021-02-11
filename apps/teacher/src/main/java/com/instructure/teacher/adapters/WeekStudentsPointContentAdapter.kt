package com.instructure.teacher.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.instructure.canvasapi2.models.uchproc.PointJournal
import com.instructure.canvasapi2.models.uchproc.PointJournalGroup
import com.instructure.teacher.R

import com.instructure.teacher.holders.WeekStudentsPointViewHolder
import com.instructure.teacher.presenters.PointJournalPresenter
import com.instructure.teacher.presenters.WeekStudentsPointPresenter
import com.instructure.teacher.viewinterface.PointJournalView
import com.instructure.teacher.viewinterface.WeekStudentsPointView
import instructure.androidblueprint.SyncRecyclerAdapter

class WeekStudentsPointContentAdapter(
        private val pointJournal: PointJournal
) : RecyclerView.Adapter<WeekStudentsPointViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeekStudentsPointViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_week_students_point, parent, false)
        return WeekStudentsPointViewHolder(view)
    }

    override fun onBindViewHolder(holder: WeekStudentsPointViewHolder, position: Int) {
        holder.bind(position, pointJournal)
    }

    override fun getItemCount(): Int {
       return  pointJournal.points.size
    }/* SyncRecyclerAdapter<PointJournalGroup, WeekStudentsPointViewHolder, PointJournalView>(mContext, presenter){

    override fun bindHolder(model: PointJournalGroup, holder: WeekStudentsPointViewHolder, position: Int) {
        holder.bind(position, pointJournal)
    }

    override fun createViewHolder(v: View, viewType: Int) = WeekStudentsPointViewHolder(v)
    override fun itemLayoutResId(viewType: Int) = WeekStudentsPointViewHolder.HOLDER_RES_ID*/
}