package com.instructure.teacher.adapters

import android.content.Context
import android.view.View
import com.instructure.canvasapi2.models.uchproc.PointJournalGroup
import com.instructure.canvasapi2.models.uchproc.PointJournalSetting
import com.instructure.teacher.holders.PointJournalViewHolder
import com.instructure.teacher.interfaces.AdapterToFragmentCallback
import com.instructure.teacher.presenters.PointJournalPresenter
import com.instructure.teacher.viewinterface.PointJournalView
import instructure.androidblueprint.SyncRecyclerAdapter

class PointJournalRecyclerAdapter(context: Context, presenter: PointJournalPresenter,
                                  private val mCallback: AdapterToFragmentCallback<PointJournalGroup>) : SyncRecyclerAdapter<PointJournalGroup, PointJournalViewHolder, PointJournalView>(context, presenter) {

    override fun bindHolder(model: PointJournalGroup, holder: PointJournalViewHolder, position: Int) {
        holder.bind(model, mCallback, position)
    }

    override fun createViewHolder(v: View, viewType: Int): PointJournalViewHolder {
        return PointJournalViewHolder(v)
    }

    override fun itemLayoutResId(viewType: Int): Int {
        return PointJournalViewHolder.holderResId()
    }

}
