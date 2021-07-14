package ru.netology.nmedia

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import ru.netology.nmedia.NewPostFragment.Companion.textArg
import ru.netology.nmedia.databinding.FragmentAttachViewerBinding


class AttachViewerFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }



    val BASE_URL = "${BuildConfig.BASE_URL}"
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)
        val binding = FragmentAttachViewerBinding.inflate(
            inflater,
            container,
            false
        )


//        val url = arguments?.textArg

        Glide.with(binding.attach)
            .load("$BASE_URL/media/${arguments?.textArg}")
            .error(R.drawable.ic_error_100dp)
            .timeout(10_000)
            .into(binding.attach)
        return binding.root
    }

}