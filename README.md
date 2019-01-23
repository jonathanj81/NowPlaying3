Api Key held in ignored gradle.properties.  Reference to key in build.gradle, as shown:
buildTypes.each {
        it.buildConfigField 'String', 'API_KEY', NowPlaying3_ApiKey
    }
