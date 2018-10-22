package com.blackbox.apps.karay.ui.fragments

import android.app.Application
import androidx.lifecycle.ViewModel
import com.blackbox.apps.karay.models.post.Post
import com.blackbox.apps.karay.ui.items.PostItem
import com.blackbox.apps.karay.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import io.reactivex.Observable
import javax.inject.Inject

class PostViewModel @Inject constructor(private var app: Application) : ViewModel() {

    private var flagLike: Boolean = false

    fun setupMultiTab() {
        /*var list: List<Cat> = ArrayList()
        var subList: List<SubCategory> = ArrayList()

        val categories = FirebaseDatabase.getInstance().reference.child(Constants.TABLE_CATEGORY).ref
        categories.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                for (snapshot in dataSnapshot.children) {
                    val cat = snapshot.getValue<com.pakistan.blackbox.raastatourguide.ui.items.categories.Category>(com.pakistan.blackbox.raastatourguide.ui.items.categories.Category::class.java)

                    var catItem = Cat(R.drawable.carpenter, cat!!.name)
                    DBController.getInstance().addCategory(cat)

                    val subCategories = FirebaseDatabase.getInstance().reference.child(Constants.TABLE_SUB_CATEGORY).ref
                    subCategories.addValueEventListener(object : ValueEventListener {
                        override fun onCancelled(p0: DatabaseError?) {
                            Log.e(TAG, "error: " + p0?.message)
                            showProgress(false)
                        }

                        override fun onDataChange(snapshot: DataSnapshot) {

                            try {
                                val list2 = snapshot.children
                                for (i in list2) {
                                    val subCategory = i.getValue<SubCategory>(SubCategory::class.java)
                                    if (subCategory!!.catId == cat.catId) {
                                        catItem.add(SubCat(subCategory.name))
                                        DBController.getInstance().addSubCategory(subCategory)
                                        (subList as ArrayList).add(subCategory)
                                    }
                                }
                            } catch (e: Exception) {

                            }
                            showProgress(false)


                            (list as ArrayList).add(catItem)

                            if (list.isNotEmpty()) {
                                if (activity == null)
                                    return

                                try {
                                    multiTabMenu.setAdapter(HomeTabsAdapter(context(), list))
                                    multiTabMenu.onCategorySelectedListener = object : MultiTabMenu.OnCategorySelectedListener {

                                        override fun onCategorySelected(category: Category<Any>?) {
                                            mAdapter!!.filter((category as Cat).title)

                                        }

                                        override fun OnSubCategorySelected(`object`: Any) {
                                        }
                                    }
                                } catch (e: Exception) {

                                }
                            }
                        }
                    })

                }
                showProgress(false)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e(TAG, "error: " + databaseError.message)
                showProgress(false)
            }
        })*/
    }

    /**
     * Retrieve Data from Firebase
     */
    fun retrieveData(): Observable<List<PostItem>> {

        return Observable.create { emitter ->

            val mList = arrayListOf<Post>()
            val postItemList = arrayListOf<PostItem>()

            val feedReference = FirebaseDatabase.getInstance().reference.child(Constants.TABLE_ROUTE).orderByChild("time").ref
            feedReference.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {

                    for (snapshot in dataSnapshot.children) {
                        val feed = snapshot.getValue<Post>(Post::class.java)
                        mList.add(feed!!)
                        mList.reverse()
                    }

                    for (item in mList) {
                        val postItem = PostItem(item)
                        postItemList.add(postItem)
                        /*//postItem.header = item.name
                    postItem.idFeed = item.idFeed
                    postItem.title = item.title
                    postItem.description = item.text
                    postItem.photoAvatar = item.photoAvatar
                    postItem.photoCover = item.photoCover
                    postItem.categoryName = item.categoryName
                    postItem.name = item.name
                    postItem.text = item.text
                    postItem.time = item.time
                    postItem.post = item.route
                    postItem.points = item.points*/

                        //mAdapter!!.add(postItem)
                        //mAdapter!!.notifyAdapterDataSetChanged()
                    }

                    if (!emitter.isDisposed) {
                        emitter.onNext(postItemList)
                        emitter.onComplete()
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    if (!emitter.isDisposed)
                        emitter.onError(Throwable(databaseError.message))
                }
            })
        }
    }

    private fun addLike(post: Post) {
        flagLike = true
        val referenceLike = FirebaseDatabase.getInstance().reference.child(Constants.TABLE_LIKE)
        val auth = FirebaseAuth.getInstance().currentUser
        referenceLike.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (flagLike) {
                    if (dataSnapshot.child(post.idFeed).hasChild(auth!!.uid)) {
                        referenceLike.child(post.idFeed).child(auth.uid).removeValue()
                        flagLike = false
                    } else {
                        referenceLike.child(post.idFeed).child(auth.uid).setValue(true)
                        flagLike = false
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })

    }

}