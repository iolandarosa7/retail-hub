import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property

interface RetailhubJacocoExtension {
    val testTask: Property<String>

    val exclusions: ListProperty<String>
}
