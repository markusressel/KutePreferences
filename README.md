# KutePreferences
A **GUI** library for easy, fast, beautiful, feature-rich and (of course) cute **preferences** in your app.

| Master | Dev |
|--------|-----|
| [![Master](https://travis-ci.org/markusressel/KutePreferences.svg?branch=master)](https://travis-ci.org/markusressel/KutePreferences/branches) | [![Master](https://travis-ci.org/markusressel/KutePreferences.svg?branch=dev)](https://travis-ci.org/markusressel/KutePreferences/branches) |
| [![codebeat badge](https://codebeat.co/badges/418ec567-fa53-4f97-ad98-eb8c4bb816ec)](https://codebeat.co/projects/github-com-markusressel-kutepreferences-master) | [![codebeat badge](https://codebeat.co/badges/431a535a-cc23-4713-a061-307d97727b97)](https://codebeat.co/projects/github-com-markusressel-kutepreferences-dev) |

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
        ...
    
        def kutePreferencesVersion = "v1.3.1"
        // choose the ones you need
        implementation("com.github.markusressel.KutePreferences:boolean:${codeEditorVersion}")
        implementation("com.github.markusressel.KutePreferences:color:${codeEditorVersion}")
        implementation("com.github.markusressel.KutePreferences:date:${codeEditorVersion}")
        implementation("com.github.markusressel.KutePreferences:number:${codeEditorVersion}")
        implementation("com.github.markusressel.KutePreferences:selection:${codeEditorVersion}")
        implementation("com.github.markusressel.KutePreferences:text:${codeEditorVersion}")
    }

in your desired module ```build.gradle``` file.

## Specify the preferences you need

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

KutePreferenceItems are simple objects that specify view behaviour.
They work in conjunction with a data provider that defines how the state of the preference item is persisted.

Usually the preferences of your app are organized in some kind of tree.
KutePreferences lets you specify the preference structure exactly like that.

### Included Preference Types

KutePreferences includes implementation for most of the commonly needed preference items that you might encounter when building an app.

| Name                       | Item Type    | Description |
|----------------------------|--------------|-------------|
| KuteTogglePreference       | Boolean      | A simple on/off preference. |
| KuteTextPreference         | String       | A simple text preference.   |
| KutePasswordPreference     | String       | A password text preference. This type works exactly like `KuteTextPreference` but includes obscuring typed input. |
| KuteNumberPreference       | Integer      | A preference for number values. |
| KuteSliderPreference       | Integer      | Like `KuteNumberPreference` but with a slider GUI instead of a direct input field. |
| KuteDatePreference         | Date         | Let's the user select and store a date. |
| KuteColorPreference        | Color        | Let's the user select and store an ARGB color. |
| KuteSingleSelectPreference | List\<T\>    | Allows a single selection from a specified list of items. |
| KuteMultiSelectPreference  | List\<T\>    | Allows to select multiple items from a specified list of items. |

### Other Types

If you want to implement other list items that don't need any persistence you can use one of these implementations:

| Name                       | Description |
|----------------------------|-------------|
| KuteAction                 | A simple `click` action provided with a name. |

### Grouping

For better context preference items can be grouped in categories or divided by simple headlines.
To do this you can use one of these classes:

| Name                       | Description |
|----------------------------|-------------|
| KuteCategory               | A Category groups multiple preference items or even other category items. |
| KuteDivider                | A simple divider of preference items inside a single preference category. |

### Implement custom preferences

If the integrated preference classes don't fit your needs you can easily create your own custom preference implementation.
To do so you have to create an implementation of `KutePreferenceItem`.
You can use `KutePreferenceBase` instead (which has some basic functionality implemented already) if it fit's your needs.

Important things to keep in mind if you implement your own preference item:

* remember to update the description of a preference item to always reflect the currently persisted value
* when defining layouts remember to use the existing styles like
  * KutePreferences.Preference.Icon
  * KutePreferences.Preference.Title
  * KutePreferences.Preference.Description
  * etc.

# Contributing

GitHub is for social coding: if you want to write code, I encourage contributions through pull requests from forks
of this repository. Create GitHub tickets for bugs and new features and comment on the ones that you are interested in.


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