PACKAGE="xdotool"
BROWSER=$1

#check if the package is found (0 is true)
found=1
(dpkg -s $PACKAGE | grep -q "ok installed") && found=0

# options=$(getopt -o x)

# [ $? -eq 0 ] || { 
#     echo "Incorrect options provided"
#     exit 1
# }

# if [ $2 == "-"  ]

if [ $found -eq 0 ]
then
    echo "Found the package $PACKAGE"

     #zooms four times
     for i in {1..4}; do xdotool key Ctrl+plus; done

     ./gradlew clean createFatJar

     if [ $# -eq 0 ]; then
        ./gradlew -q --console plain run 
     elif [ $# -eq 2 ]; then
        ./gradlew -q --console plain run --args="$1 $2"
    else 
        echo "Invalid number of arguments"
        exit
     fi

    # ./gradlew clean createFatJar
    # ./gradlew -q --console plain run 

     for i in {1..4}; do xdotool key Ctrl+minus; done

else
    echo "$PACKAGE is required to proceed"
    echo "Installing $PACKAGE in 5 seconds..."
    sleep 5
    sudo apt install xdotool
    echo "You may now rerun the program"
fi



