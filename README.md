Maryland Transit Commuters Android App
==========
A simple app that is used to determine upcoming bus times

Known bugs:
1) There is an issue with how Google handles the fragment backstack when the
fragments are destroyed. The fragments lose their transaction animation when
they are recreated. I might put my own fix for this in the future, but for now
the fragments are working pretty good, minus the lack of animations.

