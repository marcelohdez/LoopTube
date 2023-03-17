# LoopTube
Loop your favorite section(s) of YouTube videos and save them into playlists
with this simple and modern Java Swing application, built using the MVC
architecture. This program was created for educational purposes as a final
project in an Introduction to Java college course.

## Why?
I created this program because... JSON, MVC, Java, and more.

## Setup
### Requirements
The following
programs or libraries are required for LoopTube to function:
* **A [JDK] version 17 or higher:** To run Java.
* **[ffmpeg]** (and ffprobe, which should come with [ffmpeg]): To extract the
downloaded YouTube videos' audio.
* **[yt-dlp]:** To download YouTube videos.

### Installation
After ensuring you have met the requirements above, the jar file for this
application may be installed from the [releases] page, which you should be
able to double-click to run.

### Building
If you would like to build LoopTube from source, download the source code,
`cd` into it with your desired terminal application and run `gradlew build
--no-daemon` _(on macOS you may need to run `chmod +x gradlew` first to make
gradlew executable)_. The `--no-deamon` argument can be removed if you would
like to keep the gradle daemon in memory, which makes future gradle builds
quicker.

Once finished, the resulting files will be in the `build` folder. The .jar
will be in `build/libs` and gradle's default run scripts will be in
`build/bin`.

---
## License
The code in the LoopTube repository is licensed under the GPLv3 license: a free
and permissive license. For more information please read the [LICENSE] file in
the repository root.

[JDK]: https://adoptium.net
[ffmpeg]: https://ffmpeg.org/
[yt-dlp]: https://github.com/yt-dlp/yt-dlp
[releases]: https://github.com/marcelohdez/LoopTube/releases/

[LICENSE]: https://github.com/marcelohdez/LoopTube/blob/master/LICENSE
