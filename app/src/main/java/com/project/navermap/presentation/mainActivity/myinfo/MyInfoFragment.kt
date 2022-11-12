package com.project.navermap.presentation.mainActivity.myinfo

import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.datastore.core.DataStore
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.google.protobuf.Method
import com.project.navermap.R
import com.project.navermap.databinding.FragmentMyInfoBinding
import com.project.navermap.domain.model.ProfileData
import com.project.navermap.presentation.base.BaseFragment
import com.project.navermap.presentation.login.LoginActivity
import com.project.navermap.presentation.myLocation.MyLocationActivity
import com.project.navermap.util.ConvertBitmap
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONException
import org.json.JSONObject
import java.lang.Exception
import java.util.prefs.Preferences

@AndroidEntryPoint
class MyInfoFragment: BaseFragment<FragmentMyInfoBinding>() {


    private lateinit var bitmap: Bitmap
    private lateinit var getResultImage: ActivityResultLauncher<Intent>
    private lateinit var sharedManager : com.project.navermap.util.PreferenceManager
    private val userName = arguments?.getString("data")


//    private val viewModel by viewModels<MyInfoViewModel>()

    private fun popUp() {
        requireContext().let { it1 -> Method().popUp(it1) }
    }


    private val check = true;

    override fun getViewBinding(): FragmentMyInfoBinding =
        FragmentMyInfoBinding.inflate(layoutInflater)

    override fun observeData() {

        /**
         * 추후 로그인 acesss->로 기능작동 하도록 initView()뒤로 이동예정
         */
    }

