PACKAGE="xdotool"
BROWSER=$1

#check if the package is found (0 is true)
found=1
(dpkg -s $PACKAGE | grep -q "ok installed") && found=0

if [ $found -eq 0 ]
then
    echo "Found the package $PACKAGE"

     #zooms four times
     xdotool key Ctrl+plus 
     xdotool key Ctrl+plus 
     xdotool key Ctrl+plus   
     xdotool key Ctrl+plus 

    ./gradlew clean createFatJar
    ./gradlew -q --console plain run 

     xdotool key Ctrl+minus
     xdotool key Ctrl+minus
     xdotool key Ctrl+minus
     xdotool key Ctrl+minus

else
    echo "$PACKAGE is required to proceed"
    echo "Installing $PACKAGE in 5 seconds..."
    sleep 5
    sudo apt install xdotool
    echo "You may now rerun the program"
fi



