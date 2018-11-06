# KutePreferences
A **GUI** library for easy to use, fast and beautiful **preferences** in your app.

| Master | Dev |
|--------|-----|
| [![Master](https://travis-ci.org/markusressel/KutePreferences.svg?branch=master)](https://travis-ci.org/markusressel/KutePreferences/branches) | [![Master](https://travis-ci.org/markusressel/KutePreferences.svg?branch=dev)](https://travis-ci.org/markusressel/KutePreferences/branches) |
| [![codebeat badge](https://codebeat.co/badges/418ec567-fa53-4f97-ad98-eb8c4bb816ec)](https://codebeat.co/projects/github-com-markusressel-kutepreferences-master) | [![codebeat badge](https://codebeat.co/badges/431a535a-cc23-4713-a061-307d97727b97)](https://codebeat.co/projects/github-com-markusressel-kutepreferences-dev) |

# Features
* Easy to use
* Many included preference types
* Integrated search
* Persistence
* Easily expandable with custom styles and logic
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
    
        def kutePreferencesVersion = "v1.5.0"
        // choose the ones you need
        implementation("com.github.markusressel.KutePreferences:bool:${kutePreferencesVersion}")
        implementation("com.github.markusressel.KutePreferences:color:${kutePreferencesVersion}")
        implementation("com.github.markusressel.KutePreferences:date:${kutePreferencesVersion}")
        implementation("com.github.markusressel.KutePreferences:time:${kutePreferencesVersion}")
        implementation("com.github.markusressel.KutePreferences:number:${kutePreferencesVersion}")
        implementation("com.github.markusressel.KutePreferences:selection:${kutePreferencesVersion}")
        implementation("com.github.markusressel.KutePreferences:text:${kutePreferencesVersion}")
    }

in your desired module `build.gradle` file.

## Specify the preferences you need

Since **KutePreferences** provides navigation between categories, search and other things it is necessary to extend `KutePreferencesMainFragment` (that builds on `android.support.v4.app.Fragment`) with a custom class of yours in your project and implements it's `fun initPreferenceTree(): KutePreferencesTree` method similar to this:

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

## Structure

The items that you return in the `initPreferenceTree()` method are a direct representation of the categories, sections preferences and custom items that will be visible in your app. Items that need persistence require a `KuteDataProvider` parameter that defines how the current state of the preference item is persisted.

### Preference Types

KutePreferences includes implementation for most of the commonly needed preference items that you might encounter when building an app.

| Name                       | Item Type    | Description |
|----------------------------|--------------|-------------|
| KuteBooleanPreference      | Boolean      | A simple on/off preference. |
| KuteTextPreference         | String       | A simple text preference.   |
| KuteUrlPreference          | String       | A url preference.   |
| KutePasswordPreference     | String       | A password text preference. This type works exactly like `KuteTextPreference` but includes obscuring typed input. |
| KuteNumberPreference       | Integer      | A preference for number values. |
| KuteSliderPreference       | Integer      | Like `KuteNumberPreference` but with a slider GUI instead of a direct input field. |
| KuteDatePreference         | Date         | Let's the user select and store a date. |
| KuteTimePreference         | TimePersistenceModel         | Let's the user select and store a time of the day. |
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
| KuteCategory               | A category groups sections, preference items and/or other category items. |
| KuteSection                | A group of preference items inside a single preference category. |

### Implement custom preferences

If the integrated preference classes don't fit your needs you can easily create your own custom preference implementation.
To do so you have to create an implementation of `KutePreferenceItem`.
You can use `KutePreferenceBase` as a base class instead of implementing everything from scrath (which has some basic functionality implemented already) if it fit's your needs.

Important things to keep in mind if you implement your own preference item:

* remember to update the description of a preference item to always reflect the currently persisted value
* when defining layouts remember to use the existing styles like
  * KutePreferences.Preference.Icon
  * KutePreferences.Preference.Title
  * KutePreferences.Preference.Description
  * etc.

# Troubleshooting

## Navigation
* The back button doesn't work
  * ensure that you are calling the `onBackPressed()` methof of your `KutePreferencesMainFragment` implementation from the activity that is hosting it (like seen in the example app)
* The back button in the toolbar doesn't work
  * Similar to the issue above ensure that you are passing the `onOptionsItemSelected()` event for the `android.R.id.home` id to your `KutePreferencesMainFragment`

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
