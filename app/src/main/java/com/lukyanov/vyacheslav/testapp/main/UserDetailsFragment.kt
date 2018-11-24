package com.lukyanov.vyacheslav.testapp.main

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lukyanov.vyacheslav.testapp.db.model.User
import android.databinding.DataBindingUtil
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.lukyanov.vyacheslav.testapp.databinding.FragmentUserDetailsBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.fragment_user_details.*
import com.google.android.gms.maps.model.MarkerOptions
import com.lukyanov.vyacheslav.testapp.R


private const val ARG_USER = "user"
private const val MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey"

/**
 * A simple [Fragment] subclass.
 * Use the [UserDetailsFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class UserDetailsFragment : Fragment(), OnMapReadyCallback {

    private var user: User? = null
    private var gmap: GoogleMap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            user = it.getParcelable(ARG_USER)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<FragmentUserDetailsBinding>(
                inflater, R.layout.fragment_user_details, container, false)
        val view = binding.root
        //here data must be an instance of the class MarsDataProvider
        binding.user = user
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var mapViewBundle: Bundle? = null
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY)
        }

        mapView.onCreate(mapViewBundle)
        mapView.getMapAsync(this)
    }

    companion object {
        @JvmStatic
        fun newInstance(user: User) =
                UserDetailsFragment().apply {
                    arguments = Bundle().apply {
                        putParcelable(ARG_USER, user)
                    }
                }
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        gmap = googleMap
//        gmap?.setMinZoomPreference(4F)
        val ny = LatLng(user?.address?.geo?.lat!!, user?.address?.geo?.lng!!)
        val markerOptions = MarkerOptions()
        markerOptions.position(ny)
        gmap?.addMarker(markerOptions)

        gmap?.moveCamera(CameraUpdateFactory.newLatLng(ny))
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        var mapViewBundle = outState.getBundle(MAP_VIEW_BUNDLE_KEY)
        if (mapViewBundle == null) {
            mapViewBundle = Bundle()
            outState.putBundle(MAP_VIEW_BUNDLE_KEY, mapViewBundle)
        }

        mapView.onSaveInstanceState(mapViewBundle)
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onPause() {
        mapView.onPause()
        super.onPause()
    }

    override fun onDestroy() {
        mapView?.onDestroy()
        super.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }
}
