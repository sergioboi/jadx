plugins {
	id("org.gradle.toolchains.foojay-resolver-convention") version ("1.0.0")
}

if (!JavaVersion.current().isJava11Compatible) {
	throw GradleException("Jadx requires at least Java 11 for build (current version is '${JavaVersion.current()}')")
}

rootProject.name = "jadx"

include("jadx-core")
include("jadx-cli")
include("jadx-gui")

include("jadx-plugins-tools")

include("jadx-commons:jadx-app-commons")
include("jadx-commons:jadx-zip")

include("jadx-plugins:jadx-input-api")
include("jadx-plugins:jadx-dex-input")
include("jadx-plugins:jadx-java-input")
include("jadx-plugins:jadx-raung-input")
include("jadx-plugins:jadx-smali-input")
include("jadx-plugins:jadx-java-convert")
include("jadx-plugins:jadx-rename-mappings")
include("jadx-plugins:jadx-kotlin-metadata")
include("jadx-plugins:jadx-kotlin-source-debug-extension")
include("jadx-plugins:jadx-xapk-input")
include("jadx-plugins:jadx-aab-input")
include("jadx-plugins:jadx-apkm-input")
include("jadx-plugins:jadx-apks-input")

buildCache {
	local {
		// Disable local buildcache to maximize use of BuildFetch remote cache.
		isEnabled = false
	}

	remote<HttpBuildCache> {
		// On CI it's easiest to provide Env Vars
		// On local macOS it's easier to provide ~/.gradle/gradle.properties for consistency between Terminal & IDE
		val remoteUrl: String? =
			"JADX_GRADLE_REMOTE_CACHE_URL"
				.let { System.getenv(it) ?: providers.gradleProperty(it).orNull }

		val user: String? =
			"JADX_GRADLE_REMOTE_CACHE_USER"
				.let { System.getenv(it) ?: providers.gradleProperty(it).orNull }

		val token: String? =
			"JADX_GRADLE_REMOTE_CACHE_TOKEN"
				.let { System.getenv(it) ?: providers.gradleProperty(it).orNull }

		if (remoteUrl != null && user != null && token != null) {
			isEnabled = true

			url = uri(remoteUrl.trim())

			credentials {
				username = user.trim()
				password = token.trim()
			}

			isPush = true
		} else {
			isEnabled = false
		}
	}
}
