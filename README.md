CloudOCRClient
==============

ABBYY Cloud OCR SDK Client



Introduction
This is an application for Android devices that shows the basic usage of the ABBYY Cloud OCR SDK. It allows very basic operations to be performed.
It is expected that the mantainer knows the basics of the Android development guidelines (elements, lifecycles, structures, workflows, interactions, etc)

License

Requirements
-Android development environment (Eclipse was used for the development)
-Android SDK
-Android emulators/devices for test purposes
-Android Support Library
-ABBYY Cloud OCR account

Basics
  This app is designed for internal use only. Demonstration purposes and training with the Cloud SDK are also goals for this application. Currently, most of the Android devices from the company are running Android 2.3.*, so the Support Library is a muss
  Loaders are used instead of standard cursors. Loaders are heavy and require a Content Provider to be implemented, but offer additional advantages:
    -Automatic update of the UI when the data in the background is updated (through calling onLoadFinished every time the data is changed)
    -Lifecycle related to the activity/fragment
    -The Content Provider allows for future improvements for the application and interaction with third party apps
  Fragments are heavily used. This allows for easier movement through the app and a uniform view style for the user.
  Action Bar compatibility is used.
  Tab-related comaptibility libraries are used.

Packages
  -com.abbyy.cloudocr:    Contains the activities, the main service and the Task basic object used through the app.
  -com.abbyy.cloudocr.compat:    Contains the compatibility layer. This allows for use of a tabbed interface regardless of the internal implementation. Also allows for the use of the Action bar on pre-honeycomb devices (Which didn't implement it).
  -com.abbyy.cloudocr.database:    Contains the required classes for the database.
  -com.abbyy.cloudocr.fragments:    Contains the main information fragments used through the application
  -com.abbyy.cloudocr.optionsfragments:    Contains the fragments with the options for the different types of processing the application is able to perform.
  -com.abbyy.cloudocr.utils:    Contains other utilities that are randomly used through the app and that can be externalized.

Activities
  -MainActivity
  -ChooseTaskActivity
  -CreateTaskActivity
  -SettingsActivity
  -TaskDetailsActivity

Fragments
  -TasksFragment
    -ActiveTasksFragment
    -CompletedTasksFragment
  -ChooseTaskFragment
  -TaskDetailsFragment
  -ProcessOptionsFragment
    -ProcessImageOptionsFragment
    -ProcessBusinessCardOptionsFragment
    -Others to come.

Others
  -TasksManagerService
  -BootCompleteReceiver
  -CloudClient
  -XMLParser
  -FileManager
  -LanguageHelper

Resources

Workflows
  -Normal workflow
    -Entry point: MainActivity
    -Portrait
      ActiveTasks and CompletedTasks tabs are shown. Their respective fragments are associated to each tab.
      ActiveTasks tab shows a list with the Tasks in the database currently in a non-completed status.
      CompletedTasks tab shows a list with the Tasks in the database currently in a completed status.
      ActiveTasks tab shows an icon to create a new task. This leads to ChooseTaskActivity.
      ChooseTaskActivity is displayed. Contains ChooseTaskFragment.
      ChooseTaskFragment displays the different available options. When the user chooses an option, CreateTaskActivity is called
      CreateTaskActivity loads the correct ProcessOptionsFragment.
      
      Per standard Android action, rotation of an activity will remain on the same activity, even if further fragments were shown. That is, if CreateTaskActivity is rotated, it will remain on that activity and not go back to MainActivity, which is the only activity shown on landscape mode.
      
    -Landscape
      MainActivity is always shown. It is composed of two panes. Left pane shows the tabbed main activity. Click on a task will show its details in right pane. Click on create task will show the ChooseTaskFragment in right pane. Click on an option on the ChooseTaskFragment will replace it with the CreateTaskFragment
      
      Per standard Android action, rotation of an activity will remain on the same activity, even if further fragments were shown. That is, rotation from MainActivity will always lead to MainActivity.
      
  -External input:
    -Entry point: CreateTaskActivity
      This app is able to receive an image file through the standard Android "Share" action.
      -<External application uses Share Action, user chooses Cloud OCR>
      -CreateTaskActivity is directly opened.
      -ProcessImageOptionsFragment is added to the activity
      -A Spinner is created and added so the user can choose to modify the type of processing to be done. The image will of course remain.

Credits:
  -ABBYY: Cloud OCR SDK. SDK Documentation
  -Google: Android SDK. Android SDK Documentation.
  

TODO:
  -Create multi-image process.
  -Add settings
