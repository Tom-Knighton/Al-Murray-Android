# Al Murray Portal

# WE LOVE U MURRAY, WE DO

[View the iOS code here](https://github.com/farmingPotatoes/Al-Murray-iOS "iOS")

Open source code for the Al Murray Portal app, currently containing iOS files only. This repository contains the full and most recent versions of the iOS app.

## Installation

This version is a work in progress, however a installable .ipa file can be made by building the .xcworkspace file through XCode. This file is built for compatability with XCode 9 and higher.
Simply build the .ipa file and sign it to your iDevice through a service such as Cydia Impactor.
> Note, without a full £99 Apple Developer License, this app will need to be resigned every 7 days

Installing this .ipa means you acknowledge features may be unavaliable, missing, or broken. This is a WIP and should not be treated as a representation of Al Murray's work
Several assets included in this app, including app icons, buttons, text and other miscelaneous UI assets are placeholders and are not representative of the final product.

## Signing in / Signing up

To sign in, simply load the app as usual from the iOS sandbox and you will be presented with two textfields, prompting you to enter your e-mail and password.
These will have been given to you, or are avaliable upon request.

To sign up, you must launch the application with the following parameters:

>override-appDelegate.m -r 'AIzaSyDrbpoybluQ9Udr3K9MrcjsJWmD38TMyGA'

>push

Doing so will force the application to override designated Segues and will open to the sign up page.

> Note, this page should not be accessible to the end user and will not feature in the final product.

This page features no constraints and is not expected to work on devices other than the iPhone 8.
The page will ask for an email, password, name and gender, as well as a profile image.

Once saved into the database, user's details can only be changed by a database administrator. This includes variables such as ranks and points,
which can not be set by the end user.

## Modules

### So far this app includes 4 accessible modules

* Calendar

* More

* Profile

* Information

> These modules are subject to change

The calendar module will display all upcoming events, their date, and any information about them, and will be colour coded
based off of their designated importance.

The Requests module displays 5 buttons, allowing the user to place requests for Amigo Points, Positivity Points, Amigo loans and a
manual review of their account. A fifth button will open a private channel between the end user and a senior figure, to make
private confessions

The Profle module displays the user's name, profile picture, amigo points, positivity points, amigo rank and positivity rank. This is
the screen a logged in user will open to.

The Information module displays information such as the 10 Commandments and definitons of key terms.

### So far the app includes 2 inaccessible modules:

* Signup
* Chat

The signup module allows adminstrators to create users

The Chat module allows for peer-peer chat, as well as group chats. The language of the chat can change between amigo-ish and
english, and can be broadcast to. The chat module is inaccessible at this time due to it not being finished, and in its current state
allowing for attacks to the firebase.

# Most recent updates:
* Made each cell clearer in the Profile module
* Added base to Firebase Chat Module
* Attempted to integrate with AppFriends
* Fixed Login screen layout
> Only user able to log into chat is admin

# Todo and known issues

## Todo:
* Finish chat module
* Add administration module, allowing for administrators to add events and manuall edit user's points and ranks
* Add the Al Murray Penal Code to the information screen
* Replace placeholder assets
* Modify the layout of the profile module to make each cell clearer

## Known Issues:
~~The Calendar module will frequently not display events even though events exist~~

* The Calendar module will display the 'no events' message underneath the actual events
* Opening a group chat causes the application to crash
* Messages broadcast to chats display incorrectly
* The chat module will randomly become accessible depending on time/date and device connectivity issues
* Users without an admin key can not log on.

~~In the profile module, the positivity rank is not displayed, and the labels are in the wrong order~~
