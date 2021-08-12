package com.timecat.middle.block.page

import androidx.lifecycle.*
import androidx.savedstate.SavedStateRegistry
import androidx.savedstate.SavedStateRegistryController
import androidx.savedstate.SavedStateRegistryOwner
import com.timecat.extend.arms.BaseApplication

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/3/9
 * @description null
 * @usage null
 */
abstract class ViewModelPage : LifeCyclePage(),
    ViewModelStoreOwner,
    HasDefaultViewModelProviderFactory,
    SavedStateRegistryOwner {
    private var mViewModelStore: ViewModelStore? = null
    private var mDefaultFactory: ViewModelProvider.Factory? = null
    private val mSavedStateRegistryController = SavedStateRegistryController.create(this)

    override fun onFragmentCreate(): Boolean {
        mSavedStateRegistryController.performRestore(null)
        return super.onFragmentCreate()
    }

    /**
     * Returns the [ViewModelStore] associated with this activity
     *
     * @return a `ViewModelStore`
     * @throws IllegalStateException if called before the Activity is attached to the Application
     * instance i.e., before onCreate()
     */
    override fun getViewModelStore(): ViewModelStore {
        if (BaseApplication.getContext() == null) {
            throw IllegalStateException(
                "Your activity is not yet attached to the "
                        + "Application instance. You can't request ViewModel before onCreate call."
            )
        }
        if (mViewModelStore == null) {
            mViewModelStore = ViewModelStore()
        }
        return mViewModelStore!!
    }

    override fun onFragmentDestroy() {
        super.onFragmentDestroy()
        if (mViewModelStore != null) {
            mViewModelStore!!.clear()
        }
    }

    override fun getSavedStateRegistry(): SavedStateRegistry {
        return mSavedStateRegistryController.savedStateRegistry
    }

    /**
     * {@inheritDoc}
     *
     *
     * The extras of getIntent() when this is first called will be used as
     * the defaults to any [androidx.lifecycle.SavedStateHandle] passed to a view model
     * created using this factory.
     */
    override fun getDefaultViewModelProviderFactory(): ViewModelProvider.Factory {
        if (BaseApplication.getContext() == null) {
            throw IllegalStateException(
                ("Your activity is not yet attached to the "
                        + "Application instance. You can't request ViewModel before onCreate call.")
            )
        }
        if (mDefaultFactory == null) {
            mDefaultFactory = SavedStateViewModelFactory(
                BaseApplication.getContext(),
                this,
                null
            )
        }
        return mDefaultFactory!!
    }
}