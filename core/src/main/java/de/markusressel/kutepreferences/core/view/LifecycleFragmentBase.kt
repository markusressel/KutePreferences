/*
 * DataMunch by Markus Ressel
 * Copyright (c) 2018.
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.markusressel.kutepreferences.core.view

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.annotation.CallSuper
import androidx.annotation.CheckResult
import com.eightbitlab.rxbus.Bus
import com.trello.rxlifecycle2.LifecycleProvider
import com.trello.rxlifecycle2.LifecycleTransformer
import com.trello.rxlifecycle2.RxLifecycle
import com.trello.rxlifecycle2.android.FragmentEvent
import com.trello.rxlifecycle2.android.RxLifecycleAndroid
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject


/**
 * Created by Markus on 16.02.2018.
 */
abstract class LifecycleFragmentBase : StateFragmentBase(), LifecycleProvider<FragmentEvent> {

    private val lifecycleSubject: BehaviorSubject<FragmentEvent> = BehaviorSubject
            .create()

    @CheckResult
    override fun lifecycle(): Observable<FragmentEvent> {
        return lifecycleSubject
                .hide()
    }

    @CheckResult
    override fun <T> bindUntilEvent(event: FragmentEvent): LifecycleTransformer<T> {
        return RxLifecycle
                .bindUntilEvent(lifecycleSubject, event)
    }

    @CheckResult
    override fun <T> bindToLifecycle(): LifecycleTransformer<T> {
        return RxLifecycleAndroid
                .bindFragment(lifecycleSubject)
    }

    override fun onAttach(context: Context) {
        initComponents(context)

        super
                .onAttach(context)
        lifecycleSubject
                .onNext(FragmentEvent.ATTACH)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super
                .onCreate(savedInstanceState)
        lifecycleSubject
                .onNext(FragmentEvent.CREATE)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super
                .onViewCreated(view, savedInstanceState)
        lifecycleSubject
                .onNext(FragmentEvent.CREATE_VIEW)
    }

    @CallSuper
    override fun onStart() {
        super
                .onStart()
        lifecycleSubject
                .onNext(FragmentEvent.START)
    }

    @CallSuper
    override fun onResume() {
        super
                .onResume()
        lifecycleSubject
                .onNext(FragmentEvent.RESUME)
    }

    @CallSuper
    override fun onPause() {
        lifecycleSubject
                .onNext(FragmentEvent.PAUSE)
        super
                .onPause()
    }

    @CallSuper
    override fun onStop() {
        Bus
                .unregister(this)
        lifecycleSubject
                .onNext(FragmentEvent.STOP)
        super
                .onStop()
    }

    override fun onDestroyView() {
        lifecycleSubject
                .onNext(FragmentEvent.DESTROY_VIEW)
        super
                .onDestroyView()
    }

    @CallSuper
    override fun onDestroy() {
        lifecycleSubject
                .onNext(FragmentEvent.DESTROY)
        super
                .onDestroy()
    }

    override fun onDetach() {
        lifecycleSubject
                .onNext(FragmentEvent.DETACH)
        super
                .onDetach()
    }

    /**
     * Add a simple reference to your components in this method so lazy initializers trigger
     */
    @CallSuper
    open fun initComponents(context: Context) {
    }

}
