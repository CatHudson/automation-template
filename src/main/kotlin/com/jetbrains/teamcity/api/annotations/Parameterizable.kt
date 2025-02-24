package com.jetbrains.teamcity.api.annotations

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
/**
 * Fields will contain the passed value
 */
annotation class Parameterizable()
