# Orbis

Orbis is a program I made for my advanced higher physics project, in which I needed a method of measuring a ball's position/velocity/acceleration when rolled into a cone.
My solution is to take high-framerate recording footage of the situation from above, and to then use my own implementation of the Hough Transform for circles over the entire video to find the ball's position.
Many many results were produced; about 60,000 or so. It makes me laugh every time when I remember that this took more than a day of computation in order to process 7 videos.
I thought to possibly add multithreading and interpolate different frames to seperate threads, but that may require an overhaul.

Main method in CircleDetector.java IGNORE THE HORRIBLE MESSY CODE IN THIS CLASS!
(I had to make the GUI in windowbuilder I believe it is called, a plugin for eclipse that lets you create GUI with a WYSIWYG editor, hence the unorganised code.
The advantage of this was that I could create my own custom GUI component and see what it would look like almost instantly, as well as being able to keep track of all the style settings.

imageProcessors.IPOP.java (Image Processing OPerations) is essentially a static class full of methods required for imageProcessing.
The main product of the class are helper methods for converting BufferedImages into integer arrays and fast circle drawing methods (using the midpoint circle drawing algorithm).
you may see opHoughCircle method in IPOP, but it actually serves no purpose and is just an earlier, non-working, implemenation of the method. The actual method I use to find the coordinates of a circle are in
imageProcessors.HoughCircle.java (HoughCircle.process). I have a pseudo-exlplaination of the hough algorithm I employ in my physics project, which you can view here if you wish https://drive.google.com/drive/u/0/folders/0B4fdeYEXNt5vfmNQYVhUdXFJVW12QmxtMHhyU2ZSdk5hNzZILWRBcmRtUXgzRGxtakpidEE?ltmpl=drive 
(I warn you, it is way too long). It also shows graphs of the results obtained, which are interesting modulated functions that resemble simple harmonic motion.

I had to get FFMPEG's framegrabber working for java, which was an extremely hair-pulling process. At least it eventually worked.
I would create a downloadable jar executable, however the UI I created is not that fit for any use, it would need work first.
