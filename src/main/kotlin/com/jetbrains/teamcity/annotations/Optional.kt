package com.jetbrains.teamcity.annotations

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
/**
 * Fields will be empty by default but may be set manually
 */
annotation class Optional()
