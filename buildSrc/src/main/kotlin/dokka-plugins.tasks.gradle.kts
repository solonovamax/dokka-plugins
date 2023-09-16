/*
 * Copyright (c) 2023 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file dokka-plugins.tasks.gradle.kts is part of DokkaScriptPlugin
 * Last modified on 14-09-2023 02:22 p.m.
 *
 * MIT License
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * KT-FUZZY IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

import org.gradle.jvm.tasks.Jar
import org.jetbrains.dokka.gradle.DokkaTask

plugins {
    kotlin("jvm")

    id("org.jetbrains.dokka")
}

val base = the<BasePluginExtension>()

tasks {
    withType<AbstractArchiveTask>().configureEach {
        archiveBaseName = project.name
    }

    withType<Javadoc>().configureEach {
        options {
            encoding = "UTF-8"
        }
    }

    withType<Jar>().configureEach {
        from(rootProject.file("LICENSE"))
    }

    named<Task>("build") {
        dependsOn(withType<Jar>())
    }

    val dokkaHtml by named<DokkaTask>("dokkaHtml")

    val javadocJar by register<Jar>("javadocJar") {
        dependsOn(dokkaHtml)
        from(dokkaHtml.outputDirectory)
        archiveClassifier = "javadoc"
        group = JavaBasePlugin.DOCUMENTATION_GROUP
    }

    artifacts {
        archives(javadocJar)
    }
}