    companion object {
        const val TAG = "MyInfoFragment"

        fun newInstance(): MyInfoFragment {
            return MyInfoFragment().apply {

            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        // val per = requireContext().getSharedPreferences(getMy_ID(requireContext()) +"_account", AppCompatActivity.MODE_PRIVATE)
        //   val col_val: Collection<*> = per.all.values
        //     val it_val = col_val.iterator()
        //     val value = it_val.next() as String

        //     val jsonObject = JSONObject(value)
        //    var My_ID = jsonObject.getString("MY_ID") as String



        //    val prefs : android.content.SharedPreferences = requireContext().getSharedPreferences(My_ID+"_jpg", Context.MODE_PRIVATE)
        //    Log.d("검색", "${prefs.getString(My_ID, "").toString()}검색한다")

        //   if(prefs.getString(My_ID, "").toString() != "") {
        //      binding.profileImage.setImageBitmap(
        //        ConvertBitmap.StringToBitmap(
        //           prefs.getString(My_ID, "").toString()
        //          )
        //         )
        //   }

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun initViews() {
        super.initViews()

//        if (!viewModel.isEmpty()){
//            viewModel.getAllProfile()
//            binding.profileImage.setImageBitmap(viewModel.imageData)
//
//        }
        binding.nameText.text = userName

        binding.addressChagneTextview.setOnClickListener { startActivity(Intent(requireActivity(),MyLocationActivity::class.java)) }

        binding.logout.setOnClickListener { startActivity(Intent(requireActivity(),LoginActivity::class.java)) }

        binding.terms.setOnClickListener { openTerms() }

        // 이미지 view를 눌렀을때 이미지 변동하도록 변경하기
        binding.profileImage.setOnClickListener { loadImage() }

        binding.noticeText.setOnClickListener { popUp() }

        binding.centerTextview.setOnClickListener { openCSCenter() }

        binding.setting.setOnClickListener { openSetting() }

        binding.personalTextview.setOnClickListener { openPersonal() }

        binding.back.setOnClickListener { back() }

        binding.heart.setOnClickListener { moveLike() }

        binding.heartText.setOnClickListener { moveLike() }

        binding.categoryText.setOnClickListener { Toast.makeText(context,"업데이트 될 예정입니다.",Toast.LENGTH_SHORT).show() }
        binding.category.setOnClickListener {Toast.makeText(context,"업데이트 될 예정입니다.",Toast.LENGTH_SHORT).show()  }
        binding.reviewTextView.setOnClickListener { Toast.makeText(context,"업데이트 될 예정입니다.",Toast.LENGTH_SHORT).show() }
        binding.orderReview.setOnClickListener { Toast.makeText(context,"업데이트 될 예정입니다.",Toast.LENGTH_SHORT).show() }
        binding.imageOrder.setOnClickListener { Toast.makeText(context,"업데이트 될 예정입니다.",Toast.LENGTH_SHORT).show() }


//        binding.heart.setOnClickListener { openHeart() }
//        binding.heartText.setOnClickListener { openHeart()  }

        getResultImage = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val dataUri: Uri? = result.data?.data
                try {

                    val bitmap: Bitmap =
                        MediaStore.Images.Media.getBitmap(context?.contentResolver, dataUri)

//                    val imageBitmap  =  ImageDecoder.createSource(context?.contentResolver!!,dataUri!!)
//                    val image = ImageDecoder.decodeBitmap(imageBitmap)
//                    viewModel.insertProfile(ProfileData(0,image))

                    binding.profileImage.setImageBitmap(bitmap)
                    var My_ID : String = ""
                    val per = requireContext().getSharedPreferences("account", Activity.MODE_PRIVATE)
                    val col_val : Collection<*> = per.all.values
                    val it_val = col_val.iterator()
                    val value = it_val.next() as String

                    try {
                        val jsonObject = JSONObject(value)
                        My_ID = jsonObject.getString("heetae") as String
                    }catch (e: JSONException){}


                    sharedManager.setImgString(requireContext(),My_ID, ConvertBitmap.BitmapToString(bitmap)
                        ,My_ID+"_jpg")

                } catch (e: Exception) {
                    Toast.makeText(context, "$e", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

    private fun moveLike(){
        view?.let { it1 ->
            Navigation.findNavController(it1)
                .navigate(R.id.action_myInfoFragment_to_likeFragment)
        }
    }

    private fun loadImage() {

        var intent_image = Intent()
        intent_image.type = "image/*"
        intent_image.action = Intent.ACTION_GET_CONTENT

        getResultImage.launch(intent_image)
//        startActivityForResult(Intent.createChooser(intent_image,"Load Picture"),galley)
    }

    private fun openSetting() {
        view?.let { it1 ->
            Navigation.findNavController(it1)
                .navigate(R.id.action_myInfoFragment_to_configurationFragment)
        }
    }

    private fun openTerms() {
        view?.let { it1 ->
            Navigation.findNavController(it1).navigate(R.id.action_myInfoFragment_to_termsFragment)
        }
    }

    private fun openPersonal() {
        view?.let { it1 ->
            Navigation.findNavController(it1)
                .navigate(R.id.action_myInfoFragment_to_personalFragment)
        }
    }

    private fun back() {
//        activity?.let {
//            var intent = Intent(context, MainActivity::class.java)
//            startActivity(intent)
//        }
//        backStack()
        activity?.finish()
    }

//    private fun darkMode() {
//        if (check == binding.darkSwitch.isChecked) {
//            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
//        } else {
//            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
//        }
//    }


    private fun openCSCenter() {
        view?.let { it1 ->
            Navigation.findNavController(it1)
                .navigate(R.id.action_myInfoFragment_to_CSCenterFragment)
        }
    }

    private fun backStack() {
        activity?.finish()
    }

//    private fun openHeart(){
//        view?.let { it1 ->
//            Navigation.findNavController(it1)
//                .navigate(R.id.action_myInfoFragment_to_likeFragment)
//        }
//    }
}





//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if(galley == requestCode){
//            if(resultCode == RESULT_OK){
//                val dataUri : Uri? = data?.data
//                try {
//                    val bitmap : Bitmap = MediaStore.Images.Media.getBitmap(context?.contentResolver,dataUri)
//                    binding.profileImage.setImageBitmap(bitmap)
//                }  catch (e: Exception) {
//                    Toast.makeText(context,"$e",Toast.LENGTH_SHORT).show()
//                }
//            }
//        }
//    }
