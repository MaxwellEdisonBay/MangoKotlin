# Mango App - let's make some friends
![image](https://i.ibb.co/D5yFywB/Screenshot-from-2021-09-22-00-50-25.png)


## Introduction

### New generation of social media

![image](https://www.mobileaction.co/blog/wp-content/uploads/2020/02/top-dating-apps-january-2020.jpg)

While social networks are losing their position as a dating tool, new services offering interest-based dating are
gaining popularity. The traditional "add a friend" has been replaced by the new "swipe right," and instead of statuses,
people write their own short bios and cheesy pickup lines.

### Top dating apps

<div class="row">
        <img src="https://i.ibb.co/nmqBRcv/new-pic.jpg" height="200" alt="" />
        <img src="https://i.pinimg.com/originals/04/8f/c5/048fc540f2b256b70f912db3df0c7075.jpg" height="200"  alt=""/>
        <img src="https://www.thesun.co.uk/wp-content/uploads/2018/05/nintchdbpict000404594241.jpg?strip=all&w=602" alt="" height="200">
        <img src="https://inspirationfeed.com/wp-content/uploads/2020/07/Tinder-Biography-Idea-25.jpg" alt="" height="200">
</div>

The largest dating apps are Tinder and Badoo. Unfortunately, their brand style and target audience is more focused on
flirting than common interests. The very fact of having an account on such services has a certain connotation of looking
for non-serious dating.

### Mango App - feel free to make friends
![image](https://i.ibb.co/D9vr95w/Screenshot-from-2021-09-21-22-04-40.png)


Mango App is a resource EXCLUSIVELY for finding friends and like-minded people, devoid of any objectification and
discrimination, especially relevant during COVID.The main goal is to give people socialization and the opportunity to
seek companionship and support outside of public places without the connotation of a one-night stand.

### No more objectification

The most important feature is gender neutrality, absence of gender search and assessment of appearance. Also, each user
can see the people who are interested in him, which will make it possible to move on to communication in the shortest
possible time without waiting for a match.

## Functionality

### Main activities
1. #### Profile flow recommendations activity
   Check the most relevant profiles, like other people. User data is provided by a full-screen media inside a draggable CardView. Like/dislike and profiles fetching causes Python API interaction.
2. #### My profile activity
   Change user search preferences, profile data and modify app settings. Check people who liked you in a "Likes" tab. Interacts with Python API to change DB values and fetch info
3. #### Active chats activity
   Handles active chats and latest messages previews, new chats are automatically added by matching other users.
4. #### Chat log
   Minimalistic chat log based in Groupie RecyclerView. Username, profile image and time are displayed in a message row.

### Key features
* **Fast matching**
* **Minimalistic UI/UX**
* **Interest tags**
    * burning interest
    * interested
* No objectification _(image recognition, future)_
* Tips to start up a conversation _(future)_
* Gamification _(experimental, future)_
* Local events invite _(future)_
## Technical documentation

### Main classes

#### Sign up / Sign in

| Class  | Description |
| --- | --- | 
| `LoginActivity` | Connects to FirebaseAuth API and handles the user app login
| `RegisterActivity` | Connects to FirebaseAuth API and creates a mango app account, initial view for new users

#### Models
| Class  | Description |
| --- | --- | 
| `ChatMessage` | Chat message data container
| `User` | User profile data container
| `UserItem` | User message preview data container in LatestMessagesActivity

#### Messages

| Class  | Description |
| --- | --- | 
| `ChatLogActivity` | Handles the chat log, fetches users messages from Firebase Realtime Database, implements message sending
| `NewMessageActivity` | Handles New Message activity
| `LatestMessagesActivity`| Handles Latest messages activity, controls the recycler view adapter and listens for new latest messages in chats

#### Expands
| Class  | Description |
| --- | --- | 
| `ExpandedTextWatcher`| Overrides EditText Text Watcher listeners to manage input data in chat log TextView (trailing spaces and new lines are blocked)

### Technologies and libraries
* [Kotlin](https://kotlinlang.org/docs/home.html)
* [Android API](https://developer.android.com/docs)
* [Firebase - noSQL databases and safe authentication]()
* [Groupie - complex RecyclerViews management](https://blog.devgenius.io/make-complex-recyclerviews-easily-with-groupie-in-android-659227b207f3)
* [Picasso - image downloading and caching library](https://square.github.io/picasso/)
* [CircleImageView - fancy profile images](https://github.com/hdodenhof/CircleImageView)

### Inspiration / tutorials
* [Messenger app tutorial (bad DB architecture)](https://www.youtube.com/watch?v=ihJGxFu2u9Q&list=PL0dzCUj1L5JE-jiBHjxlmXEkQkum_M3R-&ab_channel=LetsBuildThatApp)
* [Draggable CardView guide](https://youtu.be/nrmAsu3zTrw)
* [Simple Instagram API app](https://medium.com/@1537877453702/instagram-api-simple-android-application-with-login-and-getting-user-info-e24f4f1bc023)

## Project planning

### [Global project mental map](https://www.mindmeister.com/ru/1947417957?t=HRdWG2jShi)
![image](https://i.ibb.co/LpyDGhV/Screenshot-from-2021-09-22-00-01-59.png)
### [User start activity plan]()
![image](https://i.ibb.co/JdgkP4n/Screenshot-from-2021-09-22-00-06-18.png)

## Backend and DB structure

Check [Mango-Python-Server repository documentation](https://github.com/MaxwellEdisonBay/Mango-Python-Server)