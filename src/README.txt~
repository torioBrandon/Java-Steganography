UTEID: ofl74; bct562; 
FIRSTNAME: Olivia; Brandon; 
LASTNAME: Labath; Torio;
CSACCOUNT: ofl74; btorio;
EMAIL: olivialabath@utexas.edu; nodnarbtorio@yahoo.com;

2. In our main, we take in the image and store it as a BufferedImage. We analyze the input to decide if we will call our encode or decode function. 

In our encode function, we take in the file that we are encoding into the image with a fileInputStream, and then we increment through the individual bytes in the message. We examine 3 bytes at a time, and then increment through the bits 3 bits at a time, to set the 3 RGB values of the next pixel accordingly. We will either finish iterating through the message, and then add a byte of 0s to signify the end, or simply encode all we can. After, we write out the new image. 

For decode, we take in the image and analyze it a pixel at a time. We then store the bits we extact from the least significant place of each pixel, and after 24 bits we write out a bitarray to the file we are constructing. We do this until we encounter the null byte. 
We have finished all requirements.

3. We have finished all requirements.

4. 

a. No, it's not noticeable because the change to the pixel color value is so small, and not ever pixel is changed. 

b. You could also utilize the alpha channel if the image is encoded with an alpha channel. In other media files you could do similar encoding by changing bits that wouldn't be noticeable in the final product. In audio this might mean changing the 32 bit value for each part of hte sound in hte 48khz/41khz stream, in video we could store the same way as images only change things a frame at a time, etc. 

c. You could experiment with using more than 1 bit in each color, or if you were to be more advanced changing things like the entire shade of an image (making it appear normal, but be very different in information). 

d. You could analyze the image with different decoding algorithms, like ours, and look for output that contains a high number of ascii characters. You would need to find a balance between time you want to slow down the flow of images to check them, and certainty about their lack of hidden messages. 

e. It could be, assuming that the computer's method for storing images is the system resource being used to communicate, and that one subject is creating the images and another subject is able to view the images. 
