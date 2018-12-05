package com.kannadavoicenotes.app.kannadavoicenotes.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.airbnb.lottie.LottieAnimationView
import com.kannadavoicenotes.app.kannadavoicenotes.R

/**
 * Created by varun.am on 05/12/18
 */
class SpeechToTextFragment : Fragment() {

    companion object {
        fun newInstance(): SpeechToTextFragment {
            val fragment = SpeechToTextFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return LayoutInflater.from(container!!.context).inflate(R.layout.layout_speech_to_text, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val micLayout = view.findViewById<ImageView>(R.id.micId)
        micLayout.setOnClickListener {
            startAnimation(view)
        }
    }

    private fun startAnimation(view: View) {
        val lottieAnimation = view.findViewById<LottieAnimationView>(R.id.mic_animation_id)
        lottieAnimation.playAnimation()
    }

}