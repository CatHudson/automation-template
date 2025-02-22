package com.jetbrains.teamcity.api.annotations

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
/**
 * Fields will be empty by default but may be set manually
 */
annotation class Optional()
