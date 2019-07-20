# Orbis
Use to find (and record into an excel file) the position of a ball in a video file! I warn though, this was made entirely for my own purposes, I wouldn't rely on this if I were you and needed to do the same.

#### Overview
Orbis is a program I made for my _advanced higher physics_ project (SQA) in 2015, in which I needed a method of measuring a ball's position/velocity/acceleration when rolled into a cone.
My solution was to take high-framerate recording footage of the cone from above, and to then use my own implementation of the Hough Transform for circles over the entire video to find the ball's position.

The program is quite slow. Though I did not optimise this further as it was entirely for my own purposes. I thought to possibly add multithreading and interpolate different frames to seperate threads, but that may require an overhaul. Certainly, if the operations outlines in IPOP.java could be run on a GPU then the program would likely be much better at what it does.

#### Guide
The main method I use to start the GUI and start processing video when necessary resides in CircleDetector.java (most of the code here was developed using WindowBuilder for eclipse) (most of the code here was also made before I knew how to make clean code ;) ).

The file, IPOP.java (Image Processing OPerations) is essentially a static class full of methods required for image processing.

##### Methods include:
- conversion between int[] and RGB BufferedImage types
- pixel management (get value, set value, get RGB components etc.)
- greyscaling operations:
  - average
  - desaturation
  - minimum decomposition
  - maximum decomposition
  - red, green or blue greyscaling
- threshold operations
- canny edge operations (via the implementation by _Tom Gibara_[1])

The main usage of the IPOP class is in helper methods for converting BufferedImages into integer arrays and fast circle drawing methods (using the midpoint circle drawing algorithm). The actual method I use to find the coordinates of a circle are in
imageProcessors.HoughCircle.java (HoughCircle.process). I have a pseudo-exlplaination of the hough transformation algorithm I employ in my physics project, which you can view here if you wish https://drive.google.com/drive/u/0/folders/0B4fdeYEXNt5vfmNQYVhUdXFJVW12QmxtMHhyU2ZSdk5hNzZILWRBcmRtUXgzRGxtakpidEE?ltmpl=drive 
The project report also shows graphs of the results obtained, which seem to resemble modulated linear sine waves.

In order to read frames from a video, I use FFMPEG's framegrabber, included in OpenCV, which was an extremely hair-pulling process; but worked in the end.

#### References
1. Tom Gibara - canny edge detector implementation (http://www.tomgibara.com/computer-vision/CannyEdgeDetector.java)
