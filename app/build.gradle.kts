plugins {
    alias(libs.plugins.android.application)
    id("com.google.gms.google-services")
}

android {
    namespace = "co.edu.cineapp"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "co.edu.cineapp"
        minSdk = 26
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            optimization {
                enable = false
            }
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(libs.activity.ktx)
    implementation(libs.appcompat)
    implementation(libs.cardview)
    implementation(libs.constraintlayout)
    implementation(libs.gridlayout)
    implementation(libs.legacy.support.v4)
    implementation(libs.material)

    implementation(platform("com.google.firebase:firebase-bom:34.18.0"))
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.android.material:material:1.14.0")
    implementation(libs.recyclerview)

    //Room: motor sobre SQLite que crea las tablas y consultas
    implementation("androidx.room:room-runtime:2.8.5")
    //Genera automáticamente el codigo del DAO(objeto de acceso a datos) en tiempo de compilación
    annotationProcessor("androidx.room:room-compiler:2.8.5")
    //Permite que Room devuelva LiveData, que actualiza la UI sola cuando cambian los datos
    implementation("androidx.lifecycle:lifecycle-livedata:2.11.0")

    // DataStore: remplazo moderno de SharedPreferences para guardar ajustes simples
    implementation("androidx.datastore:datastore-preferences:1.2.1")
    //variante de DataStore basada en RxJava
    implementation("androidx.datastore:datastore-preferences-rxjava3:1.2.1")
    //Libreria RxJava, se usa ya que java no tiene suspend fun
    implementation("io.reactivex.rxjava3:rxjava:3.1.8")

    testImplementation(libs.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(libs.ext.junit)
}
