package com.instructure.teacher.view

import android.annotation.SuppressLint
import android.content.Context
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.instructure.annotations.PdfSubmissionView
import com.instructure.canvasapi2.models.ApiValues
import com.instructure.canvasapi2.models.DocSession
import com.instructure.canvasapi2.models.canvadocs.CanvaDocAnnotation
import com.instructure.pandautils.views.ProgressiveCanvasLoadingView
import com.instructure.teacher.R
import com.instructure.teacher.activities.SpeedGraderActivity
import com.instructure.teacher.activities.WeekStudentsPointActivity
import com.pspdfkit.ui.inspector.PropertyInspectorCoordinatorLayout
import com.pspdfkit.ui.special_mode.controller.AnnotationCreationController
import com.pspdfkit.ui.special_mode.controller.AnnotationEditingController
import com.pspdfkit.ui.special_mode.manager.AnnotationManager
import com.pspdfkit.ui.toolbar.ToolbarCoordinatorLayout
import java.util.ArrayList


@SuppressLint("ViewConstructor")
class WeekStudentsPointView(
        context: Context,
        ) : FrameLayout(context), AnnotationManager.OnAnnotationCreationModeChangeListener, AnnotationManager.OnAnnotationEditingModeChangeListener{
    override fun onEnterAnnotationCreationMode(p0: AnnotationCreationController) {
        TODO("Not yet implemented")
    }

    override fun onChangeAnnotationCreationMode(p0: AnnotationCreationController) {
        TODO("Not yet implemented")
    }

    override fun onExitAnnotationCreationMode(p0: AnnotationCreationController) {
        TODO("Not yet implemented")
    }

    override fun onEnterAnnotationEditingMode(p0: AnnotationEditingController) {
        TODO("Not yet implemented")
    }

    override fun onChangeAnnotationEditingMode(p0: AnnotationEditingController) {
        TODO("Not yet implemented")
    }

    override fun onExitAnnotationEditingMode(p0: AnnotationEditingController) {
        TODO("Not yet implemented")
    }


}