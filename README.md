# Image-Editor
BMP and PAM image editor that can work with layers and selections of image and apply basic or composite operations on image in .bmp or .pam format.

The project has two versions: one written in C++ as a console application and one written in Java as a GUI application. The main version - the Java application - heavily depends on the C++ program as it is basically integrated into the Java code. In other words, the Java version provides graphical user interface and some other simple functionalities, where the C++ program (being the core of the project) is only being called by the Java code (although it can also run independently as a console application).
