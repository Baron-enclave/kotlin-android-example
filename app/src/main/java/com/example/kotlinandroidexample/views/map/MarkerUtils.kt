package com.example.kotlinandroidexample.views.map

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BlurMaskFilter
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.RectF
import android.view.View
import android.widget.TextView
import com.example.kotlinandroidexample.R
import com.example.kotlinandroidexample.models.Restaurant
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory

class MarkerUtils {
    companion object {
        fun viewToBitmap(view: View): Bitmap {
            view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
            val bitmap =
                Bitmap.createBitmap(
                    view.measuredWidth,
                    view.measuredHeight,
                    Bitmap.Config.ARGB_8888
                )

            val canvas = Canvas(bitmap)
            view.layout(0, 0, view.measuredWidth, view.measuredHeight)
            view.draw(canvas)

            return bitmap
        }

        fun addShadow(
            bm: Bitmap,
            dstHeight: Int,
            dstWidth: Int,
            color: Int,
            size: Int,
            dx: Float,
            dy: Float
        ): Bitmap {
            val mask = Bitmap.createBitmap(dstWidth, dstHeight, Bitmap.Config.ALPHA_8)

            val scaleToFit = Matrix()
            val src = RectF(0f, 0f, bm.width.toFloat(), bm.height.toFloat())
            val dst = RectF(0f, 0f, dstWidth - 2 * dx, dstHeight - 2 * dy)
            scaleToFit.setRectToRect(src, dst, Matrix.ScaleToFit.CENTER)

            val dropShadow = Matrix(scaleToFit)
            dropShadow.postTranslate(dx, dy)

            val maskCanvas = Canvas(mask)
            val paint = Paint()
            maskCanvas.drawBitmap(bm, dropShadow, paint)

            val filter = BlurMaskFilter(size.toFloat(), BlurMaskFilter.Blur.NORMAL)
            paint.reset()
            paint.color = color
            paint.setMaskFilter(filter)
            paint.isFilterBitmap = true

            val ret = Bitmap.createBitmap(dstWidth, dstHeight, Bitmap.Config.ARGB_8888)
            val retCanvas = Canvas(ret)
            retCanvas.drawBitmap(mask, 0f, 0f, paint)
            retCanvas.drawBitmap(bm, scaleToFit, null)
            mask.recycle()
            return ret
        }


        fun getDotIcon(context: Context): BitmapDescriptor {
            val dotMarker = View.inflate(context, R.layout.view_dot_marker, null)
            val bitmap = Bitmap.createScaledBitmap(
                viewToBitmap(dotMarker),
                dotMarker.width,
                dotMarker.height,
                true
            )

            return BitmapDescriptorFactory.fromBitmap(bitmap)
        }

        fun getPillIcon(context: Context, restaurant: Restaurant): BitmapDescriptor {
            val pillMarker = View.inflate(context, R.layout.view_pill_marker, null)
            val textView: TextView = pillMarker.findViewById(R.id.pillMarkerRatingText)
            textView.text = restaurant.rating.toString()
            val bitmap = Bitmap.createScaledBitmap(
                viewToBitmap(pillMarker),
                pillMarker.width,
                pillMarker.height,
                true
            )

            val markerViewIcon = BitmapDescriptorFactory.fromBitmap(
                addShadow(
                    bitmap,
                    pillMarker.height,
                    pillMarker.width,
                    0xFF707070.toInt(),
                    5,
                    0f,
                    5f
                )
            )
            return markerViewIcon
        }

        fun getScalePillIcon(context: Context, restaurant: Restaurant): BitmapDescriptor {
            val pillMarker = View.inflate(context, R.layout.view_pill_marker, null)
            val textView: TextView = pillMarker.findViewById(R.id.pillMarkerRatingText)
            textView.text = restaurant.rating.toString()
            val bitmap = Bitmap.createScaledBitmap(
                viewToBitmap(pillMarker),
                pillMarker.width * 2,
                pillMarker.height * 2,
                true
            )

            val markerViewIcon = BitmapDescriptorFactory.fromBitmap(
                addShadow(
                    bitmap,
                    pillMarker.height * 2,
                    pillMarker.width * 2,
                    0xFF707070.toInt(),
                    5,
                    0f,
                    5f
                )
            )
            return markerViewIcon
        }
    }


}