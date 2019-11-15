package(default_visibility=["//visibility:public"])

load("@io_bazel_rules_kotlin//kotlin:kotlin.bzl", "kt_android_library")

# This target fails.

#kt_android_library(
#    name = "doesthiswork",
#    srcs = ["Item.java"],
#    idl_srcs = ["ItemReceiver.aidl"],
#    idl_parcelables = ["Item.aidl"],
#)

# Uncomment the following target, and comment-out the previous, for success.

android_library(
    name = "doesthiswork",
    srcs = ["Item.java"],
    idl_srcs = ["ItemReceiver.aidl"],
    idl_parcelables = ["Item.aidl"],
)

