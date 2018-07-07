# KutePreferences
A **GUI** library for easy, fast, beautiful, feature-rich and (of course) cute **preferences** in your app.

# Features
* Simple to use
* Many included preference types
* Easily expandable
* Written in Kotlin

# How to use
Have a look at the demo app (```app```  module) for a complete sample.
The sample app uses **Dagger 2** for Dependency Injection but this is not a requirement.

## Gradle
To use this library just include it in your dependencies using

    repositories {
        ...
        maven { url "https://jitpack.io" }
    }

in your project build.gradle file and

    dependencies {
        compile('com.github.markusressel:KutePreferences:v1.1.1') {
            exclude module: 'app'
            transitive = true
        }
    }

in your desired module ```build.gradle``` file.

## Included Preference Types
KuteTogglePreference 

KuteTextPreference
KutePasswordPreference

KuteNumberPreference
KuteSliderPreference

KuteDatePreference

KuteSingleSelectPreference
KuteMultiSelectPreference

## Other Types

KuteAction
KuteCategory
KuteDivider

## KutePreferencesMainFragment

**KutePreferences** builds on ```android.support.v4.app.Fragment``` for increased flexibility.
The first thing you have to do is create a class in your project that extends ```KutePreferencesMainFragment```
and implements it's ```fun initPreferenceTree(): KutePreferencesTree``` method like this:

```
class PreferencesFragment : KutePreferencesMainFragment() {

    private val dataProvider by lazy {
        DefaultKutePreferenceDataProvider(activity as Context)
    }

    private val togglePreference by lazy {
        KuteTogglePreference(key = R.string.key_demo_toggle_pref,
                icon = getIcon(MaterialDesignIconic.Icon.gmi_airplane),
                title = getString(R.string.title_demo_toggle_pref),
                defaultValue = false,
                dataProvider = dataProvider)
    }

    override fun initPreferenceTree(): KutePreferencesTree {
        return KutePreferencesTree(
            KuteCategory(
                key = R.string.key_category_battery,
                title = getString(R.string.title_category_battery),
                description = getString(R.string.description_category_battery),
                children = listOf(
                        togglePreference
                )
            )
        )
    }

}
```

As you can see the ```KutePreferencesTree``` is just a list of items that may or may not have child items.


# License

```
MIT License

Copyright (c) 2018 Markus Ressel

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```