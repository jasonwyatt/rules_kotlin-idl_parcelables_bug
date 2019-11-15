load("@bazel_tools//tools/build_defs/repo:http.bzl", "http_archive")

# Android SDK

android_sdk_repository(
    name = "androidsdk",
    api_level = 29,
)

# Kotlin

RULES_KOTLIN_VERSION = "legacy-1.3.0-rc1"
RULES_KOTLIN_SHA = "9de078258235ea48021830b1669bbbb678d7c3bdffd3435f4c0817c921a88e42"
http_archive(
    name = "io_bazel_rules_kotlin",
    urls = ["https://github.com/bazelbuild/rules_kotlin/archive/%s.zip" % RULES_KOTLIN_VERSION],
    type = "zip",
    strip_prefix = "rules_kotlin-%s" % RULES_KOTLIN_VERSION,
    sha256 = RULES_KOTLIN_SHA,
)

load("@io_bazel_rules_kotlin//kotlin:kotlin.bzl", "kotlin_repositories", "kt_register_toolchains")
kotlin_repositories()
kt_register_toolchains()
