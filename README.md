[![](https://jitpack.io/v/Rere-kt/Rekukler.svg)](https://jitpack.io/#Rere-kt/Rekukler)

# Rekukler
Library for simplifications work with RecyclerView. Using this library removes the routine of writing Adapter and ViewHolder.

## Gradle
Add it in your root build.gradle at the end of repositories:
```groovy
allprojects {
  repositories {
    ...
    maven { url 'https://jitpack.io' }
  }
}
```
Add the dependency:
```groovy
dependencies {
  implementation 'com.github.Rere-kt:Rekukler:${version}'
}
```

## Usage

First, you need to declare ViewBinder. 

```kotlin
fun articlesBinder(
    onClick: (Article) -> Unit = {}
) = viewBinder<Article, ListItemBinding>(
    layoutResId = R.layout.list_item,
    binder = ListItemBinding::bind,
    isForItem = { it is Article }, // optional, using with same Data Classes and multiple ViewBinder
    areItemsSame = { old, new -> old.id == new.id }, // optional, for DiffUtil
    areContentsSame = { old, new -> old == new }, // optional, for DiffUtil
) {
    bindView { data ->
          ...
          tvTitle.text = data.title
          tvPosition.text = getString(R.string.position, position)
          setOnClickListener { onClick.invoke(data) }
          ...
    }

    onDetachedFromWindow {
        println("on item detached from window")
    }

    onAttachedToWindow {
        println("on item attached to window")
    }
}
```

Now we can create MultibindingAdapter. This adapter can accept multiple ViewBinders as an argument.

```kotlin
private val articlesAdapter by lazy {
    MultiBindingAdapter(
        articlesBinder { println("Click from Article item") }
    )
}
````

Finally, we should configure our RecyclerView with created adapter using simple DSL.

```kotlin
binding.rvArticles.configure(articlesAdapter) {
      linearLayout {
          orientation = LinearLayout.VERTICAL
      }
      itemDecoration(
          MarginDividerItemDecoration(
              dividerLineWidth = 1.dip(resources),
              dividerLineMargin = 12.dip(resources),
              dividerLineColorRes = R.color.black
          )
      )
      itemTouchHelper()
  }
```

Also we can use ```configure``` extension without any arguments. 
LinearLayout with vertical orientation will be used as default.

```kotlin
binding.rvArticles.configure(articlesAdapter)
```

Now, after configuration, we can update adapter items. Items will be accepted with DiffUtil(we can configure behaviour in ViewBinder with ```areItemsSame``` and ```areContentsTheSame``` lamdas.

```kotlin
articlesAdapter.items = newItems
```

You can see samples in repository. Thanks.

## Contributions welcome!
