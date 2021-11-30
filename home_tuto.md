# Afficher les catégories de films sur l'accueil 

### Objectifs: 
Modifier la page d'accueil de l'application afin d'y afficher les catégories de films récupérées à partir de l'API. 

### Contraintes: 
- Gérer les données des vues en utilisant le databinding 
- Afficher les catégories sous forme de grid 
- Utiliser le endpoint /genre/movie/list (cf. https://developers.themoviedb.org/3/genres/get-movie-list)

### Marche à suivre 

#### 1. Mise en place du databinding 

1.1 Activez le databinding dans le module (app)

```kotlin
...
apply plugin: 'kotlin-kapt'
...

android {
    ...

    dataBinding {
        enabled = true
    }

}

```

1.2 Utilisez le databiding pour gérer la vue de `HomeFragment`

Dans le layout `home_fragment`, ajoutez la balise <layout> </layout> à la racine du document. 
```xml
<?xml version="1.0" encoding="utf-8"?>
<layout>
    <androidx.constraintlayout.widget.ConstraintLayout
       ***
    </androidx.constraintlayout.widget.ConstraintLayout>
</layout>
```

1.3 Utilisez le databinding dans le fragment `HomeFragment` 

> Android Studio générera automatiquement une classe dont les attributs sont les objets de vue déclarés dans le fichier; le nom de chaque élément est déterminer par leur id.

> Le nom de la classe est déterminé par le nom du fichier layout (utilisation du camelCase, les _ sont enlevés). Exemple `fragment_home` devient `FragmentHomeBinding`. 

Modifiez la méthode onCreateView dans `HomeFragment` afin d'utiliser la classe autogénérée par le databinding pour créer la vue de la home. 

```kotlin
...
private lateinit var binding: FragmentHomeBinding
...

override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }
```
> Le databinding nous affranchit des tâches répétitives comme findViewById pour récupérer les éléments de vue 

Modifiez la méthode onViewCreated...
```kotlin
override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homeViewModel.token.observe(viewLifecycleOwner, Observer {
            binding.textHome.text = "${it.requestToken} - ${it.expiresAt}"
        })

        homeViewModel.error.observe(viewLifecycleOwner, Observer {
            binding.textHome.text = "Erreur $it"
        })

        binding.buttonHome.setOnClickListener {
            val action = HomeFragmentDirections
                    .actionHomeFragmentToHomeSecondFragment("From HomeFragment")
            NavHostFragment.findNavController(this@HomeFragment)
                    .navigate(action)
        }
    }
``` 

1.4 Compilez le projet, s'il n'y a pas d'erreurs vous devriez avoir le même comportement qu'avant. 

#### 2. Ajoutez l'action de l'API et les méthodes necessaires permettant de récuper la liste des catégories de films 

* Uri: genre/movie/list
* Réponse
````json
{
  "genres": [
    {
      "id": 28,
      "name": "Action"
    }
  ]
}
````
> Remarquez que l'API retourne un JSON avec un attribut `genres` qui contient le tableau des catégories. 

2.1 Créez les objets du modèle de données permettant de modéliser la réponse de l'API 

- Dans le package response, créez une classe `CategoryReponse 
- Dans le fichier de la classe, créez les deux classes permettant de déserialiser la réponse du serveur 

```kotlin 
internal data class CategoryResponse(
    @SerializedName("genres")
    val genres: List<Genre>
) {
    data class Genre(
        @SerializedName("id")
        val id: Int,
        @SerializedName("name")
        val name: String
    )
}
```


2.2. Modifiez l'interface du service `MovieService` pour y ajouter une action permettant de lister les catégoris

```kotlin
internal interface MovieService {
    ...

    @GET("/genre/movie/list")
    suspend fun getCategories(): Response<CategoryResponse>
}

``` 

2.3 Modifiez la classe `OnlineDataSource` pour y ajouter la méthode permettant d'exécuter l'action getCategories 

```kotlin
suspend fun getCategories(): Result<List<CategoryResponse.Genre>> {
        return try {
            val response = service.getCategories()
            if (response.isSuccessful) {
                Result.Succes(response.body()!!.genres)
            } else {
                Result.Error(
                    exception = Exception(),
                    message = response.message(),
                    code = response.code()
                )
            }
        } catch (e: Exception) {
            Result.Error(
                exception = e,
                message = e.message ?: "No message",
                code = -1
            )
        }
    }
   ```
2.4 Ajoutez la classe permettant d'exposer les catégories aux autres composants de l'application 
> Ici, il n'y a pas d'intérêt à créer deux classes, car on veut dans tous les cas retourner la liste des catégories; pas besoin de l'encapsuler dans un autre objet. 

Dans le package data, créez la classe `Category 
```kotlin 
data class Category(
    val id: Int,
    val name: String
)
```

2.4 Modifiez le repository pour y ajouter la méthode getCategories 

```kotlin
suspend fun getCategories(): Result<List<Category>> {
        return when(val result = online.getCategories()) {
            is Result.Succes -> {
                // On utilise la fonction map pour convertir les catégories de la réponse serveur
                // en liste de categories d'objets de l'application
                val categories = result.data.map {
                    it.toCategory()
                }
                Result.Succes(categories)
            }
            is Result.Error -> result
        }
    }
 ```
 
 2.5 Modifiez le VM `HomeViewModel` pour y ajouter la méthode getCategories afin de récupérer la liste des catégories de film depuis le repository
 
 ```kotlin
 ...
 private val _categories: MutableLiveData<List<Category>> = MutableLiveData()
    val categories: LiveData<List<Category>>
        get() = _categories
 ...
 
 fun getCategories() {
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = repository.getCategories()) {
                is Result.Succes -> {
                    _categories.postValue(result.data)
                }
                is Result.Error -> {
                    _error.postValue(result.message)
                }
            }
        }
    }
 ```

#### 3. Affichez les catégories dans le fragment de la home `HomeFragment`

3.1 Créez un layout modélisant l'affichage de chaque item catégory de la home (utilisez le databinding) 
```xml
<?xml version="1.0" encoding="utf-8"?>
<layout>
    <data>
        <variable
            name="item"
            type="com.gmail.eamosse.idbdata.data.Category" />
    </data>

    <androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
        xmlns:app="http://schemas.android.com/apk/res-auto"
        xmlns:tools="http://schemas.android.com/tools"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginEnd="2dp"
        android:layout_marginStart="2dp">

        <androidx.appcompat.widget.AppCompatImageView
            android:id="@+id/category_img"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:src="@drawable/ic_launcher_background"
            app:layout_constraintBottom_toBottomOf="parent"
            app:layout_constraintEnd_toEndOf="parent"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintTop_toTopOf="parent" />

        <androidx.appcompat.widget.AppCompatTextView
            android:id="@+id/category_name"
            android:layout_width="0dp"
            android:layout_height="wrap_content"
            android:gravity="center"
            android:textColor="@android:color/white"
            android:textSize="20sp"
            android:text="@{item.name}"
            app:layout_constraintBottom_toBottomOf="@id/category_img"
            app:layout_constraintEnd_toEndOf="@id/category_img"
            app:layout_constraintStart_toStartOf="@id/category_img"
            app:layout_constraintTop_toTopOf="@id/category_img"
            tools:text="Actions et Aventures" />
    </androidx.constraintlayout.widget.ConstraintLayout>
</layout>
```

3.2. Modifiez le layout du fragment `home_fragment` afin d'y ajouter un `RecyclerView`

```xml
<?xml version="1.0" encoding="utf-8"?>
<layout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    xmlns:app="http://schemas.android.com/apk/res-auto">

    <androidx.constraintlayout.widget.ConstraintLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        tools:context=".ui.home.HomeFragment">

        <androidx.recyclerview.widget.RecyclerView
            android:id="@+id/category_list"
            android:layout_width="0dp"
            android:layout_height="0dp"
            app:layoutManager="androidx.recyclerview.widget.GridLayoutManager"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintEnd_toEndOf="parent"
            app:layout_constraintTop_toTopOf="parent"
            app:layout_constraintBottom_toBottomOf="parent"
            app:spanCount="4"
            tools:listitem="@layout/category_list_item" />
    </androidx.constraintlayout.widget.ConstraintLayout>
</layout>
```
3.3. Créez un adapter pour afficher les catégories dans le recycler view 

Dans le package home, ajoutez une classe `CategoryAdapter`

```kotlin
class CategoryAdapter(private val items: List<Category>) :
    RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: CategoryListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Category) {
            binding.item = item
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(CategoryListItemBinding.inflate(inflater, parent, false))
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }
}
```

3.4 Modifiez le fragment de la home pour instancier l'adapteur quand les catégories sont récupérées du serveur 

```kotlin 
....
override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(homeViewModel) {
            token.observe(viewLifecycleOwner, Observer {
                //récupérer les catégories
                getCategories()
            })

            categories.observe(viewLifecycleOwner, Observer {
                binding.categoryList.adapter = CategoryAdapter(it)
            })

            error.observe(viewLifecycleOwner, Observer {
                //afficher l'erreur
            })
        }
    }
....
```

3.5 Exécutez l'application 

### 4. Utilisation des extensions pour éviter de dupliquer du code  

Les instructions des deux méthodes du repository sont quasi similaires, on va utiliser les fonctions d'extensions et les inline function pour limiter la redondance dans le code. 

#### 4.1 Utilisez une fonction d'extension pour traiter la réponse de l'API 
Créez une fonction d'extension sur la classe `Redponse` puis déplacez le code qui traite le statut de la réponse dans cette fonction. 

```kotlin
import retrofit2.Response

internal fun <T : Any> Response<T>.parse(): Result<T> {
    return if (isSuccessful) {
        body()?.let {
            Result.Succes(it)
        } ?: run {
            Result.Error(
                exception = NoDataException(),
                message = "Aucune donnée",
                code = 404
            )
        }
    } else {
        Result.Error(
            exception = Exception(),
            message = message(),
            code = code()
        )
    }
}

class NoDataException: Exception()
```

Modifier les méthodes de `OnlineDataSource` pour utiliser la fonction d'extension 

```kotlin 
suspend fun getToken(): Result<TokenResponse> {
        return try {
            val response = service.getToken()
            response.parse()
        } catch (e: Exception) {
            Result.Error(
                exception = e,
                message = e.message ?: "No message",
                code = -1
            )
        }
    }
```

#### 4.2 Utilisez les fonctions les inlines fonctions pour exécuter les actions de l'API 
Tous les appels au fonctions de l'API sont encapsulés dans un bloc try/catch, ceci permet de traiter les exceptions IO (ex accès au réseau). Ces appels peuvent être simplifiés en utilisant un les inline fonctions. L'objectif est de créer une fonction qui encapsule le bloc try catch et délègue l'exécuton de l'action à la fonction appelante.

Dans le fichier `Extensions.kt`, ajoutez la fonction safeCall

``` kotlin 
internal suspend fun <T : Any> safeCall(execute: suspend () -> Result<T>): Result<T> {
    return try {
        execute()
    } catch (e: Exception) {
        if (e is IOException) {
            Result.Error(
                exception = NetworkException(),
                message = "Problème d'accès au réseau",
                code = -1
            )
        } else {
            Result.Error(
                exception = e,
                message = e.message ?: "No message",
                code = -1
            )
        }
    }
}

```

Modifiez les fonctions de `OnlineDataSource` afin d'utiliser la fonction safeCall

```kotlin
suspend fun getToken(): Result<TokenResponse> {
        return safeCall {
            val response = service.getToken()
            response.parse()
        }
    }
```

### 5. Gestion des erreurs 



