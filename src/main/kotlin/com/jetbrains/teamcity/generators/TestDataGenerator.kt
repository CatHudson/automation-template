package com.jetbrains.teamcity.generators

import com.jetbrains.teamcity.annotations.Optional
import com.jetbrains.teamcity.annotations.Parameterizable
import com.jetbrains.teamcity.annotations.Random
import com.jetbrains.teamcity.models.BaseModel
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.ParameterizedType

object TestDataGenerator {

    /**
     * Main method for generating test data.
     *
     * If the field has the Optional annotation, it's skipped; otherwise:
     *
     * 1) If the field has the Parameterizable annotation and parameters are passed, the parameters are assigned
     *    in the order they are encountered in the method. So if 4 fields with the Parameterizable annotation are
     *    encountered but only 3 parameters are passed, only the first 3 fields will be assigned values, in the
     *    order they appear in the method.
     *
     * 2) If the field has the Random annotation and it's a string, it is filled with random data.
     *
     * 3) If the field is a subclass of BaseModel, it is generated recursively by calling the generate method.
     *
     * 4) If the field is a List where the generic type is a subclass of BaseModel, it's set to a list with one
     *    generated element, which is generated recursively.
     *
     * The generatedModels parameter is passed when generating multiple entities in a loop, and it holds the
     * generated entities from previous steps. It allows setting previously generated entities in fields, rather
     * than generating new ones. This logic is applied only for points 3 and 4.
     * For example, if a NewProjectDescription was generated, passing it as generatedModels when generating
     * BuildType will reuse it when setting the NewProjectDescription project field, instead of generating a new one.
     */
    fun <T : BaseModel> generate(
        generatedModels: MutableList<BaseModel>,
        generatorClass: Class<T>,
        vararg parameters: Any,
    ): T? {
        try {
            val instance = generatorClass.getDeclaredConstructor().newInstance()
            var params = parameters

            for (field in generatorClass.declaredFields) {
                field.isAccessible = true

                if (!field.isAnnotationPresent(Optional::class.java)) {
                    val generatedClass = generatedModels.find { it.javaClass == field.type }

                    when {

                        field.isAnnotationPresent(Parameterizable::class.java) && params.isNotEmpty() -> {
                            field.set(instance, params[0])
                            params = params.copyOfRange(1, params.size)
                        }

                        field.isAnnotationPresent(Random::class.java) -> {
                            when (field.type) {
                                String::class.java -> field.set(instance, RandomData.string)
                                Int::class.java -> field.set(instance, RandomData.int)
                            }
                        }

                        BaseModel::class.java.isAssignableFrom(field.type) -> {
                            val finalParams = params
                            field.set(
                                instance,
                                generatedClass ?: generate(
                                    generatedModels,
                                    field.type.asSubclass(BaseModel::class.java),
                                    *finalParams
                                )
                            )
                        }

                        List::class.java.isAssignableFrom(field.type) && field.genericType is ParameterizedType -> {
                            val typeClass = (field.genericType as ParameterizedType).actualTypeArguments[0] as Class<*>
                            if (BaseModel::class.java.isAssignableFrom(typeClass)) {
                                val finalParams = params
                                field.set(
                                    instance,
                                    generatedClass?.let { mutableListOf(it) }
                                        ?: mutableListOf(
                                            generate(
                                                generatedModels,
                                                typeClass.asSubclass(BaseModel::class.java),
                                                *finalParams
                                            )
                                        )
                                )
                            }
                            else
                                throw IllegalArgumentException("Cannot generate a List of ${typeClass.typeName} as it is not a ${BaseModel::class.java.typeName} child")
                        }

//                        !BaseModel::class.java.isAssignableFrom(field.type) -> {
//                            if (!field.type.isPrimitive && field.type != String::class.java)
//                                throw IllegalArgumentException("${field.type} is not a ${BaseModel::class.java.typeName}")
//                        }
                    }
                }
                field.isAccessible = false
            }
            return instance
        } catch (e: InstantiationException) {
            throw IllegalStateException("Cannot generate test data", e)
        } catch (e: IllegalAccessException) {
            throw IllegalStateException("Cannot generate test data", e)
        } catch (e: InvocationTargetException) {
            throw IllegalStateException("Cannot generate test data", e)
        } catch (e: NoSuchMethodException) {
            throw IllegalStateException("Cannot generate test data", e)
        }
    }

    /**
     * A method to generate a single entity. Passes an empty generatedModels list.
     */
    fun <T : BaseModel> generate(generatorClass: Class<T>, vararg parameters: Any): T {
        return generate<T>(
            generatedModels = mutableListOf<BaseModel>(),
            generatorClass = generatorClass,
            parameters = parameters
        )!!
    }
}
