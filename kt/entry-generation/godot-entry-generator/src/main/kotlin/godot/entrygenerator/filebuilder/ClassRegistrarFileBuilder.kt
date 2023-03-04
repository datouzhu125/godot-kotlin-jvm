package godot.entrygenerator.filebuilder

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.TypeSpec
import godot.entrygenerator.generator.ConstructorRegistrationGenerator
import godot.entrygenerator.generator.FunctionRegistrationGenerator
import godot.entrygenerator.generator.PropertyRegistrationGenerator
import godot.entrygenerator.generator.SignalRegistrationGenerator
import godot.entrygenerator.model.RegisteredClass
import godot.tools.common.constants.GENERATED_COMMENT
import godot.tools.common.constants.GodotKotlinJvmTypes
import godot.tools.common.constants.godotEntryBasePackage
import godot.tools.common.constants.godotRegistrationPackage
import java.io.BufferedWriter

class ClassRegistrarFileBuilder(
    private val registeredClass: RegisteredClass,
    private val appendableProvider: (RegisteredClass) -> BufferedWriter
) {
    private val classRegistrarBuilder = TypeSpec
        .classBuilder("${registeredClass.name}Registrar")
        .addModifiers(KModifier.OPEN)
        .let { classBuilder ->
            if (registeredClass.isAbstract) {
                classBuilder.addKdoc("Registrar for abstract class. Does not register any members as it's only used for default value providing if any properties with default values are provided in the abstract class. Members of this abstract class are registered by the inheriting registrars")
            } else classBuilder
        }

    private val className = ClassName(registeredClass.containingPackage, registeredClass.name)

    private val registerClassControlFlow = FunSpec
        .builder("register")
        .addModifiers(KModifier.OVERRIDE)
        .addParameter("registry", ClassName(godotRegistrationPackage, GodotKotlinJvmTypes.classRegistry))
        .beginControlFlow("with(registry)") //START: with registry
        .let { funSpecBuilder ->
            if (!registeredClass.isAbstract) {
                funSpecBuilder.beginControlFlow(
                    "registerClass<%T>(%S,·%S,·%T::class,·${registeredClass.isTool},·%S,·%S)·{",
                    className,
                    registeredClass.resPath,
                    registeredClass.supertypes.first().fqName,
                    className,
                    registeredClass.godotBaseClass,
                    registeredClass.registeredName
                ) //START: registerClass
            } else {
                funSpecBuilder
                    .addComment("Abstract classes don't need to have any members to be registered")
            }
        }


    fun build(): Pair<String, Array<Any>> {
        if (!registeredClass.directlyInheritsGodotBaseClass) {
            val inheritedClass = registeredClass.supertypes.first()
            classRegistrarBuilder.superclass(
                ClassName(
                    "$godotEntryBasePackage.${inheritedClass.containingPackage}",
                    "${inheritedClass.name}Registrar"
                )
            )
        } else {
            classRegistrarBuilder.addSuperinterface(
                ClassName(godotRegistrationPackage, GodotKotlinJvmTypes.classRegistrar)
            )
        }

        if (!registeredClass.isAbstract) {
            ConstructorRegistrationGenerator.generate(registeredClass, className, registerClassControlFlow)
            FunctionRegistrationGenerator.generate(registeredClass, className, registerClassControlFlow)
            SignalRegistrationGenerator.generate(registeredClass, className, registerClassControlFlow)
            PropertyRegistrationGenerator.generate(registeredClass, className, registerClassControlFlow)
        }

        classRegistrarBuilder.addFunction(
            registerClassControlFlow
                .let { funSpecBuilder ->
                    if (!registeredClass.isAbstract) {
                        funSpecBuilder.endControlFlow() //END: registerClass
                    } else funSpecBuilder
                }
                .endControlFlow() //END: with registry
                .build()
        )

        appendableProvider(registeredClass).use { bufferedWriter ->
            FileSpec
                .builder("$godotEntryBasePackage.${registeredClass.containingPackage}", "${registeredClass.name}Entry")
                .addFileComment(GENERATED_COMMENT)
                .addType(classRegistrarBuilder.build())
                .build()
                .writeTo(bufferedWriter)
        }

        return "%T().register(registry)" to arrayOf(
            ClassName(
                "$godotEntryBasePackage.${registeredClass.containingPackage}",
                "${registeredClass.name}Registrar"
            )
        )
    }
}
