package com.example.android_development_capstone.question

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.toRoute
import com.example.android_development_capstone.StudyHelperApplication
import com.example.android_development_capstone.data.Question
import com.example.android_development_capstone.data.StudyRepository
import com.example.android_development_capstone.Routes

class AddQuestionViewModel(
   savedStateHandle: SavedStateHandle,
   private val studyRepo: StudyRepository
) : ViewModel() {

   companion object {
      val Factory: ViewModelProvider.Factory = viewModelFactory {
         initializer {
            val application = (this[APPLICATION_KEY] as StudyHelperApplication)
            AddQuestionViewModel(this.createSavedStateHandle(), application.studyRepository)
         }
      }
   }

   // Get from composable's route argument
   private val subjectId: Long = savedStateHandle.toRoute<Routes.AddQuestion>().subjectId

   var question by mutableStateOf(Question())
      private set

   fun changeQuestion(ques: Question) {
      question = ques
   }

   fun addQuestion() {
      question.subjectId = subjectId
      studyRepo.addQuestion(question)
   }
}
