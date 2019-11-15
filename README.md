# rules_kotlin minimal bug repro: idl_parcelables are borked.

This repository contains a minimal reproduction of a bug in bazel's `rules_kotlin`.

## The Bug

`kt_android_library` leads to a situation where AIDL can generate java files for the parcelables and
interfaces it's given via `idl_srcs` and `idl_parcelables`, but when the generated Java is compiled,
the parcelable's implementation class cannot be found.

From `java/doesthiswork/BUILD`:

```python
kt_android_library(
    name = "doesthiswork",
    srcs = ["Item.java"], # Item.kt also exists, but is commented-out - and neither work.
    idl_srcs = ["ItemReceiver.aidl"],
    idl_parcelables = ["Item.aidl"],
)
```

The following occurs when building:

```
$ bazel build --tool_tag=ijwb:AndroidStudio \
      --curses=no --color=yes --progress_in_terminal_title=no \
      --incompatible_depset_is_not_iterable=false \
      --sandbox_debug --verbose_failures -- //java/doesthiswork:doesthiswork

INFO: Writing tracer profile to '/private/var/tmp/_bazel_jwf/23bec90a43403c12139050ef39a28842/command.profile.gz'
Loading:
Loading: 0 packages loaded
Analyzing: target //java/doesthiswork:doesthiswork (0 packages loaded, 0 targets configured)
Analyzing: target //java/doesthiswork:doesthiswork (2 packages loaded, 8 targets configured)
INFO: Analyzed target //java/doesthiswork:doesthiswork (4 packages loaded, 222 targets configured).
INFO: Found 1 target...
[0 / 6] [Prepa] BazelWorkspaceStatusAction stable-status.txt
ERROR: /[redacted]/java/doesthiswork/BUILD:5:1: Building java/doesthiswork/libdoesthiswork_base.jar (1 source file) failed (Exit 1)
bazel-out/darwin-fastbuild/bin/java/doesthiswork/doesthiswork_base_aidl/java/doesthiswork/ItemReceiver.java:105: error: cannot find symbol
public void receiveItem(doesthiswork.Item item) throws android.os.RemoteException;
                                    ^
  symbol:   class Item
  location: package doesthiswork
bazel-out/darwin-fastbuild/bin/java/doesthiswork/doesthiswork_base_aidl/java/doesthiswork/ItemReceiver.java:81: error: cannot find symbol
@Override public void receiveItem(doesthiswork.Item item) throws android.os.RemoteException
                                              ^
  symbol:   class Item
  location: package doesthiswork
bazel-out/darwin-fastbuild/bin/java/doesthiswork/doesthiswork_base_aidl/java/doesthiswork/ItemReceiver.java:49: error: cannot find symbol
doesthiswork.Item _arg0;
            ^
  symbol:   class Item
  location: package doesthiswork
bazel-out/darwin-fastbuild/bin/java/doesthiswork/doesthiswork_base_aidl/java/doesthiswork/ItemReceiver.java:51: error: package doesthiswork.Item does not exist
_arg0 = doesthiswork.Item.CREATOR.createFromParcel(data);
                         ^
Target //java/doesthiswork:doesthiswork failed to build
INFO: Elapsed time: 1.838s, Critical Path: 0.07s
INFO: 0 processes.
FAILED: Build did NOT complete successfully
FAILED: Build did NOT complete successfully
```

## It works with `android_library`.

This build rule works just fine:

```python
android_library(
    name = "doesthiswork",
    srcs = ["Item.java"],
    idl_srcs = ["ItemReceiver.aidl"],
    idl_parcelables = ["Item.aidl"],
)
```

Here's the output:

```
$ bazel build --tool_tag=ijwb:AndroidStudio \
      --curses=no --color=yes --progress_in_terminal_title=no \
      --incompatible_depset_is_not_iterable=false \
      --sandbox_debug --verbose_failures -- //java/doesthiswork:doesthiswork

INFO: Writing tracer profile to '/private/var/tmp/_bazel_jwf/23bec90a43403c12139050ef39a28842/command.profile.gz'
Loading:
Loading: 0 packages loaded
Analyzing: target //java/doesthiswork:doesthiswork (1 packages loaded, 0 targets configured)
Analyzing: target //java/doesthiswork:doesthiswork (3 packages loaded, 9 targets configured)
INFO: Analyzed target //java/doesthiswork:doesthiswork (5 packages loaded, 87 targets configured).
INFO: Found 1 target...
[0 / 3] [Prepa] BazelWorkspaceStatusAction stable-status.txt
Target //java/doesthiswork:doesthiswork up-to-date:
  bazel-bin/java/doesthiswork/libdoesthiswork.jar
INFO: Elapsed time: 2.409s, Critical Path: 0.36s
INFO: 1 process: 1 worker.
INFO: Build completed successfully, 2 total actions
INFO: Build completed successfully, 2 total actions
```
