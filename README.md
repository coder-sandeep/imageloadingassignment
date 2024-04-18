# PAFAssignment
The app is built using MVVM architecture. 

Logs are written out whenever memory, disk or network is used, which can be used to find where image is loaded from.

**(Can be seen in recording attached or while running code in logcat)**

It uses memory and disk caching to load images. If an image is in memory, it's used. If not, disk caching is used. 

If the image isn't in memory or disk, the network is used.

Whenever an image is accessed from any source (network, disk, or memory), it's cached in both memory and disk both.

Recording with log 👇

https://drive.google.com/file/d/1Dvv7Z8A2WCnG3wLYXNuJTIGQU7m7X6u1/preview

Demo video 👇

https://github.com/coder-sandeep/imageloadingassignment/assets/54542247/db378a58-cd79-4003-b829-4fe961edf89f


