package com.project.navermap.presentation.MainActivity.myinfo.email

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.navigation.Navigation
import com.project.navermap.R
import com.project.navermap.databinding.FragmentEmailBinding
import com.project.navermap.domain.model.EmailData
import com.project.navermap.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EmailFragment: BaseFragment<FragmentEmailBinding>() {

    override fun getViewBinding(): FragmentEmailBinding =
        FragmentEmailBinding.inflate(layoutInflater)



    override fun observeData() {}
    override fun initViews() = with(binding){
        super.initViews()
        emailSend.setOnClickListener { sendEmail()  }
        emailback.setOnClickListener {
            backMove()
        }
    }

    private fun sendEmail() {
        val emaildata = EmailData(
            "gege2848@naver.com",
            binding.titleEdit.text.toString(),
            binding.contentEdit.text.toString())
        val intent = Intent(Intent.ACTION_SENDTO)
            .apply {
                type = "text/plain"
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, arrayOf(emaildata.emailAddress))
                putExtra(Intent.EXTRA_SUBJECT, emaildata.title )
                putExtra(Intent.EXTRA_TEXT,  emaildata.content  )
            }
        if (emaildata.title =="" || emaildata.title == null || emaildata.content == "" ||  emaildata.content  == null) {
            Toast.makeText(context, "제목 또는 내용을 입력해주세요", Toast.LENGTH_SHORT).show()
        }
        else {
            startActivity(Intent.createChooser(intent, "Send Email"))
            backStack()
        }
    }
    companion object{
        const val TAG = "EmailFragment"

        fun newInstance() : EmailFragment {
            return EmailFragment().apply {

            }
        }
    }

    private fun backMove(){
        view?.let { it1 -> Navigation.findNavController(it1).navigate(R.id.action_emailFragment_to_CSCenterFragment) }
        backStack()
    }

    private fun backStack(){
        view?.let{it1 ->
            Navigation.findNavController(it1).popBackStack()
        }
    }


    //    private fun emailSend(){
//        requireContext().let {  EmailMethod().sendEmail(emailFragment = EmailFragment()) }
//    }

    // 해당 글은 Pacelize로 구성하면서 불필요해진 변수들
    //        val emailAddress = "gege2848@naver.com"
//        val title = binding.titleEdit.text.toString()
//        val content = binding.contentEdit.text.toString()

}